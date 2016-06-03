package com.xhy.tool;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.xhy.weather.MainActivity;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by change100 on 2016/5/30.
 */
public class WeatherAnalysis extends Thread{


    // WeatherAnalysis weatherAnalysis = new WeatherAnalysis();

    private String weatherToday = null;
    private String weatherTomorrow = null;
    private String weatherAfterday = null;
    private String weatherAfterday2 = null;
    private String weatherAfterday3 = null;


    private String weatherCurrent = null;


    private String city;
    private SoapObject detail;
    private String date;

    public WeatherAnalysis(){

    }
    public WeatherAnalysis(String city){
        this.city = city;

    }


    public void showWeather()
    {

        // 根据城市获取城市具体天气情况
        // 获取天气实况

        weatherCurrent = detail.getProperty(4).toString();

        // 解析今天的天气情况
        date = detail.getProperty(7).toString();
        weatherToday = date.split(" ")[0];
        weatherToday = weatherToday + ";" + date.split(" ")[1];
        weatherToday = weatherToday + ";"
                + detail.getProperty(8).toString();
        weatherToday = weatherToday + ";"
                + detail.getProperty(10).toString();
        weatherToday = weatherToday + ";"
                + detail.getProperty(11).toString();


        // 解析明天的天气情况
        date = detail.getProperty(12).toString();
        weatherTomorrow = date.split(" ")[0];
        weatherTomorrow = weatherTomorrow + ";" + date.split(" ")[1];
        weatherTomorrow = weatherTomorrow + ";"
                + detail.getProperty(13).toString();
        weatherTomorrow = weatherTomorrow + ";"
                + detail.getProperty(15).toString();
        weatherTomorrow = weatherTomorrow + ";"
                + detail.getProperty(16).toString();


        // 解析后天的天气情况
        date = detail.getProperty(17).toString();
        weatherAfterday = date.split(" ")[0];
        weatherAfterday = weatherAfterday + ";" + date.split(" ")[1];
        weatherAfterday = weatherAfterday + ";"
                + detail.getProperty(18).toString();
        weatherAfterday = weatherAfterday + ";"
                + detail.getProperty(20).toString();
        weatherAfterday = weatherAfterday + ";"
                + detail.getProperty(21).toString();

        // 解析第四天的天气情况
        date = detail.getProperty(22).toString();
        weatherAfterday2 = date.split(" ")[0];
        weatherAfterday2 = weatherAfterday2 + ";" + date.split(" ")[1];
        weatherAfterday2 = weatherAfterday2 + ";"
                + detail.getProperty(23).toString();
        weatherAfterday2 = weatherAfterday2 + ";"
                + detail.getProperty(25).toString();
        weatherAfterday2 = weatherAfterday2 + ";"
                + detail.getProperty(26).toString();


        // 解析第五天的天气情况
        date = detail.getProperty(27).toString();
        weatherAfterday3 = "后天：" + date.split(" ")[0];
        weatherAfterday3 = weatherAfterday3 + ";" + date.split(" ")[1];
        weatherAfterday3 = weatherAfterday3 + ";"
                + detail.getProperty(28).toString();
        weatherAfterday3 = weatherAfterday3 + ";"
                + detail.getProperty(30).toString();
        weatherAfterday3 = weatherAfterday3 + ";"
                + detail.getProperty(31).toString();



    }

    public String getWeatherToday() {
        return weatherToday;
    }


    public String getWeatherTomorrow() {
        return weatherTomorrow;
    }


    public String getWeatherAfterday() {
        return weatherAfterday;
    }

    public SoapObject getDetail() {
        return detail;
    }

    @Override
    public void run() {

        // 获取远程Web Service返回的对象
        detail = WeatherService.getWeatherByCity(city);
        if(detail != null) {
            showWeather();
            Log.i("bb", weatherToday);
            Message msg = new Message();
            msg.what = 0x0003;
            Bundle data = new Bundle();
            data.putString("WeatherToday", weatherToday);
            data.putString("weatherTomorrow", weatherTomorrow);
            data.putString("weatherAfterday", weatherAfterday);
            data.putString("weatherAfterday2", weatherAfterday2);
            data.putString("weatherAfterday3", weatherAfterday3);
            msg.setData(data);
            MainActivity.handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x00010;
            Bundle data = new Bundle();
            data.putString("wrong", "无法连接到网络或天气接口失效");
            msg.setData(data);
            MainActivity.handler.sendMessage(msg);
        }
    }
}
