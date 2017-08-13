package com.istandev.musicmax;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.istandev.musicmax.entity.Track;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;

import static com.istandev.musicmax.DaftarLaguActivity.loadTracks;

public class SplashScreen extends AppCompatActivity implements MusikuService{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        MusikuService scService = SoundCloud.getService();
        scService.getRecentTracks(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),50, new Callback<ArrayList<Track>>() {
            @Override
            public void success(ArrayList<Track> tracks, Response response) {
                loadTracks(tracks,SplashScreen.this);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, DaftarLaguActivity.class);
                startActivity(i);

                finish();
            }
        }, 2000);
    }

    @Override
    public void getRecentTracks(@Query("created_at[to]") String date, @Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getSearchTracks(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getGenreTracks(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getRecentTracks1(@Query("created_at[to]") String date, @Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getSearchTracks1(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getGenreTracks1(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }
}
