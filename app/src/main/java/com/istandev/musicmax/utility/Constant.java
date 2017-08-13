package com.istandev.musicmax.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.istandev.musicmax.R;

/**
 * Created by ADIK on 24/08/2016.
 */
public class Constant {

   // public static final String MUSIC_MAX_URL = "http://adikistanto.esy.es/rmd/musicmax.php";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MUSICMAX = "musicmax";


    public static final String PROVIDER_NAME = "com.istandev.musicmax";
    public static final String URL = "content://" + PROVIDER_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static String TABLE_NAME_TRACK = "tb_track";
    public static String TABLE_NAME_TRACK_FAVORIT = "tb_favorit";
    public static String TRACK_ID = "id_track";
    public static String TRACK_ID_PLAY = "id_play";
    public static String TRACK_NAMA = "nama_track";
    public static String TRACK_STREAM_URL = "stream_url";
    public static String TRACK_IMG_URL = "img_url";
    public static String TRACK_DURATION = "duration";
    public static String TRACK_DOWNLOADABLE = "downloadable";
    public static String TRACK_DOWLOAD_URL = "downlaod_url";

    public static String TABLE_NAME_PLAYLIST = "tb_playlist";
    public static String PLAYLIST_ID = "id_playlist";
    public static String PLAYLIST_NAME = "name_playlist";




    public static final int TRACK = 101;
    public static final int LOADER_TRACK = 101;

    public static final int FAVORIT = 102;
    public static final int LOADER_FAVORIT = 102;

    public static final int PLAYLIST = 103;
    public static final int LOADER_PLAYLIST = 103;


    public static String DOWNLOAD_STATUS="1";


}

