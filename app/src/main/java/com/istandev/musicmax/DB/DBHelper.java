package com.istandev.musicmax.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.istandev.musicmax.utility.Constant;

/**
 * Created by ADIK on 24/08/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "musicmax.db";

    static final int DATABASE_VERSION = 10;

    static final String CREATE_TABLE_TRACK =
            " CREATE TABLE " + Constant.TABLE_NAME_TRACK +
                    " ("+Constant.TRACK_ID+" INTEGER PRIMARY KEY, " +
                    Constant.TRACK_ID_PLAY+" INTEGER, " +
                    Constant.TRACK_NAMA+" TEXT, " +
                    Constant.TRACK_STREAM_URL+" TEXT, " +
                    Constant.TRACK_IMG_URL+" TEXT, " +
                    Constant.TRACK_DURATION+" TEXT, " +
                    Constant.TRACK_DOWNLOADABLE+" TEXT, " +
                    Constant.TRACK_DOWLOAD_URL+" TEXT);";

    static final String CREATE_TABLE_FAVORIT =
            " CREATE TABLE " + Constant.TABLE_NAME_TRACK_FAVORIT +
                    " ("+Constant.TRACK_ID +" INTEGER PRIMARY KEY, " +
                    Constant.TRACK_ID_PLAY+" INTEGER, " +
                    Constant.PLAYLIST_ID+" INTEGER, " +
                    Constant.TRACK_NAMA +" TEXT, " +
                    Constant.TRACK_STREAM_URL +" TEXT, " +
                    Constant.TRACK_IMG_URL +" TEXT, " +
                    Constant.TRACK_DURATION +" TEXT, " +
                    Constant.TRACK_DOWNLOADABLE +" TEXT, " +
                    Constant.TRACK_DOWLOAD_URL +" TEXT);";

    static final String CREATE_TABLE_PLAYLIST=
            " CREATE TABLE " + Constant.TABLE_NAME_PLAYLIST +
                    " ("+Constant.PLAYLIST_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constant.PLAYLIST_NAME +" INTEGER);";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRACK);
        db.execSQL(CREATE_TABLE_FAVORIT);
        db.execSQL(CREATE_TABLE_PLAYLIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_NAME_TRACK);
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_NAME_TRACK_FAVORIT);
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_NAME_PLAYLIST);
        onCreate(db);
    }

    public void deleteAllTable(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name,null, null);

    }
}
