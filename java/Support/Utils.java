package Support;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public final int tempWeight = 45;
    public final int feelLikeWeight = 100;
    public final int windSpeedWeight = 23;
    public final int uvWeight = 10;
    public final int cloudCoverWeight = 1;
    public final int precipWeight = 55;
    public int dewWeight = 95;
    public final int precipIntensityWeight = 50;
    public int detailReducer = 1;

    public double lat;
    public double lon;

    public boolean isGraphZoomed = false;
    public List radioListDate = new ArrayList();

    public String userCity = "";


    public String DEG_UNITS = "°F";
    public String SPEED_UNITS = "mph";
    public String DIST_UNITS = "mi";
    public String RAIN_UNITS = "in / hr";
    public String PRESSURE_UNITS = "mb";
    public String STORM_SURGE_UNITS = "ft";
    public String PRECIP_ACCUM_UNITS = "in";
    public final String DEFAULT_LATITUDE = "32.947724";
    public final String DEFAULT_LONGITUDE = "-80.158673";
    public final String DEFAULT_CITY_NAME = "Summerville, SC";
    public String CURRENT_CITY_NAME = "";

    public List DATABASE_NAME_LIST = new ArrayList();
    public List hourlyTime = new ArrayList();


    //Summerville, South Carolina, United States        32.947724   -80.158673
    //Washington DC, District of Columbia, United States        38.9072     -77.0369


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }


    public void setGraphZoomed(boolean graphZoomed) {
        isGraphZoomed = graphZoomed;
    }

    public String getPRESSURE_UNITS() {
        return PRESSURE_UNITS;
    }

    public void setPRESSURE_UNITS(String PRESSURE_UNITS) {
        this.PRESSURE_UNITS = PRESSURE_UNITS;
    }

    public String getSTORM_SURGE_UNITS() {
        return STORM_SURGE_UNITS;
    }

    public void setSTORM_SURGE_UNITS(String STORM_SURGE_UNITS) {
        this.STORM_SURGE_UNITS = STORM_SURGE_UNITS;
    }

    public List getRadioListDate() {
        return radioListDate;
    }

    public void setRadioListDate(List radioListDate) {
        this.radioListDate = radioListDate;
    }


    public String getCURRENT_CITY_NAME() {
        return CURRENT_CITY_NAME;
    }

    public void setCURRENT_CITY_NAME(String CURRENT_CITY_NAME) {
        this.CURRENT_CITY_NAME = CURRENT_CITY_NAME;
    }

    public List getHourlyTime() {
        return hourlyTime;
    }

    public void setHourlyTime(List hourlyTime) {
        this.hourlyTime = hourlyTime;
    }


}


