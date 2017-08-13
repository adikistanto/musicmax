package com.istandev.musicmax;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.istandev.musicmax.adapter.TrackAdapter;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.Constant;
import com.istandev.musicmax.utility.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ADIK on 24/08/2016.
 */
public class FragmentLagu extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView noDataTextView;
    private ArrayList<Track> trackList = new ArrayList<Track>();
    private ArrayList<Track> trackListWithAds = new ArrayList<Track>();
    private View view;

    public FragmentLagu() {
    }

    public static FragmentLagu newInstance(int sectionNumber) {
        FragmentLagu fragment = new FragmentLagu();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constant.LOADER_TRACK, null,this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lagu, container, false);

        return view;
    }

    private void getData(Cursor data){
        if (data == null) {
            Log.e("mCursor ", "Error");
        } else if (data.getCount() < 1) {
            Log.e("mCursor", "Data tidak ditemukan");
            //view.findViewById(R.id.tidak_ada_resep).setVisibility(View.VISIBLE);
        } else {
            trackList.clear();
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
                track.setAddStatus(1);
                trackList.add(track);
            }
            urutkanIdPlay(trackList);
            trackListWithAds.clear();
            //trackListWithAds.addAll(Utils.setTrackWithAds(trackList,getActivity()));
            showData();
        }
    }

    private void showData(){
        DaftarLaguActivity.progressBar.setVisibility(View.GONE);
        if(trackList !=null){
            mRecyclerView = (RecyclerView) view.findViewById(R.id.track_list_view);
            mRecyclerView.setNestedScrollingEnabled(false);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new TrackAdapter(getActivity(),trackList,
                    new TrackAdapter.OnItemClickListener() {
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
                                    DaftarLaguActivity.downloadUrl = item.getDownloadURL();
                                    DaftarLaguActivity.currentTrack = item.getIdPlay();
                                    DaftarLaguActivity.controlProgressBar.setVisibility(View.VISIBLE);
                                    DaftarLaguActivity.controlLinearLayout.setVisibility(View.GONE);
                                    DaftarLaguActivity.messageProgressBar.setVisibility(View.VISIBLE);
                                    DaftarLaguActivity.isTrackReady=false;
                                    DaftarLaguActivity.showTitleImageNotification(getActivity());
                                    DaftarLaguActivity.mListItems.clear();
                                    //DaftarLaguActivity.mListItems.addAll(trackListWithAds);
                                    DaftarLaguActivity.mListItems.addAll(trackList);
                                    mRecyclerView.setPadding(0,0,0,250);
                                }else{
                                    Toast.makeText(getActivity(),"Please wait, still processing",Toast.LENGTH_SHORT).show();
                                    DaftarLaguActivity.isTrackReady=false;
                                }
                            }else{
                                Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private ArrayList<Track> urutkanIdPlay(ArrayList<Track> trackList){

        Collections.sort(trackList, new Comparator<Track>() {
            @Override
            public int compare(Track track, Track track1) {

                return String.valueOf(track.getIdPlay()).compareTo(String.valueOf(track1.getIdPlay()));
            }
        });

        return trackList;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK).build();

        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, null, null, null, null);
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
