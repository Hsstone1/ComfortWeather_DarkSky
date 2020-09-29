package model;

import java.util.ArrayList;
import java.util.List;

public class Weather {


    public Weather.Location location = new Weather.Location();
    public Weather.Daily dailyCondition = new Weather.Daily();
    public Weather.Hourly hourlyCondition = new Weather.Hourly();
    public Weather.Minutely minutelyCondition = new Weather.Minutely();
    public Weather.Currently currentCondition = new Weather.Currently();
    public Weather.Alerts alerts = new Weather.Alerts();
    public Weather.Flags flags = new Weather.Flags();






    public class Location {
        private float latitude;
        private float longitude;
        private String locationLat;
        private String locationLong;
        private String timezone;
        private String myCity;

        private float offset;


        // Getter Methods
        public float getLatitude() { return latitude; }
        public float getLongitude() { return longitude; }
        public String getTimezone() { return timezone; }
        public String getMyCity() { return myCity; }
        public float getOffset() { return offset; }
        public String getLocationLat() { return locationLat; }
        public String getLocationLong() { return locationLong; }


        // Setter Methods
        public void setLatitude(float latitude) { this.latitude = latitude; }
        public void setLongitude(float longitude) { this.longitude = longitude; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
        public void setMyCity(String myCity) { this.myCity = myCity; }
        public void setOffset(float offset) { this.offset = offset; }
        public void setLocationLat(String locationLat) { this.locationLat = locationLat; }
        public void setLocationLong(String locationLong) { this.locationLong = locationLong; }
    }


    public class Currently {
        private int time;
        private String summary;
        private String icon;
        private float nearestStormDistance;
        private int nearestStormBearing;
        private float precipIntensity;
        private float precipProbability;
        private float precipIntensityError;
        private String precipType;
        private float temperature;
        private float apparentTemperature;
        private float dewPoint;
        private float humidity;
        private float pressure;
        private float windSpeed;
        private float windGust;
        private int windBearing;
        private float cloudCover;
        private int uvIndex;
        private float visibility;
        private float ozone;
        private double comfortIndex;
        private double tempIndex;
        private double dewIndex;
        private double precipIndex;
        private double windSpeedIndex;
        private double uvIndexIndex;
        private double cloudCoverIndex;
        private double precipIntensityIndex;



        // Getter Methods
        public int getTime() { return time; }
        public String getSummary() { return summary; }
        public String getIcon() { return icon; }
        public float getNearestStormDistance() { return nearestStormDistance; }
        public int getNearestStormBearing() { return nearestStormBearing; }
        public float getPrecipIntensity() { return precipIntensity; }
        public float getPrecipProbability() { return precipProbability; }
        public float getPrecipIntensityError() { return precipIntensityError; }
        public String getPrecipType() { return precipType; }
        public float getTemperature() { return temperature; }
        public float getApparentTemperature() { return apparentTemperature; }
        public float getDewPoint() { return dewPoint; }
        public float getHumidity() { return humidity; }
        public float getPressure() { return pressure; }
        public float getWindSpeed() { return windSpeed; }
        public float getWindGust() { return windGust; }
        public int getWindBearing() { return windBearing; }
        public float getCloudCover() { return cloudCover; }
        public int getUvIndex() { return uvIndex; }
        public float getVisibility() { return visibility; }
        public float getOzone() { return ozone; }
        public double getComfortIndex() { return comfortIndex; }
        public double getTempIndex() { return tempIndex; }
        public double getDewIndex() { return dewIndex; }
        public double getPrecipIndex() { return precipIndex; }
        public double getWindSpeedIndex() { return windSpeedIndex; }
        public double getUvIndexIndex() { return uvIndexIndex; }
        public double getCloudCoverIndex() { return cloudCoverIndex; }
        public double getPrecipIntensityIndex() { return precipIntensityIndex; }

