package com.yamblz.memoryleakssample;

import android.app.Application;
import android.content.Context;
import android.widget.SimpleAdapter;

import com.squareup.leakcanary.LeakCanary;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

/**
 * Created by i-sergeev on 07.07.16
 */
public class SampleApplication extends Application
{
    private static Api api;

    public Api getApi()
    {
        return api;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        api = new Api(this);
        LeakCanary.install(this);
    }
    public static SampleApplication from(Context context){
        return (SampleApplication) context.getApplicationContext();
    }
}
