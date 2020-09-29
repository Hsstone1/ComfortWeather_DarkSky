package com.example.sneek.outdoorindex;

public class City {

    private String name;
    private String currentTemp;
    private String precipChance;

    public City(String name, String currentTemp, String precipChance) {
        this.name = name;
        this.currentTemp = currentTemp;
        this.precipChance = precipChance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(String precipChance) {
        this.precipChance = precipChance;
    }
}
