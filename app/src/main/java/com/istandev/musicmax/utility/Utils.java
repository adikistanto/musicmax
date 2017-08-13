package com.istandev.musicmax.utility;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.istandev.musicmax.R;
import com.istandev.musicmax.entity.Track;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by ADIK on 15/10/2016.
 */

public class Utils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setIcon(R.drawable.ic_wifi);
        alertDialog.setTitle("No internet connection");

        alertDialog
                .setMessage("Activate internet connection ?");

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    public static ArrayList<Track> setTrackWithAds(ArrayList<Track> tracks, Context context){

        int number=0,number1=0,number2=0;

        number2 = 0;

        Track ads1 = new Track();
        Track ads2 = new Track();
        Track ads3 = new Track();

        ads1.setAddStatus(2);// ads status
        ads2.setAddStatus(2);// ads status
        ads3.setAddStatus(2);// ads status

        ads3.setTitle(tracks.get(number2).getTitle());
        ads3.setArtworkURL(tracks.get(number2).getArtworkURL());
        ads3.setmDownloadable(tracks.get(number2).getDownloadable());
        ads3.setmDownloadURL(tracks.get(number2).getDownloadURL());
        ads3.setDuration(tracks.get(number2).getDuration());
        ads3.setIdPlay(tracks.get(number2).getIdPlay());
        ads3.setID(tracks.get(number2).getID());
        ads3.setStreamURL(tracks.get(number2).getStreamURL());

        if((Utils.isNetworkConnected(context))&&(isLolipop())){
            for (int i=0;i<tracks.size();i++){
                tracks.get(i).setAddStatus(1);// track item
            }
            if(tracks.size() > 5){
                if(tracks.size() > 10){
                    number = 6;
                    number1 = 10;

                    ads1.setTitle(tracks.get(number).getTitle());
                    ads1.setArtworkURL(tracks.get(number).getArtworkURL());
                    ads1.setmDownloadable(tracks.get(number).getDownloadable());
                    ads1.setmDownloadURL(tracks.get(number).getDownloadURL());
                    ads1.setDuration(tracks.get(number).getDuration());
                    ads1.setIdPlay(number);
                    ads1.setID(tracks.get(number).getID());
                    ads1.setStreamURL(tracks.get(number).getStreamURL());

                    ads2.setTitle(tracks.get(number1).getTitle());
                    ads2.setArtworkURL(tracks.get(number1).getArtworkURL());
                    ads2.setmDownloadable(tracks.get(number1).getDownloadable());
                    ads2.setmDownloadURL(tracks.get(number1).getDownloadURL());
                    ads2.setDuration(tracks.get(number1).getDuration());
                    ads2.setIdPlay(number1);
                    ads2.setID(tracks.get(number1).getID());
                    ads2.setStreamURL(tracks.get(number1).getStreamURL());

                    tracks.add(number,ads1);
                    tracks.add(number1,ads2);
                    tracks.add(number2,ads3);

                }else{
                    number = 5;
                    ads1.setTitle(tracks.get(number).getTitle());
                    ads1.setArtworkURL(tracks.get(number).getArtworkURL());
                    ads1.setmDownloadable(tracks.get(number).getDownloadable());
                    ads1.setmDownloadURL(tracks.get(number).getDownloadURL());
                    ads1.setDuration(tracks.get(number).getDuration());
                    ads1.setIdPlay(number);
                    ads1.setID(tracks.get(number).getID());
                    ads1.setStreamURL(tracks.get(number).getStreamURL());

                    tracks.add(number, ads1);
                    tracks.add(number2 ,ads3);
                }
            }else{
                tracks.add(number2,ads3);
            }
        }else{
            for (int i=0;i<tracks.size();i++){
                tracks.get(i).setAddStatus(1);// track item
            }
        }
        for (int i=0;i<tracks.size();i++){
            tracks.get(i).setIdPlay(i);// track item
            Log.v("tracs =>","ID :"+tracks.get(i).getID()+", ID PLAY :"+tracks.get(i).getIdPlay()+", JUDUL"+tracks.get(i).getTitle()+", DURASI :"+tracks.get(i).getDuration());
        }
        return tracks;
    }

   /* public static class getStatusTask extends AsyncTask<String, Void, String> {
        static JSONParser jParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getDownloadStatus();
            return returnResult;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equalsIgnoreCase("Exception Caught"))
            {

            }

            if(result.equalsIgnoreCase("no results"))
            {

            }else{

            }

        }

        public String getDownloadStatus() {

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(Constant.MUSIC_MAX_URL,"POST", parameter);

                int success = json.getInt(Constant.TAG_SUCCESS);
                if (success == 1) {
                    JSONArray status = json.getJSONArray(Constant.TAG_MUSICMAX);
                    for (int i = 0; i < status.length() ; i++){
                        JSONObject c = status.getJSONObject(i);
                        Constant.DOWNLOAD_STATUS = c.getString(Constant.TAG_MUSICMAX);
                    }
                    return "OK";
                }
                else {
                    return "no results";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

    }*/

    public  static  boolean isLolipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        else {
           return false;
        }
    }
}
