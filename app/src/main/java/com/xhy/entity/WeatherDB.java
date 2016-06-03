package com.xhy.entity;

/**
 * Created by change100 on 2016/6/3.
 */
public class WeatherDB {
    private String city;
    private String date;
    private String weather;
    private String temperature;
    private int icon_left;
    private int icon_right;

    public WeatherDB() {

    }


    public WeatherDB(String city,String date, String weather, String temperature, int icon_left, int icon_right) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.icon_left = icon_left;
        this.icon_right = icon_right;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }


    public int getIcon_left() {
        return icon_left;
    }

    public void setIcon_left(int icon_left) {
        this.icon_left = icon_left;
    }

    public int getIcon_right() {
        return icon_right;
    }

    public void setIcon_right(int icon_right) {
        this.icon_right = icon_right;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
