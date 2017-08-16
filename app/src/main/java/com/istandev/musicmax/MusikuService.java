package com.istandev.musicmax;

import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.Config;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ADIK on 08/06/2016.
 */
public interface MusikuService {

    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    public void getRecentTracks1(@Query("created_at[to]") String date,@Query("limit") int limit, Callback<ArrayList<Track>> cb);

    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    public void getSearchTracks1(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb);

    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    public void getGenreTracks1(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb);

    @GET("/tracks?client_id=" + Config.CLIENT_ID2)
    public void getRecentTracks(@Query("created_at[to]") String date,@Query("limit") int limit, Callback<ArrayList<Track>> cb);

    @GET("/tracks?client_id=" + Config.CLIENT_ID2)
    public void getSearchTracks(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb);

    @GET("/tracks?client_id=" + Config.CLIENT_ID2)
    public void getGenreTracks(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb);

}
