package com.yamblz.memoryleakssample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.yamblz.memoryleakssample.di.AppComponent;
import com.yamblz.memoryleakssample.di.AppModule;
import com.yamblz.memoryleakssample.di.DaggerAppComponent;

/**
 * Created by i-sergeev on 07.07.16
 */
public class SampleApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent component() {
        return component;
    }
}
