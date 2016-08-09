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
import com.yamblz.memoryleakssample.model.Artist;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class ArtistsListActivity extends AppCompatActivity {
    public static final String ARTIST = "ARTIST";
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    @State
    int completelyVisiblePosition = -1;
    @State
    Artist[] data;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private LoadArtistsAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));


        if (data == null) {
            task = new LoadArtistsAsyncTask(this);
            task.execute();
        } else {
            showContent(data);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        completelyVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showContent(Artist[] data) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        artistsAdapter = new ArtistsAdapter(data,
                Picasso.with(this),
                getResources(),
                new ArtistsAdapter.ArtistsAdapterListener() {
                    @Override
                    public void onClickArtist(@NonNull Artist artist) {
                        showArtistDetails(artist);
                    }
                });
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();

        if (completelyVisiblePosition != -1)
            recyclerView.scrollToPosition(completelyVisiblePosition);
    }

    private void showArtistDetails(@NonNull Artist artist) {
        final Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra(ARTIST, artist);
        startActivity(intent);
    }

    private static class LoadArtistsAsyncTask extends AsyncTask<Void, Void, Artist[]> {
        WeakReference<ArtistsListActivity> callback;

        public LoadArtistsAsyncTask(ArtistsListActivity activity) {
            callback = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ArtistsListActivity activity = callback.get();
            if (activity != null)
                activity.showProgress();
        }

        @Override
        protected Artist[] doInBackground(Void... voids) {
            return SampleApplication.getApi().getArtists();
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            super.onPostExecute(artists);
            ArtistsListActivity activity = callback.get();
            if (activity != null) {
                activity.data = artists;
                activity.showContent(artists);
            }
        }
    }
}
