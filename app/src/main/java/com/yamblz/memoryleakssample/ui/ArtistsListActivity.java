package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Artist[]>
{
    private static final int ARTISTS_LOADER_ID = 101;
    private static final String ARTISTS_STATE_KEY = "visible_position_state";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private Parcelable listState;

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
        showProgress();
        getSupportLoaderManager().initLoader(
                ARTISTS_LOADER_ID,
                null,
                this).forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(ARTISTS_STATE_KEY, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        listState = savedInstanceState.getParcelable(ARTISTS_STATE_KEY);
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
        gridLayoutManager.onRestoreInstanceState(listState);

    }

    private void showArtistDetails(@NonNull Artist artist)
    {
        ArtistDetailsActivity.artist = artist;
        startActivity(new Intent(this, ArtistDetailsActivity.class));
    }

    @Override
    public Loader<Artist[]> onCreateLoader(int id, Bundle args)
    {
        return new ArtistsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Artist[]> loader, Artist[] data)
    {
        showContent(data);
    }

    @Override
    public void onLoaderReset(Loader<Artist[]> loader)
    {

    }
}
