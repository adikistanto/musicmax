package com.istandev.musicmax;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.Config;
import com.istandev.musicmax.utility.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Query;

import static com.istandev.musicmax.utility.CRUD.addToTrackDB;


public class DaftarLaguActivity extends AppCompatActivity implements Runnable,MusikuService,SeekBar.OnSeekBarChangeListener,View.OnClickListener{


    private static final String TAG = "DaftarLaguActivity";
    private static final String ADMOB_APP_ID = "ca-app-pub-3456079026845978~7648914959";
    private String linkAplikasi = "https://play.google.com/store/apps/details?id=com.istandev.musicmax";
    private String URL;
    public static String packageName;
    public static String judulTrack,idTrack, imgTrack,streamTrack,downloadable,downloadUrl;

    public static ArrayList<Track> mListItems = new ArrayList<Track>();
    private static ArrayList<Track> mListItems1 = new ArrayList<Track>();

    public static TextView mSelectedTrackTitle,mSelectedTrackDuration,messageProgressBar,notFoundMessage,mSelectedTrackSeekbar;
    public static ImageView mSelectedTrackImage;
    public static MediaPlayer mMediaPlayer;
    public static ImageView mPlayerControl,mPlayerPrev,mPlayerNext,mPlayerRepeat,mPlayerDownload,mPlayerShare;
    public static ProgressBar progressBar,controlProgressBar;
    public static SeekBar seekBar;
    public static LinearLayout controlLinearLayout;

    public static int currentTrack=0;

    public static boolean isRepeat=false;
    public static boolean isPlaying;
    public static boolean isTrackReady=true;

