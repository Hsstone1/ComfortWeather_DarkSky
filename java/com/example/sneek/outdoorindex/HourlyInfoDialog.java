package com.example.sneek.outdoorindex;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Support.Utils;

public class HourlyInfoDialog extends AppCompatDialogFragment {

    Utils utils = new Utils();
    private static final String TAG = "CREATION";
    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;

    TextView hourlyInfoDate_textView;
    TextView hourlyInfoDesc_textView;
    TextView hourlyInfoMonth_textView;
    TextView hourlyInfoYear_textView;
    TextView hourlyInfoTemp_textView;
    TextView hourlyInfoHumidDew_textView;
    TextView hourlyInfoUV_textView;
    TextView hourlyInfoVis_textView;
    TextView hourlyInfoPrecip_textView;
    TextView hourlyInfoWind_textView;
    TextView hourlyInfoPressure_textView;
    TextView hourlyInfoCloudy_textView;
    TextView hourlyInfoSun_textView;
    TextView hourlyInfoMoon_textView;
    TextView hourlyInfoComfortDesc_textView;
    ImageView hourlyInfoIcon_imageView;
    ImageView hourlyInfoWindImage_imageView;
    ImageView hourlyInfoMoonImage_imageView;
    ProgressBar hourlyInfoComfortIndex_progressBar;
    SeekBar dayChange_seekBar;

    ImageView nextHour_imageButton;
    ImageView backHour_imageButton;
    ImageView nextDay_imageButton;
    ImageView backDay_imageButton;

    LinearLayout backDay_linearLayout;
    LinearLayout nextDay_linearLayout;

