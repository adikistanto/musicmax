package com.istandev.musicmax;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import com.istandev.musicmax.utility.Constant;
import com.squareup.picasso.Picasso;

/**
 * Created by ADIK on 19/07/2016.
 */
public class NotificationService extends Service {
    Notification status,status1;
    private final String LOG_TAG = "NotificationService";
    Intent intent1;
    Boolean playStatus;

    public static final String BROADCAST_ACTION_PLAY_PAUSE = "com.istandev.musicmax.PLAY_PAUSE";
    public static final String BROADCAST_ACTION_NEXT = "com.istandev.musicmax.NEXT";
    public static final String BROADCAST_ACTION_PREV = "com.istandev.musicmax.PREV";
    public static final String STARTFOREGROUND_ACTION = "com.istandev.musicmax.START";
    public static final String STOPFOREGROUND_ACTION = "com.istandev.musicmax.STOP";

    public static final int FOREGROUND_SERVICE = 101;



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playStatus=DaftarLaguActivity.isPlaying;
        if(intent!=null){
            if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {
                showNotification(playStatus);

            } else if (intent.getAction().equals(BROADCAST_ACTION_PREV)) {

                showNotification(playStatus);
                sendBroadcast(intent1 = new Intent(BROADCAST_ACTION_PREV));

            } else if (intent.getAction().equals(BROADCAST_ACTION_PLAY_PAUSE)) {
                showNotification(playStatus);
                sendBroadcast(intent1 = new Intent(BROADCAST_ACTION_PLAY_PAUSE));

            } else if (intent.getAction().equals(BROADCAST_ACTION_NEXT)) {

                showNotification(playStatus);
                sendBroadcast(intent1 = new Intent(BROADCAST_ACTION_NEXT));


            } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {

                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;
    }

    private void showNotification(Boolean statusPlay) {

        RemoteViews views = new RemoteViews(getPackageName(),R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);


        Intent notificationIntent = new Intent(this, DaftarLaguActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(BROADCAST_ACTION_PREV);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(BROADCAST_ACTION_PLAY_PAUSE);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(BROADCAST_ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,nextIntent, 0);


        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);


        views.setTextViewText(R.id.status_bar_track_name, DaftarLaguActivity.judulTrack);
        views.setTextColor(R.id.status_bar_track_name, Color.BLACK);
        bigViews.setTextViewText(R.id.status_bar_track_name, DaftarLaguActivity.judulTrack);
        bigViews.setTextColor(R.id.status_bar_track_name, Color.BLACK);


        if(statusPlay==true){
            views.setImageViewResource(R.id.status_bar_play,
                    R.drawable.ic_pause);
            bigViews.setImageViewResource(R.id.status_bar_play,
                    R.drawable.ic_pause_black_48dp);
        }else{
            views.setImageViewResource(R.id.status_bar_play,
                    R.drawable.ic_play);
            bigViews.setImageViewResource(R.id.status_bar_play,
                    R.drawable.ic_play_arrow_black_48dp);
        }


        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags =  Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        status.icon = R.drawable.ic_notifikasi;
        status.contentIntent = pendingIntent;
        startForeground(FOREGROUND_SERVICE, status);


    }
}
