package com.xhy.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by change100 on 2016/6/3.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    private static final String CREATE_TABLE_WEATHER_SQL =
            "create table weather(" +
            "city text primary key autoincrement," +
            "date text not null," +
            "weather text not null," +
            "temperature text not null," +
            "icon_left integer," +
            "icon_right integer)" ;



    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEATHER_SQL);
        Log.v("databaseHelper", "开始构造数据库");
    }

    // 升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS weather");
        onCreate(db);
        Log.v("databaseHelper","开始构造数据库");
    }
}
