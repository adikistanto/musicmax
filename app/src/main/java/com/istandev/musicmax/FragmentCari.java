package com.istandev.musicmax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.istandev.musicmax.entity.Track;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;

import static com.istandev.musicmax.DaftarLaguActivity.loadTracks;
import static com.istandev.musicmax.DaftarLaguActivity.progressBar;
import static com.istandev.musicmax.DaftarLaguActivity.tab;
import static com.istandev.musicmax.DaftarLaguActivity.tabLayout;

/**
 * Created by ADIK on 24/08/2016.
 */
public class FragmentCari extends DialogFragment implements MusikuService,View.OnClickListener{

    private Context context;
    private EditText cari;
    Boolean isHome;


    public FragmentCari() {
    }

    public static FragmentCari newInstance(Boolean isHome) {
        FragmentCari fragment = new FragmentCari();
        Bundle args = new Bundle();
        args.putBoolean("key", isHome);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cari, container, false);
        cari = (EditText) view.findViewById(R.id.cari);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        context = getActivity();

        ImageView btnCari = (ImageView) view.findViewById(R.id.btn_cari);

        isHome = getArguments().getBoolean("key");

        if(isHome){
            cari.setVisibility(View.GONE);
            btnCari.setVisibility(View.GONE);
        }

        TextView a = (TextView) view.findViewById(R.id.alternativerock);
        TextView b = (TextView) view.findViewById(R.id.ambient);
        TextView c = (TextView) view.findViewById(R.id.clasical);
        TextView d = (TextView) view.findViewById(R.id.country);
        TextView e = (TextView) view.findViewById(R.id.danceedm);
        TextView f = (TextView) view.findViewById(R.id.dancehall);
        TextView g = (TextView) view.findViewById(R.id.deephouse);
        TextView h = (TextView) view.findViewById(R.id.disco);
        TextView i = (TextView) view.findViewById(R.id.drumbass);
        TextView j = (TextView) view.findViewById(R.id.dubstep);
        TextView k = (TextView) view.findViewById(R.id.electronic);
        TextView l = (TextView) view.findViewById(R.id.folksingersongwriter);
        TextView m = (TextView) view.findViewById(R.id.hiphoprap);
        TextView n = (TextView) view.findViewById(R.id.house);
        TextView o = (TextView) view.findViewById(R.id.indie);
        TextView p = (TextView) view.findViewById(R.id.jazzblues);
        TextView r = (TextView) view.findViewById(R.id.latin);
        TextView s = (TextView) view.findViewById(R.id.metal);
        TextView t = (TextView) view.findViewById(R.id.piano);
        TextView u = (TextView) view.findViewById(R.id.pop);
        TextView v = (TextView) view.findViewById(R.id.rbsoul);
        TextView w = (TextView) view.findViewById(R.id.reggae);
        TextView x = (TextView) view.findViewById(R.id.reggaeton);
        TextView y = (TextView) view.findViewById(R.id.rock);
        TextView z = (TextView) view.findViewById(R.id.soundtrack);
        TextView a1 = (TextView) view.findViewById(R.id.techno);
        TextView b1 = (TextView) view.findViewById(R.id.trance);
        TextView c1 = (TextView) view.findViewById(R.id.trap);
        TextView d1 = (TextView) view.findViewById(R.id.triphop);
        TextView e1 = (TextView) view.findViewById(R.id.world);
        TextView f1 = (TextView) view.findViewById(R.id.audiobooks);
        TextView g1 = (TextView) view.findViewById(R.id.business);
        TextView h1 = (TextView) view.findViewById(R.id.comedy);
        TextView i1 = (TextView) view.findViewById(R.id.entertainment);
        TextView j1 = (TextView) view.findViewById(R.id.learning);
        TextView k1 = (TextView) view.findViewById(R.id.newspolitics);
        TextView l1 = (TextView) view.findViewById(R.id.religionspirituality);
        TextView m1 = (TextView) view.findViewById(R.id.science);
        TextView n1 = (TextView) view.findViewById(R.id.sports);
        TextView o1 = (TextView) view.findViewById(R.id.storytelling);
        TextView p1 = (TextView) view.findViewById(R.id.technology);



