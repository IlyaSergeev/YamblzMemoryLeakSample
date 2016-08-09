package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public Artist[] getArtists() {
        InputStream inStream = null;
        InputStreamReader inStreamReader = null;
        Artist[] artists = new Artist[0];

        try {
            inStream = context.getResources().openRawResource(R.raw.artists);
            inStreamReader = new InputStreamReader(inStream);
            artists = gson.fromJson(inStreamReader, Artist[].class);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inStream);
            closeQuietly(inStreamReader);
        }

        return artists;
    }

    private void closeQuietly(Closeable c) {
        try {
            if (c != null) c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
