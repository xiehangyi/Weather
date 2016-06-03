package com.xhy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhy.entity.Weather;
import com.xhy.weather.R;

import java.util.ArrayList;

/**
 * Created by change100 on 2016/5/31.
 */
public class WeaAdapter extends BaseAdapter{

    private ArrayList<Weather> data;

    private Context context;

    private LayoutInflater layoutInflater;

    public WeaAdapter(ArrayList<Weather> data, Context context) {
        this.data = data;
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            //  没有可重复
            //  创建一个视图项
            convertView = layoutInflater.inflate(R.layout.list_weather_demo, parent, false);

            //当前的ViewHolder需要保存视图结构
            holder = new ViewHolder(convertView);

            //视图绑定一个对象
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        //  绑定数据
        Weather weather = data.get(position);

        //在视图中绑定数据
        holder.bindData(weather);

        return convertView;
    }

    class ViewHolder {

        ImageView icon_left;
        ImageView icon_right;
        TextView tem;
        TextView weather;
        TextView date;



        public ViewHolder(View v) {

            icon_left = (ImageView) v.findViewById(R.id.image_left);
            icon_right = (ImageView) v.findViewById(R.id.image_right);
            tem = (TextView) v.findViewById(R.id.tv_tem);
            weather = (TextView) v.findViewById(R.id.tv_wea);
            date = (TextView) v.findViewById(R.id.tv_date);

        }


        public void bindData(Weather w) {

            icon_left.setImageResource(w.getIcon_left());
            icon_right.setImageResource(w.getIcon_right());
            tem.setText(w.getTemperature());
            weather.setText(w.getWeather());
            date.setText(w.getDate());

        }
    }
}
