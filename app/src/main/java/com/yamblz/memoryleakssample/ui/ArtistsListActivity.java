package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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


public class ArtistsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Artist[]> {
    private static final int SPAN_COUNT = 2;
    private static final int LOADER_ID = 1;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        ButterKnife.bind(this);
        resources = getResources();

        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(resources.getDrawable(R.drawable.divider),
                resources.getDimensionPixelSize(R.dimen.card_insets));
        recyclerView.addItemDecoration(decoration);

        getSupportLoaderManager().initLoader(
                LOADER_ID,
                null,
                this).forceLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        Artist firstVisibleArtist = artistsAdapter.getArtist(firstVisiblePosition);
        ((SampleApplication) getApplication()).setFirstVisibleArtistInListActivity(firstVisibleArtist);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showContent(Artist[] data) {
        hideProgress();

        artistsAdapter = new ArtistsAdapter(data,
                Picasso.with(this),
                resources,
                new ArtistsAdapter.ArtistsAdapterListener() {
                    @Override
                    public void onClickArtist(@NonNull Artist artist) {
                        showArtistDetails(artist);
                    }
                });
        recyclerView.setAdapter(artistsAdapter);

        Artist firstVisibleArtist = ((SampleApplication) getApplication()).getFirstVisibleArtistInListActivity();
        if (firstVisibleArtist != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i].getId().equals(firstVisibleArtist.getId())) {
                    recyclerView.scrollToPosition(i);
                    break;
                }
            }
        }
    }



    private void showArtistDetails(@NonNull Artist artist) {
        Intent detailArtistIntent = new Intent(this, ArtistDetailsActivity.class);
        detailArtistIntent.putExtra(ArtistDetailsActivity.EXTRA_ARTIST, artist);
        startActivity(detailArtistIntent);
    }

    @Override
    public Loader<Artist[]> onCreateLoader(int id, Bundle args) {
        final Context context = this;
        showProgress();
        return new AsyncTaskLoader<Artist[]>(context) {
            @Override
            public Artist[] loadInBackground() {
                return SampleApplication.from(context).getApi().getArtists();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Artist[]> loader, Artist[] data) {
        showContent(data);
    }

    @Override
    public void onLoaderReset(Loader<Artist[]> loader) {

    }
}
