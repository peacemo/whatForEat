package com.carlchenxq.zidong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //public static final String DATABASE_NAME = "food.db";
    //public static final int VERSION_CODE = 1;
    //public static final String TABLE_NAME = "menu";
    public static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //第一次创建的时候调用
        Log.d(TAG, "创建数据库...");
        String sql = "create table menu(name varchar(100),loc varchar(100),primary key(name,loc))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级数据库的回调
        Log.d(TAG, "升级数据库...");
    }
}
