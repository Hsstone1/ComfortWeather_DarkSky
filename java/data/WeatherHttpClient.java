package data;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;


import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import model.Weather;

import android.content.Context;


import com.example.sneek.outdoorindex.HomeFragment;
import com.example.sneek.outdoorindex.MainActivity;

import javax.net.ssl.HttpsURLConnection;


public class WeatherHttpClient {
    Weather weather = new Weather();
    private static String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String APPID = "5b111bb38d71d7f45a28c9f6cd1d9e89/";
    //TIME REQUEST IS IN UNIX TIMESTAMP
    public static String TIME_REQUEST = "";
    private static final String EXTRA_REQUEST = "?lang=en&extend=extend%3Dhourly&units=us";
    private static final int SECONDS_BETWEEN_READS = 2 * 60;

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;
    private static final String TAG = "CREATION";
    int itemID = -1;


    //private static String COORDINATES = "32.94760622243483,-80.17066955566408"; summerville
    //GOOGLE API KEY: AIzaSyBVWfx4BRwYKeJ1fMWGT5LcEEkSVqTWMxM

    //TODO every 3 hours load the weather for each city on the searched city list. Only load the visable searches. (top 10)

    //     https://api.openweathermap.org/data/2.5/weather?q=summerville,us&APPID=121c9cf562dc39e942513e4b8a97bf1d&units=imperial
    //     https://api.openweathermap.org/data/2.5/weather?lat=33.02&lon=-80.17&APPID=121c9cf562dc39e942513e4b8a97bf1d&units=imperial
    //     https://api.darksky.net/forecast/5b111bb38d71d7f45a28c9f6cd1d9e89/33.02,-80.17


    public String getWeatherData(String COORDINATES) throws InterruptedException {

        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        String data = "";


        Log.d(TAG, "HTTP WEATHER REACHED");
        try {

            Cursor tableData = homeFragment.mWeatherDataDatabaseHelper.getData();
            data = "";
            String dataTableTime = "0";
            String dataCoords = "";
            String weatherCoords = "";


            Log.d(TAG, "RECENT DATA ID: " + itemID);


            while (tableData.moveToNext()) {
                itemID = tableData.getInt(0);
                data = (tableData.getString(1));
            }
            if (itemID > -1) {
                int dbstrStart = data.indexOf("currently\":{\"time\":") + 19;
                dataTableTime = data.substring(dbstrStart, dbstrStart + 10);


                double dataLat = homeFragment.utils.getLat();
                double dataLong = homeFragment.utils.getLon();


                dataCoords = homeFragment.d3f.format(dataLat) + homeFragment.d3f.format(dataLong);
                weatherCoords = homeFragment.d3f.format(homeFragment.weather.location.getLatitude()) + homeFragment.d3f.format(homeFragment.weather.location.getLongitude());
                Log.d(TAG, "dataCoords:     " + dataCoords);
                //Log.d(TAG, "dbTableTime " + dataTableTime);
                Log.d(TAG, "getWeatherData: " + weatherCoords);
            }
            if ((((int)(System.currentTimeMillis() / 1000) > Integer.parseInt(dataTableTime) + SECONDS_BETWEEN_READS) || !dataCoords.equals(weatherCoords)) && homeFragment.isNetworkAvailable()) {


                Log.d(TAG, "isNetworkAval? " + homeFragment.isNetworkAvailable());
                connection = (HttpsURLConnection) (new URL(BASE_URL + APPID + COORDINATES + TIME_REQUEST + EXTRA_REQUEST)).openConnection();

                connection.setRequestMethod("GET");

                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.connect();


                Log.d("CREATION", "Full URL: " + BASE_URL + APPID + COORDINATES + TIME_REQUEST + EXTRA_REQUEST);

                StringBuffer stringBuffer = new StringBuffer();
                inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));             //only object that can read input stream from the internet
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\r\n");
                }
                inputStream.close();
                connection.disconnect();
                data = stringBuffer.toString();
                Log.d("CREATION", "current data " + data);


                Cursor databaseData = homeFragment.mWeatherDataDatabaseHelper.getItemID(data);
                while (databaseData.moveToNext()) {
                    itemID = databaseData.getInt(0);
                    Log.d(TAG, "RECENT DATA ID: " + itemID);
                }
                if (itemID > -1) {
                    homeFragment.mWeatherDataDatabaseHelper.deleteData();
                    Log.d(TAG, "Data deleted");

                }
                homeFragment.mWeatherDataDatabaseHelper.addData(data);
                Log.d(TAG, "Data added");

            } else {
                Log.d(TAG, "ELSE");

                tableData = homeFragment.mWeatherDataDatabaseHelper.getData();
                while (tableData.moveToNext()) {
                    data = (tableData.getString(1));
                    Log.d("CREATION", "RETRIEVED DATABASE DATA " + data);
                }
            }


            return data;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Throwable e) {
            }
            try {
                connection.disconnect();
            } catch (Throwable e) {
            }
        }


        return null;
    }

}