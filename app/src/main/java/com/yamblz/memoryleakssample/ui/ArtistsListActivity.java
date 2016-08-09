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

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;
import com.yamblz.memoryleakssample.model.ArtistLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Artist>> {
    private static final int ARTISTS_LOADER_ID = 101;
    private static final String FIRST_VISIBLE_ARTIST_ID = "first_visible_artist_id";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

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
    protected void onPause() {
        super.onPause();

        int firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        Artist firstVisibleArtist = artistsAdapter.getArtist(firstVisiblePosition);

        getPreferences(MODE_PRIVATE).edit().putString(FIRST_VISIBLE_ARTIST_ID, firstVisibleArtist.getId()).apply();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showContent(List<Artist> data) {
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
    }

    private void showArtistDetails(@NonNull Artist artist) {
        Intent i = ArtistDetailsActivity.getIntent(this, artist);
        startActivity(i);
    }

    @Override
    public Loader<List<Artist>> onCreateLoader(int id, Bundle args) {
        return new ArtistLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> data) {
        showContent(data);
        if (recyclerView != null) {
            String idArtist = getPreferences(MODE_PRIVATE).getString(FIRST_VISIBLE_ARTIST_ID, null);
            if (idArtist != null) {
                for (int i = 0; i < data.size(); i++) {
                    if (idArtist.equals(data.get(i).getId())) {
                        recyclerView.scrollToPosition(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Artist>> loader) {

    }
}