        btnCari.setOnClickListener(this);
        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);
        d.setOnClickListener(this);
        e.setOnClickListener(this);
        f.setOnClickListener(this);
        g.setOnClickListener(this);
        h.setOnClickListener(this);
        i.setOnClickListener(this);
        j.setOnClickListener(this);
        k.setOnClickListener(this);
        l.setOnClickListener(this);
        m.setOnClickListener(this);
        n.setOnClickListener(this);
        o.setOnClickListener(this);
        p.setOnClickListener(this);
        r.setOnClickListener(this);
        s.setOnClickListener(this);
        t.setOnClickListener(this);
        u.setOnClickListener(this);
        v.setOnClickListener(this);
        w.setOnClickListener(this);
        x.setOnClickListener(this);
        y.setOnClickListener(this);
        z.setOnClickListener(this);
        a1.setOnClickListener(this);
        b1.setOnClickListener(this);
        c1.setOnClickListener(this);
        d1.setOnClickListener(this);
        e1.setOnClickListener(this);
        f1.setOnClickListener(this);
        g1.setOnClickListener(this);
        h1.setOnClickListener(this);
        i1.setOnClickListener(this);
        j1.setOnClickListener(this);
        k1.setOnClickListener(this);
        l1.setOnClickListener(this);
        m1.setOnClickListener(this);
        n1.setOnClickListener(this);
        o1.setOnClickListener(this);
        p1.setOnClickListener(this);

        cari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String key = cari.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    serchByQuery(key);
                    return true;
                }
                return false;
            }
        });

        return view;


    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_cari:
                serchByQuery(cari.getText().toString());
                break;
            case R.id.alternativerock:
                serchByGenre("alternativerock");
                break;
            case R.id.ambient:
                serchByGenre("ambient");
                break;
            case R.id.clasical:
                serchByGenre("clasical");
                break;
            case R.id.country:
                serchByGenre("country");
                break;
            case R.id.danceedm:
                serchByGenre("danceedm");
                break;
            case R.id.dancehall:
                serchByGenre("dancehall");
                break;
            case R.id.deephouse:
                serchByGenre("deephouse");
                break;
            case R.id.disco:
                serchByGenre("disco");
                break;
            case R.id.drumbass:
                serchByGenre("drumbass");
                break;
            case R.id.dubstep:
                serchByGenre("dubstep");
                break;
            case R.id.electronic:
                serchByGenre("electronic");
                break;
            case R.id.folksingersongwriter:
                serchByGenre("folksingersongwriter");
                break;
            case R.id.hiphoprap:
                serchByGenre("hiphoprap");
                break;
            case R.id.house:
                serchByGenre("house");
                break;
            case R.id.indie:
                serchByGenre("indie");
                break;
            case R.id.jazzblues:
                serchByGenre("jazzblues");
                break;
            case R.id.latin:
                serchByGenre("latin");
                break;
            case R.id.metal:
                serchByGenre("metal");
                break;
            case R.id.piano:
                serchByGenre("piano");
                break;
            case R.id.pop:
                serchByGenre("pop");
                break;
            case R.id.rbsoul:
                serchByGenre("rbsoul");
                break;
            case R.id.reggae:
                serchByGenre("reggae");
                break;
            case R.id.reggaeton:
                serchByGenre("reggaeton");
                break;
            case R.id.rock:
                serchByGenre("rock");
                break;
            case R.id.soundtrack:
                serchByGenre("soundtrack");
                break;
            case R.id.techno:
                serchByGenre("techno");
                break;
            case R.id.trance:
                serchByGenre("trance");
                break;
            case R.id.trap:
                serchByGenre("trap");
                break;
            case R.id.triphop:
                serchByGenre("triphop");
                break;
            case R.id.world:
                serchByGenre("world");
                break;
            case R.id.audiobooks:
                serchByGenre("audiobooks");
                break;
            case R.id.business:
                serchByGenre("business");
                break;
            case R.id.comedy:
                serchByGenre("comedy");
                break;
            case R.id.learning:
                serchByGenre("learning");
                break;
            case R.id.newspolitics:
                serchByGenre("newspolitics");
                break;
            case R.id.religionspirituality:
                serchByGenre("religionspirituality");
                break;
            case R.id.science:
                serchByGenre("science");
                break;
            case R.id.sports:
                serchByGenre("sports");
                break;
            case R.id.storytelling:
                serchByGenre("storytelling");
                break;
            case R.id.technology:
                serchByGenre("technology");
                break;
        }
    }


    private void serchByQuery(String query){
        progressBar.setVisibility(View.VISIBLE);
        getDialog().cancel();
        tab = tabLayout.getTabAt(0);
        tab.select();
        MusikuService scService = SoundCloud.getService();
        scService.getSearchTracks(query,50,new Callback<ArrayList<Track>>() {
            @Override
            public void success(ArrayList<Track> tracks, Response response) {
                loadTracks(tracks,context);
            }

            @Override
            public void failure(RetrofitError error) {
                //Toast.makeText(getActivity(),"Can't find any tracks",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void serchByGenre(String genre){

        if(isHome){

            SearchActivity.progressBar.setVisibility(View.VISIBLE);
            getDialog().cancel();
            //tab = tabLayout.getTabAt(0);
            //tab.select();
            MusikuService scService = SoundCloud.getService();
            scService.getGenreTracks(genre,50,new Callback<ArrayList<Track>>() {
                @Override
                public void success(ArrayList<Track> tracks, Response response) {
                    loadTracks(tracks,context);

                    Intent i = new Intent(context, DaftarLaguActivity.class);
                    context.startActivity(i);

                    SearchActivity.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void failure(RetrofitError error) {
                    //Toast.makeText(getActivity(),"Can't find any tracks",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            progressBar.setVisibility(View.VISIBLE);
            getDialog().cancel();
            tab = tabLayout.getTabAt(0);
            tab.select();
            MusikuService scService = SoundCloud.getService();
            scService.getGenreTracks(genre,50,new Callback<ArrayList<Track>>() {
                @Override
                public void success(ArrayList<Track> tracks, Response response) {
                    loadTracks(tracks,context);
                }

                @Override
                public void failure(RetrofitError error) {
                    //Toast.makeText(getActivity(),"Can't find any tracks",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    @Override
    public void getRecentTracks(@Query("created_at[to]") String date, @Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getSearchTracks(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getGenreTracks(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getRecentTracks1(@Query("created_at[to]") String date, @Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getSearchTracks1(@Query("q") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }

    @Override
    public void getGenreTracks1(@Query("genre") String key,@Query("limit") int limit, Callback<ArrayList<Track>> cb) {

    }



}
