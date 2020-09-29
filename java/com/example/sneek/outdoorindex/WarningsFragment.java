package com.example.sneek.outdoorindex;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapters.CityListAdapter;
import Adapters.WarningsAdapter;
import model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WarningsFragment extends Fragment {

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;
    private static final String TAG = "WARNINGFRAG";

    final List<String> alertTitleArr = new ArrayList();
    final List<String> severityArr = new ArrayList();
    final List<String> certaintyArr = new ArrayList();
    final List<String> expire = new ArrayList();
    final List<String> desc = new ArrayList<>();
    final List<String> instruction = new ArrayList<>();
    final List<String> areaDesc = new ArrayList<>();
    final List<String> state = new ArrayList();
    final List<String> id = new ArrayList<>();
    final List<String> geofence = new ArrayList<>();
    final List<Integer> alertIconArr = new ArrayList();
    final List<String> colorArr = new ArrayList<>();

    final List<String> NWSalertTitle = new ArrayList();
    final List<String> NWSseverity = new ArrayList();
    final List<String> NWScertainty = new ArrayList();
    final List<String> NWSexpire = new ArrayList();
    final List<String> NWSstate = new ArrayList();
    final List<String> NWSareaDesc = new ArrayList<>();
    final List<String> NWSID = new ArrayList<>();
    final List<String> NWSGeofence = new ArrayList<>();

    final List<String> expireArr = new ArrayList<>();
    final List<Integer> expireArrUNIX = new ArrayList<>();
    final List<String> stateArr = new ArrayList<>();
    final List<String> descArr = new ArrayList<>();
    final List<String> areaDescArr = new ArrayList<>();
    final String[] prec0Arr = {"Hurricane Warning", "Tropical Storm Warning", "Extreme Wind Warning", "Tornado Warning", "Hurricane Watch", "Storm Surge Warning", "Avalanche Warning", "Civil Danger Warning", "Earthquake Warning", "Extreme Cold Warning", "Civil Emergency Message", "Evacuation - Immediate", "Extreme Fire Danger", "Gale Warning", "Shelter In Place Warning", "High Wind Warning", "Hurricane Force Wind Warning", "Law Enforcement Warning", "Local Area Emergency", "Tsunami Warning", "Nuclear Power Plant Warning", "Radiological Hazard Warning", "Typhoon Warning", "Volcano Warning"};
    final String[] prec1Arr = {"Avalanche Advisory", "Avalanche Watch", "Extreme Cold Watch", "Excessive Heat Warning", "Gale Watch", "Hazardous Materials Warning", "High Wind Watch", "Hurricane Force Wind Watch", "Storm Surge Watch", "Tornado Watch", "Tropical Storm Watch", "Tsunami Advisory", "Tsunami Watch", "Typhoon Watch", "Wind Chill Warning"};
    final String[] prec2Arr = {"Blizzard Warning", "Dust Storm Warning", "Hard Freeze Warning", "Hurricane Local Statement", "Heavy Freezing Spray Warning", "Ice Storm Warning", "Hazardous Weather Outlook", "Severe Thunderstorm Warning", "Snow Squall Warning", "Storm Warning", "Winter Storm Warning", "Tropical Storm Local Statement", "Lake Effect Snow Warning"};
    final String[] prec3Arr = {"Coastal Flood Warning", "Flash Flood Warning", "Lakeshore Flood Warning", "Flood Warning"};
    final String[] prec4Arr = {"Air Quality Alert", "Air Stagnation Advisory", "Ashfall Warning", "Beach Hazards Statement", "Blizzard Watch", "Blowing Dust Warning", "Brisk Wind Advisory", "Dense Fog Advisory", "Dense Smoke Advisory", "Excessive Heat Watch", "Child Abduction Emergency", "Freeze Warning", "Freeze Watch", "Freezing Fog Advisory", "Freezing Rain Advisory", "Frost Advisory", "Hard Freeze Watch", "Hazardous Seas Warning", "Hazardous Seas Watch", "Heat Advisory", "Heavy Freezing Spray Watch", "High Surf Warning", "Lake Effect Snow Advisory", "Lake Effect Snow Watch", "Lake Wind Advisory", "Marine Weather Statement", "Rip Current Statement", "Severe Thunderstorm Watch", "Short Term Forecast", "Small Craft Advisory For Hazardous Seas", "Small Craft Advisory For Rough Bar", "Small Craft Advisory For Winds", "Special Marine Warning", "Special Weather Statement", "Storm Watch", "Tropical Depression Local Statement", "Typhoon Local Statement", "Wind Advisory", "Wind Chill Advisory", "Wind Chill Watch", "Winter Storm Watch", "Winter Weather Advisory"};
    final String[] prec5Arr = {"Arroyo And Small Stream Flood Advisory", "Coastal Flood Advisory", "Coastal Flood Statement", "Coastal Flood Watch", "Flash Flood Statement", "Flood Advisory", "Flood Statement", "Flood Watch", "Freezing Spray Advisory", "Lakeshore Flood Advisory", "Lakeshore Flood Statement", "Lakeshore Flood Watch", "Low Water Advisory", "Small Stream Flood Advisory", "Urban And Small Stream Flood Advisory"};
    final String[] prec6Arr = {"Ashfall Advisory", "Blowing Dust Advisory", "Dust Advisory", "Fire Warning", "High Surf Advisory", "Hydrologic Advisory", "Hydrologic Outlook", "Small Craft Advisory"};
    Handler handler = new Handler();
    Runnable refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.warnings_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh = new Runnable() {
            @Override
            public void run() {
                alertTitleArr.clear();
                severityArr.clear();
                certaintyArr.clear();
                expire.clear();
                desc.clear();
                instruction.clear();
                areaDesc.clear();
                id.clear();
                state.clear();
                geofence.clear();
                colorArr.clear();

                NWSalertTitle.clear();
                stateArr.clear();
                NWScertainty.clear();
                expireArr.clear();
                expireArrUNIX.clear();
                descArr.clear();
                areaDescArr.clear();
                alertIconArr.clear();
                NWSGeofence.clear();
                NWSID.clear();
                instruction.clear();


                try {
                    getWarnings();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(refresh, 1000 * 60 * 2);
                Log.d(TAG, "REFRESHED WARNINGS RUNNABLE");
            }
        };
        handler.post(refresh);
    }


    private void getWarnings() throws InterruptedException {


        new Thread(new Runnable() {

            @Override
            public void run() {

                //Log.d(TAG, "START WARNING THREAD");
                final Document docNWS;
                Document docDESC;
                Elements descElements = null;
                String parseState;
                String parseArea = "";
                String[] parseAreaArr;

                try {

                    OkHttpClient okHttp = new OkHttpClient();
                    Request request = new Request.Builder().url("https://alerts.weather.gov/cap/us.php?x=1").get().build();
                    docNWS = Jsoup.parse(okHttp.newCall(request).execute().body().string());

//                    Log.d(TAG, "Alerts Started");
//                    docNWS = Jsoup.connect("https://alerts.weather.gov/cap/us.php?x=1").get();
                    Elements NWSEntry = docNWS.select("entry");

                    //Log.d(TAG, "RETRIEVED WARNING ELEMENTS");

                    for (Element e : NWSEntry) {
                        if (e.text().contains("Severe") || e.text().contains("Extreme")) {
                            if (!e.text().contains("Red Flag Warning") && !e.text().contains("Fire Weather Watch") && !e.text().contains("Flash Flood Watch")/* && !e.text().contains("Severe Weather Statement")*/) {
                                certaintyArr.add(e.select("entry > cap|certainty").text());
                                severityArr.add(e.select("entry > cap|severity").text());
                                alertTitleArr.add(e.select("entry > cap|event").text());
                                expire.add(e.select("entry > cap|expires").text());
                                areaDesc.add(e.select("entry > cap|areaDesc").text());
                                state.add(e.select("cap|geocode > value").last().text());
                                id.add(e.select("entry > id").text());
                                geofence.add(e.select("entry > cap|polygon").text());

                            }
                        }
                    }
                    //Log.d(TAG, "PARSED WARNING ELEMENTS");
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec0Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#C80000");
                            //Log.d(TAG, "PREC 0 REACHED");
                        }
                    }
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec1Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#FF9696");
                            //Log.d(TAG, "PREC 1 REACHED");
                        }
                    }
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec2Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#FFF314");
                            //Log.d(TAG, "PREC 2 REACHED");
                        }
                    }
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec3Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#33FF14");
                            //Log.d(TAG, "PREC 3 REACHED");
                        }
                    }
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec4Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#FFCA81");
                            //Log.d(TAG, "PREC 4 REACHED");
                        }
                    }
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec5Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#A1FFA5");
                            //Log.d(TAG, "PREC 5 REACHED");
                        }
                    }
                    for (int i = 0; i < alertTitleArr.size(); i++) {
                        if (Arrays.asList(prec6Arr).contains(alertTitleArr.get(i))) {
                            getElementValues(certaintyArr.get(i), severityArr.get(i), alertTitleArr.get(i), expire.get(i), areaDesc.get(i), state.get(i), id.get(i), geofence.get(i), "#FFCA81");
                            //Log.d(TAG, "PREC 6 REACHED");
                        }
                    }
                    //Log.d(TAG, "FOR LOOPS FINISH WARNING");


                    if (NWSexpire.size() != 0) {
                        for (int i = 0; i < NWSexpire.size(); i++) {
                            String expire = NWSexpire.get(i);

                            Date date = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ssXXX", Locale.getDefault()).parse(expire);
                            long unixtime = date.getTime();
                            //Log.d(TAG, "DATE: " + date + " " + unixtime/1000);
                            String arrDate = new SimpleDateFormat("EEE hh:mm a", Locale.getDefault()).format(unixtime);

                            expireArr.add(arrDate);
                            expireArrUNIX.add((int) (unixtime / 1000));
                        }
                    }
                    //Log.d(TAG, "FORMAT EXPIRE FINISH");

                    if (NWSstate.size() != 0) {
                        for (int i = 0; i < NWSstate.size(); i++) {
                            parseState = NWSstate.get(i).substring(0, 2);

                            stateArr.add(STATE_MAP.get(parseState));

                        }
                    }
                    if (NWSareaDesc.size() != 0) {
                        for (int i = 0; i < NWSareaDesc.size(); i++) {
                            parseAreaArr = NWSareaDesc.get(i).split(";");
                            if (parseAreaArr.length >= 2) {
                                parseArea = parseAreaArr[0] + ", " + parseAreaArr[1];
                            } else {
                                parseArea = parseAreaArr[0];
                            }
                            areaDescArr.add(parseArea);
                        }
                    }

                    if (NWSalertTitle.size() != 0) {
                        for (int i = 0; i < NWSalertTitle.size(); i++) {
                            alertIconArr.add(Integer.parseInt(WEATHER_SEVERITY.get(NWSalertTitle.get(i))));
                        }
                    }


                    //THIS SETS DESCRIPTION TO NOTHING AS TO ALLOW HOMEFRAGMENT TO PASS CURRENT LOCATION ALERTS.
                    //DESCRIPTIONS ARE PARSED USING WEATHER ALERT DIALOG CLASS.
                    for (int i = 0; i < NWSID.size(); i++) {
                        descArr.add("");
                    }


                    //Log.d(TAG, "FORMAT DESC FINISH");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.natWeatherAlerts_recyclerView);
                            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                            //recyclerView.setLayoutManager(layoutManager);
                            WarningsAdapter adapter = new WarningsAdapter(getActivity(), NWSalertTitle, stateArr, NWScertainty, expireArr, expireArrUNIX, descArr, areaDescArr, alertIconArr, NWSGeofence, colorArr, NWSID);
                            recyclerView.setAdapter(adapter);
                            Log.d(TAG, "END WARNING THREAD");

                        }
                    });

                } catch (IOException e) {
                    Log.d(TAG, "Error : " + (e.getMessage()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    private void getElementValues(String certainty, String severity, String alertTitle, String expire, String areaDesc, String state, String id, String geofence, String color) {
        NWScertainty.add(certainty);
        NWSseverity.add(severity);
        NWSalertTitle.add(alertTitle);
        NWSexpire.add(expire);
        NWSareaDesc.add(areaDesc);
        NWSstate.add(state);
        NWSID.add(id);
        NWSGeofence.add(geofence);
        colorArr.add(color);
    }


    public static final Map<String, String> WEATHER_SEVERITY;

    static {
        WEATHER_SEVERITY = new HashMap<String, String>();
        WEATHER_SEVERITY.put("Hurricane Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Tropical Storm Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Extreme Wind Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Tornado Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Hurricane Watch", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Storm Surge Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Air Quality Alert", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Air Stagnation Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Arroyo And Small Stream Flood Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Ashfall Advisory", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Ashfall Warning", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Avalanche Advisory", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Avalanche Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Avalanche Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Beach Hazards Statement", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Blizzard Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Blizzard Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Blowing Dust Advisory", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Blowing Dust Warning", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Brisk Wind Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Child Abduction Emergency", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Civil Danger Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Civil Emergency Message", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Coastal Flood Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Coastal Flood Statement", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Coastal Flood Warning", String.valueOf(R.drawable.ic_warning_green));
        WEATHER_SEVERITY.put("Coastal Flood Watch", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Dense Fog Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Dense Smoke Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Dust Advisory", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Dust Storm Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Earthquake Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Evacuation - Immediate", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Excessive Heat Warning", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Excessive Heat Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Extreme Cold Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Extreme Cold Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Extreme Fire Danger", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Fire Warning", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Flash Flood Statement", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Flash Flood Warning", String.valueOf(R.drawable.ic_warning_green));
        WEATHER_SEVERITY.put("Flood Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Flood Statement", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Flood Warning", String.valueOf(R.drawable.ic_warning_green));
        WEATHER_SEVERITY.put("Flood Watch", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Freeze Warning", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Freeze Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Freezing Fog Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Freezing Rain Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Freezing Spray Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Frost Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Gale Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Gale Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Hard Freeze Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Hard Freeze Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Hazardous Materials Warning", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Hazardous Seas Warning", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Hazardous Seas Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Hazardous Weather Outlook", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Heat Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Heavy Freezing Spray Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Heavy Freezing Spray Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("High Surf Advisory", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("High Surf Warning", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("High Wind Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("High Wind Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Hurricane Force Wind Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Hurricane Force Wind Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Hurricane Local Statement", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Hydrologic Advisory", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Hydrologic Outlook", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Ice Storm Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Lake Effect Snow Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Lake Effect Snow Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Lake Effect Snow Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Lake Wind Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Lakeshore Flood Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Lakeshore Flood Statement", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Lakeshore Flood Warning", String.valueOf(R.drawable.ic_warning_green));
        WEATHER_SEVERITY.put("Lakeshore Flood Watch", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Law Enforcement Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Local Area Emergency", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Low Water Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Marine Weather Statement", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Nuclear Power Plant Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Radiological Hazard Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Rip Current Statement", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Severe Thunderstorm Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Severe Thunderstorm Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Shelter In Place Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Short Term Forecast", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Small Craft Advisory", String.valueOf(R.drawable.ic_warning_blank));
        WEATHER_SEVERITY.put("Small Craft Advisory For Hazardous Seas", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Small Craft Advisory For Rough Bar", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Small Craft Advisory For Winds", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Small Stream Flood Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Snow Squall Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Special Marine Warning", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Special Weather Statement", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Storm Surge Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Storm Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Storm Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Tornado Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Tropical Depression Local Statement", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Tropical Storm Local Statement", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Tropical Storm Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Tsunami Advisory", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Tsunami Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Tsunami Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Typhoon Local Statement", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Typhoon Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Typhoon Watch", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Urban And Small Stream Flood Advisory", String.valueOf(R.drawable.ic_warning_light_green));
        WEATHER_SEVERITY.put("Volcano Warning", String.valueOf(R.drawable.ic_warning_red));
        WEATHER_SEVERITY.put("Wind Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Wind Chill Advisory", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Wind Chill Warning", String.valueOf(R.drawable.ic_warning_light_red));
        WEATHER_SEVERITY.put("Wind Chill Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Winter Storm Warning", String.valueOf(R.drawable.ic_warning_yellow));
        WEATHER_SEVERITY.put("Winter Storm Watch", String.valueOf(R.drawable.ic_warning_orange));
        WEATHER_SEVERITY.put("Winter Weather Advisory", String.valueOf(R.drawable.ic_warning_orange));
    }


    public static final Map<String, String> STATE_MAP;

    static {
        STATE_MAP = new HashMap<String, String>();
        STATE_MAP.put("AL", "Alabama");
        STATE_MAP.put("AK", "Alaska");
        STATE_MAP.put("AB", "Alberta");
        STATE_MAP.put("AZ", "Arizona");
        STATE_MAP.put("AR", "Arkansas");
        STATE_MAP.put("BC", "British Columbia");
        STATE_MAP.put("CA", "California");
        STATE_MAP.put("CO", "Colorado");
        STATE_MAP.put("CT", "Connecticut");
        STATE_MAP.put("DE", "Delaware");
        STATE_MAP.put("DC", "District Of Columbia");
        STATE_MAP.put("FL", "Florida");
        STATE_MAP.put("GA", "Georgia");
        STATE_MAP.put("GU", "Guam");
        STATE_MAP.put("HI", "Hawaii");
        STATE_MAP.put("ID", "Idaho");
        STATE_MAP.put("IL", "Illinois");
        STATE_MAP.put("IN", "Indiana");
        STATE_MAP.put("IA", "Iowa");
        STATE_MAP.put("KS", "Kansas");
        STATE_MAP.put("KY", "Kentucky");
        STATE_MAP.put("LA", "Louisiana");
        STATE_MAP.put("ME", "Maine");
        STATE_MAP.put("MB", "Manitoba");
        STATE_MAP.put("MD", "Maryland");
        STATE_MAP.put("MA", "Massachusetts");
        STATE_MAP.put("MI", "Michigan");
        STATE_MAP.put("MN", "Minnesota");
        STATE_MAP.put("MS", "Mississippi");
        STATE_MAP.put("MO", "Missouri");
        STATE_MAP.put("MT", "Montana");
        STATE_MAP.put("NE", "Nebraska");
        STATE_MAP.put("NV", "Nevada");
        STATE_MAP.put("NB", "New Brunswick");
        STATE_MAP.put("NH", "New Hampshire");
        STATE_MAP.put("NJ", "New Jersey");
        STATE_MAP.put("NM", "New Mexico");
        STATE_MAP.put("NY", "New York");
        STATE_MAP.put("NF", "Newfoundland");
        STATE_MAP.put("NC", "North Carolina");
        STATE_MAP.put("ND", "North Dakota");
        STATE_MAP.put("NT", "Northwest Territories");
        STATE_MAP.put("NS", "Nova Scotia");
        STATE_MAP.put("NU", "Nunavut");
        STATE_MAP.put("OH", "Ohio");
        STATE_MAP.put("OK", "Oklahoma");
        STATE_MAP.put("ON", "Ontario");
        STATE_MAP.put("OR", "Oregon");
        STATE_MAP.put("PA", "Pennsylvania");
        STATE_MAP.put("PE", "Prince Edward Island");
        STATE_MAP.put("PR", "Puerto Rico");
        STATE_MAP.put("QC", "Quebec");
        STATE_MAP.put("RI", "Rhode Island");
        STATE_MAP.put("SK", "Saskatchewan");
        STATE_MAP.put("SC", "South Carolina");
        STATE_MAP.put("SD", "South Dakota");
        STATE_MAP.put("TN", "Tennessee");
        STATE_MAP.put("TX", "Texas");
        STATE_MAP.put("UT", "Utah");
        STATE_MAP.put("VT", "Vermont");
        STATE_MAP.put("VI", "Virgin Islands");
        STATE_MAP.put("VA", "Virginia");
        STATE_MAP.put("WA", "Washington");
        STATE_MAP.put("WV", "West Virginia");
        STATE_MAP.put("WI", "Wisconsin");
        STATE_MAP.put("WY", "Wyoming");
        STATE_MAP.put("YT", "Yukon Territory");
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {


        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
}
