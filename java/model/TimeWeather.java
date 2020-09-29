package model;

import java.util.List;

public class TimeWeather {

    public TimeWeather.Daily dailyCondition = new TimeWeather.Daily();
    public TimeWeather.Hourly hourlyCondition = new TimeWeather.Hourly();
    public TimeWeather.Flags flags = new TimeWeather.Flags();


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
