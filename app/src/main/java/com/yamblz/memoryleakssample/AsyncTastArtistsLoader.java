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


public class AsyncTastArtistsLoader extends AsyncTaskLoader<List<Artist>> {
    private static final Type collectionType = new TypeToken<List<Artist>>() {
    }.getType();
    private final Gson gson = new Gson();

    public AsyncTastArtistsLoader(Context context) {
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
        List<Artist> list = gson.fromJson(reader, collectionType);

        try {
            inStream.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
