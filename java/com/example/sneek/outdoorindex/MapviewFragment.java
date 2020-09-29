package com.example.sneek.outdoorindex;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;

import java.net.URL;

import Support.Utils;
import model.MapCreator;
import model.Weather;

public class MapviewFragment extends Fragment {


    Weather weather = new Weather();
    MapCreator mapCreator = new MapCreator();
    Utils utils = new Utils();
    WebView radarView;
    WebView globalWebView;
    WebView visableColorStaticWebView;
    WebView visableColorLoopWebView;
    WebView visableVisWebView;
    WebView visableIRWebView;
    WebView visableMoistWebView;


    LinearLayout visableWebView_linearLayout;
    SeekBar radarAnimTime_seekBar;
    TextView startTime_textView;
    TextView endTime_textView;
    TextView attributionText;
    ImageButton animRadarStartStop_imageButton;
    ImageButton myLocation_imageButton;
    ImageButton geocolorPause_imageButton;
    ImageButton geocolorPlay_imageButton;
    
    ImageView testView;


    RadioButton radarRadio_radioButton;
    RadioButton globalRadio_radioButton;
    RadioButton visableRadio_radioButton;
    RadioButton visableColorStatic_radioButton;
    RadioButton visableColorLoop_radioButton;
    RadioButton visableSeaTempStandard_radioButton;
    RadioButton visableSeaTempHigh_radioButton;

    LinearLayout geocolorPlayPause_linearLayout;
    ScrollView visableScrollView;

    Boolean radarCreated = false;
    Boolean globalCreated = false;
    Boolean visableCreated = false;


    final String API_KEY = "gGoQKRqTjBJlTHqFMcxK3bcS9oL29wJx";
    private static final String TAG = "CREATION";
    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.mapview_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        radarView = (WebView) view.findViewById(R.id.radarView_webview);
        globalWebView = (WebView) view.findViewById(R.id.globalView_webview);
        visableColorStaticWebView = (WebView) view.findViewById(R.id.visableColorStaticView_webView);
        visableColorLoopWebView = (WebView) view.findViewById(R.id.visableColorLoopView_webView);
        visableVisWebView = (WebView) view.findViewById(R.id.visableVisView_webView);
        visableIRWebView = (WebView) view.findViewById(R.id.visableIRView_webView);
        visableMoistWebView = (WebView) view.findViewById(R.id.visableMoistView_webView);

        geocolorPlayPause_linearLayout = view.findViewById(R.id.geocolorPlayPause_linearLayout);

        visableWebView_linearLayout = (LinearLayout) view.findViewById(R.id.visableWebView_linearLayout);
        radarAnimTime_seekBar = (SeekBar) view.findViewById(R.id.radarAnimTime_seekBar);
        startTime_textView = (TextView) view.findViewById(R.id.startTime_textView);
        endTime_textView = (TextView) view.findViewById(R.id.endTime_textView);
        attributionText = (TextView) view.findViewById(R.id.attribution_textView);
        animRadarStartStop_imageButton = (ImageButton) view.findViewById(R.id.animRadarStartStop_imageButton);
        myLocation_imageButton = (ImageButton) view.findViewById(R.id.myLocation_imageButton);
        geocolorPause_imageButton = view.findViewById(R.id.geocolorPause_imageButton);
        geocolorPlay_imageButton = view.findViewById(R.id.geocolorPlay_imageButton);
        radarRadio_radioButton = (RadioButton) view.findViewById(R.id.radarRadio_radioButton);
        globalRadio_radioButton = (RadioButton) view.findViewById(R.id.globalRadio_radioButton);
        visableRadio_radioButton = (RadioButton) view.findViewById(R.id.visableRadio_radioButton);
        visableColorStatic_radioButton = (RadioButton) view.findViewById(R.id.visableColorStatic_radioButton);
        visableColorLoop_radioButton = (RadioButton) view.findViewById(R.id.visableColorLoop_radioButton);

        visableScrollView = (ScrollView) view.findViewById(R.id.visable_scrollView);
        radarView.setVisibility(View.VISIBLE);
        globalWebView.setVisibility(View.GONE);
        visableWebView_linearLayout.setVisibility(View.GONE);




        CreateWeatherMap(homeFragment.utils.getLat(), homeFragment.utils.getLon());


