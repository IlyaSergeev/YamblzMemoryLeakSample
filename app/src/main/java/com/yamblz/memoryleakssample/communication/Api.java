package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Api {
    @NonNull
    private final Context context;
    private final Gson gson = new Gson();

    public Api(@NonNull Context context) {
        this.context = context;
    }

    public List<Artist> getArtists() {
        Type type = new TypeToken<List<Artist>>() {
        }.getType();

        InputStream inStream = context.getResources().openRawResource(R.raw.artists);
        InputStreamReader inStreamReader = new InputStreamReader(inStream);

        return gson.fromJson(inStreamReader, type);
    }
}
