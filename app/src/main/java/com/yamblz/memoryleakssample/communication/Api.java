package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Api {
    private static final String TAG = Api.class.getSimpleName();

    @NonNull
    private final Context context;
    private final Gson gson = new Gson();

    public Api(@NonNull Context context) {
        this.context = context;
    }

    public Artist[] loadArtists() {
        Artist[] artists;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(context.getResources().openRawResource(R.raw.artists));
            artists = gson.fromJson(isr, Artist[].class);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }
        return artists;
    }

    public void loadCover(ImageView imageView, String path) {
        Picasso.with(context).load(path).into(imageView);
    }

    public String getPlurals(@PluralsRes int resId, int quantity) {
        return context.getResources().getQuantityString(resId, quantity, quantity);
    }
}