    public ProgressDialog pDialog,mProgressDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    public static Notification status1;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static TabLayout tabLayout;
    public static TabLayout.Tab tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_lagu);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("MusicMax");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        MobileAds.initialize(this, ADMOB_APP_ID);

        mSelectedTrackTitle = (TextView)findViewById(R.id.selected_track_title);
        mSelectedTrackDuration = (TextView)findViewById(R.id.track_duration);
        mSelectedTrackSeekbar = (TextView)findViewById(R.id.seek_progress);
        mSelectedTrackImage = (ImageView)findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView)findViewById(R.id.player_control);
        mPlayerPrev = (ImageView)findViewById(R.id.player_prev);
        mPlayerNext = (ImageView)findViewById(R.id.player_next);
        mPlayerRepeat = (ImageView)findViewById(R.id.player_repeat);
        mPlayerDownload = (ImageView)findViewById(R.id.player_download);
        mPlayerShare = (ImageView)findViewById(R.id.player_share);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        controlProgressBar = (ProgressBar) findViewById(R.id.controlProgressBar);
        messageProgressBar = (TextView)findViewById(R.id.messageProgressBar);
        controlLinearLayout = (LinearLayout) findViewById(R.id.controlLinearLayout);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        notFoundMessage = (TextView) findViewById(R.id.notFoundMessage);

        packageName=getPackageName();


        //notifikasi
        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_PLAY_PAUSE));
        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_NEXT));
        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_PREV));
        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.STARTFOREGROUND_ACTION));
        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.STOPFOREGROUND_ACTION));

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(R.drawable.ic_play);
                if(isRepeat==false){
                    currentTrack = currentTrack + 1;
                }
                if(currentTrack >= mListItems.size()){
                    currentTrack = currentTrack - 1;
                }
                Track track = mListItems.get(currentTrack);

                showTrackAttribut(track.getTitle(),track.getArtworkURL(),track.getDuration(),DaftarLaguActivity.this);
                idTrack = ""+track.getID();
                judulTrack = track.getTitle();
                imgTrack = track.getArtworkURL();
                streamTrack = track.getStreamURL();
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                StartService();
                showTitleImageNotification(DaftarLaguActivity.this);
                try {
                    mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + Config.CLIENT_ID2);
                    mMediaPlayer.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mAdView = (AdView) findViewById(R.id.adViewBanner);
        if(Utils.isNetworkConnected(this)){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            //new Utils.getStatusTask().execute();
        }else{
            Utils.showSettingsAlert(this);
            mAdView.setVisibility(View.GONE);
        }

        mPlayerControl.setOnClickListener(this);
        mPlayerPrev.setOnClickListener(this);
        mPlayerNext.setOnClickListener(this);
        mPlayerDownload.setOnClickListener(this);
        mPlayerRepeat.setOnClickListener(this);
        mPlayerShare.setOnClickListener(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.notifyDataSetChanged();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tab = tabLayout.getTabAt(0);
        tab.select();



    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.player_control:
                togglePlayPause();
                break;
            case R.id.player_prev:
                togglePrev();
                break;
            case R.id.player_next:
                toggleNext();
                break;
            case R.id.player_repeat:
                toggleRepeat();
                break;
            case R.id.player_download:
                DownloadDialog();
                break;
            case R.id.player_share:
                Intent intent=new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                String pesan= "Now listening \""+judulTrack+"\"\nLink app : \n"+linkAplikasi;
                intent.putExtra(Intent.EXTRA_SUBJECT, "#MusicMaxShare");
                intent.putExtra(Intent.EXTRA_TEXT, pesan);
                startActivity(Intent.createChooser(intent, "How do you want to share?"));
                break;
        }
    }

    public static void showTrackAttribut(String judul,String url,String durati, Context context){
        mSelectedTrackTitle.setText(judul);
        if(url==null){
            Picasso.with(context).load(R.drawable.track_no_image).error(R.drawable.track_no_image).into(mSelectedTrackImage);
        }else{
            Picasso.with(context).load(url).error(R.drawable.track_no_image).into(mSelectedTrackImage);
        }
        Long duration = Long.valueOf(durati);
        mSelectedTrackDuration.setText(String.format("%tM",duration)+":"+String.format("%tS",duration));
    }

    public static void loadTracks(ArrayList<Track> tracks,Context context) {
        mListItems1.clear();
        mListItems.clear();
        mListItems1.addAll(tracks);
        for(int i=0;i< mListItems1.size();i++){
            Track tr = new Track();
            Track track = mListItems1.get(i);
            tr.setIdPlay(i);
            tr.setID(track.getID());
            tr.setTitle(track.getTitle());
            tr.setStreamURL(track.getStreamURL());
            tr.setArtworkURL(track.getArtworkURL());
            tr.setDuration(track.getDuration());
            tr.setmDownloadable(track.getDownloadable());
            tr.setmDownloadURL(track.getDownloadURL());
            mListItems.add(tr);
            Log.v("artwork_url=","-->"+track.getArtworkURL());
        }
        currentTrack = 0;
        addToTrackDB(mListItems,context);
    }

    public static void streamTrack(String urlStream){
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(urlStream + "?client_id=" + Config.CLIENT_ID2);
            Log.v("url stream",urlStream);
            mMediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showTitleImageNotification(Context context){
        RemoteViews views = new RemoteViews(packageName, R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(packageName,R.layout.status_bar_expanded);

        Intent notificationIntent = new Intent(context, DaftarLaguActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);


        views.setTextViewText(R.id.status_bar_track_name, DaftarLaguActivity.judulTrack);
        views.setTextColor(R.id.status_bar_track_name, Color.BLACK);

        bigViews.setTextViewText(R.id.status_bar_track_name, DaftarLaguActivity.judulTrack);
        bigViews.setTextColor(R.id.status_bar_track_name, Color.BLACK);

        status1 = new Notification.Builder(context).build();
        status1.contentView = views;
        status1.bigContentView = bigViews;
        status1.flags =  Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        status1.icon = R.drawable.ic_notifikasi;
        status1.contentIntent = pendingIntent;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NotificationService.FOREGROUND_SERVICE, status1);

        if(imgTrack==null){
            Picasso.with(context).load(R.drawable.track_no_image).error(R.drawable.track_no_image).into(bigViews,R.id.status_bar_album_art,NotificationService.FOREGROUND_SERVICE, status1);
            Picasso.with(context).load(R.drawable.track_no_image).error(R.drawable.track_no_image).into(views,R.id.status_bar_icon,NotificationService.FOREGROUND_SERVICE, status1);
        }else {
            Picasso.with(context).load(imgTrack).error(R.drawable.track_no_image).into(bigViews,R.id.status_bar_album_art,NotificationService.FOREGROUND_SERVICE, status1);
            Picasso.with(context).load(imgTrack).error(R.drawable.track_no_image).into(views,R.id.status_bar_icon,NotificationService.FOREGROUND_SERVICE, status1);
        }


    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPlaying=false;
            showPlayerControl(isPlaying,isRepeat,downloadable);
        } else {
            isPlaying=true;
            mMediaPlayer.start();
            seekBar.setMax(mMediaPlayer.getDuration());
            new Thread(this).start();
            showPlayerControl(isPlaying,isRepeat,downloadable);
        }
        StartService();
        showTitleImageNotification(DaftarLaguActivity.this);
    }

    public static void showPlayerControl(boolean isPlaying,boolean isRepeat,String downloadable){
        controlProgressBar.setVisibility(View.GONE);
        controlLinearLayout.setVisibility(View.VISIBLE);
        messageProgressBar.setVisibility(View.GONE);

        if(isPlaying==false){
            mPlayerControl.setImageResource(R.drawable.ic_play);

        }else {
            mPlayerControl.setImageResource(R.drawable.ic_pause);

        }

        if(isRepeat==false){
            mPlayerRepeat.setImageResource(R.drawable.ic_repeat_white_48dp);
            mPlayerRepeat.setBackgroundResource(R.color.colorBlack);
        }else{
            mPlayerRepeat.setImageResource(R.drawable.ic_repeat_white_48dp);
            mPlayerRepeat.setBackgroundResource(R.color.colorAccent);
        }

        if(downloadable.equalsIgnoreCase("true")){
            mPlayerDownload.setVisibility(View.VISIBLE);
        }else if (downloadable.equalsIgnoreCase("false")){
            mPlayerDownload.setVisibility(View.GONE);
        }
        mPlayerPrev.setImageResource(R.drawable.ic_skip_previous_white_48dp);
        mPlayerNext.setImageResource(R.drawable.ic_skip_next_white_48dp);
        isTrackReady=true;
    }

    private void togglePrev(){
        if(isTrackReady==true){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            controlProgressBar.setVisibility(View.VISIBLE);
            messageProgressBar.setVisibility(View.VISIBLE);
            controlLinearLayout.setVisibility(View.GONE);
            isRepeat=false;
            mPlayerRepeat.setBackgroundResource(R.color.colorBlack);
            isPlaying=true;
            if(currentTrack > 0){
                currentTrack = currentTrack - 1;
                Track track = mListItems.get(currentTrack);
                judulTrack = track.getTitle();
                imgTrack = track.getArtworkURL();
                showTrackAttribut(track.getTitle(),track.getArtworkURL(),track.getDuration(),this);
                streamTrack(track.getStreamURL());
            }else{
                if(mListItems.size()>0){
                    currentTrack = mListItems.size()-1;
                    Track track = mListItems.get(currentTrack);
                    judulTrack = track.getTitle();
                    imgTrack = track.getArtworkURL();
                    showTrackAttribut(track.getTitle(),track.getArtworkURL(),track.getDuration(),this);
                    streamTrack(track.getStreamURL());
                }
            }
            StartService();
            showTitleImageNotification(DaftarLaguActivity.this);
        }else{
            Toast.makeText(DaftarLaguActivity.this,"Please wait, still proccess",Toast.LENGTH_SHORT).show();
        }
        isTrackReady=false;
    }

    private void toggleNext(){
        if(isTrackReady==true){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            controlProgressBar.setVisibility(View.VISIBLE);
            messageProgressBar.setVisibility(View.VISIBLE);
            controlLinearLayout.setVisibility(View.GONE);
            isRepeat=false;
            mPlayerRepeat.setBackgroundResource(R.color.colorBlack);
            isPlaying=true;
            if(currentTrack < mListItems.size()-1){
                currentTrack = currentTrack + 1;
                Track track = mListItems.get(currentTrack);
                judulTrack = track.getTitle();
                imgTrack = track.getArtworkURL();
                showTrackAttribut(track.getTitle(),track.getArtworkURL(),track.getDuration(),this);
                streamTrack(track.getStreamURL());
            }else{
                currentTrack = 0;
                Track track = mListItems.get(currentTrack);
                judulTrack = track.getTitle();
                imgTrack = track.getArtworkURL();
                showTrackAttribut(track.getTitle(),track.getArtworkURL(),track.getDuration(),this);
                streamTrack(track.getStreamURL());
            }
            StartService();
            showTitleImageNotification(DaftarLaguActivity.this);
        }else{
            Toast.makeText(DaftarLaguActivity.this,"Please wait, still proccess",Toast.LENGTH_SHORT).show();
        }
        isTrackReady=false;
    }

    private boolean toggleRepeat(){
        if(isRepeat==false){
            isRepeat=true;
            mPlayerRepeat.setImageResource(R.drawable.ic_repeat_white_48dp);
            mPlayerRepeat.setBackgroundResource(R.color.colorAccent);
        }else{
            isRepeat=false;
            mPlayerRepeat.setImageResource(R.drawable.ic_repeat_white_48dp);
            mPlayerRepeat.setBackgroundResource(R.color.colorBlack);
            return isRepeat;
        }
        return false;
    }


    @Override
    public void run() {
        int currentPosition= 0;
        int total = mMediaPlayer.getDuration();
        while (mMediaPlayer!=null && currentPosition<total) {
            try {
                Thread.sleep(1000);
                currentPosition= mMediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Long duration = Long.valueOf(mMediaPlayer.getCurrentPosition());
                        mSelectedTrackSeekbar.setText(String.format("%tM",duration)+":"+String.format("%tS",duration));
                    }
                });
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }




            //
        }
    }

    @Override
    public void getRecentTracks(@Query("created_at[to]") String date, @Query("limit") int limit,Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getSearchTracks(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getGenreTracks(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getRecentTracks1(@Query("created_at[to]") String date, @Query("limit") int limit,Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getSearchTracks1(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getGenreTracks1(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_daftar_lagu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentCari myDialogFragment = FragmentCari.newInstance(false);
            myDialogFragment.show(fm, "dialog_fragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent serviceIntent = new Intent(DaftarLaguActivity.this, NotificationService.class);
        serviceIntent.setAction(NotificationService.STOPFOREGROUND_ACTION);
        startService(serviceIntent);


        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //currentTrack=0;
        //StartService();
        //showTitleImageNotification();
    }

    @Override
    public void onRestart() {
        super.onRestart();  // Always call the superclass method first
        //currentTrack=0;
        StartService();
        showTitleImageNotification(DaftarLaguActivity.this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StartService();
        showTitleImageNotification(DaftarLaguActivity.this);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
        if(fromUser) {
            mMediaPlayer.seekTo(progress);
            seekBar.setProgress(progress);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Long duration = Long.valueOf(mMediaPlayer.getCurrentPosition());
                    mSelectedTrackSeekbar.setText(String.format("%tM",duration)+":"+String.format("%tS",duration));
                }
            });
            //mSelectedTrackSeekbar.setText(String.valueOf((float)progress/1000) + "s");
        }

    }


    private void DownloadDialog(){
        new AlertDialog.Builder(DaftarLaguActivity.this)
                .setTitle("Download")
                .setMessage("Are you sure you want to download "+judulTrack+" ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        // starting new Async Task
                        boolean permission = isStoragePermissionGranted();
                        if(permission==true){
                            URL="https://api.soundcloud.com/tracks/"+idTrack+"/stream?client_id="+Config.CLIENT_ID2;
                            new DownloadFileFromURL().execute(URL);
                        }else{
                            isStoragePermissionGranted();
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DaftarLaguActivity.this);
            mProgressDialog.setMessage("A message");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            java.net.URL u = null;
            InputStream is = null;

            try {
                u = new URL(f_url[0]);
                is = u.openStream();
                HttpURLConnection huc = (HttpURLConnection)u.openConnection();//to know the size of video
                int size = huc.getContentLength();

                if(huc != null){
                    String fileName = judulTrack+".mp3";
                    String storagePath = Environment.getExternalStorageDirectory().toString();
                    File dir = new File(storagePath);
                    dir.mkdir();
                    File f = new File(dir,fileName);

                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    long total = 0;
                    int len1 = 0;
                    if(is != null){
                        while ((len1 = is.read(buffer)) > 0) {
                            total+=len1;
                            publishProgress(""+(int)((total*100)/size));
                            fos.write(buffer,0, len1);
                        }
                    }
                    if(fos != null){
                        fos.close();
                    }
                }
            }catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if(is != null){
                        is.close();
                    }
                }catch (IOException ioe) {
                    // just going to ignore this one
                }
            }
            return "";
        }


        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            mProgressDialog.dismiss();
            Toast.makeText(DaftarLaguActivity.this,"Download success",Toast.LENGTH_LONG).show();
            ShowInerAds();
        }

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            URL="https://api.soundcloud.com/tracks/"+idTrack+"/stream?client_id="+Config.CLIENT_ID2;
            new DownloadFileFromURL().execute(URL);
        }
    }

    public void StartService() {
        Intent serviceIntent = new Intent(DaftarLaguActivity.this, NotificationService.class);
        serviceIntent.setAction(NotificationService.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                String action = intent.getAction();
                if(action.equals(NotificationService.BROADCAST_ACTION_PLAY_PAUSE)){
                    togglePlayPause();
                }else if(action.equals(NotificationService.BROADCAST_ACTION_NEXT)){
                    toggleNext();
                }else if(action.equals(NotificationService.BROADCAST_ACTION_PREV)){
                    togglePrev();
                }
            }
        }
    };

    private void ShowInerAds(){
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest1 = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest1);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            public void onAdClosed() {

            }

        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return FragmentTracks.newInstance(position);
            }else if (position==1){
                return FragmentPlaylist.newInstance(position);

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Tracks";
            }else if(position==1){
                return "My Playlist";
            }
            return null;
        }
    }
}
