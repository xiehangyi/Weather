package com.xhy.Thread;

import android.os.Bundle;
import android.os.Message;

import com.xhy.weather.MainActivity;
import com.xhy.tool.WeatherService;

import java.util.List;

/**
 * Created by change100 on 2016/5/30.
 */
public class ThreadCitys extends Thread{

    private String province;
    private List<String> citys;

    public ThreadCitys(String province){
        this.province = province;
    }

    @Override
    public void run() {

        citys = WeatherService.getCityListByProvince(province);
        String[] strCitys = new String[citys.size()];
        for(int i = 0;i<citys.size();i++){

            strCitys[i] = citys.get(i);
        }

        Message msg = new Message();
        msg.what = 0x0002;
        Bundle data = new Bundle();
        data.putStringArray("Citys", strCitys);
        msg.setData(data);
        MainActivity.handler.sendMessage(msg);

    }


}