        // Setter Methods
        public void setTime(int time) { this.time = time; }
        public void setSummary(String summary) { this.summary = summary; }
        public void setIcon(String icon) { this.icon = icon; }
        public void setNearestStormDistance(float nearestStormDistance) { this.nearestStormDistance = nearestStormDistance; }
        public void setNearestStormBearing(int nearestStormBearing) { this.nearestStormBearing = nearestStormBearing; }
        public void setPrecipIntensity(float precipIntensity) { this.precipIntensity = precipIntensity; }
        public void setPrecipProbability(float precipProbability) { this.precipProbability = precipProbability; }
        public void setPrecipIntensityError(float precipIntensityError) { this.precipIntensityError = precipIntensityError; }
        public void setPrecipType(String precipType) { this.precipType = precipType; }
        public void setTemperature(float temperature) { this.temperature = temperature; }
        public void setApparentTemperature(float apparentTemperature) { this.apparentTemperature = apparentTemperature; }
        public void setDewPoint(float dewPoint) { this.dewPoint = dewPoint; }
        public void setHumidity(float humidity) { this.humidity = humidity; }
        public void setPressure(float pressure) { this.pressure = pressure; }
        public void setWindSpeed(float windSpeed) { this.windSpeed = windSpeed; }
        public void setWindGust(float windGust) { this.windGust = windGust; }
        public void setWindBearing(int windBearing) { this.windBearing = windBearing; }
        public void setCloudCover(float cloudCover) { this.cloudCover = cloudCover; }
        public void setUvIndex(int uvIndex) { this.uvIndex = uvIndex; }
        public void setVisibility(float visibility) { this.visibility = visibility; }
        public void setOzone(float ozone) { this.ozone = ozone; }
        public void setComfortIndex(double comfortIndex) { this.comfortIndex = comfortIndex; }
        public void setTempIndex(double tempIndex) { this.tempIndex = tempIndex; }
        public void setDewIndex(double dewIndex) { this.dewIndex = dewIndex; }
        public void setPrecipIndex(double precipIndex) { this.precipIndex = precipIndex; }
        public void setWindSpeedIndex(double windSpeedIndex) { this.windSpeedIndex = windSpeedIndex; }
        public void setUvIndexIndex(double uvIndexIndex) { this.uvIndexIndex = uvIndexIndex; }
        public void setCloudCoverIndex(double cloudCoverIndex) { this.cloudCoverIndex = cloudCoverIndex; }
        public void setPrecipIntensityIndex(double precipIntensityIndex) { this.precipIntensityIndex = precipIntensityIndex; }
    }


    public class Minutely {
        private String summary;
        private String icon;
        private List time;
        private List precipIntensity;
        private List precipProbability;
        private List precipType;
        private List precipIntensityError;



        //Getter Methods
        public String getSummary() { return summary; }
        public String getIcon() { return icon; }
        public List getTime() {return time;}
        public List getPrecipIntensity() { return precipIntensity; }
        public List getPrecipProbability() { return precipProbability; }
        public List getPrecipType() { return precipType; }
        public List getPrecipIntensityError() { return precipIntensityError; }

        //Setter Methods
        public void setSummary( String summary) { this.summary = summary; }
        public void setIcon( String icon) { this.icon = icon; }
        public void setTime( List time) { this.time = time; }
        public void setPrecipIntensity( List precipIntensity) { this.precipIntensity = precipIntensity; }
        public void setPrecipProbability( List precipProbability) { this.precipProbability = precipProbability; }
        public void setPrecipType( List precipType) { this.precipType = precipType; }
        public void setPrecipIntensityError( List precipIntensityError) { this.precipIntensityError = precipIntensityError; }
    }


    public class Hourly {
        private String fullSummary;
        private String fullIcon;
        private List time;
        private List summary;
        private List icon;
        private List precipIntensity;
        private List precipProbability;
        private List precipType;
        private List temperature;
        private List apparentTemperature;
        private List dewPoint;
        private List humidity;
        private List pressure;
        private List windSpeed;
        private List windGust;
        private List windBearing;
        private List cloudCover;
        private List uvIndex;
        private List visibility;
        private List ozone;
        private List comfortIndex;





