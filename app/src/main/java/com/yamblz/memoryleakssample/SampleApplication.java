package com.yamblz.memoryleakssample;

import android.app.Application;
import android.content.Context;

import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

/**
 * Created by i-sergeev on 07.07.16
 */
public class SampleApplication extends Application {
    private Api api;

    Artist firstVisibleArtistInListActivity = null;

    public Api getApi() {
        if(api == null){
            api = new Api(this.getResources());
        }
        return api;
    }

    public Artist getFirstVisibleArtistInListActivity() {
        return firstVisibleArtistInListActivity;
    }

    public void setFirstVisibleArtistInListActivity(Artist firstVisibleArtistInListActivity) {
        this.firstVisibleArtistInListActivity = firstVisibleArtistInListActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static SampleApplication from(Context context){
        return (SampleApplication) context.getApplicationContext();
    }
}
