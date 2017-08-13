package com.istandev.musicmax.utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.istandev.musicmax.DB.DBHelper;
import com.istandev.musicmax.entity.Playlist;
import com.istandev.musicmax.entity.Track;

import java.util.ArrayList;

/**
 * Created by ADIK on 15/10/2016.
 */

public class CRUD {

    public static void addToTrackDB(ArrayList<Track> tracks, Context context){

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.deleteAllTable(Constant.TABLE_NAME_TRACK);

        for (int i=0;i<tracks.size();i++){

            Track track = tracks.get(i);

            ContentValues contentValues = new ContentValues();

            contentValues.put(Constant.TRACK_ID, track.getID());
            contentValues.put(Constant.TRACK_ID_PLAY, track.getIdPlay());
            contentValues.put(Constant.TRACK_NAMA, track.getTitle());
            contentValues.put(Constant.TRACK_STREAM_URL, track.getStreamURL());
            contentValues.put(Constant.TRACK_IMG_URL, track.getArtworkURL());
            contentValues.put(Constant.TRACK_DOWNLOADABLE, track.getDownloadable());
            contentValues.put(Constant.TRACK_DURATION, track.getDuration());
            contentValues.put(Constant.TRACK_DOWLOAD_URL, track.getDownloadURL());

            Uri contentURI = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK).build();
            Uri uri = context.getContentResolver().insert(contentURI, contentValues);
        }

    }

    public static void addToPlaylistItemDB(Track track, Context context){

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.TRACK_ID, track.getID());
        contentValues.put(Constant.TRACK_ID_PLAY, track.getIdPlay());
        contentValues.put(Constant.TRACK_NAMA, track.getTitle());
        contentValues.put(Constant.PLAYLIST_ID, track.getIdPlaylist());
        contentValues.put(Constant.TRACK_STREAM_URL, track.getStreamURL());
        contentValues.put(Constant.TRACK_IMG_URL, track.getArtworkURL());
        contentValues.put(Constant.TRACK_DOWNLOADABLE, track.getDownloadable());
        contentValues.put(Constant.TRACK_DURATION, track.getDuration());
        contentValues.put(Constant.TRACK_DOWLOAD_URL, track.getDownloadURL());

        Uri contentURI = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK_FAVORIT).build();
        Uri uri = context.getContentResolver().insert(contentURI, contentValues);

    }

    public static void deletePlaylistItemDB(Track track, Context context){

        Uri contentURI = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK_FAVORIT).build();
        String mSelectionClause = Constant.TRACK_ID + " = ?";
        String[] mSelectionArgs = {String.valueOf(track.getID())};
        context.getContentResolver().delete(contentURI, mSelectionClause, mSelectionArgs);

    }

    /*
    playlist CRUD
     */

    public static void addToPlaylistDB(Playlist playlist, Context context){

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.PLAYLIST_NAME, playlist.getNama_playlist());

        Uri contentURI = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_PLAYLIST).build();
        Uri uri = context.getContentResolver().insert(contentURI, contentValues);

    }
    public static void deletePlaylistDB(Playlist playlist, Context context){

        Uri contentURI = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_PLAYLIST).build();
        String mSelectionClause = Constant.PLAYLIST_ID + " = ?";
        String[] mSelectionArgs = {String.valueOf(playlist.getId_playlist())};
        context.getContentResolver().delete(contentURI, mSelectionClause, mSelectionArgs);

    }




}
