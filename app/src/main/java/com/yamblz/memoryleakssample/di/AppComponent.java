package com.yamblz.memoryleakssample.di;

import com.yamblz.memoryleakssample.ui.ArtistsListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(ArtistsListActivity artistsListActivity);
}
