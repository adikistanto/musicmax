package com.istandev.musicmax.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ADIK on 20/10/2016.
 */

public class Playlist implements Parcelable {
    private int id_playlist;
    private String nama_playlist;

    public Playlist() {
    }

    public int getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(int id_playlist) {
        this.id_playlist = id_playlist;
    }

    public String getNama_playlist() {
        return nama_playlist;
    }

    public void setNama_playlist(String nama_playlist) {
        this.nama_playlist = nama_playlist;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id_playlist);
        dest.writeString(this.nama_playlist);
    }

    protected Playlist(Parcel in) {
        this.id_playlist = in.readInt();
        this.nama_playlist = in.readString();
    }

    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
