package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity
{
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private Api api;
    private ArtistsLoadingTask artistsLoadingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        api = new Api(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        artistsLoadingTask = new ArtistsLoadingTask(this);
        artistsLoadingTask.execute(api);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        int firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        Artist firstVisibleArtist = artistsAdapter.getArtist(firstVisiblePosition);
        ((SampleApplication)getApplication()).setFirstVisibleArtistInListActivity(firstVisibleArtist);
        resetArtistsLoadingTask();
    }

    private void resetArtistsLoadingTask()
    {
        if (artistsLoadingTask != null)
        {
            artistsLoadingTask.cancel(false);
        }
        artistsLoadingTask = null;
    }

    private void showProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showContent(Artist[] data)
    {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        artistsAdapter = new ArtistsAdapter(data,
                                            Picasso.with(this),
                                            getResources(),
                                            new ArtistsAdapter.ArtistsAdapterListener()
                                            {
                                                @Override
                                                public void onClickArtist(@NonNull Artist artist)
                                                {
                                                    showArtistDetails(artist);
                                                }
                                            });
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();

        Artist firstVisibleArtist = ((SampleApplication)getApplication()).getFirstVisibleArtistInListActivity();
        if (firstVisibleArtist != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                if (data[i].getId().equals(firstVisibleArtist.getId()))
                {
                    recyclerView.scrollToPosition(i);
                    break;
                }
            }
        }
    }

    private void showArtistDetails(@NonNull Artist artist)
    {
        ArtistDetailsActivity.artist = artist;
        startActivity(new Intent(this, ArtistDetailsActivity.class));
    }

    private static class ArtistsLoadingTask extends AsyncTask<Api, Void, Artist[]>
    {
        final WeakReference<ArtistsListActivity> weakActivity;

        private ArtistsLoadingTask(@NonNull ArtistsListActivity activity)
        {
            this.weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            ArtistsListActivity activity = weakActivity.get();
            if (activity != null)
            {
                activity.showProgress();
            }
            else
            {
                cancel(false);
            }
        }

        @Override
        protected Artist[] doInBackground(Api... apis)
        {
            return apis[0].getArtists();
        }

        @Override
        protected void onPostExecute(Artist[] artists)
        {
            super.onPostExecute(artists);
            ArtistsListActivity activity = weakActivity.get();
            if (activity != null)
            {
                activity.showContent(artists);
                activity.artistsLoadingTask = null;
            }
        }
    }
}
