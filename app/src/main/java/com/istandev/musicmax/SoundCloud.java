package com.istandev.musicmax;

import retrofit.RestAdapter;

/**
 * Created by ADIK on 08/06/2016.
 */
public class SoundCloud {

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint(Config.API_URL).build();
    private static final MusikuService SERVICE = REST_ADAPTER.create(MusikuService.class);

    public static MusikuService getService() {
        return SERVICE;
    }
}
