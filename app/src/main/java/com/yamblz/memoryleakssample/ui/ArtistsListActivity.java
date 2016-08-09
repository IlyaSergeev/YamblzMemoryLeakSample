package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.yamblz.memoryleakssample.AsyncTastArtistsLoader;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Artist>>
{
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawable(null);

        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        artistsAdapter = new ArtistsAdapter(null,
                new ArtistsAdapter.ArtistsAdapterListener() {
                    @Override
                    public void onClickArtist(@NonNull Artist artist) {
                        showArtistDetails(artist);
                    }
                });
        recyclerView.setAdapter(artistsAdapter);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void showProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showContent(List<Artist> data)
    {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        artistsAdapter.setData(data);
    }

    private void showArtistDetails(@NonNull Artist artist)
    {
        ArtistDetailsActivity.artist = artist;
        startActivity(new Intent(this, ArtistDetailsActivity.class));
    }

    @Override
    public Loader<List<Artist>> onCreateLoader(int id, Bundle args) {
        return new AsyncTastArtistsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> data) {
        showContent(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Artist>> loader) {

    }
}
