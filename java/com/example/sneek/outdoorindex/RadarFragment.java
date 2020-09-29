package com.example.sneek.outdoorindex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Support.Utils;

public class RadarFragment extends Fragment {

    WebView radarView;
    Utils utils = new Utils();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.radar_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        radarView = (WebView) view.findViewById(R.id.radarView_webview);
        //CreateWeatherMap(Double.parseDouble(utils.DEFAULT_LATITUDE), Double.parseDouble(utils.DEFAULT_LONGITUDE));
        Log.d("CREATION", "CREATED RADAR");
    }

//    public void CreateWeatherMap(double latitude, double longitude) {
//
//        radarView.getSettings().setJavaScriptEnabled(true);
//        radarView.setWebViewClient(new WebViewClient());
//        radarView.getSettings().setLoadWithOverviewMode(true);
//        radarView.getSettings().setUseWideViewPort(true);
//
//
//
//        String customHTML = "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<head>\n" +
//                "    <title>RainViewer API Example</title>\n" +
//                "\n" +
//                "    <meta charset=\"utf-8\"/>\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                "\n" +
//                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.css\"/>\n" +
//                "    <script src=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.js\"></script>\n" +
//                "    <style type=\"text/css\">\n" +
//                "        li {\n" +
//                "            list-style: none;\n" +
//                "            display: inline-block;\n" +
//                "        }\n" +
//                "    </style>\n" +
//                "</head>\n" +
//                "\n" +
//                "<body>\n" +
//                "<ul style=\"text-align:left; position: absolute;bottom: 0px; left: -30px; right: 0; height: 10px;\">\n" +
//                "    <li><input type=\"button\" onclick=\"stop(); if(!(animationPosition == 0)){showFrame(animationPosition - 1);} return;\" value=\"&lt;10m\" /></li>\n" +
//                "    <li><input type=\"button\" onclick=\"playStop();\" value=\"Play / Stop\" /></li>\n" +
//                "    <li><input type=\"button\" onclick=\"stop(); if(!(animationPosition == 12)){showFrame(animationPosition + 1);} return;\" value=\"10m&gt;\" /></li>\n" +
//                "    <li><div id=\"timestamp\" style=\"text-align:right; font-size: 10px; position: absolute;bottom: 5px; left: 0; right: 5px; height: 0px;\">FRAME TIME</div></li>\n" +
//                "\n" +
//                "</ul>\n" +
//                "\n" +
//                "\n" +
//                "<div id=\"mapid\" style=\"position: absolute;  top: 0px; left: 0px; bottom: 30px; right: 0px;\"></div>\n" +
//                "\n" +
//                "<script>\n" +
//                "    var map = L.map('mapid').setView([" + latitude + ", " + longitude + "], 8);\n" +
//                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
//                "        attributions: 'Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors',\n" +
//                "        maxZoom: 13\n" +
//                "    }).addTo(map);\n" +
//                "\n" +
//                "    var marker = L.marker([" + latitude + ", " + longitude + "]).addTo(map);\n" +
//                "    var timestamps = [];\n" +
//                "    var radarLayers = [];\n" +
//                "    var animationPosition = 0;\n" +
//                "    var animationTimer = false;\n" +
//                "\n" +
//                "    var apiRequest = new XMLHttpRequest();\n" +
//                "    apiRequest.open(\"GET\", \"https://tilecache.rainviewer.com/api/maps.json\", true);\n" +
//                "    apiRequest.onload = function(e) {\n" +
//                "        // save available timestamps and show the latest frame: \"-1\" means \"timestamp.lenght - 1\"\n" +
//                "        timestamps = JSON.parse(apiRequest.response);\n" +
//                "        showFrame(-1);\n" +
//                "    };\n" +
//                "    apiRequest.send();\n" +
//                "\n" +
//                "    function addLayer(ts) {\n" +
//                "        if (!radarLayers[ts]) {\n" +
//                "            radarLayers[ts] = new L.TileLayer('https://tilecache.rainviewer.com/v2/radar/' + ts + '/256/{z}/{x}/{y}/6/1_1.png', {\n" +
//                "                tileSize: 256,\n" +
//                "                opacity: 0.001,\n" +
//                "                zIndex: ts\n" +
//                "            });\n" +
//                "        }\n" +
//                "        if (!map.hasLayer(radarLayers[ts])) {\n" +
//                "            map.addLayer(radarLayers[ts]);\n" +
//                "        }\n" +
//                "    }\n" +
//                "\n" +
//                "    function changeRadarPosition(position, preloadOnly) {\n" +
//                "        while (position >= timestamps.length) {\n" +
//                "            position -= timestamps.length;\n" +
//                "        }\n" +
//                "        while (position < 0) {\n" +
//                "            position += timestamps.length;\n" +
//                "        }\n" +
//                "        var currentTimestamp = timestamps[animationPosition];\n" +
//                "        var nextTimestamp = timestamps[position];\n" +
//                "        addLayer(nextTimestamp);\n" +
//                "        if (preloadOnly) {\n" +
//                "            return;\n" +
//                "        }\n" +
//                "        animationPosition = position;\n" +
//                "        if (radarLayers[currentTimestamp]) {\n" +
//                "            radarLayers[currentTimestamp].setOpacity(0);\n" +
//                "        }\n" +
//                "        radarLayers[nextTimestamp].setOpacity(.65);\n" +
//                "\n" +
//                "        document.getElementById(\"timestamp\").innerHTML = \"Last updated: \" + timeConverter(nextTimestamp).toString();\n" +
//                "    }\n" +
//                "\n" +
//                "       function timeConverter(UNIX_timestamp){\n" +
//                "         var a = new Date(UNIX_timestamp * 1000);\n" +
//                "         var hour = a.getHours();\n" +
//                "         var min = a.getMinutes();\n" +
//                "         var ampm = \"a\";\n" +
//                "         var newHour = 0;\n" +
//                "         var newMinutes = 0;\n" +
//                "         if(hour > 12) {\n" +
//                "            newHour = hour - 12;\n" +
//                "            ampm = \"p\"\n" +
//                "         } else if(hour < 12) {\n" +
//                "            newHour = hour;\n" +
//                "            ampm = \"a\"\n" +
//                "         }\n" +
//                "\n" +
//                "         if(min == 0){\n" +
//                "            newMinutes = \"00\"\n" +
//                "         } else {\n" +
//                "            newMinutes = min;\n" +
//                "         }\n" +
//                "         var time = newHour + ':' + newMinutes + ' ' + ampm;\n" +
//                "         return time;\n" +
//                "        }\n" +
//                "    /**\n" +
//                "     * Check avialability and show particular frame position from the timestamps list\n" +
//                "     */\n" +
//                "    function showFrame(nextPosition) {\n" +
//                "        var preloadingDirection = nextPosition - animationPosition > 0 ? 1 : -1;\n" +
//                "        changeRadarPosition(nextPosition);\n" +
//                "        // preload next next frame (typically, +1 frame)\n" +
//                "        // if don't do that, the animation will be blinking at the first loop\n" +
//                "        changeRadarPosition(nextPosition + preloadingDirection, true);\n" +
//                "    }\n" +
//                "    /**\n" +
//                "     * Stop the animation\n" +
//                "     * Check if the animation timeout is set and clear it.\n" +
//                "     */\n" +
//                "    function stop() {\n" +
//                "        if (animationTimer) {\n" +
//                "            clearTimeout(animationTimer);\n" +
//                "            animationTimer = false;\n" +
//                "            return true;\n" +
//                "        }\n" +
//                "        return false;\n" +
//                "    }\n" +
//                "    function play() {\n" +
//                "        showFrame(animationPosition + 1);\n" +
//                "        // Main animation driver. Run this function every 500 ms\n" +
//                "        if(!(animationPosition == 12)){\n" +
//                "            animationTimer = setTimeout(play, 500);\n" +
//                "        } else {\n" +
//                "           animationTimer = setTimeout(play,1500)\n" +
//                "        }\n" +
//                "        \n" +
//                "    }\n" +
//                "    function playStop() {\n" +
//                "        if (!stop()) {\n" +
//                "            play();\n" +
//                "        }\n" +
//                "    }\n" +
//                "</script>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>";
//
//
//        radarView.loadData(customHTML, "text/html", "UTF-8");
//    }

}




