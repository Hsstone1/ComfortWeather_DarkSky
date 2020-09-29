package com.example.sneek.outdoorindex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import Adapters.ExtendHourlyAdapter;
import Support.Utils;
import model.Weather;

public class ExtendHourlyActivity extends AppCompatActivity {

    private static final String TAG = "Creation";
    ArrayList<String> mTime = new ArrayList<>();
    ArrayList<String> mConditionDesc = new ArrayList<>();
    ArrayList<String> mTemp = new ArrayList<>();
    ArrayList<String> mApparent = new ArrayList<>();
    ArrayList<String> mDew = new ArrayList<>();
    ArrayList<String> mPrecip = new ArrayList<>();
    ArrayList<String> mWind = new ArrayList<>();
    ArrayList<String> mUVNum = new ArrayList<>();
    ArrayList<String> mUVCondition = new ArrayList<>();
    ArrayList<String> mHumid = new ArrayList<>();
    ArrayList<String> mComfort = new ArrayList<>();

    ArrayList<Integer> mConditionImage = new ArrayList<>();
    ArrayList<Integer> mWindImage = new ArrayList<>();

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.extend_hourly_activity);
        Log.d(TAG, "EXTENDED HOURLY CREATED");
        initWeatherConditions();
        Slidr.attach(this);


    }

    private void initWeatherConditions(){
        mTime.clear();
        mConditionDesc.clear();
        mTemp.clear();
        mApparent.clear();
        mDew.clear();
        mPrecip.clear();
        mWind.clear();
        mUVNum.clear();
        mUVCondition.clear();
        mHumid.clear();
        mComfort.clear();
        mWindImage.clear();
        mConditionImage.clear();

        for (int i = 0; i < 168; i++) {

            mTime.add("" + homeFragment.utils.getHourlyTime().get(i));
            mConditionDesc.add(homeFragment.getWeatherConditionString(homeFragment.weather.hourlyCondition.getIcon().get(i).toString()));
            mTemp.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getTemperature().get(i)))) + homeFragment.utils.DEG_UNITS);
            mApparent.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getApparentTemperature().get(i)))) + homeFragment.utils.DEG_UNITS);
            mDew.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getDewPoint().get(i)))) + homeFragment.utils.DEG_UNITS);
            mPrecip.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getPrecipProbability().get(i)))*100) + "%");
            mWind.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getWindSpeed().get(i)))) + homeFragment.utils.SPEED_UNITS +" " + String.valueOf(homeFragment.convertDegreeToCardinalDirection(homeFragment.weather.currentCondition.getWindSpeed())));
            mUVNum.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getUvIndex().get(i)))));
            mUVCondition.add("" + homeFragment.getDailyMaxUV(Integer.parseInt(homeFragment.weather.hourlyCondition.getUvIndex().get(i).toString())));
            mHumid.add(homeFragment.d0f.format(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getHumidity().get(i)))*100) + "%");
            mComfort.add(homeFragment.ComfortIndexConditions(Double.parseDouble(String.valueOf(homeFragment.weather.hourlyCondition.getComfortIndex().get(i)))));

            mWindImage.add(homeFragment.getWindSpeedIcon(Double.parseDouble(homeFragment.weather.hourlyCondition.getWindSpeed().get(i).toString())));
            mConditionImage.add(homeFragment.getWeatherConditionIcon(homeFragment.weather.hourlyCondition.getIcon().get(i).toString(), Double.parseDouble(homeFragment.weather.hourlyCondition.getPrecipIntensity().get(i).toString()), Double.parseDouble(homeFragment.weather.hourlyCondition.getCloudCover().get(i).toString())));

        }

        initWeatherConditionsRecyclerView();


    }

    private void initWeatherConditionsRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.extendHourly_recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        ExtendHourlyAdapter adapter = new ExtendHourlyAdapter(this, mTime, mConditionDesc, mTemp, mApparent, mDew, mPrecip, mWind, mUVNum, mUVCondition, mHumid, mComfort, mConditionImage, mWindImage);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "initWeatherConditionsRecyclerView: PASSED");

    }

}
