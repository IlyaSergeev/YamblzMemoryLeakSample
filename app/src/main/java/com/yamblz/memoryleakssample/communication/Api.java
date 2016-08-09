package com.yamblz.memoryleakssample.communication;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Api {
    private static final Type collectionType = new TypeToken<List<Artist>>() {
    }.getType();
    @NonNull
    private final Context context;
    private final Gson gson = new Gson();


    public Api(@NonNull Context context) {
        this.context = context;
    }

    public List<Artist> getArtists() {

//        try
//        {
//            Thread.sleep(3000);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }

        InputStream inStream = context.getResources().openRawResource(R.raw.artists);
        InputStreamReader inStreamReader = new InputStreamReader(inStream);


        List<Artist> artists = gson.fromJson(inStreamReader, collectionType);

        try {
            inStreamReader.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return artists;
    }
}
