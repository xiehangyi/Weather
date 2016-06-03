package com.xhy.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhy.weather.R;

import java.util.ArrayList;

/**
 * Created by change100 on 2016/6/1.
 */
public class SaveCityAdapter extends BaseAdapter {

    //Preferece机制操作的文件名
    public static final String PREFERENCE_NAME = "SaveCity";
    //Preferece机制的操作模式
    public static int MODE = Context.MODE_PRIVATE;

    private ArrayList<String> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private SharedPreferences sharedPreferences;

    public SaveCityAdapter(ArrayList<String> data, Context context,SharedPreferences sharedPreferences) {

        this.sharedPreferences = sharedPreferences;
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

            //  加载XML布局，实例化，并且返回视图
            convertView = layoutInflater.inflate(R.layout.list_show_citys, parent, false);

            //  获得【视图项】的结构
            holder = new ViewHolder(convertView,data.get(position),sharedPreferences,position);

            //  为视图绑定一个数据（结构）
            convertView.setTag(holder);
        } else {
            //重可复用的视图项中获得结构
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(data.get(position));


        return convertView;
    }

    /**
     * 适配器视图中【视图项】的结构
     */
    class ViewHolder {

        private TextView tv_showCitys;
        private ImageView img_remove;
        private SharedPreferences sharedPreferences;
        private int position;


        public ViewHolder(View v,String city,SharedPreferences sharedPreferences,int position) {

            this.position = position;
            this.sharedPreferences = sharedPreferences;
            final String city1 = city;
            tv_showCitys = (TextView) v.findViewById(R.id.tv_showCitys);
            img_remove = (ImageView) v.findViewById(R.id.img_remove);

            img_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCity(city1);
                }
            });

        }

        private void removeCity(String city){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(city);
            editor.commit();
            data.remove(position);
            notifyDataSetChanged();
        }

        public void bindData(String s) {

            tv_showCitys.setText(s);

        }

    }


}
