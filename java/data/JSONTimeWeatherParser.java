package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.TimeWeather;
import model.Weather;


public class JSONTimeWeatherParser {


    //retrieves string returned from WeatherHttpClient stringBuffer (PROBLEM CAUSER)
    public static TimeWeather getWeather(String data) throws JSONException {
        String TAG = "CREATION";

        TimeWeather weather = new TimeWeather();
        JSONObject jObj = new JSONObject(data);
        Random rand = new Random();



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
        for (int j = 0; j < hArr.length(); j++) {
            int randTemp = rand.nextInt(50)+40;

            JSONObject JSONDataHourly = hArr.getJSONObject(j);
            hourlyTime.add(getInt("time", JSONDataHourly));
            hourlySummary.add(getString("summary", JSONDataHourly));
            hourlyIcon.add(getString("icon", JSONDataHourly));
            if (JSONDataHourly.has("precipIntensity")) {
                hourlyPrecipIntensity.add(getFloat("precipIntensity", JSONDataHourly));
            } else {
                hourlyPrecipIntensity.add(0);
            }
            if (JSONDataHourly.has("precipProbability")) {
                hourlyPrecipProbability.add(getFloat("precipProbability", JSONDataHourly));
            } else {
                hourlyPrecipProbability.add(0);
            }
            if (JSONDataHourly.has("temperature")) {
                hourlyTemperature.add(getFloat("temperature", JSONDataHourly));
            } else {
                hourlyTemperature.add(randTemp);
            }
            if (JSONDataHourly.has("apparentTemperature")) {
                hourlyApparentTemperature.add(getFloat("apparentTemperature", JSONDataHourly));
            } else {
                hourlyApparentTemperature.add(randTemp);
            }
            if (JSONDataHourly.has("dewPoint")) {
                hourlyDewPoint.add(getFloat("dewPoint", JSONDataHourly));
            } else {
                hourlyDewPoint.add(randTemp);
            }
            if (JSONDataHourly.has("humidity")) {
                hourlyHumidity.add(getFloat("humidity", JSONDataHourly));
            } else {
                hourlyHumidity.add(50);
            }
            if (JSONDataHourly.has("pressure")) {
                hourlyPressure.add(getFloat("pressure", JSONDataHourly));
            } else {
                hourlyPressure.add(1000);
            }
            if (JSONDataHourly.has("windSpeed")) {
                hourlyWindSpeed.add(getFloat("windSpeed", JSONDataHourly));
            } else {
                hourlyWindSpeed.add(0);
            }
            if (JSONDataHourly.has("windGust")) {
                hourlyWindGust.add(getFloat("windGust", JSONDataHourly));
            } else {
                if(JSONDataHourly.has("windSpeed")){
                    hourlyWindGust.add(1.3 * getFloat("windSpeed", JSONDataHourly));
                } else {
                    hourlyWindGust.add(0);
                }
            }
            if (JSONDataHourly.has("windBearing")) {
                hourlyWindBearing.add(getInt("windBearing", JSONDataHourly));
            } else {
                hourlyWindBearing.add(0);
            }
            if (JSONDataHourly.has("cloudCover")) {
                hourlyCloudCover.add(getFloat("cloudCover", JSONDataHourly));
            } else {
                hourlyCloudCover.add(0);
            }
            if (JSONDataHourly.has("uvIndex")) {
                hourlyUvIndex.add(getInt("uvIndex", JSONDataHourly));
            } else {
                hourlyUvIndex.add(0);
            }
            if (JSONDataHourly.has("visibility")) {
                hourlyVisibility.add(getFloat("visibility", JSONDataHourly));
            } else {
                hourlyVisibility.add(10);
            }
            if (JSONDataHourly.has("ozone")) {
                hourlyOzone.add(getFloat("ozone", JSONDataHourly));
            } else {
                hourlyOzone.add("");
            }

        }

        weather.hourlyCondition.setTime(hourlyTime);
        weather.hourlyCondition.setSummary(hourlySummary);
        weather.hourlyCondition.setIcon(hourlyIcon);
        weather.hourlyCondition.setPrecipIntensity(hourlyPrecipIntensity);
        weather.hourlyCondition.setPrecipProbability(hourlyPrecipProbability);

        //weather.hourlyCondition.setPrecipType(hourlyPrecipType);
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

        //Log.d("CREATION", "Hourly Passed ");
//        for (int i = 0; i < 169; i++) {
//
//            Log.d(TAG, "getWeather: " + weather.hourlyCondition.getTemperature().get(i));
//            //Log.d(TAG, "getWeather: " + Double.parseDouble((weather.hourlyCondition.getTemperature().get(i)).toString()));
//
//        }


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
            JSONArray dArr = dailyObj.getJSONArray("data");
            for (int k = 0; k < 1; k++) {
                int randTemp = rand.nextInt(50)+40;

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
                if (JSONDataDaily.has("precipIntensity")) {
                    PrecipIntensity.add(getFloat("precipIntensity", JSONDataDaily));
                } else {
                    PrecipIntensity.add(0);
                }
                if (JSONDataDaily.has("precipIntensityMax")) {
                    PrecipIntensityMax.add(getFloat("precipIntensityMax", JSONDataDaily));
                } else {
                    PrecipIntensityMax.add(0);
                }
                if (JSONDataDaily.has("precipIntensityMaxTime")) {
                    PrecipIntensityMaxTime.add(getInt("precipIntensityMaxTime", JSONDataDaily));
                } else {
                    PrecipIntensityMaxTime.add("");
                }
                if (JSONDataDaily.has("precipProbability")) {
                    PrecipProbability.add(getFloat("precipProbability", JSONDataDaily));
                } else {
                    PrecipProbability.add(0);
                }
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
                if (JSONDataDaily.has("pressure")) {
                    Pressure.add(getFloat("pressure", JSONDataDaily));
                } else {
                    Pressure.add(1000);
                }
                if (JSONDataDaily.has("windSpeed")) {
                    WindSpeed.add(getFloat("windSpeed", JSONDataDaily));
                } else {
                    WindSpeed.add(0);
                }
                if (JSONDataDaily.has("windGust")) {
                    WindGust.add(getFloat("windGust", JSONDataDaily));
                } else {
                    if(JSONDataDaily.has("windSpeed")){
                        hourlyWindGust.add(1.3 * getFloat("windSpeed", JSONDataDaily));
                    } else {
                        hourlyWindGust.add(0);
                    }
                }
                if (JSONDataDaily.has("windBearing")) {
                    WindBearing.add(getInt("windBearing", JSONDataDaily));
                } else {
                    WindBearing.add(0);
                }
                Log.d(TAG, "DAILY: WIND BREARING PASSED");
                CloudCover.add(getFloat("cloudCover", JSONDataDaily));
                UvIndex.add(getInt("uvIndex", JSONDataDaily));
                UvIndexTime.add(getInt("uvIndexTime", JSONDataDaily));
                if(JSONDataDaily.has("visibility")) {
                    Visibility.add(getFloat("visibility", JSONDataDaily));
                } else {
                    Visibility.add(10);
                }
                if (JSONDataDaily.has("ozone")) {
                    Ozone.add(getFloat("ozone", JSONDataDaily));
                } else {
                    Ozone.add("");
                }
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

            Log.d(TAG, "TIME: " + weather.dailyCondition.getTime());
            //Log.d(TAG, "INTENSE MAX: " + weather.dailyCondition.getPrecipIntensityMaxTime());
            //Log.d(TAG, "PRECIP TYPE: " + weather.dailyCondition.getPrecipType());
            //Log.d("CREATION", "Daily Passed ");
        }


        //Log.d("CREATION", "Daily Passed ");


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

