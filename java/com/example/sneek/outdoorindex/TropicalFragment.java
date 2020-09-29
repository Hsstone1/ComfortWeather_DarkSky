package com.example.sneek.outdoorindex;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Support.Utils;
import data.DatabaseHelper;
import model.Weather;

public class TropicalFragment extends Fragment {
    private static final String TAG = "CREATION";
    //public final DecimalFormat d0f = new DecimalFormat("#");
    Weather weather = new Weather();
    Utils utils = new Utils();
    WebView floaterWebView;
    WebView windTSWebView;
    WebView windHURWebView;
    WebView fiveDayDevelopmentWebView;
    WebView oceanTempWebView;
    WebView sevenDayRainOutlookWebview;


    TextView hurricaneTitle_textView;
    TextView alertExpire_textView;
    TextView windSpeed_textView;
    TextView windGust_textView;
    TextView currentPressure_textView;
    TextView groundSpeed_textView;
    TextView stormSurge_textView;
    TextView catagoryLevel_textView;
    TextView currentLocation_textView;
    TextView hurricaneDescription_textView;
    TextView tropicalDevelopmentOutlook_textView;
    TextView floater_textView;
    TextView windProb_textView;
    TextView attributionText;

    LinearLayout floater_linearLayout;

    ImageButton floaterPause_imageButton;
    ImageButton floaterPlay_imageButton;

    RadioButton windTSRadio;
    RadioButton windHURRadio;

    Spinner oceanSelect_spinner;

    NestedScrollView severeScrollView;
    DatabaseHelper mDatabaseHelper;


    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.tropical_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        floaterWebView = view.findViewById(R.id.floater_webView);
        windTSWebView = view.findViewById(R.id.windTSProbability_webView);
        windHURWebView = view.findViewById(R.id.windHURProbability_webView);

        fiveDayDevelopmentWebView = view.findViewById(R.id.development5Day_webView);
        oceanTempWebView = view.findViewById(R.id.visableSeaTempsView_webView);
        sevenDayRainOutlookWebview = view.findViewById(R.id.sevenDayRainOutlookWebview_webview);


        hurricaneTitle_textView = view.findViewById(R.id.hurricaneTitle_textView);
        alertExpire_textView = view.findViewById(R.id.alertExpire_textView);
        windSpeed_textView = view.findViewById(R.id.windSpeed_textView);
        windGust_textView = view.findViewById(R.id.windGust_textView);
        currentPressure_textView = view.findViewById(R.id.currentPressure_textView);
        groundSpeed_textView = view.findViewById(R.id.groundSpeed_textView);
        stormSurge_textView = view.findViewById(R.id.stormSurge_textView);
        catagoryLevel_textView = view.findViewById(R.id.catagoryLevel_textView);
        currentLocation_textView = view.findViewById(R.id.currentLocation_textView);
        hurricaneDescription_textView = view.findViewById(R.id.hurricaneDescription_textView);
        tropicalDevelopmentOutlook_textView = view.findViewById(R.id.tropicalDevelopmentOutlook_textView);
        windProb_textView = view.findViewById(R.id.windProb_textView);
        floater_textView = view.findViewById(R.id.floater_textView);

        floaterPause_imageButton = view.findViewById(R.id.floaterPause_imageButton);
        floaterPlay_imageButton = view.findViewById(R.id.floaterPlay_imageButton);
        attributionText = view.findViewById(R.id.attribution_textView);

        floater_linearLayout = view.findViewById(R.id.floater_linearLayout);
        windTSRadio = view.findViewById(R.id.windTS_radioButton);
        windHURRadio = view.findViewById(R.id.windHUR_radioButton);
        oceanSelect_spinner = view.findViewById(R.id.oceanSelect_spinner);

        severeScrollView = view.findViewById(R.id.tropicalNestedScroll_nestedScrollView);

        mDatabaseHelper = new DatabaseHelper(getActivity());


//        setAtlanticSelected(true);
//        setCentPacificSelected(false);
//        setEastPacificSelected(false);


