package com.istandev.musicmax;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.istandev.musicmax.adapter.PlaylistNameAdapter;
import com.istandev.musicmax.adapter.TrackAdapter;
import com.istandev.musicmax.entity.Playlist;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.CRUD;
import com.istandev.musicmax.utility.Constant;
import com.istandev.musicmax.utility.Utils;

import java.util.ArrayList;


public class FragmentTambahPlaylist extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Playlist> arrayList = new ArrayList();
    private TextView no_playlist;
    private EditText nama_playlist;

    private View view;

    public FragmentTambahPlaylist() {
        // Required empty public constructor
    }

    public static FragmentTambahPlaylist newInstance(Track track) {
        FragmentTambahPlaylist fragment = new FragmentTambahPlaylist();
        Bundle args = new Bundle();
        args.putParcelable("data",track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constant.LOADER_PLAYLIST, null,this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tambah_playlist, container, false);

        no_playlist = (TextView)  view.findViewById(R.id.no_playlist);
        nama_playlist = (EditText) view.findViewById(R.id.nama_playlist);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageView btnSimpan = (ImageView) view.findViewById(R.id.btn_simpan);

       btnSimpan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!nama_playlist.getText().toString().isEmpty()){
                   Playlist playlist = new Playlist();
                   playlist.setNama_playlist(nama_playlist.getText().toString());
                   CRUD.addToPlaylistDB(playlist,getActivity());
                   Toast.makeText(getActivity(),"A playlist has been created",Toast.LENGTH_SHORT).show();
                   nama_playlist.setText("");
                   //getDialog().cancel();
               }else{
                   Toast.makeText(getActivity(),"Please create a name of playlist",Toast.LENGTH_SHORT).show();
               }
           }
       });

        return view;
    }

    private void getData(Cursor data){
        if (data == null) {
            Log.e("mCursor ", "Error");
        } else if (data.getCount() < 1) {
            Log.e("mCursor", "Data tidak ditemukan");
            //view.findViewById(R.id.tidak_ada_resep).setVisibility(View.VISIBLE);
        } else {
            arrayList.clear();
            while (data.moveToNext()) {
                Playlist playlist = new Playlist();
                int id = data.getInt(data.getColumnIndex(Constant.PLAYLIST_ID));
                String name = data.getString(data.getColumnIndex(Constant.PLAYLIST_NAME));
                playlist.setId_playlist(id);
                playlist.setNama_playlist(name);
                arrayList.add(playlist);
            }
            showData();
        }
    }

    private void showData(){
        DaftarLaguActivity.progressBar.setVisibility(View.GONE);
        if(arrayList !=null){
            no_playlist.setVisibility(View.GONE);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.track_list_view);
            mRecyclerView.setNestedScrollingEnabled(false);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new PlaylistNameAdapter(getActivity(), arrayList, new PlaylistNameAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Playlist item) {
                    Track track = getArguments().getParcelable("data");
                    track.setIdPlaylist(item.getId_playlist());
                    if(ceckID(track.getID())==true){
                        CRUD.addToPlaylistItemDB(track,getActivity());
                        Toast.makeText(getActivity(),"Added to "+item.getNama_playlist(),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"Track has been added to "+item.getNama_playlist(),Toast.LENGTH_SHORT).show();
                    }
                    getDialog().cancel();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }else{
            no_playlist.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_PLAYLIST).build();
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

    private boolean ceckID(String idTrack){
        Uri contentURI = Constant.CONTENT_URI.buildUpon().appendPath(Constant.TABLE_NAME_TRACK_FAVORIT).build();
        String mSelectionClause = Constant.TRACK_ID + " = ?";
        String[] mSelectionArgs = {String.valueOf(idTrack)};
        Cursor data = getContext().getContentResolver().query(contentURI, null, mSelectionClause, mSelectionArgs, null);
        if (data == null) {
            Log.e("mCursor ", "Error");
            return false;
        } else if (data.getCount() < 1) {
            Log.e("mCursor", "Data tidak ditemukan");
            return true;
        } else {
            return false;
        }
    }
}
