package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

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
    private Artist[] artists;


    public Api(@NonNull Context context) {
        this.context = context;
    }

    public Artist[] getArtists() {
        if(artists==null){
            InputStream inStream = context.getResources().openRawResource(R.raw.artists);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            artists= gson.fromJson(inStreamReader, Artist[].class);
            try {
                inStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return artists;
    }

    public boolean isArtistLoad(){
        return artists!=null;
    }
}
