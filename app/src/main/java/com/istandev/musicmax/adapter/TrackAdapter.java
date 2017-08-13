package com.istandev.musicmax.adapter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.istandev.musicmax.FragmentTambahPlaylist;
import com.istandev.musicmax.R;
import com.istandev.musicmax.entity.Track;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ADIK on 08/06/2016.
 */
public class TrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private ArrayList<Track> mTracks;


    public interface OnItemClickListener {
        void onItemClick(Track item);
    }

    public TrackAdapter(Activity a, ArrayList<Track> daftar_track, OnItemClickListener listener) {
        this.activity = a;
        this.mTracks = daftar_track;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout itemReativeLayout;
        public TextView titleTextView, durationTextView,downloadableTextView;
        private ImageView trackImageView,addPlaylistImageView;

        public MyViewHolder(View view) {
            super(view);
            trackImageView = (ImageView) view.findViewById(R.id.track_image);
            addPlaylistImageView = (ImageView) view.findViewById(R.id.add_playlist_button);
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
            mAdView = (NativeExpressAdView) view.findViewById(R.id.adViewNative);
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType){
            case 1:{
                View v = inflater.inflate(R.layout.track_list_row, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
            }
            case 2:{
                View v = inflater.inflate(R.layout.list_item_ads, parent, false);
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
                viewHolder.addPlaylistImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //CRUD.addToPlaylistItemDB(track,activity);
                        //Toast.makeText(activity,"Added to playlist",Toast.LENGTH_SHORT).show();
                        android.support.v4.app.FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
                        FragmentTambahPlaylist myDialogFragment = FragmentTambahPlaylist.newInstance(track);
                        myDialogFragment.show(manager,"dialoge");
                    }
                });
                break;
            }
            case 2:{
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mTracks.get(position).getAddStatus();
    }

}