        // Getter Methods
        public String getFullSummary() { return fullSummary; }
        public String getFullIcon() { return fullIcon; }
        public List getTime() { return time; }
        public List getSummary() { return summary; }
        public List getIcon() { return icon; }
        public List getPrecipIntensity() { return precipIntensity; }
        public List getPrecipProbability() { return precipProbability; }
        public List getPrecipType() { return precipType; }
        public List getTemperature() { return temperature; }
        public List getApparentTemperature() { return apparentTemperature; }
        public List getDewPoint() { return dewPoint; }
        public List getHumidity() { return humidity; }
        public List getPressure() { return pressure; }
        public List getWindSpeed() { return windSpeed; }
        public List getWindGust() { return windGust; }
        public List getWindBearing() { return windBearing; }
        public List getCloudCover() { return cloudCover; }
        public List getUvIndex() { return uvIndex; }
        public List getVisibility() { return visibility; }
        public List getOzone() { return ozone; }
        public List getComfortIndex() { return comfortIndex; }
// Setter Methods

        public void setFullSummary(String fullSummary) { this.fullSummary = fullSummary; }
        public void setFullIcon(String fullIcon) { this.fullIcon = fullIcon; }
        public void setTime(List time) { this.time = time; }
        public void setSummary(List summary) { this.summary = summary; }
        public void setIcon(List icon) { this.icon = icon; }
        public void setPrecipIntensity(List precipIntensity) { this.precipIntensity = precipIntensity; }
        public void setPrecipProbability(List precipProbability) { this.precipProbability = precipProbability; }
        public void setPrecipType(List precipType) { this.precipType = precipType; }
        public void setTemperature(List temperature) { this.temperature = temperature; }
        public void setApparentTemperature(List apparentTemperature) { this.apparentTemperature = apparentTemperature; }
        public void setDewPoint(List dewPoint) { this.dewPoint = dewPoint; }
        public void setHumidity(List humidity) { this.humidity = humidity; }
        public void setPressure(List pressure) { this.pressure = pressure; }
        public void setWindSpeed(List windSpeed) { this.windSpeed = windSpeed; }
        public void setWindGust(List windGust) { this.windGust = windGust; }
        public void setWindBearing(List windBearing) { this.windBearing = windBearing; }
        public void setCloudCover(List cloudCover) { this.cloudCover = cloudCover; }
        public void setUvIndex(List uvIndex) { this.uvIndex = uvIndex; }
        public void setVisibility(List visibility) { this.visibility = visibility; }
        public void setOzone(List ozone) { this.ozone = ozone; }
        public void setComfortIndex(List comfortIndex) { this.comfortIndex = comfortIndex; }

    }


    public class Daily {
        private String fullSummary;
        private String fullIcon;
        private List time;
        private List summary;
        private List icon;
        private List sunriseTime;
        private List sunsetTime;
        private List moonPhase;
        private List precipIntensity;
        private List precipIntensityMax;
        private List precipIntensityMaxTime;
        private List precipProbability;
        private List precipType;
        private List temperatureHigh;
        private List temperatureHighTime;
        private List temperatureLow;
        private List temperatureLowTime;
        private List apparentTemperatureHigh;
        private List apparentTemperatureHighTime;
        private List apparentTemperatureLow;
        private List apparentTemperatureLowTime;
        private List dewPoint;
        private List humidity;
        private List pressure;
        private List windSpeed;
        private List windGust;
        private List windGustTime;
        private List windBearing;
        private List cloudCover;
        private List uvIndex;
        private List uvIndexTime;
        private List visibility;
        private List ozone;
        private List temperatureMin;
        private List temperatureMinTime;
        private List temperatureMax;
        private List temperatureMaxTime;
        private List apparentTemperatureMin;
        private List apparentTemperatureMinTime;
        private List apparentTemperatureMax;
        private List apparentTemperatureMaxTime;
        private List sunriseDateList;
        private List sunsetDateList;


        //Getter Methods