        radarRadio_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    radarView.setVisibility(View.VISIBLE);
                    globalWebView.setVisibility(View.GONE);
                    visableWebView_linearLayout.setVisibility(View.GONE);
                    Log.d(TAG, "RADAR MAP SELECTED");

            }
        });

        globalRadio_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeFragment.isNetworkAvailable()) {
                    if (globalCreated == false) {
                        CreateGlobalMap(Float.parseFloat(utils.DEFAULT_LATITUDE), Float.parseFloat(utils.DEFAULT_LONGITUDE));
                        radarView.setVisibility(View.GONE);
                        globalWebView.setVisibility(View.VISIBLE);
                        visableWebView_linearLayout.setVisibility(View.GONE);
                        globalCreated = true;
                    } else {
                        radarView.setVisibility(View.GONE);
                        globalWebView.setVisibility(View.VISIBLE);
                        visableWebView_linearLayout.setVisibility(View.GONE);
                        Log.d(TAG, "GLOBAL MAP SELECTED");
                    }
                } else {
                    radarView.setVisibility(View.VISIBLE);
                    globalWebView.setVisibility(View.GONE);
                    visableWebView_linearLayout.setVisibility(View.GONE);
                }
            }
        });

        visableRadio_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeFragment.isNetworkAvailable()) {
                    if (visableCreated == false) {
                        Log.d(TAG, "VISABLE MAP CREATED");
                        loadSateliteImages();
                        radarView.setVisibility(View.GONE);
                        globalWebView.setVisibility(View.GONE);
                        visableWebView_linearLayout.setVisibility(View.VISIBLE);
                        geocolorPlayPause_linearLayout.setVisibility(View.GONE);

                        visableCreated = true;
                    } else {
                        radarView.setVisibility(View.GONE);
                        globalWebView.setVisibility(View.GONE);
                        visableWebView_linearLayout.setVisibility(View.VISIBLE);
                        Log.d(TAG, "VISABLE MAP SELECTED");
                    }
                } else {
                    radarView.setVisibility(View.VISIBLE);
                    globalWebView.setVisibility(View.GONE);
                    visableWebView_linearLayout.setVisibility(View.GONE);
                }
            }
        });
        visableColorStatic_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visableColorStaticWebView.setVisibility(View.VISIBLE);
                visableColorLoopWebView.setVisibility(View.GONE);
                geocolorPlayPause_linearLayout.setVisibility(View.GONE);

            }
        });
        visableColorLoop_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visableColorStaticWebView.setVisibility(View.GONE);
                visableColorLoopWebView.setVisibility(View.VISIBLE);
                geocolorPlayPause_linearLayout.setVisibility(View.VISIBLE);
            }
        });