//        String customHTML = "<!DOCTYPE html>\n" +
////                "<html>\n" +
////                "<head>\n" +
////                "    <title>RainViewer API Example</title>\n" +
////                "\n" +
////                "    <meta charset=\"utf-8\"/>\n" +
////                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
////                "\n" +
////                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.css\"/>\n" +
////                "    <script src=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.js\"></script>\n" +
////                "    <style type=\"text/css\">\n" +
////                "        li {\n" +
////                "            list-style: none;\n" +
////                "            display: inline-block;\n" +
////                "        }\n" +
////                "    </style>\n" +
////                "</head>\n" +
////                "<body>\n" +
////                "\n" +
////                "<div id=\"mapid\" style=\"position: absolute; top: 0px; left: 0; bottom: 0; right: 0;\"></div>\n" +
////                "\n" +
////                "<script>\n" +
////                "    var map = L.map('mapid').setView([" + latitude + ", " + longitude + "], 9);\n" +
////                "    map.removeControl( map.zoomControl );\n" +
////                "    const attribution = 'Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors'\n" +
////                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution }).addTo(map);\n" +
////                "\n" +
////                "    var timestamps = [];\n" +
////                "    var radarLayers = [];\n" +
////                "    var animationPosition = 0;\n" +
////                "    var apiRequest = new XMLHttpRequest();\n" +
////                "    apiRequest.open(\"GET\", \"https://tilecache.rainviewer.com/api/maps.json\", true);\n" +
////                "    apiRequest.onload = function(e) {\n" +
////                "        timestamps = JSON.parse(apiRequest.response);\n" +
////                "        showFrame(-1);\n" +
////                "    };\n" +
////                "    apiRequest.send();\n" +
////                "    function addLayer(ts) {\n" +
////                "        if (!radarLayers[ts]) {\n" +
////                "            radarLayers[ts] = new L.TileLayer('https://tilecache.rainviewer.com/v2/radar/' + ts + '/256/{z}/{x}/{y}/6/1_1.png', {\n" +
////                "                tileSize: 256,\n" +
////                "                opacity: 0.001,\n" +
////                "                zIndex: ts\n" +
////                "            });\n" +
////                "        }\n" +
////                "        if (!map.hasLayer(radarLayers[ts])) {\n" +
////                "            map.addLayer(radarLayers[ts]);\n" +
////                "        }\n" +
////                "    }\n" +
////                "    function changeRadarPosition(position, preloadOnly) {\n" +
////                "        while (position >= timestamps.length) {\n" +
////                "            position -= timestamps.length;\n" +
////                "        }\n" +
////                "        while (position < 0) {\n" +
////                "            position += timestamps.length;\n" +
////                "        }\n" +
////                "        var currentTimestamp = timestamps[animationPosition];\n" +
////                "        var nextTimestamp = timestamps[position];\n" +
////                "        addLayer(nextTimestamp);\n" +
////                "        if (preloadOnly) {\n" +
////                "            return;\n" +
////                "        }\n" +
////                "        animationPosition = position;\n" +
////                "        if (radarLayers[currentTimestamp]) {\n" +
////                "            radarLayers[currentTimestamp].setOpacity(0);\n" +
////                "        }\n" +
////                "        radarLayers[nextTimestamp].setOpacity(.65);\n" +
////                "        document.getElementById(\"timestamp\").innerHTML = (new Date(nextTimestamp * 1000)).toString();\n" +
////                "    }\n" +
////                "\n" +
////                "    function showFrame(nextPosition) {\n" +
////                "        var preloadingDirection = nextPosition - animationPosition > 0 ? 1 : -1;\n" +
////                "        changeRadarPosition(nextPosition);\n" +
////                "        changeRadarPosition(nextPosition + preloadingDirection, true);\n" +
////                "    }\n" +
////                "    \n" +
////                "   \n" +
////                "</script>\n" +
////                "\n" +
////                "</body>\n" +
////                "</html>";
