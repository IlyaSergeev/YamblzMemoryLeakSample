package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Api
{
    @NonNull
    private final Context context;
    private final Gson gson = new Gson();


    public Api(@NonNull Context context)
    {
        this.context = context.getApplicationContext();
    }

    public Artist[] getArtists() {

        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        InputStream inStream = context.getResources().openRawResource(R.raw.artists);
        InputStreamReader inStreamReader = new InputStreamReader(inStream);

        return gson.fromJson(inStreamReader, Artist[].class);
    }
}
