package com.xhy.entity;

/**
 * 用于封装天气信息的类
 * Created by change100 on 2016/5/31.
 */
public class Weather {
    private String date;
    private String weather;
    private String temperature;
    private int icon_left;
    private int icon_right;

    public Weather(String date, String weather, String temperature) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
    }

    public Weather(String date, String weather, String temperature, int icon_left, int icon_right) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.icon_left = icon_left;
        this.icon_right = icon_right;
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
