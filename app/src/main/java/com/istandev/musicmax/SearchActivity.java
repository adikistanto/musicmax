package com.istandev.musicmax;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.istandev.musicmax.DaftarLaguActivity.loadTracks;

public class SearchActivity extends AppCompatActivity {

    EditText searchET;

    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchET = (EditText) findViewById(R.id.key_search);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        findViewById(R.id.btn_cari).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideSoftKeyboard(SearchActivity.this);

                String key = searchET.getText().toString().trim();

                if(key.isEmpty()){
                    Toast.makeText(SearchActivity.this,"Please fill search field",Toast.LENGTH_SHORT).show();
                }else{
                    serchByQuery(key);
                }
            }
        });

        findViewById(R.id.choose_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideSoftKeyboard(SearchActivity.this);

                FragmentManager fm = getSupportFragmentManager();
                FragmentCari myDialogFragment = FragmentCari.newInstance(true);
                myDialogFragment.show(fm, "dialog_fragment");

            }
        });

    }


    private void serchByQuery(String query){

        progressBar.setVisibility(View.VISIBLE);

        MusikuService scService = SoundCloud.getService();
        scService.getSearchTracks(query,50,new Callback<ArrayList<Track>>() {
            @Override
            public void success(ArrayList<Track> tracks, Response response) {
                loadTracks(tracks,SearchActivity.this);

                Intent i = new Intent(SearchActivity.this, DaftarLaguActivity.class);
                startActivity(i);
                finish();

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SearchActivity.this,"Can't find any tracks",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
