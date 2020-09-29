package com.example.sneek.outdoorindex;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapters.WarningsAdapter;
import model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WeatherAlertDialog extends AppCompatDialogFragment {

    private static final String TAG = "CREATION";
    private TextView alertDesc;
    private TextView alertTitle;
    private TextView alertExpire;
    private ImageView alertIcon;
    private WebView warningRadar;
    private ScrollView alertScroll_scrollView;

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.weather_alert_dialog_layout, null);

        builder.setView(view).setTitle("Weather Alerts").setPositiveButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDesc = (TextView) view.findViewById(R.id.alertDesc_textView);
        alertTitle = (TextView) view.findViewById(R.id.alertTitle_textView);
        alertExpire = (TextView) view.findViewById(R.id.alertExpire_textView);
        alertIcon = (ImageView) view.findViewById(R.id.warningAlert_imageView);
        warningRadar = (WebView) view.findViewById(R.id.alertDialogRadar_webView);
        alertScroll_scrollView = view.findViewById(R.id.alertScroll_scrollView);


        String title = getArguments().getString("title");
        String expire = getArguments().getString("expire");
        int expireUNIX = getArguments().getInt("expireUNIX");
        String desc = getArguments().getString("desc");
        int icon = getArguments().getInt("icon");
        String geofence = getArguments().getString("geofence");
        String color = getArguments().getString("color");
        String id = getArguments().getString("id");
        getWeatherAlert(title, expire, expireUNIX, desc, icon, geofence, color, id);
        return builder.create();
    }


    public void getWeatherAlert(final String title, final String expire, final int expireUNIX, final String desc, final int icon, final String geofence, final String color, final String id) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                final String[] description = {desc};
                String instruction = "";
                final org.jsoup.nodes.Document docDESC;

                Log.d(TAG, "ID: " + id);
                try {
                    if (!id.equals("")) {
                        OkHttpClient okHttpDESC = new OkHttpClient();
                        Request requestDESC = new Request.Builder().url(id).get().build();
                        docDESC = Jsoup.parse(okHttpDESC.newCall(requestDESC).execute().body().string());

                        description[0] = docDESC.select("info > description").text();
                        instruction = docDESC.select("info > instruction").text();
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Error : " + (e.getMessage()));
                }
                Log.d(TAG, "FORMAT ID FINISH");


                final String finalInstruction = instruction;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertTitle.setText(title);
                        if (desc.contains("*")) {
                            description[0] = desc.replace("*", "\n\n");
                        }

                        alertDesc.setText("\n\n" + description[0] + "\n\n" + finalInstruction);
                        alertIcon.setImageResource(icon);
                        Log.d(TAG, "ICON COLOR: " + icon);

                        int alertExpiration = expireUNIX - (int) (System.currentTimeMillis() / 1000);
                        //int alertExpiration = Integer.parseInt(homeFragment.weather.alerts.getExpireTime().get(0).toString()) - homeFragment.weather.currentCondition.getTime();
                        int hours = (int) Math.floor(alertExpiration / 3600);
                        int remainder = (int) (alertExpiration - hours * 3600);
                        int minutes = remainder / 60;

                        if (hours != 0) {
                            alertExpire.setText("Expires in " + hours + " hours " + minutes + " mins");
                        } else {
                            alertExpire.setText("Expires in " + minutes + " mins");
                        }

                        alertExpire.append(" (" + expire + ")");


                        String geofenceFormat;
                        String[] geofenceArr;
                        StringBuilder stringBuilder = new StringBuilder();

                        if (geofence.length() != 0) {
                            geofenceArr = geofence.split(" ");
                            for (int i = 0; i < geofenceArr.length; i++) {
                                stringBuilder.append("[" + geofenceArr[i] + "],");
                            }
                            warningRadar.setVisibility(View.VISIBLE);
                            geofenceFormat = stringBuilder.toString();
                            //String color = getColorValues(icon);
                            createRadarMap(geofenceFormat, geofenceArr[0], title, color);

                            Log.d(TAG, "GEOFENCE:" + geofenceFormat);
                            Log.d(TAG, "GEOFENCE LOC" + geofenceArr[0]);
                        } else {
                            warningRadar.setVisibility(View.GONE);
                            Log.d(TAG, "NO GEOFENCE DATA");
                        }
                    }
                });
            }
        }).start();
    }


    private void createRadarMap(String geofenceFormat, String geofenceLoc, String title, String color) {

        warningRadar.getSettings().setJavaScriptEnabled(true);
        warningRadar.getSettings().setSupportZoom(true);
        warningRadar.getSettings().setLoadWithOverviewMode(true);
        warningRadar.getSettings().setUseWideViewPort(true);
        warningRadar.getSettings().setBuiltInZoomControls(true);
        warningRadar.getSettings().setDisplayZoomControls(false);

        warningRadar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                alertScroll_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        String customHTML = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>RainViewer API Example</title>\n" +
                "\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.css\"/>\n" +
                "    <script src=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.js\"></script>\n" +
                "    <style type=\"text/css\">\n" +
                "        li {\n" +
                "            list-style: none;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<ul style=\"text-align:left; position: absolute;bottom: 0px; left: -30px; right: 0; height: 10px;\">\n" +
                "    <li><input type=\"button\" onclick=\"stop(); if(!(animationPosition == 0)){showFrame(animationPosition - 1);} return;\" value=\"&lt;10m\" /></li>\n" +
                "    <li><input type=\"button\" onclick=\"playStop();\" value=\"Play / Stop\" /></li>\n" +
                "    <li><input type=\"button\" onclick=\"stop(); if(!(animationPosition == 12)){showFrame(animationPosition + 1);} return;\" value=\"10m&gt;\" /></li>\n" +
                "    <li><div id=\"timestamp\" style=\"text-align:right; font-size: 10px; position: absolute;bottom: 5px; left: 0; right: 5px; height: 0px;\">FRAME TIME</div></li>\n" +
                "\n" +
                "</ul>\n" +
                "\n" +
                "\n" +
                "<div id=\"mapid\" style=\"position: absolute;  top: 0px; left: 0px; bottom: 30px; right: 0px;\"></div>\n" +
                "\n" +
                "<script>\n" +
                "    var map = L.map('mapid').setView([" + geofenceLoc + "], 8);\n" +
                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "        attributions: 'Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors',\n" +
                "        maxZoom: 13\n" +
                "    }).addTo(map);\n" +
                "\n" +
                "var geofence = L.polygon([" + geofenceFormat + "],{\n" +
                "        color: '" + color + "',\n" +
                "        opacity: 0.8\n" +
                "    }).addTo(map);\n" +
                "    var timestamps = [];\n" +
                "    var radarLayers = [];\n" +
                "    var animationPosition = 0;\n" +
                "    var animationTimer = false;\n" +
                "\n" +
                "    geofence.bindPopup(\"<em>" + title + "</em>\")\n" +
                "    var apiRequest = new XMLHttpRequest();\n" +
                "    apiRequest.open(\"GET\", \"https://tilecache.rainviewer.com/api/maps.json\", true);\n" +
                "    apiRequest.onload = function(e) {\n" +
                "        // save available timestamps and show the latest frame: \"-1\" means \"timestamp.lenght - 1\"\n" +
                "        timestamps = JSON.parse(apiRequest.response);\n" +
                "        showFrame(-1);\n" +
                "    };\n" +
                "    apiRequest.send();\n" +
                "\n" +
                "    function addLayer(ts) {\n" +
                "        if (!radarLayers[ts]) {\n" +
                "            radarLayers[ts] = new L.TileLayer('https://tilecache.rainviewer.com/v2/radar/' + ts + '/256/{z}/{x}/{y}/6/1_1.png', {\n" +
                "                tileSize: 256,\n" +
                "                opacity: 0.001,\n" +
                "                zIndex: ts\n" +
                "            });\n" +
                "        }\n" +
                "        if (!map.hasLayer(radarLayers[ts])) {\n" +
                "            map.addLayer(radarLayers[ts]);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    function changeRadarPosition(position, preloadOnly) {\n" +
                "        while (position >= timestamps.length) {\n" +
                "            position -= timestamps.length;\n" +
                "        }\n" +
                "        while (position < 0) {\n" +
                "            position += timestamps.length;\n" +
                "        }\n" +
                "        var currentTimestamp = timestamps[animationPosition];\n" +
                "        var nextTimestamp = timestamps[position];\n" +
                "        addLayer(nextTimestamp);\n" +
                "        if (preloadOnly) {\n" +
                "            return;\n" +
                "        }\n" +
                "        animationPosition = position;\n" +
                "        if (radarLayers[currentTimestamp]) {\n" +
                "            radarLayers[currentTimestamp].setOpacity(0);\n" +
                "        }\n" +
                "        radarLayers[nextTimestamp].setOpacity(.65);\n" +
                "\n" +
                "        document.getElementById(\"timestamp\").innerHTML = \"Last updated: \" + timeConverter(nextTimestamp).toString();\n" +
                "    }\n" +
                "\n" +
                "       function timeConverter(UNIX_timestamp){\n" +
                "         var a = new Date(UNIX_timestamp * 1000);\n" +
                "         var hour = a.getHours();\n" +
                "         var min = a.getMinutes();\n" +
                "         var ampm = \"a\";\n" +
                "         var newHour = 0;\n" +
                "         var newMinutes = 0;\n" +
                "         if(hour > 12) {\n" +
                "            newHour = hour - 12;\n" +
                "            ampm = \"p\"\n" +
                "         } else if(hour < 12) {\n" +
                "            newHour = hour;\n" +
                "            ampm = \"a\"\n" +
                "         }\n" +
                "\n" +
                "         if(min == 0){\n" +
                "            newMinutes = \"00\"\n" +
                "         } else {\n" +
                "            newMinutes = min;\n" +
                "         }\n" +
                "         var time = newHour + ':' + newMinutes + ' ' + ampm;\n" +
                "         return time;\n" +
                "        }\n" +
                "    /**\n" +
                "     * Check avialability and show particular frame position from the timestamps list\n" +
                "     */\n" +
                "    function showFrame(nextPosition) {\n" +
                "        var preloadingDirection = nextPosition - animationPosition > 0 ? 1 : -1;\n" +
                "        changeRadarPosition(nextPosition);\n" +
                "        // preload next next frame (typically, +1 frame)\n" +
                "        // if don't do that, the animation will be blinking at the first loop\n" +
                "        changeRadarPosition(nextPosition + preloadingDirection, true);\n" +
                "    }\n" +
                "    /**\n" +
                "     * Stop the animation\n" +
                "     * Check if the animation timeout is set and clear it.\n" +
                "     */\n" +
                "    function stop() {\n" +
                "        if (animationTimer) {\n" +
                "            clearTimeout(animationTimer);\n" +
                "            animationTimer = false;\n" +
                "            return true;\n" +
                "        }\n" +
                "        return false;\n" +
                "    }\n" +
                "    function play() {\n" +
                "        showFrame(animationPosition + 1);\n" +
                "        // Main animation driver. Run this function every 500 ms\n" +
                "        if(!(animationPosition == 12)){\n" +
                "            animationTimer = setTimeout(play, 500);\n" +
                "        } else {\n" +
                "           animationTimer = setTimeout(play,1500)\n" +
                "        }\n" +
                "        \n" +
                "    }\n" +
                "    function playStop() {\n" +
                "        if (!stop()) {\n" +
                "            play();\n" +
                "        }\n" +
                "    }\n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";


        warningRadar.loadData(customHTML, "text/html", "UTF-8");

    }
}