        ArrayAdapter<CharSequence> oceanAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.oceans_array, android.R.layout.simple_spinner_item);
        oceanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oceanSelect_spinner.setAdapter(oceanAdapter);
        if (homeFragment.isNetworkAvailable()) {
            severeScrollView.setVisibility(View.VISIBLE);
            oceanSelect_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String passedPosition = parent.getItemAtPosition(position).toString();
                    Log.d(TAG, "Selected Basin: " + passedPosition);
                    if (passedPosition.equals("Atlantic")) {
                        setAtlanticSelected(true);
                        setCentPacificSelected(false);
                        setEastPacificSelected(false);

                    } else if (passedPosition.equals("Central Pacific")) {
                        setAtlanticSelected(false);
                        setAtlanticSelected(false);
                        setCentPacificSelected(true);
                        setEastPacificSelected(false);

                    } else if (passedPosition.equals("Eastern Pacific")) {
                        setAtlanticSelected(false);
                        setCentPacificSelected(false);
                        setEastPacificSelected(true);


                    } else {
                        Toast.makeText(parent.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "GOT HURRICANE DATA");
                    getHurricaneData();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback

                }
            });
        } else {
            severeScrollView.setVisibility(View.GONE);
        }


        attributionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nhc.noaa.gov/gtwo.php"));
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


    public void CreateTropicalMaps(String ATCF, String hurName) {
        Log.d(TAG, "TROPICAL MAPS CREATED");

        final String basinString = ATCF.substring(0, 2);
        final String floaterString = ATCF.substring(2, 4) + ATCF.substring(0, 1);

        floater_textView.setText(hurName + " Infrared");
        windProb_textView.setText(hurName + " Wind Speed Probability");
        visableWebHelper(floaterWebView, "https://www.ssd.noaa.gov/PS/TROP/floaters/" + floaterString + "/imagery/rbtop_lalo-animated.gif");
        if (basinString.equals("AL")) {
            floater_linearLayout.setVisibility(View.GONE);
            visableWebHelper(windTSWebView, "https://www.nhc.noaa.gov/storm_graphics/" + "AT" + ATCF.substring(2, 4) + "/refresh/" + ATCF + "_wind_probs_34_F072+png/png");
            visableWebHelper(windHURWebView, "https://www.nhc.noaa.gov/storm_graphics/" + "AT" + ATCF.substring(2, 4) + "/refresh/" + ATCF + "_wind_probs_64_F072+png/png");
            visableWebHelper(fiveDayDevelopmentWebView, "https://www.nhc.noaa.gov/storm_graphics/" + "AT" + ATCF.substring(2, 4) + "/refresh/" + ATCF + "_5day_cone_with_line_and_wind+png/0_5day_cone_with_line_and_wind.png");
            visableWebHelper(sevenDayRainOutlookWebview, "https://www.nhc.noaa.gov/storm_graphics/" +  "AT" + ATCF.substring(2, 4) + "/" + ATCF.substring(0,4) + ATCF.substring(6,8) + "WPCQPF.gif");

        } else {
            floater_linearLayout.setVisibility(View.VISIBLE);
            visableWebHelper(windTSWebView, "https://www.nhc.noaa.gov/storm_graphics/" + ATCF.substring(0, 4) + "/refresh/" + ATCF + "_wind_probs_34_F072+png/png");
            visableWebHelper(windHURWebView, "https://www.nhc.noaa.gov/storm_graphics/" + ATCF.substring(0, 4) + "/refresh/" + ATCF + "_wind_probs_64_F072+png/png");
            visableWebHelper(fiveDayDevelopmentWebView, "https://www.nhc.noaa.gov/storm_graphics/" + ATCF.substring(0, 4) + "/refresh/" + ATCF + "_5day_cone_with_line_and_wind+png/0_5day_cone_with_line_and_wind.png");
            visableWebHelper(sevenDayRainOutlookWebview, "https://www.nhc.noaa.gov/storm_graphics/" + ATCF.substring(0, 4) + "/" + ATCF.substring(0,4) + ATCF.substring(6,8) + "WPCQPF.gif");


        }

//https://www.nhc.noaa.gov/storm_graphics/AT05/refresh/AL052019_3day_earliest_reasonable_toa_34+png/_earliest_reasonable_toa_34.png
        Log.d(TAG, "CreateTropicalMaps: https://www.ssd.noaa.gov/PS/TROP/floaters/" + floaterString + "/imagery/rbtop_lalo-animated.gif");
        Log.d(TAG, "CreateTropicalMaps: https://www.nhc.noaa.gov/storm_graphics/" +  "AT" + ATCF.substring(2, 4) + "/" + ATCF.substring(0,4) + ATCF.substring(6,8) + "WPCQPF.gif");
        //https://www.nhc.noaa.gov/storm_graphics/AT05/AL0519WPCQPF.gif                                                 //7 DAY RAIN TOTALS
//https://www.nhc.noaa.gov/storm_graphics/AT05/AL052019_most_likely_toa_34.png                                          //MOST LIKELY TROPICAL STORM FORCE WIND WITH DATE LABELS
        //https://cdn.star.nesdis.noaa.gov/GOES16/ABI/SECTOR/taw/GEOCOLOR/1800x1080.jpg                                 //WIDE ATLANTIC COLOR


        tropicalDevelopmentOutlook_textView.setText("Five Day Development");
        floaterPause_imageButton.setVisibility(View.VISIBLE);
        floaterPlay_imageButton.setVisibility(View.GONE);

        floaterPause_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floaterWebView.onPause();
                floaterPause_imageButton.setVisibility(View.GONE);
                floaterPlay_imageButton.setVisibility(View.VISIBLE);
            }
        });

        floaterPlay_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floaterWebView.onResume();
                floaterPause_imageButton.setVisibility(View.VISIBLE);
                floaterPlay_imageButton.setVisibility(View.GONE);
            }
        });


        windTSRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windTSWebView.setVisibility(View.VISIBLE);
                windHURWebView.setVisibility(View.GONE);
            }
        });
        windHURRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windTSWebView.setVisibility(View.GONE);
                windHURWebView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void visableWebHelper(WebView resource, String url) {
        resource.loadUrl(url);
        resource.getSettings().setSupportZoom(true);
        resource.getSettings().setLoadWithOverviewMode(true);
        resource.getSettings().setUseWideViewPort(true);
        resource.getSettings().setBuiltInZoomControls(true);
        resource.getSettings().setDisplayZoomControls(false);

        resource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                severeScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    private void getHurricaneData() {


        new Thread(new Runnable() {

            @Override
            public void run() {
                final List<String> hurCenter = new ArrayList();
                final List<String> hurtype = new ArrayList();
                final List<String> hurname = new ArrayList();
                final List<String> hurwallet = new ArrayList();
                final List<String> huratcf = new ArrayList();
                final List<String> hurdatetime = new ArrayList();
                final List<String> hurmovement = new ArrayList();
                final List<String> hurpressure = new ArrayList();
                final List<String> hurwind = new ArrayList();
                final List<String> hurheadline = new ArrayList();

                final Document docAL;
                final Document docCP;
                final Document docEP;

                try {

                    //"https://www.nhc.noaa.gov/archive/2019/al" + stormID  + "/al" + stormID +  "2019.fstadv." + stormPublicAdvisory + ".shtml"
                    //Document doc = Jsoup.connect("https://www.nhc.noaa.gov").get();


                    if (getAtlanticSelected() == true) {
                        docAL = Jsoup.connect("https://www.nhc.noaa.gov/gis-at.xml").get();
                        setCycloneData(docAL.select("item > nhc|Cyclone"));
                        Log.d(TAG, "ATLANTIC LOADED");

                    } else if (getCentPacificSelected() == true) {
                        docCP = Jsoup.connect("https://www.nhc.noaa.gov/gis-cp.xml").get();
                        setCycloneData(docCP.select("item > nhc|Cyclone"));
                        Log.d(TAG, "C PACIFIC LOADED");

                    } else if (getEastPacificSelected() == true) {
                        docEP = Jsoup.connect("https://www.nhc.noaa.gov/gis-ep.xml").get();
                        setCycloneData(docEP.select("item > nhc|Cyclone"));
                        Log.d(TAG, "E PACIFIC LOADED");
                    }

                    //severeScrollView.setVisibility(View.GONE);
                    if (!(getCycloneData().toString().equals(""))) {
                        //Log.d(TAG, "GOT CYCLONE DATA");
                        for (Element e : getCycloneData()) {
                            hurCenter.add(e.select("nhc|Cyclone > nhc|center").text());
                            hurtype.add(e.select("nhc|Cyclone > nhc|type").text());
                            hurname.add(e.select("nhc|Cyclone > nhc|name").text());
                            hurwallet.add(e.select("nhc|Cyclone > nhc|wallet").text());
                            huratcf.add(e.select("nhc|Cyclone > nhc|atcf").text());
                            hurdatetime.add(e.select("nhc|Cyclone > nhc|datetime").text());
                            hurmovement.add(e.select("nhc|Cyclone > nhc|movement").text());
                            hurpressure.add(e.select("nhc|Cyclone > nhc|pressure").text());
                            hurwind.add(e.select("nhc|Cyclone > nhc|wind").text());
                            hurheadline.add(e.select("nhc|Cyclone > nhc|headline").text());
                        }
                    } else {
                        hurname.add("NO ACTIVE STORMS");
                    }


//                    int maxLogSize = 1000;
//                    for(int i = 0; i <= testBuilder.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i+1) * maxLogSize;
//                        end = end > testBuilder.length() ? testBuilder.length() : end;
//                        Log.v(TAG, "TEST BUILDER: " + "\n\n\n\n\n" + testBuilder.substring(start, end));
//                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout hurricaneData_linearLayout = (LinearLayout) getActivity().findViewById(R.id.hurricaneData_linearLayout);
                            WebView fiveDayDevelopmentWebView = (WebView) getActivity().findViewById(R.id.development5Day_webView);
                            WebView oceanTempWebView = getActivity().findViewById(R.id.visableSeaTempsView_webView);
                            TextView tropicalDevelopmentOutlook_textView = getActivity().findViewById(R.id.tropicalDevelopmentOutlook_textView);

                            oceanTempWebView.setInitialScale(250);
                            oceanTempWebView.setScrollX(oceanTempWebView.getWidth() + oceanTempWebView.getWidth() / 2 + oceanTempWebView.getWidth() / 6);
                            oceanTempWebView.setScrollY(oceanTempWebView.getHeight() + oceanTempWebView.getHeight());
                            Log.d(TAG, "WEBVIEW " + oceanTempWebView.getScrollX() + ", " + oceanTempWebView.getScrollY());


                            visableWebHelper(oceanTempWebView, "https://www.ospo.noaa.gov/data/sst/contour/global_small.c.gif");
                            if (hurname.get(0).equals("NO ACTIVE STORMS")) {
                                hurricaneData_linearLayout.setVisibility(View.GONE);
                                tropicalDevelopmentOutlook_textView.setText("NO ACTIVE STORMS");
                                if (atlanticSelected) {
                                    visableWebHelper(fiveDayDevelopmentWebView, "https://www.nhc.noaa.gov/archive/xgtwo/atl/latest/two_atl_5d0.png");
                                    oceanTempWebView.setScrollX(oceanTempWebView.getWidth() + oceanTempWebView.getWidth() / 2 + oceanTempWebView.getWidth() / 6);
                                    oceanTempWebView.setScrollY(oceanTempWebView.getHeight() + oceanTempWebView.getHeight());

                                } else if (centPacificSelected) {
                                    visableWebHelper(fiveDayDevelopmentWebView, "https://www.nhc.noaa.gov/archive/xgtwo/cpac/latest/two_cpac_5d0.png");
                                    oceanTempWebView.setScrollX(oceanTempWebView.getWidth() / 2);
                                    oceanTempWebView.setScrollY(oceanTempWebView.getHeight() + oceanTempWebView.getHeight());

                                } else if (eastPacificSelected) {
                                    visableWebHelper(fiveDayDevelopmentWebView, "https://www.nhc.noaa.gov/archive/xgtwo/epac/latest/two_pac_5d0.png");
                                    oceanTempWebView.setScrollX(oceanTempWebView.getWidth());
                                    oceanTempWebView.setScrollY(oceanTempWebView.getHeight() + oceanTempWebView.getHeight());

                                }
                            } else {
                                hurricaneData_linearLayout.setVisibility(View.VISIBLE);
                            }

                            Spinner stormSelect_spinner = (Spinner) getActivity().findViewById(R.id.stormSelect_spinner);

                            ArrayAdapter<CharSequence> stormAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, hurname);
                            stormAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stormSelect_spinner.setAdapter(stormAdapter);

                            stormSelect_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                                    if (!(hurname.get(0).equals("NO ACTIVE STORMS"))) {

                                        DisplayHurricaneData(hurCenter, hurtype, hurname, hurwallet, huratcf, hurdatetime, hurmovement, hurpressure, hurwind, hurheadline, position);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    });

                } catch (IOException e) {
                    Log.d(TAG, "Error : " + (e.getMessage()));
                }


            }

        }).start();
    }

    public void DisplayHurricaneData(List<String> hurCenter, List<String> hurtype, List<String> hurname, List<String> hurwallet, List<String> huratcf, List<String> hurdatetime, List<String> hurmovement, List<String> hurpressure, List<String> hurwind, List<String> hurheadline, int position) {


        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();

        //gets data from city column
        while (data.moveToNext()) {
            listData.add(data.getString(1));
        }


        String hurClassification = "";
        if (hurtype.get(position).equals("TROPICAL STORM") || hurtype.get(position).equals("TROPICAL DEPRESSION")) {
            hurClassification = hurtype.get(position).toLowerCase();
            hurClassification = hurClassification.substring(0, 1).toUpperCase() + hurClassification.substring(1);
        } else {
            hurClassification = hurtype.get(position);
        }
        hurricaneTitle_textView.setText(hurClassification + " " + hurname.get(position));
        alertExpire_textView.setText(hurdatetime.get(position));
        windSpeed_textView.setText("\n" + hurwind.get(position).replace(" mph", "") + " " + utils.SPEED_UNITS);
        currentPressure_textView.setText("\n" + hurpressure.get(position));
        groundSpeed_textView.setText("\n" + hurmovement.get(position).replace(" mph", "") + " " + utils.SPEED_UNITS);
        stormSurge_textView.setText("");
        catagoryLevel_textView.setText("\n" + getHurricaneCatagory(hurwind.get(position)));
        currentLocation_textView.setText("\n" + getHurricaneDistance(hurCenter.get(position)) + " " + utils.DIST_UNITS + " away");

        hurricaneDescription_textView.setText(hurheadline.get(position) + "\n\n ID: "  + hurwallet.get(position) + " " + huratcf.get(position) + "\n\n COORDINATES: " + hurCenter.get(position));

        CreateTropicalMaps(huratcf.get(position), hurname.get(position));

    }

    public String getHurricaneCatagory(String maxWinds) {
        String catagory = "0";
        int maxWind = Integer.parseInt(maxWinds.replace(" mph", ""));
        windGust_textView.setText("\n" + homeFragment.d0f.format(Double.parseDouble(String.valueOf((Math.round(maxWind * 1.3)/5)*5))) + " " + utils.SPEED_UNITS);
        if (maxWind >= 157) {
            catagory = "Catagory 5";
        } else if (maxWind >= 130) {
            catagory = "Catagory 4";
        } else if (maxWind >= 111) {
            catagory = "Catagory 3";
        } else if (maxWind >= 96) {
            catagory = "Catagory 2";
        } else if (maxWind >= 74) {
            catagory = "Catagory 1";
        } else if (maxWind >= 39) {
            catagory = "TS";
        } else if (maxWind <= 38) {
            catagory = "TD";
        }
        return catagory;
    }

    //CALCULATES DISTANCE BETWEEN SEARCHED CITY AND STORM
    public String getHurricaneDistance(String hurcenter) {

        String[] splitLocation = hurcenter.replace(",", "").split("\\s+");

        double lat1 = homeFragment.utils.getLat();
        double lon1 = homeFragment.utils.getLon();
        double lat2 = Double.parseDouble(String.valueOf(splitLocation[0]));
        double lon2 = Double.parseDouble(String.valueOf(splitLocation[1]));
        String distance;

        Log.d(TAG, "COORDINATES: (" + lat1 + "," + lon1 + ")");
        distance = homeFragment.d0f.format(Double.parseDouble(String.valueOf(69 * Math.sqrt((Math.pow((lon2 - lon1), 2)) + (Math.pow((lat2 - lat1), 2)))))); //69 miles per degree latitude

        return distance;
    }


    Elements cycloneData = new Elements();
    Boolean atlanticSelected;
    Boolean centPacificSelected;
    Boolean eastPacificSelected;


    public Elements getCycloneData() {
        return cycloneData;
    }

    public void setCycloneData(Elements cycloneData) {
        this.cycloneData = cycloneData;
    }

    public Boolean getAtlanticSelected() {
        return atlanticSelected;
    }

    public void setAtlanticSelected(Boolean atlanticSelected) {
        this.atlanticSelected = atlanticSelected;
    }

    public Boolean getCentPacificSelected() {
        return centPacificSelected;
    }

    public void setCentPacificSelected(Boolean centPacificSelected) {
        this.centPacificSelected = centPacificSelected;
    }

    public Boolean getEastPacificSelected() {
        return eastPacificSelected;
    }

    public void setEastPacificSelected(Boolean eastPacificSelected) {
        this.eastPacificSelected = eastPacificSelected;
    }
}