//        final FragmentManager fm = getActivity().getSupportFragmentManager();
//        fm.beginTransaction().add(R.id.radarView_frameLayout, new RadarFragment(), "radar").show(MainActivity.radarFragment).commit();

    }


    public void CreateGlobalMap(float latitude, float longitude) {
        Log.d(TAG, "GLOBAL MAP CREATED");

        globalWebView.loadUrl("about:blank");
        globalWebView.clearHistory();
        String windyMapViewHTML =
                "<html>\n" +
                        "  <head>\n" +
                        "  \t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n" +
                        "    <script src=\"https://unpkg.com/leaflet@1.4.0/dist/leaflet.js\"></script>\n" +
                        "    <script src=\"https://api4.windy.com/assets/libBoot.js\"></script>\n" +
                        "    <style>\n" +
                        "      #windy {\n" +
                        "        width: 100%;\n" +
                        "        height: 100%;\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <div id=\"windy\"></div>\n" +
                        "\n" +
                        "    <script>\n" +
                        "    const options = {\n" +
                        "        key: 'gGoQKRqTjBJlTHqFMcxK3bcS9oL29wJx',\n" +
                        "        lat: " + latitude + ",\n" +
                        "        lon: " + longitude + ",\n" +
                        "        zoom: 5,\n" +
                        "        timestamp: Date.now(),\n" +
                        "        hourFormat: '12h',\n" +
                        "        // ...etc\n" +
                        "    }\n" +
                        "    windyInit( options, windyAPI => {\n" +
                        "        const { store } = windyAPI\n" +
                        "        const levels = store.getAllowed('availLevels')\n" +
                        "        store.on('level', level => {\n" +
                        "        })\n" +
                        "\n" +
                        "        var overlays = ['rain','wind','temp','clouds'], i = 0\n" +
                        "            store.set('overlay', overlays[ 2] )\n" +
                        "        \n" +
                        "    })\n" +
                        "    </script>\n" +
                        "\n" +
                        "  </body>\n" +
                        "</html>";

        globalWebView.getSettings().setJavaScriptEnabled(true);
        globalWebView.setWebViewClient(new WebViewClient());
        globalWebView.loadData(windyMapViewHTML, "text/html", "UTF-8");

    }




    public void loadSateliteImages() {
        visableColorStaticWebView.setVisibility(View.VISIBLE);
        visableColorLoopWebView.setVisibility(View.GONE);
        visableWebHelper(visableColorStaticWebView, "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/GEOCOLOR/1250x750.jpg");
        visableWebHelper(visableColorLoopWebView, "https://cdn.star.nesdis.noaa.gov//GOES16/ABI/SECTOR/EUS/GEOCOLOR/GOES16-EUS-GEOCOLOR-1000x1000.gif");
        visableWebHelper(visableVisWebView, "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/02/1250x750.jpg");
        visableWebHelper(visableIRWebView, "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/13/1250x750.jpg");
        visableWebHelper(visableMoistWebView, "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/09/1250x750.jpg");

        //visableWebHelper(visableSeaTempHighWebView, "https://www.ospo.noaa.gov/data/sst/contour/global.cf.gif");


        geocolorPause_imageButton.setVisibility(View.VISIBLE);
        geocolorPlay_imageButton.setVisibility(View.GONE);

        geocolorPause_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visableColorLoopWebView.onPause();
                geocolorPause_imageButton.setVisibility(View.GONE);
                geocolorPlay_imageButton.setVisibility(View.VISIBLE);
            }
        });

        geocolorPlay_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visableColorLoopWebView.onResume();
                geocolorPause_imageButton.setVisibility(View.VISIBLE);
                geocolorPlay_imageButton.setVisibility(View.GONE);
            }
        });


        attributionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.weather.gov/satellite"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    Log.d(TAG, "onClick: inTryBrowser");
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Log.e(TAG, "onClick: in inCatchBrowser", ex);
                    intent.setPackage(null);
                    startActivity(intent.createChooser(intent, "Select Browser"));
                }
            }
        });

    }

    public void visableWebHelper(WebView resource, String url){
        resource.loadUrl(url);
        resource.getSettings().setSupportZoom(true);
        resource.getSettings().setLoadWithOverviewMode(true);
        resource.getSettings().setUseWideViewPort(true);
        resource.getSettings().setBuiltInZoomControls(true);
        resource.getSettings().setDisplayZoomControls(false);

        resource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                visableScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

//    public void createRadarMap(double latitude, double longitude) {
////        FragmentManager fm = getFragmentManager();
////        RadarFragment radarFragment = (RadarFragment) fm.findFragmentByTag("radar");
//        CreateWeatherMap(latitude, longitude);
////        final FragmentManager fm = getActivity().getSupportFragmentManager();
////        fm.beginTransaction().remove(MainActivity.radarFragment).commit();
////        fm.beginTransaction().add(R.id.radarView_frameLayout, new RadarFragment(), "radar").show(MainActivity.radarFragment).commit();
//
//        //MainActivity.radarFragment.CreateWeatherMap(latitude, longitude);
//
//    }

    public void CreateWeatherMap(double latitude, double longitude) {

        radarView.getSettings().setJavaScriptEnabled(true);
        radarView.setWebViewClient(new WebViewClient());
        radarView.getSettings().setLoadWithOverviewMode(true);
        radarView.getSettings().setUseWideViewPort(true);


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
                "    <li><div id=\"timestamp\" style=\"text-align:right; font-size: 10px; position: absolute;bottom: 5px; left: 0; right: 5px; height: 0px;\"></div></li>\n" +
                "\n" +
                "</ul>\n" +
                "\n" +
                "\n" +
                "<div id=\"mapid\" style=\"position: absolute;  top: 0px; left: 0px; bottom: 30px; right: 0px;\"></div>\n" +
                "\n" +
                "<script>\n" +
                "    var map = L.map('mapid').setView([" + latitude + ", " + longitude + "], 8);\n" +
                "    const attribution = 'Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors'\n" +
                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution }).addTo(map);\n" +
                "\n" +
                "    var marker = L.marker([" + latitude + ", " + longitude + "]).addTo(map);\n" +
                "    var timestamps = [];\n" +
                "    var radarLayers = [];\n" +
                "    var animationPosition = 0;\n" +
                "    var animationTimer = false;\n" +
                "\n" +
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


        radarView.loadData(customHTML, "text/html", "UTF-8");
    }


}