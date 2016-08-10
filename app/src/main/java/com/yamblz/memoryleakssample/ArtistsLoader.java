package com.yamblz.memoryleakssample;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Aleksandra on 09/08/16.
 */

public class ArtistsLoader extends AsyncTaskLoader<List<Artist>> {

    private static final Type collectionType = new TypeToken<List<Artist>>() {
    }.getType();
    private final Gson gson = new Gson();

    public ArtistsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Artist> loadInBackground() {
        InputStream inStream = getContext().getResources().openRawResource(R.raw.artists);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        List<Artist> artists = gson.fromJson(reader, collectionType);

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return artists;
    }
}
