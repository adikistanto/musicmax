package com.istandev.musicmax.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.istandev.musicmax.R;
import com.istandev.musicmax.entity.Playlist;

import java.util.ArrayList;

/**
 * Created by ADIK on 08/06/2016.
 */
public class PlaylistNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private ArrayList<Playlist> mTracks;

    public interface OnItemClickListener {
        void onItemClick(Playlist item);
    }

    public PlaylistNameAdapter(Activity a, ArrayList<Playlist> daftar_track, OnItemClickListener listener) {
        this.activity = a;
        this.mTracks = daftar_track;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout itemReativeLayout;
        public TextView titleTextView;

        public MyViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.playlist_name);
            itemReativeLayout = (FrameLayout) view.findViewById(R.id.item_track);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.playlist_name_item, parent, false);
        viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Playlist track = mTracks.get(holder.getAdapterPosition());
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.titleTextView.setText(track.getNama_playlist());

        viewHolder.itemReativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(track);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
