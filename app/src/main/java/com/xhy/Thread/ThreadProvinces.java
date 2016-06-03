package com.xhy.Thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xhy.weather.MainActivity;
import com.xhy.tool.WeatherService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by change100 on 2016/5/30.
 */
public class ThreadProvinces extends Thread{

    private List<String> listProvinces;
    // WeatherService weatherService;
    Handler handler;

    @Override
    public void run() {

        try {
            Log.v("123", "dd");
            listProvinces = WeatherService.getProvinceList();
            Log.v("456", "ee");
            String[] strProvinces = new String[listProvinces.size()];
            for (int i = 0; i < listProvinces.size(); i++) {
                Log.v(i + "", "bb");
                Log.v(listProvinces.get(i), "aa");
                strProvinces[i] = listProvinces.get(i);
            }

            Message msg = new Message();
            msg.what = 0x0001;
            Bundle data = new Bundle();
            data.putStringArray("Provinces", strProvinces);
            msg.setData(data);
            MainActivity.handler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what = 0x00010;
            Bundle data = new Bundle();
            data.putString("wrong", "无法连接到网络或天气接口失效");
            msg.setData(data);
            MainActivity.handler.sendMessage(msg);
        }
    }

    public List<String> getProvincesList(){
        return listProvinces;
    }
}