        public String getFullSummary() { return fullSummary; }
        public String getFullIcon() { return fullIcon; }
        public  List getTime() { return time; }
        public  List getSummary() { return summary; }
        public  List getIcon() { return icon; }
        public  List getSunriseTime() { return sunriseTime; }
        public  List getSunsetTime() { return sunsetTime; }
        public  List getMoonPhase() { return moonPhase; }
        public  List getPrecipIntensity() { return precipIntensity; }
        public  List getPrecipIntensityMax() { return precipIntensityMax; }
        public  List getPrecipIntensityMaxTime() { return precipIntensityMaxTime; }
        public  List getPrecipProbability() { return precipProbability; }
        public  List getPrecipType() { return precipType; }
        public  List getTemperatureHigh() { return temperatureHigh; }
        public  List getTemperatureHighTime() { return temperatureHighTime; }
        public  List getTemperatureLow() { return temperatureLow; }
        public  List getTemperatureLowTime() { return temperatureLowTime; }
        public  List getApparentTemperatureHigh() { return apparentTemperatureHigh; }
        public  List getApparentTemperatureHighTime() { return apparentTemperatureHighTime; }
        public  List getApparentTemperatureLow() { return apparentTemperatureLow; }
        public  List getApparentTemperatureLowTime() { return apparentTemperatureLowTime; }
        public  List getDewPoint() { return dewPoint; }
        public  List getHumidity() { return humidity; }
        public  List getPressure() { return pressure; }
        public  List getWindSpeed() { return windSpeed; }
        public  List getWindGust() { return windGust; }
        public  List getWindGustTime() { return windGustTime; }
        public  List getWindBearing() { return windBearing; }
        public  List getCloudCover() { return cloudCover; }
        public  List getUvIndex() { return uvIndex; }
        public  List getUvIndexTime() { return uvIndexTime; }
        public  List getVisibility() { return visibility; }
        public  List getOzone() { return ozone; }
        public  List getTemperatureMin() { return temperatureMin; }
        public  List getTemperatureMinTime() { return temperatureMinTime; }
        public  List getTemperatureMax() { return temperatureMax; }
        public  List getTemperatureMaxTime() { return temperatureMaxTime; }
        public  List getApparentTemperatureMin() { return apparentTemperatureMin; }
        public  List getApparentTemperatureMinTime() { return apparentTemperatureMinTime; }
        public  List getApparentTemperatureMax() { return apparentTemperatureMax; }
        public  List getApparentTemperatureMaxTime() { return apparentTemperatureMaxTime; }
        public  List getSunriseDateList() { return sunriseDateList; }
        public  List getSunsetDateList() { return sunsetDateList; }


//Setter Methods


