package data;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import android.content.Context;
import android.util.Log;



public class ReadCityExcel {

    private static final String TAG = "ReadCityExcel";
    public CityObject cityObject = new CityObject();



    public void readAndInsert(Context context) throws UnsupportedEncodingException {
        ArrayList<CityObject> cityList = new ArrayList<>();
        AssetManager as = context.getAssets();
        InputStream is = null;

        try {
            is = as.open("uscities.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        StringTokenizer st = null;
        List cityName = new ArrayList();
        List state = new ArrayList();
        List zip = new ArrayList();

        try {
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                //CityObject obj = new CityObject();
                //your attributes
                if (!st.hasMoreElements()) break;
                cityName.add(st.nextToken());
                if (!st.hasMoreElements()) break;
                state.add(st.nextToken());
                if (!st.hasMoreElements()) break;
                zip.add(st.nextToken());

            }
            cityObject.setCityName(cityName);
            cityObject.setState(state);
            cityObject.setZipcode(zip);


        } catch (IOException e) {

            e.printStackTrace();
        }
    }


}
