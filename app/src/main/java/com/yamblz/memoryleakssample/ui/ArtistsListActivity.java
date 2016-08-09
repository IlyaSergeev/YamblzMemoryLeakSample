package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class ArtistsListActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;

    @State
    @Nullable
    String lastVisibleArtistId = null;
    @State
    @Nullable
    Artist[] artists = null;

    private AsyncTask<Void, Void, Artist[]> asyncTask = new LoaderArtist();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        artistsAdapter = new ArtistsAdapter(this, new ArtistClickListener());
        recyclerView.setAdapter(artistsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (artists == null)
            asyncTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        asyncTask.cancel(true);
        asyncTask = new LoaderArtist();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lastVisibleArtistId = artistsAdapter.getArtist(gridLayoutManager.findFirstVisibleItemPosition()).getId();
        Icepick.saveInstanceState(this, outState);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showContent(Artist[] data) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        artistsAdapter.setArtists(data);

        String lastVisibleArtistLocked = lastVisibleArtistId;
        if (lastVisibleArtistLocked != null) {
            for (int i = 0; i < data.length; i++) {
                if (lastVisibleArtistLocked.equals(data[i].getId())) {
                    recyclerView.scrollToPosition(i);
                    break;
                }
            }
        }
    }

    private void showArtistDetails(@NonNull Artist artist) {
        startActivity(ArtistDetailsActivity.newIntent(this, artist));
    }

    public class ArtistClickListener implements ArtistsAdapter.ArtistsAdapterListener{

        @Override
        public void onClickArtist(@NonNull Artist artist) {
            showArtistDetails(artist);
        }
    }

    public class LoaderArtist extends AsyncTask<Void, Void, Artist[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Artist[] doInBackground(Void... voids) {
            return SampleApplication.from(getApplicationContext()).getApi().getArtists();
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            super.onPostExecute(artists);
            showContent(artists);
        }
    }
}