        public void setFullSummary(String fullSummary) { this.fullSummary = fullSummary; }
        public void setFullIcon(String fullIcon) { this.fullIcon = fullIcon; }
        public void setTime(List time) { this.time = time; }
        public void setSummary( List summary) { this.summary = summary; }
        public void setIcon( List icon) { this.icon = icon; }
        public void setSunriseTime( List sunriseTime) { this.sunriseTime = sunriseTime; }
        public void setSunsetTime( List sunsetTime) { this.sunsetTime = sunsetTime; }
        public void setMoonPhase( List moonPhase) { this.moonPhase = moonPhase; }
        public void setPrecipIntensity( List precipIntensity) { this.precipIntensity = precipIntensity; }
        public void setPrecipIntensityMax( List precipIntensityMax) { this.precipIntensityMax = precipIntensityMax; }
        public void setPrecipIntensityMaxTime( List precipIntensityMaxTime) { this.precipIntensityMaxTime = precipIntensityMaxTime; }
        public void setPrecipProbability( List precipProbability) { this.precipProbability = precipProbability; }
        public void setPrecipType( List precipType) { this.precipType = precipType; }
        public void setTemperatureHigh( List temperatureHigh) { this.temperatureHigh = temperatureHigh; }
        public void setTemperatureHighTime( List temperatureHighTime) { this.temperatureHighTime = temperatureHighTime; }
        public void setTemperatureLow( List temperatureLow) { this.temperatureLow = temperatureLow; }
        public void setTemperatureLowTime( List temperatureLowTime) { this.temperatureLowTime = temperatureLowTime; }
        public void setApparentTemperatureHigh( List apparentTemperatureHigh) { this.apparentTemperatureHigh = apparentTemperatureHigh; }
        public void setApparentTemperatureHighTime( List apparentTemperatureHighTime) { this.apparentTemperatureHighTime = apparentTemperatureHighTime; }
        public void setApparentTemperatureLow( List apparentTemperatureLow) { this.apparentTemperatureLow = apparentTemperatureLow; }
        public void setApparentTemperatureLowTime( List apparentTemperatureLowTime) { this.apparentTemperatureLowTime = apparentTemperatureLowTime; }
        public void setDewPoint( List dewPoint) { this.dewPoint = dewPoint; }
        public void setHumidity( List humidity) { this.humidity = humidity; }
        public void setPressure( List pressure) { this.pressure = pressure; }
        public void setWindSpeed( List windSpeed) { this.windSpeed = windSpeed; }
        public void setWindGust( List windGust) { this.windGust = windGust; }
        public void setWindGustTime( List windGustTime) { this.windGustTime = windGustTime; }
        public void setWindBearing( List windBearing) { this.windBearing = windBearing; }
        public void setCloudCover( List cloudCover) { this.cloudCover = cloudCover; }
        public void setUvIndex( List uvIndex) { this.uvIndex = uvIndex; }
        public void setUvIndexTime( List uvIndexTime) { this.uvIndexTime = uvIndexTime; }
        public void setVisibility( List visibility) { this.visibility = visibility; }
        public void setOzone( List ozone) { this.ozone = ozone; }
        public void setTemperatureMin( List temperatureMin) { this.temperatureMin = temperatureMin; }
        public void setTemperatureMinTime( List temperatureMinTime) { this.temperatureMinTime = temperatureMinTime; }
        public void setTemperatureMax( List temperatureMax) { this.temperatureMax = temperatureMax; }
        public void setTemperatureMaxTime( List temperatureMaxTime) { this.temperatureMaxTime = temperatureMaxTime; }
        public void setApparentTemperatureMin( List apparentTemperatureMin) { this.apparentTemperatureMin = apparentTemperatureMin; }
        public void setApparentTemperatureMinTime( List apparentTemperatureMinTime) { this.apparentTemperatureMinTime = apparentTemperatureMinTime; }
        public void setApparentTemperatureMax( List apparentTemperatureMax) { this.apparentTemperatureMax = apparentTemperatureMax; }
        public void setApparentTemperatureMaxTime( List apparentTemperatureMaxTime) { this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;}
        public void setSunriseDateList(List sunriseDateList) { this.sunriseDateList = sunriseDateList; }
        public void setSunsetDateList(List sunsetDateList) { this.sunsetDateList = sunsetDateList; }
    }

    public class Alerts {
        private List title;
        private List regions;
        private List severity;
        private List time;
        private List expireTime;
        private List description;
        private List url;

        //GETTER METHODS
        public List getTitle() {
            return title;
        }

        public List getRegions() {
            return regions;
        }

        public List getSeverity() {
            return severity;
        }

        public List getTime() {
            return time;
        }

        public List getExpireTime() {
            return expireTime;
        }

        public List getDescription() {
            return description;
        }


        //SETTER METHODS

        public void setTitle(List title) {
            this.title = title;
        }

        public void setRegions(List regions) {
            this.regions = regions;
        }

        public void setSeverity(List severity) {
            this.severity = severity;
        }

        public void setTime(List time) {
            this.time = time;
        }

        public void setExpireTime(List expireTime) {
            this.expireTime = expireTime;
        }

        public void setDescription(List description) {
            this.description = description;
        }

        public List getUrl() {
            return url;
        }

        public void setUrl(List url) {
            this.url = url;
        }
    }

    public class Flags {

        private float nearestStation;
        private String units;

        // Getter Methods
        public float getNearestStation() { return nearestStation; }
        public String getUnits() { return units; }

        // Setter Methods
        public void setNearestStation(float nearestStation) { this.nearestStation = nearestStation; }
        public void setUnits(String units) { this.units = units; }
    }



}

