package com.yamblz.memoryleakssample;

import android.app.Application;

import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

/**
 * Created by i-sergeev on 07.07.16
 */
public class SampleApplication extends Application
{
    private Api api;

    @Override
    public void onCreate()
    {
        super.onCreate();
        api = new Api(this);
    }

    public Api getApi() {
        return api;
    }
}
