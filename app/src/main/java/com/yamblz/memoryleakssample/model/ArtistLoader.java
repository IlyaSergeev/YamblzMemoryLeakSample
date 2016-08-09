package com.yamblz.memoryleakssample.model;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.yamblz.memoryleakssample.communication.Api;

import java.util.List;

public class ArtistLoader extends AsyncTaskLoader<List<Artist>> {
    private final Api api;

    public ArtistLoader(Context context) {
        super(context);
        api = new Api(context);
    }

    @Override
    public List<Artist> loadInBackground() {
        return api.getArtists();
    }
}
