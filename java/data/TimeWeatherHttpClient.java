package data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import model.Weather;

public class TimeWeatherHttpClient {

    Weather weather = new Weather();
    private static String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String APPID = "5b111bb38d71d7f45a28c9f6cd1d9e89/";
    private static final String EXTRA_REQUEST = "?lang=en&units=us";


    public String getTimeWeatherData(String COORDINATES, String TIME_REQUEST) throws InterruptedException {
        HttpsURLConnection connection = null;
        InputStream inputStream = null;

        try {
            //            connection = (HttpsURLConnection) (new URL(BASE_URL + APPID + COORDINATES + TIME_REQUEST + EXTRA_REQUEST)).openConnection();

            connection = (HttpsURLConnection) (new URL(BASE_URL + APPID + COORDINATES + "," + TIME_REQUEST + EXTRA_REQUEST)).openConnection();
            //wait(1000 * 60 * MINUTES_BETWEEN_READS);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();
            Log.d("CREATION", "Full URL: " + BASE_URL + APPID + COORDINATES + ","+ TIME_REQUEST + EXTRA_REQUEST);

            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));             //only object that can read input stream from the internet
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\r\n");
            }
            inputStream.close();
            connection.disconnect();
            //Log.d("CREATION", "current string buffer: " + stringBuffer);
            String data = stringBuffer.toString();
            Log.d("CREATION", "TIME string buffer: " + data);
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
