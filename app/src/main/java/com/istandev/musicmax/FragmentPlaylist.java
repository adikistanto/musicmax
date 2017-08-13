package com.istandev.musicmax;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.istandev.musicmax.adapter.PlaylistAdapter;
import com.istandev.musicmax.adapter.PlaylistName1Adapter;
import com.istandev.musicmax.adapter.PlaylistNameAdapter;
import com.istandev.musicmax.entity.Playlist;
import com.istandev.musicmax.entity.Track;
import com.istandev.musicmax.utility.CRUD;
import com.istandev.musicmax.utility.Constant;
import com.istandev.musicmax.utility.Utils;

import java.util.ArrayList;

/**
 * Created by ADIK on 24/08/2016.
 */
public class FragmentPlaylist extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Playlist> arrayList = new ArrayList();
    private View view;
    private TextView no_playlist;

    public static FragmentPlaylist newInstance(int sectionNumber) {
        FragmentPlaylist fragment = new FragmentPlaylist();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentPlaylist() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constant.LOADER_FAVORIT, null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        no_playlist = (TextView)  view.findViewById(R.id.no_playlist);
        return view;
    };

    private void getData(Cursor data){
        if (data == null) {
            Log.e("mCursor ", "Error");
        } else if (data.getCount() < 1) {
            Log.e("mCursor", "Data tidak ditemukan");
            no_playlist.setVisibility(View.VISIBLE);
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
            mAdapter = new PlaylistName1Adapter(getActivity(), arrayList, new PlaylistName1Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(Playlist item) {
                    FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                    FragmentFavorit myDialogFragment = FragmentFavorit.newInstance(item);
                    myDialogFragment.show(manager,"dialoge");
                }
            });
            mRecyclerView.setAdapter(mAdapter);
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
}