    public int hourlyInfoHour;
    int MAX_HOURS = 48;
    int timeValue = 0;







    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.hourly_info_dialog_layout, null);
        Log.d(TAG, "CREATED HOURLY INFO");

        builder.setView(view).setTitle("Hourly Info").setPositiveButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        
        hourlyInfoDate_textView = view.findViewById(R.id.hourlyInfoDate_textView); 
        hourlyInfoDesc_textView = view.findViewById(R.id.hourlyInfoDesc_textView); 
        hourlyInfoMonth_textView = view.findViewById(R.id.hourlyInfoMonth_textView); 
        hourlyInfoYear_textView = view.findViewById(R.id.hourlyInfoYear_textView); 
        hourlyInfoTemp_textView = view.findViewById(R.id.hourlyInfoTemp_textView);
        hourlyInfoHumidDew_textView = view.findViewById(R.id.hourlyInfoHumidDew_textView); 
        hourlyInfoUV_textView = view.findViewById(R.id.hourlyInfoUV_textView); 
        hourlyInfoVis_textView = view.findViewById(R.id.hourlyInfoVis_textView);
        hourlyInfoPrecip_textView = view.findViewById(R.id.hourlyInfoPrecip_textView); 
        hourlyInfoWind_textView = view.findViewById(R.id.hourlyInfoWind_textView); 
        hourlyInfoPressure_textView = view.findViewById(R.id.hourlyInfoPressure_textView); 
        hourlyInfoCloudy_textView = view.findViewById(R.id.hourlyInfoCloudy_textView); 
        hourlyInfoSun_textView = view.findViewById(R.id.hourlyInfoSun_textView); 
        hourlyInfoMoon_textView = view.findViewById(R.id.hourlyInfoMoon_textView); 
        hourlyInfoComfortDesc_textView = view.findViewById(R.id.hourlyInfoComfortDesc_textView);

        hourlyInfoIcon_imageView = view.findViewById(R.id.hourlyInfoIcon_imageView);
        hourlyInfoWindImage_imageView = view.findViewById(R.id.hourlyInfoWindImage_imageView);
        hourlyInfoMoonImage_imageView = view.findViewById(R.id.hourlyInfoMoonImage_imageView);

        nextHour_imageButton = view.findViewById(R.id.nextHour_imageButton);
        backHour_imageButton = view.findViewById(R.id.backHour_imageButton);
        nextDay_imageButton = view.findViewById(R.id.nextDay_imageButton);
        backDay_imageButton = view.findViewById(R.id.backDay_imageButton);

        hourlyInfoComfortIndex_progressBar = view.findViewById(R.id.hourlyInfoComfortIndex_progressBar);
        dayChange_seekBar = view.findViewById(R.id.dayChange_seekBar);

        backDay_linearLayout = view.findViewById(R.id.backDay_linearLayout);
        nextDay_linearLayout = view.findViewById(R.id.nextDay_linearLayout);


        int mNum = getArguments().getInt("num");
        final boolean isTimeRequest = getArguments().getBoolean("isTimeRequest");
        getHourlyConditions(mNum+1, isTimeRequest);
        return builder.create();
    }




    public void getHourlyConditions(final int i, final boolean isTimeRequest){

        String icon;
        int time;
        int temp;
        int apparent;
        int humidity;
        int dew;
        int uvIndex;
        double visibility;
        int precipProb;
        double intense;
        int wind;
        int windGust;
        int pressure;
        int cloud;
        double moon;
        double comfort = 0;
        if(!isTimeRequest) {
            backDay_linearLayout.setVisibility(View.GONE);
            nextDay_linearLayout.setVisibility(View.GONE);
            icon = homeFragment.weather.hourlyCondition.getIcon().get(i).toString();
            time = Integer.parseInt(homeFragment.weather.hourlyCondition.getTime().get(i).toString());
            temp = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getTemperature().get(i).toString())));
            apparent = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getApparentTemperature().get(i).toString())));
            humidity = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getHumidity().get(i).toString()) * 100));
            dew = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getDewPoint().get(i).toString())));
            uvIndex = Integer.parseInt(homeFragment.weather.hourlyCondition.getUvIndex().get(i).toString());
            visibility = Double.parseDouble(homeFragment.d1f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getVisibility().get(i).toString())));
            precipProb = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getPrecipProbability().get(i).toString()) * 100));
            intense = Double.parseDouble(homeFragment.d2f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getPrecipIntensity().get(i).toString())));
            wind = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getWindSpeed().get(i).toString())));
            windGust = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getWindGust().get(i).toString())));
            pressure = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getPressure().get(i).toString())));
            cloud =  Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getCloudCover().get(i).toString()) * 100));
            moon = Double.parseDouble(homeFragment.weather.dailyCondition.getMoonPhase().get((int) Math.floor((i / 24))).toString());
            comfort = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.weather.hourlyCondition.getComfortIndex().get(i).toString())));
        } else {
            backDay_linearLayout.setVisibility(View.VISIBLE);
            nextDay_linearLayout.setVisibility(View.VISIBLE);
            timeValue = Integer.parseInt(homeFragment.timeWeather.hourlyCondition.getTime().get(0).toString());
            icon = homeFragment.timeWeather.hourlyCondition.getIcon().get(i).toString();
            time = Integer.parseInt(homeFragment.timeWeather.hourlyCondition.getTime().get(i).toString());
            temp = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getTemperature().get(i).toString())));
            apparent = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getApparentTemperature().get(i).toString())));
            humidity = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getHumidity().get(i).toString()) * 100));
            dew = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getDewPoint().get(i).toString())));
            uvIndex = Integer.parseInt(homeFragment.timeWeather.hourlyCondition.getUvIndex().get(i).toString());
            visibility = Double.parseDouble(homeFragment.d1f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getVisibility().get(i).toString())));
            precipProb = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getPrecipProbability().get(i).toString()) * 100));
            intense = Double.parseDouble(homeFragment.d2f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getPrecipIntensity().get(i).toString())));
            wind = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getWindSpeed().get(i).toString())));
            windGust = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getWindGust().get(i).toString())));
            pressure = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getPressure().get(i).toString())));
            cloud = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getCloudCover().get(i).toString()) * 100));
            moon = Double.parseDouble(homeFragment.timeWeather.dailyCondition.getMoonPhase().get(0).toString());
            //comfort = Integer.parseInt(homeFragment.d0f.format(Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getComfortIndex().get(i).toString())));
            comfort = homeFragment.calcComfortIndex(
                    Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getTemperature().get(i).toString()),
                    Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getApparentTemperature().get(i).toString()),
                    Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getDewPoint().get(i).toString()),
                    Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getWindSpeed().get(i).toString()),
                    Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getCloudCover().get(i).toString()),
                    Double.parseDouble(homeFragment.timeWeather.hourlyCondition.getPrecipProbability().get(i).toString()),
                    Integer.parseInt(homeFragment.timeWeather.hourlyCondition.getUvIndex().get(i).toString())
                    );
            comfort = Math.round(comfort);
            nextDay_imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeValue += 86400;
                    Log.d(TAG, "Next Day: " + String.valueOf(timeValue));
                    dismiss();
                    homeFragment.RenderTimeWeatherData(homeFragment.weather.location.getLatitude() + "," + homeFragment.weather.location.getLongitude(), String.valueOf(timeValue));
                }
            });

            backDay_imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeValue -= 86400;
                    Log.d(TAG, "Next Day: " + String.valueOf(timeValue));
                    dismiss();
                    homeFragment.RenderTimeWeatherData(homeFragment.weather.location.getLatitude() + "," + homeFragment.weather.location.getLongitude(), String.valueOf(timeValue));
                }
            });
        }

        hourlyInfoTemp_textView.setText("Temp: " + temp + " " + utils.DEG_UNITS + "\n" + "Apparent: " + apparent + " " + utils.DEG_UNITS);
        hourlyInfoHumidDew_textView.setText("Humidity: " + humidity + "%" + "\n" + "Dew Point: " + dew + " " + utils.DEG_UNITS);
        hourlyInfoUV_textView.setText("UV Index: " + uvIndex + "\n" + "Risk: " + homeFragment.getDailyMaxUV(uvIndex));
        hourlyInfoVis_textView.setText("Visibility: " + visibility + " " + utils.DIST_UNITS + "\n" + getVisibilityContition(visibility));
        hourlyInfoPrecip_textView.setText("Precip: " + precipProb + "%" + "\n" + "Intensity: " + intense + " " + utils.PRECIP_ACCUM_UNITS);
        hourlyInfoWind_textView.setText("Winds: " + wind + " " + utils.SPEED_UNITS + " " + homeFragment.convertDegreeToCardinalDirection((double)wind) + "\n" + "Gusts: " + windGust + " " + utils.SPEED_UNITS);
        hourlyInfoPressure_textView.setText("Pressure: " + pressure + "mb" + "\n" + pressureChanging((double)pressure, Double.parseDouble(homeFragment.weather.hourlyCondition.getPressure().get(0).toString()) ));
        hourlyInfoCloudy_textView.setText("Cloud Cover: " + cloud + "%\n" + getCloudCoverString(cloud));
        hourlyInfoSun_textView.setText("Sunrise: " + homeFragment.weather.dailyCondition.getSunriseDateList().get((int)Math.floor((i/24))) + "\n" + "Sunset: " + homeFragment.weather.dailyCondition.getSunsetDateList().get((int)Math.floor((i/24))));
        hourlyInfoMoon_textView.setText("Phase: " + homeFragment.getMoonPhaseString(moon) + "\n" + "Illumination: " + homeFragment.calcMoonIllumination(moon) + "%");
        hourlyInfoComfortDesc_textView.setText(homeFragment.ComfortIndexConditions(comfort));
        hourlyInfoComfortIndex_progressBar.setProgress((int)Math.ceil(comfort));

        hourlyInfoDesc_textView.setText(homeFragment.getWeatherConditionString(icon));
        hourlyInfoIcon_imageView.setImageResource(homeFragment.getWeatherConditionIcon(icon, intense, cloud/100));
        hourlyInfoWindImage_imageView.setImageResource(homeFragment.getWindSpeedIcon((double)wind));
        hourlyInfoMoonImage_imageView.setImageResource(Integer.parseInt(homeFragment.MOON_PHASE.get(String.valueOf(moon))));

        Date hourlyTime = new Date((long)time * 1000);
        int hourlyDay = Integer.parseInt(new SimpleDateFormat("d", Locale.getDefault()).format(hourlyTime));
        String hourlySuffix = new SimpleDateFormat("a", Locale.getDefault()).format(hourlyTime);
        String hourlyDate = new SimpleDateFormat("hh", Locale.getDefault()).format(hourlyTime) + homeFragment.morningEveningSuffix(hourlySuffix) + " " + new SimpleDateFormat("EEEE d", Locale.getDefault()).format(hourlyTime) + homeFragment.getDayNumberSuffix(hourlyDay);
        String hourlyMonth = new SimpleDateFormat("MMMM", Locale.getDefault()).format(hourlyTime);
        String hourlyYear = new SimpleDateFormat("YYYY", Locale.getDefault()).format(hourlyTime);

        hourlyInfoDate_textView.setText(hourlyDate);
        hourlyInfoMonth_textView.setText(hourlyMonth);
        hourlyInfoYear_textView.setText(hourlyYear);
        if(isTimeRequest){
            MAX_HOURS = 24;
        } else {
            MAX_HOURS = 168;
        }
        dayChange_seekBar.setProgress(i);
        dayChange_seekBar.setMax(MAX_HOURS-1);


        nextHour_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i < MAX_HOURS-1) {
                    getHourlyConditions(i + 1, isTimeRequest);
                    dayChange_seekBar.incrementProgressBy(1);
                } else {
                    nextHour_imageButton.setColorFilter(Color.argb(90,0,0,0));
                }
            }
        });

        backHour_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i > 0) {
                    getHourlyConditions(i - 1, isTimeRequest);
                    dayChange_seekBar.incrementProgressBy(-1);

                } else {
                    backHour_imageButton.setColorFilter(Color.argb(90,0,0,0));
                }
            }
        });



        dayChange_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getHourlyConditions(progress, isTimeRequest);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private String getCloudCoverString(int cloud) {
        String cloudCondition = "";
        if(cloud > 90){
            cloudCondition = "Very Cloudy";
        } else if (cloud > 75) {
            cloudCondition = "Cloudy";
        } else if (cloud > 50){
            cloudCondition = "Overcast";
        } else if (cloud > 30) {
            cloudCondition = "Partly Cloudy";
        } else if (cloud > 15){
            cloudCondition = "Mostly Clear";
        } else {
            cloudCondition = "Clear";
        }
        return cloudCondition;
    }


    //VISIBILITY IS IN MILES, WILL NOT GIVE ACCURATE READINGS IN METRIC
    public String getVisibilityContition(double visibility){
        String visibilityCondition = "";
        if(visibility > 8){
            visibilityCondition = "Excellent";
        } else if (visibility > 5) {
            visibilityCondition = "Good";
        } else if (visibility > 3){
            visibilityCondition = "Poor";
        } else if (visibility > 1) {
            visibilityCondition = "Minimal";
        } else {
            visibilityCondition = "Zero";
        }
        return visibilityCondition;
    }

    public String pressureChanging(double pressure, double currentPressure) {
        String pressureChange = "";
        double pressureAdd;
        double sum = 0;

        for (int i = 0; i < 24; i++) {
            pressureAdd = pressure;
            sum += pressureAdd;
        }

        sum = sum / 24;
        if ((sum + 2 < currentPressure) && (sum - 2 > currentPressure)) {
            pressureChange = "Constant";
        } else if ((sum < currentPressure)) {
            pressureChange = "Decreasing";
        } else if ((sum > currentPressure)) {
            pressureChange = "Increasing";
        }

        return pressureChange;
    }
}
