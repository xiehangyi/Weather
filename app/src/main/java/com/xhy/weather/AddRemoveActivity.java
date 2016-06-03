package com.xhy.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xhy.Adapter.SaveCityAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddRemoveActivity extends AppCompatActivity {

    //Preferece机制操作的文件名
    public static final String PREFERENCE_NAME = "SaveCity";
    //Preferece机制的操作模式
    public static int MODE = Context.MODE_PRIVATE;

    private SharedPreferences sharedPreferences;

    Map<String, ?> ms;
    ArrayList<String> data;

    private ListView listView;
    private SaveCityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove);

        loadFromPreferece();
        setListView();
    }



    // 生成列表
    private void setListView() {
        listView = (ListView) findViewById(R.id.lv_showCitys);
        if(data != null) {

            adapter = new SaveCityAdapter(
                    data,
                    AddRemoveActivity.this,
                    sharedPreferences
            );

            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddRemoveActivity.this,MainActivity.class);
                intent.putExtra("cityName",data.get(position));
                startActivity(intent);
            }
        });

    }


    // 建立存储城市数据
    private void loadFromPreferece() {

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);

        ms = new HashMap<>();
        ms = sharedPreferences.getAll();
        data = new ArrayList<>();

        for(Object value:ms.values()){
            data.add(value.toString());
        }
    }

}
