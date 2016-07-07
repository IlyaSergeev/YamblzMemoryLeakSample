package com.yamblz.memoryleakssample;

import android.app.Application;

import com.yamblz.memoryleakssample.ui.ArtistsListActivity;

/**
 * Created by i-sergeev on 07.07.16
 */
public class SampleApplication extends Application
{
    private static ArtistsListActivity artistsListActivity;

    public static void setArtistsListActivity(ArtistsListActivity artistsListActivity)
    {
        SampleApplication.artistsListActivity = artistsListActivity;
    }
}
