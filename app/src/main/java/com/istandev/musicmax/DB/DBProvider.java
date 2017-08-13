package com.istandev.musicmax.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.istandev.musicmax.utility.Constant;

/**
 * Created by ADIK on 24/08/2016.
 */
public class DBProvider extends ContentProvider {
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(Constant.PROVIDER_NAME, Constant.TABLE_NAME_TRACK, Constant.TRACK);
        sURIMatcher.addURI(Constant.PROVIDER_NAME, Constant.TABLE_NAME_TRACK_FAVORIT, Constant.FAVORIT);
        sURIMatcher.addURI(Constant.PROVIDER_NAME, Constant.TABLE_NAME_PLAYLIST, Constant.PLAYLIST);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sURIMatcher.match(uri)) {
            case Constant.TRACK:
            {
                retCursor = dbHelper.getReadableDatabase().query(
                        Constant.TABLE_NAME_TRACK,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        Constant.TRACK_ID +" DESC");
                break;
            }
            case Constant.FAVORIT:
            {
                retCursor = dbHelper.getReadableDatabase().query(
                        Constant.TABLE_NAME_TRACK_FAVORIT,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        Constant.TRACK_ID +" DESC");
                break;
            }
            case Constant.PLAYLIST:
            {
                retCursor = dbHelper.getReadableDatabase().query(
                        Constant.TABLE_NAME_PLAYLIST,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        Constant.PLAYLIST_ID +" DESC");
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sURIMatcher.match(uri)){
            case Constant.TRACK:
                long rowID = db.insert(	Constant.TABLE_NAME_TRACK, null, values);
                if (rowID > 0)
                {
                    returnUri = ContentUris.withAppendedId(Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK).build(), rowID);
                }else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
                break;

            case Constant.FAVORIT:
                long rowIdGate = db.insert(	Constant.TABLE_NAME_TRACK_FAVORIT,null,  values);

                if (rowIdGate > 0)
                {
                    returnUri = ContentUris.withAppendedId(Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK_FAVORIT).build(), rowIdGate);
                }else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
                break;

            case Constant.PLAYLIST:
                long rowIdPlaylist = db.insert(	Constant.TABLE_NAME_PLAYLIST,null,  values);

                if (rowIdPlaylist > 0)
                {
                    returnUri = ContentUris.withAppendedId(Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_PLAYLIST).build(), rowIdPlaylist);
                }else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        switch (sURIMatcher.match(uri)) {
            case Constant.TRACK:
                rowsDeleted = db.delete(Constant.TABLE_NAME_TRACK, selection, selectionArgs);
                break;
            case Constant.FAVORIT:
                rowsDeleted = db.delete(Constant.TABLE_NAME_TRACK_FAVORIT, selection, selectionArgs);
                break;

            case Constant.PLAYLIST:
                rowsDeleted = db.delete(Constant.TABLE_NAME_PLAYLIST, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sURIMatcher.match(uri)) {
            case Constant.TRACK:
                rowsUpdated = db.update(Constant.TABLE_NAME_TRACK, values, Constant.TRACK_ID +" = ?", selectionArgs);
            case Constant.FAVORIT:
                rowsUpdated = db.update(Constant.TABLE_NAME_TRACK_FAVORIT, values, Constant.TRACK_ID +" = ?", selectionArgs);
                break;
            case Constant.PLAYLIST:
                rowsUpdated = db.update(Constant.TABLE_NAME_PLAYLIST, values, Constant.PLAYLIST_ID +" = ?", selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}

