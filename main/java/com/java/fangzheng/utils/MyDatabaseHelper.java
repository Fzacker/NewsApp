package com.java.fangzheng.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "newsApp.db";
    private Context mContext;
    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "password text)";
    public static final String CREATE_COLLECTION_NEWS = "create table Collection ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_publisher text,"
            + "news_imageUrl text,"
            + "news_videoUrl text,"
            + "news_content text)";

    public static final String CREATE_HISTORY_NEWS = "create table History ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_publisher text,"
            + "news_imageUrl text,"
            + "news_videoUrl text,"
            + "news_content text)";

    public MyDatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_COLLECTION_NEWS);
        sqLiteDatabase.execSQL(CREATE_HISTORY_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
