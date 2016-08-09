package com.yamblz.memoryleakssample.di;

import android.content.Context;

import com.yamblz.memoryleakssample.communication.Api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Singleton
    @Provides
    public Context providesContext() {
        return context;
    }

    @Singleton
    @Provides
    public Api provideApi(Context context) {
        return new Api(context);
    }
}
