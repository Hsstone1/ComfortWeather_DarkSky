package data;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

import model.Weather;




public class JSONWeatherParser {


    public static Weather getWeather(String data) throws JSONException {

        String TAG = "CREATION";

        Weather weather = new Weather();
        JSONObject jObj = new JSONObject(data);


        //currently
        JSONObject currentlyObj = getObject("currently", jObj);

        weather.currentCondition.setTime(getInt("time", currentlyObj));
        weather.currentCondition.setSummary(getString("summary", currentlyObj));
        weather.currentCondition.setIcon(getString("icon", currentlyObj));
        if (currentlyObj.has("nearestStormDistance")) {
            weather.currentCondition.setNearestStormDistance(getInt("nearestStormDistance", currentlyObj));
        } else {
            weather.currentCondition.setNearestStormDistance(0);
        }
        if (currentlyObj.has("nearestStormBearing")) {
            weather.currentCondition.setNearestStormBearing(getInt("nearestStormBearing", currentlyObj));
        } else {
            weather.currentCondition.setNearestStormBearing(-1);
        }
        weather.currentCondition.setPrecipIntensity(getFloat("precipIntensity", currentlyObj));
        if (currentlyObj.has("precipIntensityError")) {
            weather.currentCondition.setPrecipIntensityError(getFloat("precipIntensityError", currentlyObj));
        } else {
            weather.currentCondition.setPrecipIntensityError(0);
        }
        weather.currentCondition.setPrecipProbability(getFloat("precipProbability", currentlyObj));
        if (currentlyObj.has("precipType")) {
            weather.currentCondition.setPrecipType(getString("precipType", currentlyObj));
        } else {
            weather.currentCondition.setPrecipType("");
        }
        weather.currentCondition.setTemperature(getFloat("temperature", currentlyObj));
        weather.currentCondition.setApparentTemperature(getFloat("apparentTemperature", currentlyObj));
        weather.currentCondition.setDewPoint(getFloat("dewPoint", currentlyObj));
        weather.currentCondition.setHumidity(getFloat("humidity", currentlyObj));
        weather.currentCondition.setPressure(getFloat("pressure", currentlyObj));
        weather.currentCondition.setWindSpeed(getFloat("windSpeed", currentlyObj));
        weather.currentCondition.setWindGust(getFloat("windGust", currentlyObj));
        weather.currentCondition.setWindBearing(getInt("windBearing", currentlyObj));
        weather.currentCondition.setCloudCover(getFloat("cloudCover", currentlyObj));
        weather.currentCondition.setUvIndex(getInt("uvIndex", currentlyObj));
        weather.currentCondition.setVisibility(getFloat("visibility", currentlyObj));
        weather.currentCondition.setOzone(getFloat("ozone", currentlyObj));


        //Log.d(TAG, "CURRENTLY RAIN TYPE: " + weather.currentCondition.getPrecipType());
        //Log.d(TAG, "CURRENTLY INTENSE ERROR: " + weather.currentCondition.getPrecipIntensityError());
        //Log.d(TAG, "CURRENTLY STORM BEARING: " + weather.currentCondition.getNearestStormBearing());
        //Log.d(TAG, "CURRENTLY STORM DISTANCE: " + weather.currentCondition.getNearestStormDistance());
        //Log.d("CREATION", "Currently Passed ");


        //minutely

        if (jObj.has("minutely")) {
            String minutelySummary;
            String minutelyIcon;
            List minutelyTime = new ArrayList();
            List minutelyPrecipIntensity = new ArrayList();
            List minutelyPrecipIntensityError = new ArrayList();
            List minutelyPrecipProbability = new ArrayList();
            List minutelyPrecipType = new ArrayList();

            JSONObject minutelyObj = getObject("minutely", jObj);
            weather.minutelyCondition.setSummary(getString("summary", minutelyObj));
            weather.minutelyCondition.setIcon(getString("icon", minutelyObj));

            JSONArray mArr = minutelyObj.getJSONArray("data");
            for (int i = 0; i <= 60; i++) {
                JSONObject JSONDataMinutely = mArr.getJSONObject(i);
                minutelyTime.add((getInt("time", JSONDataMinutely)));
                minutelyPrecipIntensity.add(getFloat("precipIntensity", JSONDataMinutely));
                minutelyPrecipProbability.add(getFloat("precipProbability", JSONDataMinutely));
                if (JSONDataMinutely.has("precipIntensityError")) {
                    minutelyPrecipIntensityError.add(getFloat("precipIntensityError", JSONDataMinutely));
                } else {
                    minutelyPrecipIntensityError.add("");
                }
                if (JSONDataMinutely.has("precipType")) {
                    minutelyPrecipType.add(getString("precipType", JSONDataMinutely));
                } else {
                    minutelyPrecipType.add("");
                }
            }

            weather.minutelyCondition.setTime(minutelyTime);
            weather.minutelyCondition.setPrecipIntensity(minutelyPrecipIntensity);
            weather.minutelyCondition.setPrecipProbability(minutelyPrecipProbability);
            weather.minutelyCondition.setPrecipIntensityError(minutelyPrecipIntensityError);
            weather.minutelyCondition.setPrecipType(minutelyPrecipType);


            //Log.d(TAG, "MINUTELY RAIN TYPE: " + weather.minutelyCondition.getPrecipType());
            //Log.d(TAG, "MINUTELY INTENSE ERROR: " + weather.minutelyCondition.getPrecipIntensityError());
            Log.d("CREATION", "Minutely Passed ");

        } else {
            List minutelyTime = new ArrayList();
            List minutelyPrecipIntensity = new ArrayList();
            List minutelyPrecipIntensityError = new ArrayList();
            List minutelyPrecipProbability = new ArrayList();
            List minutelyPrecipType = new ArrayList();

            for (int i = 0; i <= 60; i++) {
                minutelyTime.add(0);
                minutelyPrecipIntensity.add(0);
                minutelyPrecipProbability.add(0);
                minutelyPrecipIntensityError.add(0);
                minutelyPrecipType.add("");
            }
            weather.minutelyCondition.setSummary("");
            weather.minutelyCondition.setIcon("");
            weather.minutelyCondition.setTime(minutelyTime);
            weather.minutelyCondition.setPrecipIntensity(minutelyPrecipIntensity);
            weather.minutelyCondition.setPrecipProbability(minutelyPrecipProbability);

            weather.minutelyCondition.setPrecipIntensityError(minutelyPrecipIntensityError);
            weather.minutelyCondition.setPrecipType(minutelyPrecipType);


            //Log.d(TAG, "MINUTELY RAIN TYPE: " + weather.minutelyCondition.getPrecipType());
            //Log.d(TAG, "MINUTELY INTENSE ERROR: " + weather.minutelyCondition.getPrecipIntensityError());
            Log.d("CREATION", "Minutely Passed ");
        }


        if (jObj.has("hourly")) {
            //hourly
            List hourlyFullSummary = new ArrayList();
            List hourlyFullIcon = new ArrayList();
            List hourlyTime = new ArrayList();
            List hourlySummary = new ArrayList();
            List hourlyIcon = new ArrayList();
            List hourlyPrecipIntensity = new ArrayList();
            List hourlyPrecipProbability = new ArrayList();
            List hourlyPrecipType = new ArrayList();
            List hourlyTemperature = new ArrayList();
            List hourlyApparentTemperature = new ArrayList();
            List hourlyDewPoint = new ArrayList();
            List hourlyHumidity = new ArrayList();
            List hourlyPressure = new ArrayList();
            List hourlyWindSpeed = new ArrayList();
            List hourlyWindGust = new ArrayList();
            List hourlyWindBearing = new ArrayList();
            List hourlyCloudCover = new ArrayList();
            List hourlyUvIndex = new ArrayList();
            List hourlyVisibility = new ArrayList();
            List hourlyOzone = new ArrayList();

            JSONObject hourlyObj = getObject("hourly", jObj);
            weather.hourlyCondition.setFullSummary(getString("summary", hourlyObj));
            weather.hourlyCondition.setFullIcon(getString("icon", hourlyObj));

            JSONArray hArr = hourlyObj.getJSONArray("data");
            for (int j = 0; j < 169; j++) {
                JSONObject JSONDataHourly = hArr.getJSONObject(j);
                hourlyTime.add(getInt("time", JSONDataHourly));
                hourlySummary.add(getString("summary", JSONDataHourly));
                hourlyIcon.add(getString("icon", JSONDataHourly));
                hourlyPrecipIntensity.add(getFloat("precipIntensity", JSONDataHourly));
                hourlyPrecipProbability.add(getFloat("precipProbability", JSONDataHourly));
                if (JSONDataHourly.has("precipType")) {
                    hourlyPrecipType.add(getString("precipType", JSONDataHourly));
                } else {
                    hourlyPrecipType.add("");
                }
                hourlyTemperature.add(getFloat("temperature", JSONDataHourly));
                hourlyApparentTemperature.add(getFloat("apparentTemperature", JSONDataHourly));
                hourlyDewPoint.add(getFloat("dewPoint", JSONDataHourly));
                hourlyHumidity.add(getFloat("humidity", JSONDataHourly));
                hourlyPressure.add(getFloat("pressure", JSONDataHourly));
                hourlyWindSpeed.add(getFloat("windSpeed", JSONDataHourly));
                hourlyWindGust.add(getFloat("windGust", JSONDataHourly));
                hourlyWindBearing.add(getInt("windBearing", JSONDataHourly));
                hourlyCloudCover.add(getFloat("cloudCover", JSONDataHourly));
                hourlyUvIndex.add(getInt("uvIndex", JSONDataHourly));
                hourlyVisibility.add(getFloat("visibility", JSONDataHourly));
                hourlyOzone.add(getFloat("ozone", JSONDataHourly));

            }

            weather.hourlyCondition.setTime(hourlyTime);
            weather.hourlyCondition.setSummary(hourlySummary);
            weather.hourlyCondition.setIcon(hourlyIcon);
            weather.hourlyCondition.setPrecipIntensity(hourlyPrecipIntensity);
            weather.hourlyCondition.setPrecipProbability(hourlyPrecipProbability);

            weather.hourlyCondition.setPrecipType(hourlyPrecipType);
            weather.hourlyCondition.setTemperature(hourlyTemperature);
            weather.hourlyCondition.setApparentTemperature(hourlyApparentTemperature);
            weather.hourlyCondition.setDewPoint(hourlyDewPoint);
            weather.hourlyCondition.setHumidity(hourlyHumidity);
            weather.hourlyCondition.setPressure(hourlyPressure);
            weather.hourlyCondition.setWindSpeed(hourlyWindSpeed);
            weather.hourlyCondition.setWindGust(hourlyWindGust);
            weather.hourlyCondition.setWindBearing(hourlyWindBearing);
            weather.hourlyCondition.setCloudCover(hourlyCloudCover);
            weather.hourlyCondition.setUvIndex(hourlyUvIndex);
            weather.hourlyCondition.setVisibility(hourlyVisibility);
            weather.hourlyCondition.setOzone(hourlyOzone);

            Log.d("CREATION", "Hourly Passed ");
        }


        //daily
        if (jObj.has("daily")) {
            List Time = new ArrayList();
            List Summary = new ArrayList();
            List Icon = new ArrayList();
            List SunriseTime = new ArrayList();
            List SunsetTime = new ArrayList();
            List MoonPhase = new ArrayList();
            List PrecipIntensity = new ArrayList();
            List PrecipIntensityMax = new ArrayList();
            List PrecipIntensityMaxTime = new ArrayList();
            List PrecipProbability = new ArrayList();
            List PrecipType = new ArrayList();
            List TemperatureHigh = new ArrayList();
            List TemperatureHighTime = new ArrayList();
            List TemperatureLow = new ArrayList();
            List TemperatureLowTime = new ArrayList();
            List ApparentTemperatureHigh = new ArrayList();
            List ApparentTemperatureHighTime = new ArrayList();
            List ApparentTemperatureLow = new ArrayList();
            List ApparentTemperatureLowTime = new ArrayList();
            List DewPoint = new ArrayList();
            List Humidity = new ArrayList();
            List Pressure = new ArrayList();
            List WindSpeed = new ArrayList();
            List WindGust = new ArrayList();
            List WindBearing = new ArrayList();
            List CloudCover = new ArrayList();
            List UvIndex = new ArrayList();
            List UvIndexTime = new ArrayList();
            List Visibility = new ArrayList();
            List Ozone = new ArrayList();
            List TemperatureMin = new ArrayList();
            List TemperatureMinTime = new ArrayList();
            List TemperatureMax = new ArrayList();
            List TemperatureMaxTime = new ArrayList();
            List ApparentTemperatureMin = new ArrayList();
            List ApparentTemperatureMinTime = new ArrayList();
            List ApparentTemperatureMax = new ArrayList();
            List ApparentTemperatureMaxTime = new ArrayList();

            JSONObject dailyObj = getObject("daily", jObj);
            weather.dailyCondition.setFullSummary(getString("summary", dailyObj));
            weather.dailyCondition.setFullIcon(getString("icon", dailyObj));

            JSONArray dArr = dailyObj.getJSONArray("data");
            for (int k = 0; k < 8; k++) {
                JSONObject JSONDataDaily = dArr.getJSONObject(k);
                Time.add(getInt("time", JSONDataDaily));
                Summary.add(getString("summary", JSONDataDaily));
                Icon.add(getString("icon", JSONDataDaily));
                if (JSONDataDaily.has("sunriseTime")) {
                    SunriseTime.add(getInt("sunriseTime", JSONDataDaily));
                } else {
                    SunriseTime.add(0);
                }
                if (JSONDataDaily.has("sunsetTime")) {
                    SunsetTime.add(getInt("sunsetTime", JSONDataDaily));
                } else {
                    SunsetTime.add(0);
                }
                MoonPhase.add(getFloat("moonPhase", JSONDataDaily));
                PrecipIntensity.add(getFloat("precipIntensity", JSONDataDaily));
                PrecipIntensityMax.add(getFloat("precipIntensityMax", JSONDataDaily));
                if (JSONDataDaily.has("precipIntensityMaxTime")) {
                    PrecipIntensityMaxTime.add(getInt("precipIntensityMaxTime", JSONDataDaily));
                } else {
                    PrecipIntensityMaxTime.add("");
                }
                PrecipProbability.add(getFloat("precipProbability", JSONDataDaily));
                if (JSONDataDaily.has("precipType")) {
                    PrecipType.add(getString("precipType", JSONDataDaily));
                } else {
                    PrecipType.add("");
                }
                TemperatureHigh.add(getFloat("temperatureHigh", JSONDataDaily));
                TemperatureHighTime.add(getInt("temperatureHighTime", JSONDataDaily));
                TemperatureLow.add(getFloat("temperatureLow", JSONDataDaily));
                TemperatureLowTime.add(getInt("temperatureLowTime", JSONDataDaily));
                ApparentTemperatureHigh.add(getFloat("apparentTemperatureHigh", JSONDataDaily));
                ApparentTemperatureHighTime.add(getInt("apparentTemperatureHighTime", JSONDataDaily));
                ApparentTemperatureLow.add(getFloat("apparentTemperatureLow", JSONDataDaily));
                ApparentTemperatureLowTime.add(getInt("apparentTemperatureLowTime", JSONDataDaily));
                DewPoint.add(getFloat("dewPoint", JSONDataDaily));
                Humidity.add(getFloat("humidity", JSONDataDaily));
                Pressure.add(getFloat("pressure", JSONDataDaily));
                WindSpeed.add(getFloat("windSpeed", JSONDataDaily));
                WindGust.add(getFloat("windGust", JSONDataDaily));
                WindBearing.add(getInt("windBearing", JSONDataDaily));
                CloudCover.add(getFloat("cloudCover", JSONDataDaily));
                UvIndex.add(getInt("uvIndex", JSONDataDaily));
                UvIndexTime.add(getInt("uvIndexTime", JSONDataDaily));
                Visibility.add(getFloat("visibility", JSONDataDaily));
                Ozone.add(getFloat("ozone", JSONDataDaily));
                TemperatureMin.add(getFloat("temperatureMin", JSONDataDaily));
                TemperatureMinTime.add(getInt("temperatureMinTime", JSONDataDaily));
                TemperatureMax.add(getFloat("temperatureMax", JSONDataDaily));
                TemperatureMaxTime.add(getInt("temperatureMaxTime", JSONDataDaily));
                ApparentTemperatureMin.add(getFloat("apparentTemperatureMin", JSONDataDaily));
                ApparentTemperatureMinTime.add(getInt("apparentTemperatureMinTime", JSONDataDaily));
                ApparentTemperatureMax.add(getFloat("apparentTemperatureMax", JSONDataDaily));
                ApparentTemperatureMaxTime.add(getInt("apparentTemperatureMaxTime", JSONDataDaily));


            }

            weather.dailyCondition.setTime(Time);
            weather.dailyCondition.setSummary(Summary);
            weather.dailyCondition.setIcon(Icon);
            weather.dailyCondition.setSunriseTime(SunriseTime);
            weather.dailyCondition.setSunsetTime(SunsetTime);
            weather.dailyCondition.setMoonPhase(MoonPhase);
            weather.dailyCondition.setPrecipIntensity(PrecipIntensity);
            weather.dailyCondition.setPrecipIntensityMax(PrecipIntensityMax);
            weather.dailyCondition.setPrecipIntensityMaxTime(PrecipIntensityMaxTime);
            weather.dailyCondition.setPrecipProbability(PrecipProbability);
            weather.dailyCondition.setPrecipType(PrecipType);
            weather.dailyCondition.setTemperatureHigh(TemperatureHigh);
            weather.dailyCondition.setTemperatureHighTime(TemperatureHighTime);
            weather.dailyCondition.setTemperatureLow(TemperatureLow);
            weather.dailyCondition.setTemperatureLowTime(TemperatureLowTime);
            weather.dailyCondition.setApparentTemperatureHigh(ApparentTemperatureHigh);
            weather.dailyCondition.setApparentTemperatureHighTime(ApparentTemperatureHighTime);
            weather.dailyCondition.setApparentTemperatureLow(ApparentTemperatureLow);
            weather.dailyCondition.setApparentTemperatureLowTime(ApparentTemperatureLowTime);
            weather.dailyCondition.setDewPoint(DewPoint);
            weather.dailyCondition.setHumidity(Humidity);
            weather.dailyCondition.setPressure(Pressure);
            weather.dailyCondition.setWindSpeed(WindSpeed);
            weather.dailyCondition.setWindGust(WindGust);
            weather.dailyCondition.setWindBearing(WindBearing);
            weather.dailyCondition.setCloudCover(CloudCover);
            weather.dailyCondition.setUvIndex(UvIndex);
            weather.dailyCondition.setUvIndexTime(UvIndexTime);
            weather.dailyCondition.setVisibility(Visibility);
            weather.dailyCondition.setOzone(Ozone);
            weather.dailyCondition.setTemperatureMin(TemperatureMin);
            weather.dailyCondition.setTemperatureMinTime(TemperatureMinTime);
            weather.dailyCondition.setTemperatureMax(TemperatureMax);
            weather.dailyCondition.setTemperatureMaxTime(TemperatureMaxTime);
            weather.dailyCondition.setApparentTemperatureMin(ApparentTemperatureMin);
            weather.dailyCondition.setApparentTemperatureMinTime(ApparentTemperatureMinTime);
            weather.dailyCondition.setApparentTemperatureMax(ApparentTemperatureMax);
            weather.dailyCondition.setApparentTemperatureMaxTime(ApparentTemperatureMaxTime);


            //Log.d(TAG, "INTENSE MAX: " + weather.dailyCondition.getPrecipIntensityMaxTime());
            //Log.d(TAG, "PRECIP TYPE: " + weather.dailyCondition.getPrecipType());
            Log.d("CREATION", "Daily Passed ");
        }

        //ALERTS

        //List alertRegions = new ArrayList<>();
        if (jObj.has("alerts")) {

            List titleList = new ArrayList();
            List severityList = new ArrayList();
            List timeList = new ArrayList();
            List expireList = new ArrayList();
            List descList = new ArrayList();
            List urlList = new ArrayList();


            List regionList = new ArrayList();

            JSONArray alertArr = jObj.getJSONArray("alerts");

            for (int l = 0; l < alertArr.length(); l++) {

                JSONObject alertObj = alertArr.getJSONObject(l);
//                JSONArray regionArr = alertObj.getJSONArray("regions");
//
//                for (int i = 0; i < regionArr.length(); i++) {
//                    JSONObject regionsObj = regionArr.getJSONObject(i);
//                    regionList.add(getString("regions", regionsObj));
//                }

                titleList.add(getString("title", alertObj));
                severityList.add(getString("severity", alertObj));
                timeList.add(getInt("time", alertObj));
                expireList.add(getInt("expires", alertObj));
                descList.add(getString("description", alertObj));
                urlList.add(getString("uri", alertObj));
            }
            weather.alerts.setTitle(titleList);
            weather.alerts.setSeverity(severityList);
            weather.alerts.setTime(timeList);
            weather.alerts.setExpireTime(expireList);
            weather.alerts.setDescription(descList);
            weather.alerts.setUrl(urlList);

            //weather.alerts.setRegions(regionList);
            //Log.d(TAG, "REGIONS " + weather.alerts.getRegions());

        } else {
            List titleList = new ArrayList();
            List severityList = new ArrayList();
            List timeList = new ArrayList();
            List expireList = new ArrayList();
            List descList = new ArrayList();
            List urlList = new ArrayList();

                titleList.add("");
                severityList.add("");
                timeList.add(0);
                expireList.add(0);
                descList.add("");
                urlList.add("");

            weather.alerts.setTitle(titleList);
            weather.alerts.setSeverity(severityList);
            weather.alerts.setTime(timeList);
            weather.alerts.setExpireTime(expireList);
            weather.alerts.setDescription(descList);
            weather.alerts.setUrl(urlList);

        }
        Log.d("CREATION", "Alert Passed ");





        if (jObj.has("flags")) {

            //FLAGS
            JSONObject flagsObj = getObject("flags", jObj);
            weather.flags.setUnits(getString("units", flagsObj));
            if (jObj.has("nearest-station")) {
                weather.flags.setNearestStation(getFloat("nearest-station", flagsObj));
            } else {
                weather.flags.setNearestStation(0);
            }
        }

        if (jObj.has("offset")) {
            weather.location.setOffset(getFloat("offset", jObj));
        } else {
            weather.location.setOffset(0);
        }

        Log.d(TAG, "PARSER PASSED:");


        return weather;

    }


    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static long getLong(String tagName, JSONObject jObj) throws JSONException {
        return (long) jObj.getLong(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
