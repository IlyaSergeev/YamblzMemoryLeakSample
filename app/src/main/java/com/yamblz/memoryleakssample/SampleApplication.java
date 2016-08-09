package com.yamblz.memoryleakssample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.yamblz.memoryleakssample.communication.Api;

/**
 * Created by i-sergeev on 07.07.16
 */
public class SampleApplication extends Application {
    private Api api;

    public Api getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        api = new Api(this);
        LeakCanary.install(this);
    }
}
