package com.istandev.musicmax.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ADIK on 08/06/2016.
 */
public class Track implements Parcelable {

    public Track(){}

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private String mID;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("duration")
    private String mDuration;

    @SerializedName("downloadable")
    private String mDownloadable;

    @SerializedName("download_url")
    private String mDownloadURL;

    private int mIdPlay;

    private int addStatus;

    private int idPlaylist;

    public void setTitle(String mTitle){
        this.mTitle = mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setID(String mID){
        this.mID = mID;
    }

    public String getID() {
        return mID;
    }

    public void setIdPlay(int mIdPlay){
        this.mIdPlay = mIdPlay;
    }

    public int getIdPlay() {
        return mIdPlay;
    }

    public void setStreamURL(String mStreamURL){
        this.mStreamURL = mStreamURL;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setArtworkURL(String mArtworkURL){
        this.mArtworkURL = mArtworkURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public void setDuration(String mDuration){
        this.mDuration = mDuration;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getDownloadable() {
        return mDownloadable;
    }

    public String getDownloadURL() {
        return mDownloadURL;
    }

    public void setmDownloadURL(String mDownloadURL) {
        this.mDownloadURL = mDownloadURL;
    }

    public void setmDownloadable(String mDownloadable) {
        this.mDownloadable = mDownloadable;
    }

    public int getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(int addStatus) {
        this.addStatus = addStatus;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mID);
        dest.writeString(this.mStreamURL);
        dest.writeString(this.mArtworkURL);
        dest.writeString(this.mDuration);
        dest.writeString(this.mDownloadable);
        dest.writeString(this.mDownloadURL);
        dest.writeInt(this.mIdPlay);
        dest.writeInt(this.addStatus);
        dest.writeInt(this.idPlaylist);
    }

    protected Track(Parcel in) {
        this.mTitle = in.readString();
        this.mID = in.readString();
        this.mStreamURL = in.readString();
        this.mArtworkURL = in.readString();
        this.mDuration = in.readString();
        this.mDownloadable = in.readString();
        this.mDownloadURL = in.readString();
        this.mIdPlay = in.readInt();
        this.addStatus = in.readInt();
        this.idPlaylist = in.readInt();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
