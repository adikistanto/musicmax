package com.istandev.musicmax;

import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.istandev.musicmax.adapter.PlaylistAdapter;
import com.istandev.musicmax.entity.Playlist;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.Constant;
import com.istandev.musicmax.utility.Utils;

import java.util.ArrayList;

/**
 * Created by ADIK on 24/08/2016.
 */
public class FragmentFavorit extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView no_playlist,playlist_title,play_all;
    private LinearLayout title;
    private ArrayList<Track> playList = new ArrayList<Track>();
    private ArrayList<Track> playListTerurut = new ArrayList<Track>();
    private ArrayList<Track> playListTerurutAds = new ArrayList<Track>();
    private View view;
    private Playlist playlistItem = new Playlist();

    public static FragmentFavorit newInstance(Playlist playlist) {
        FragmentFavorit fragment = new FragmentFavorit();
        Bundle args = new Bundle();
        args.putParcelable("data", playlist);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentFavorit() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constant.LOADER_FAVORIT, null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorit, container, false);
        no_playlist = (TextView)  view.findViewById(R.id.no_playlist);
        playlist_title = (TextView)  view.findViewById(R.id.plyalist_title);
        play_all = (TextView)  view.findViewById(R.id.play_all);
        title = (LinearLayout)  view.findViewById(R.id.title);
        playlistItem = getArguments().getParcelable("data");
        playlist_title.setText(playlistItem.getNama_playlist());


        play_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(getActivity())){
                    if(DaftarLaguActivity.isTrackReady==true){
                        DaftarLaguActivity.downloadable = playListTerurut.get(0).getDownloadable();
                        if(DaftarLaguActivity.isPlaying==false){
                            DaftarLaguActivity.isPlaying=true;
                            DaftarLaguActivity.mMediaPlayer.start();
                            DaftarLaguActivity.showPlayerControl(DaftarLaguActivity.isPlaying,DaftarLaguActivity.isRepeat,DaftarLaguActivity.downloadable);
                        }
                        getActivity().findViewById(R.id.active_track).setVisibility(View.VISIBLE);
                        DaftarLaguActivity.showTrackAttribut(playListTerurut.get(0).getTitle(),playListTerurut.get(0).getArtworkURL(),playListTerurut.get(0).getDuration(),getActivity());
                        DaftarLaguActivity.streamTrack(playListTerurut.get(0).getStreamURL());
                        DaftarLaguActivity.idTrack = ""+playListTerurut.get(0).getID();
                        DaftarLaguActivity.judulTrack = playListTerurut.get(0).getTitle();
                        DaftarLaguActivity.imgTrack = playListTerurut.get(0).getArtworkURL();
                        DaftarLaguActivity.streamTrack = playListTerurut.get(0).getStreamURL();
                        DaftarLaguActivity.currentTrack = playListTerurut.get(0).getIdPlay();
                        DaftarLaguActivity.downloadUrl = playListTerurut.get(0).getDownloadURL();
                        DaftarLaguActivity.controlProgressBar.setVisibility(View.VISIBLE);
                        DaftarLaguActivity.controlLinearLayout.setVisibility(View.GONE);
                        DaftarLaguActivity.messageProgressBar.setVisibility(View.VISIBLE);
                        DaftarLaguActivity.isTrackReady=false;
                        DaftarLaguActivity.showTitleImageNotification(getActivity());
                        DaftarLaguActivity.mListItems.clear();
                        DaftarLaguActivity.mListItems.addAll(playListTerurut);
                        //myList.setPadding(0,0,0,250);
                        getDialog().cancel();
                    }else{
                        Toast.makeText(getActivity(),"Please wait, still processing",Toast.LENGTH_SHORT).show();
                        DaftarLaguActivity.isTrackReady=false;
                    }
                }else{
                    Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    };

    private void getData(Cursor data){
        if (data == null) {
            Log.e("mCursor ", "Error");
        } else if (data.getCount() < 1) {
            Log.e("mCursor", "Data tidak ditemukan");
            title.setVisibility(View.GONE);
            play_all.setVisibility(View.GONE);
            //view.findViewById(R.id.tidak_ada_resep).setVisibility(View.VISIBLE);
        } else {
            playList.clear();
            while (data.moveToNext()) {
                Track track = new Track();
                String id = data.getString(data.getColumnIndex(Constant.TRACK_ID));
                int idPlay = data.getInt(data.getColumnIndex(Constant.TRACK_ID_PLAY));
                String nama = data.getString(data.getColumnIndex(Constant.TRACK_NAMA));
                String gambar_url = data.getString(data.getColumnIndex(Constant.TRACK_IMG_URL));
                String stream_url = data.getString(data.getColumnIndex(Constant.TRACK_STREAM_URL));
                String durasi = data.getString(data.getColumnIndex(Constant.TRACK_DURATION));
                String downloadable = data.getString(data.getColumnIndex(Constant.TRACK_DOWNLOADABLE));
                String download_url = data.getString(data.getColumnIndex(Constant.TRACK_DOWLOAD_URL));
                track.setID(id);
                track.setIdPlay(idPlay);
                track.setTitle(nama);
                track.setArtworkURL(gambar_url);
                track.setStreamURL(stream_url);
                track.setDuration(durasi);
                track.setmDownloadable(downloadable);
                track.setmDownloadURL(download_url);
                playList.add(track);
            }
            playListTerurut.clear();
            for(int i=0;i<playList.size();i++){
                Track tr = new Track();
                Track track = playList.get(i);
                tr.setIdPlay(i);
                tr.setID(track.getID());
                tr.setTitle(track.getTitle());
                tr.setStreamURL(track.getStreamURL());
                tr.setArtworkURL(track.getArtworkURL());
                tr.setDuration(track.getDuration());
                tr.setmDownloadable(track.getDownloadable());
                tr.setmDownloadURL(track.getDownloadURL());
                tr.setAddStatus(1);
                playListTerurut.add(tr);
            }
            //playListTerurutAds.clear();
            //playListTerurutAds.addAll(Utils.setTrackWithAds(playListTerurut,getActivity()));
            showData();
        }
    }

    private void showData(){
        DaftarLaguActivity.progressBar.setVisibility(View.GONE);
        if(playListTerurut !=null){
            no_playlist.setVisibility(View.GONE);
            final RecyclerView myList = (RecyclerView) view.findViewById(R.id.track_list_view);
            myList.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            myList.setLayoutManager(mLayoutManager);
            myList.setItemAnimator(new DefaultItemAnimator());
            PlaylistAdapter mAdapter = new PlaylistAdapter(getActivity(),playListTerurut ,
                    new PlaylistAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Track item) {
                            if(Utils.isNetworkConnected(getActivity())){
                                if(DaftarLaguActivity.isTrackReady==true){
                                    DaftarLaguActivity.downloadable = item.getDownloadable();
                                    if(DaftarLaguActivity.isPlaying==false){
                                        DaftarLaguActivity.isPlaying=true;
                                        DaftarLaguActivity.mMediaPlayer.reset();
                                        DaftarLaguActivity.mMediaPlayer.start();
                                        DaftarLaguActivity.showPlayerControl(DaftarLaguActivity.isPlaying,DaftarLaguActivity.isRepeat,DaftarLaguActivity.downloadable);
                                    }
                                    getActivity().findViewById(R.id.active_track).setVisibility(View.VISIBLE);
                                    DaftarLaguActivity.showTrackAttribut(item.getTitle(),item.getArtworkURL(),item.getDuration(),getActivity());
                                    DaftarLaguActivity.streamTrack(item.getStreamURL());
                                    DaftarLaguActivity.idTrack = ""+item.getID();
                                    DaftarLaguActivity.judulTrack = item.getTitle();
                                    DaftarLaguActivity.imgTrack = item.getArtworkURL();
                                    DaftarLaguActivity.streamTrack = item.getStreamURL();
                                    DaftarLaguActivity.currentTrack = item.getIdPlay();
                                    DaftarLaguActivity.downloadUrl = item.getDownloadURL();
                                    DaftarLaguActivity.controlProgressBar.setVisibility(View.VISIBLE);
                                    DaftarLaguActivity.controlLinearLayout.setVisibility(View.GONE);
                                    DaftarLaguActivity.messageProgressBar.setVisibility(View.VISIBLE);
                                    DaftarLaguActivity.isTrackReady=false;
                                    DaftarLaguActivity.showTitleImageNotification(getActivity());
                                    DaftarLaguActivity.mListItems.clear();
                                    DaftarLaguActivity.mListItems.addAll(playListTerurut);
                                    //myList.setPadding(0,0,0,250);
                                    getDialog().cancel();
                                }else{
                                    Toast.makeText(getActivity(),"Please wait, still processing",Toast.LENGTH_SHORT).show();
                                    DaftarLaguActivity.isTrackReady=false;
                                }
                            }else{
                                Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            myList.setAdapter(mAdapter);
        }else {
            no_playlist.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK_FAVORIT).build();
        String mSelectionClause = Constant.PLAYLIST_ID + " = ?";
        String[] mSelectionArgs = {String.valueOf(playlistItem.getId_playlist())};
        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, null, mSelectionClause, mSelectionArgs, null);
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        getData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
