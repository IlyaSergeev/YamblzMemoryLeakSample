package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity {
    private static final String LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE";
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private Parcelable layoutManagerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_list);

        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (layoutManagerState != null) {
            gridLayoutManager.onRestoreInstanceState(layoutManagerState);
        }

        new AsyncTask<Void, Void, Artist[]>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Artist[] doInBackground(Void... voids) {
                return SampleApplication.getApi().getArtists();
            }

            @Override
            protected void onPostExecute(Artist[] artists) {
                super.onPostExecute(artists);
                showContent(artists);
            }
        }.execute();
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
    }

    private void showArtistDetails(@NonNull Artist artist) {
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra(getString(R.string.artist), artist);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        layoutManagerState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE, layoutManagerState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }
    }
}
