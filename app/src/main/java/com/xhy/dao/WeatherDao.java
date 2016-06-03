package com.xhy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.xhy.Helper.DataBaseHelper;
import com.xhy.entity.Weather;
import com.xhy.entity.WeatherDB;

import java.sql.SQLDataException;

/**
 * Created by change100 on 2016/6/3.
 */
public class WeatherDao {

    private static final String DB_TABLE = "weather";
    private SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;
    private String TAG = "WeatherDao";

    public WeatherDao(Context context) {
        this.context = context;
    }

    // 打开数据库
    public void open() {
        dbHelper = new DataBaseHelper(context, "WeatherMange.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    // 关闭数据库
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    // 插入数据
    public long addWeatherDB(WeatherDB w) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("city", w.getCity());
        contentValues.put("date", w.getDate());
        contentValues.put("weather", w.getWeather());
        contentValues.put("temperature", w.getTemperature());
        contentValues.put("icon_left", w.getIcon_left());
        contentValues.put("icon_right", w.getIcon_right());
        contentValues.put("view",w.getView());
        return db.insert(DB_TABLE, null, contentValues);
    }

    //获得weather表中所有用户
    public WeatherDB[] showAllWeather() {
        //查询所有用户，获得id，userName和userPassw
        Cursor cursor = db.query(DB_TABLE,
                new String[]{"city", "date", "weather", "temperature",
                        "icon_left", "icon_right","view"},
                null, null, null, null, null);
        return convertToWeather(cursor);
    }

    public WeatherDB[] showOneWeather(String city) {

        Cursor result = db.rawQuery("select * from weather where city = ?", new String[]{city});

        return convertToWeather(result);
    }

    public boolean checkData(String city) {

        Cursor result = db.rawQuery("select * from weather where city = ?", new String[]{city});
//            Cursor result =  db.query(DB_TABLE, new String[] {"city","date","weather","temperature",
//                "icon_left","icon_right"}, "city" + "=" + city, null, null, null, null);
        if(result.moveToFirst()){
            return true;
        }

        return false;
    }

    //删除所有用户
    public void deleteAllWeather() {
        //删除weather表中所有数据
        db.delete(DB_TABLE, null, null);
    }

    public void deleteOneWeather(String city) {
        //删除weather表中所有数据
        db.delete(DB_TABLE, "city" + "=?", new String[]{city});
    }


    private WeatherDB[] convertToWeather(Cursor cursor) {
        //获得cursor中数据的个数
        int count = cursor.getCount();
        Log.i("aa", String.valueOf(count) + "count");
        if (count == 0)
            return null;
        WeatherDB[] weatherDB = new WeatherDB[count];
        cursor.moveToFirst(); //cursor指向第一个数据
        for (int i = 0; i < count; i++) {

            WeatherDB w = new WeatherDB();

            //获得各种属性
            w.setCity(cursor.getString(0));
            w.setDate(cursor.getString(1));
            w.setWeather(cursor.getString(2));
            w.setTemperature(cursor.getString(3));
            w.setIcon_left(cursor.getInt(4));
            w.setIcon_right(cursor.getInt(5));
            w.setView(cursor.getString(6));

            weatherDB[i] = w;

            //cursor指向下一个数据
            cursor.moveToNext();
        }
        return weatherDB;
    }

}
