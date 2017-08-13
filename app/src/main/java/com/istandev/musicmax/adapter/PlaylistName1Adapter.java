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

import com.istandev.musicmax.DaftarLaguActivity;
import com.istandev.musicmax.R;
import com.istandev.musicmax.entity.Playlist;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.CRUD;

import java.util.ArrayList;

/**
 * Created by ADIK on 08/06/2016.
 */
public class PlaylistName1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private ArrayList<Playlist> mTracks;

    public interface OnItemClickListener {
        void onItemClick(Playlist item);
    }

    public PlaylistName1Adapter(Activity a, ArrayList<Playlist> daftar_track, OnItemClickListener listener) {
        this.activity = a;
        this.mTracks = daftar_track;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout itemReativeLayout;
        public TextView titleTextView;
        public ImageView deleteImageView;

        public MyViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.name_playlist);
            deleteImageView = (ImageView) view.findViewById(R.id.delete_playlist_button);
            itemReativeLayout = (FrameLayout) view.findViewById(R.id.item_track);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.playlist_name_item1, parent, false);
        viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Playlist playlist = mTracks.get(holder.getAdapterPosition());
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.titleTextView.setText(playlist.getNama_playlist());

        viewHolder.itemReativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(playlist);
            }
        });

        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusPlayList(playlist);
            }
        });
    }

    private void hapusPlayList(final Playlist item){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                activity);
        alertDialog.setTitle("Are you sure want to delete it ?");

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CRUD.deletePlaylistDB(item,activity);
                        Toast.makeText(activity,"Playlist has been deleted",Toast.LENGTH_SHORT).show();
                        mTracks.remove(item);
                        //DaftarLaguActivity.mListItems.clear();
                        //DaftarLaguActivity.mListItems.addAll(urutkanData(mTracks));
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

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
