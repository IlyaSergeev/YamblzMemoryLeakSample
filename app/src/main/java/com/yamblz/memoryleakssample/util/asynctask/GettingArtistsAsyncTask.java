package com.yamblz.memoryleakssample.util.asynctask;

import android.os.AsyncTask;

import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.model.Artist;
import com.yamblz.memoryleakssample.util.function.Consumer;

/**
 * Created by root on 8/9/16.
 */
public class GettingArtistsAsyncTask extends AsyncTask<Void, Void, Artist[]> {

    private final Runnable before;
    private final Consumer<Artist[]> after;

    public GettingArtistsAsyncTask(Runnable before, Consumer<Artist[]> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        before.run();
    }

    @Override
    protected Artist[] doInBackground(Void... voids)
    {
        return SampleApplication.getApi().getArtists();
    }

    @Override
    protected void onPostExecute(Artist[] artists)
    {
        super.onPostExecute(artists);
        after.apply(artists);
    }
}
