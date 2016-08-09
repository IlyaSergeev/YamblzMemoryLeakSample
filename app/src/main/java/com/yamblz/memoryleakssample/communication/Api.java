package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Api
{
    private final Gson gson = new Gson();
    private Context context;

    public Api(@NonNull Context context)
    {
        this.context = context;
    }

    public Artist[] getArtists() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InputStreamReader inStreamReader = null;
        try {
            inStreamReader = new InputStreamReader(
                    context.getResources().openRawResource(R.raw.artists));
            context = null;
            return gson.fromJson(inStreamReader, Artist[].class);
        } finally {
            if (inStreamReader != null) {
                try {
                    inStreamReader.close();
                } catch (IOException e) {
                    Log.d(this.getClass().getSimpleName(), "error during close", e);
                }
            }
        }
    }
}
