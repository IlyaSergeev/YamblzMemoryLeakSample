package com.yamblz.memoryleakssample.communication;

import android.os.AsyncTask;

import com.yamblz.memoryleakssample.model.Artist;

import java.lang.ref.WeakReference;

public class LoadArtistsTask extends AsyncTask<Void, Void, Artist[]>
{

    public interface Callbacks
    {
        void onPre();

        void onPost(Artist[] artists);
    }

    private WeakReference<Callbacks> ref;
    private Api api;

    public LoadArtistsTask(Callbacks callbacks, Api api)
    {
        this.ref = new WeakReference<>(callbacks);
        this.api = api;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        Callbacks callbacks = ref.get();
        if(callbacks != null)
        {
            callbacks.onPre();
        }
    }

    @Override
    protected Artist[] doInBackground(Void... voids)
    {
        return api.getArtists();
    }

    @Override
    protected void onPostExecute(Artist[] artists)
    {
        super.onPostExecute(artists);

        Callbacks callbacks = ref.get();
        if(callbacks != null)
        {
            callbacks.onPost(artists);
        }
    }

}
