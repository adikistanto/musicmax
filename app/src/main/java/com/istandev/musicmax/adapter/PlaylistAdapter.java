package com.istandev.musicmax.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.istandev.musicmax.DaftarLaguActivity;
import com.istandev.musicmax.R;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.CRUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ADIK on 08/06/2016.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private ArrayList<Track> mTracks;

    public interface OnItemClickListener {
        void onItemClick(Track item);
    }

    public PlaylistAdapter(Activity a, ArrayList<Track> daftar_track, OnItemClickListener listener) {
        this.activity = a;
        this.mTracks = daftar_track;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout itemReativeLayout;
        public TextView titleTextView, durationTextView,downloadableTextView;
        private ImageView trackImageView, removePlaylistImageView;

        public MyViewHolder(View view) {
            super(view);
            trackImageView = (ImageView) view.findViewById(R.id.track_image);
            removePlaylistImageView = (ImageView) view.findViewById(R.id.delete_playlist_button);
            titleTextView = (TextView) view.findViewById(R.id.track_title);
            downloadableTextView = (TextView) view.findViewById(R.id.track_downloadable);
            durationTextView = (TextView) view.findViewById(R.id.track_duration);
            itemReativeLayout = (FrameLayout) view.findViewById(R.id.item_track);
        }
    }

    public static class ViewHolderAdMob extends RecyclerView.ViewHolder {
        public NativeExpressAdView mAdView;
        public ViewHolderAdMob(View view) {
            super(view);
            mAdView = (NativeExpressAdView) view.findViewById(R.id.adViewNative1);
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType){
            case 1:{
                View v = inflater.inflate(R.layout.play_list_row, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
            }
            case 2:{
                View v = inflater.inflate(R.layout.list_item_ads1, parent, false);
                viewHolder = new ViewHolderAdMob(v);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Track track = mTracks.get(holder.getAdapterPosition());

        switch(holder.getItemViewType()){
            case 1:{
                MyViewHolder viewHolder = (MyViewHolder) holder;
                viewHolder.titleTextView.setText(track.getTitle());
                if(track.getArtworkURL()==null){
                    Picasso.with(activity).load(R.drawable.track_no_image).error(R.drawable.track_no_image).into(viewHolder.trackImageView);
                }else{
                    Picasso.with(activity).load(track.getArtworkURL()).error(R.drawable.track_no_image).into(viewHolder.trackImageView);
                }
                Long duration = Long.valueOf(track.getDuration());
                viewHolder.durationTextView.setText(String.format("%tM",duration)+":"+String.format("%tS",duration));
                if(track.getDownloadable().equalsIgnoreCase("true")){
                    viewHolder.downloadableTextView.setVisibility(View.VISIBLE);
                    viewHolder.downloadableTextView.setText("Downloadable");
                }else if(track.getDownloadable().equalsIgnoreCase("false")){
                    viewHolder.downloadableTextView.setVisibility(View.GONE);
                }

                viewHolder.itemReativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(track);
                    }
                });

                viewHolder.removePlaylistImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // show dialog
                        hapusPlayList(track);

                    }
                });

                break;
            }
            case 2:{
                break;
            }
        }
    }

    private void hapusPlayList(final Track item){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                activity);
        alertDialog.setTitle("Are you sure want to delete it ?");

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CRUD.deletePlaylistItemDB(item,activity);
                        Toast.makeText(activity,"Deleted from  playlist",Toast.LENGTH_SHORT).show();
                        mTracks.remove(item);
                        DaftarLaguActivity.mListItems.clear();
                        DaftarLaguActivity.mListItems.addAll(urutkanData(mTracks));
                        notifyDataSetChanged();
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

    private ArrayList<Track> urutkanData(ArrayList<Track> tracks){
        ArrayList<Track> tracks1 = new ArrayList<>();
        for(int i=0;i<tracks.size();i++){
            Track tr = new Track();
            Track track = tracks.get(i);
            tr.setIdPlay(i);
            tr.setID(track.getID());
            tr.setTitle(track.getTitle());
            tr.setStreamURL(track.getStreamURL());
            tr.setArtworkURL(track.getArtworkURL());
            tr.setDuration(track.getDuration());
            tracks1.add(tr);
        }
        return tracks1;
    }


    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mTracks.get(position).getAddStatus();
    }


}
