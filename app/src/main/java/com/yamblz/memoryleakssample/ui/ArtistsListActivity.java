package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.App;
import com.yamblz.memoryleakssample.model.Artist;
import com.yamblz.memoryleakssample.ui.ArtistsAdapter.ArtistSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity implements ArtistSelectedListener {
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.artists_recycler_view) RecyclerView recyclerView;

    private Loader loader;
    private ArtistsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);

        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter = new ArtistsAdapter(this));

        loader = new Loader();
        loader.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loader != null) {
            loader.cancel(false);
            loader = null;
        }
    }


    @Override
    public void onArtistSelected(Artist artist) {
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra(Artist.KEY_ARTIST, artist);
        startActivity(intent);
    }


    private class Loader extends AsyncTask<Void, Void, Artist[]> {
        @Override
        protected Artist[] doInBackground(Void... voids) {
            return App.getApi().loadArtists();
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setArtists(artists);
        }
    }
}
