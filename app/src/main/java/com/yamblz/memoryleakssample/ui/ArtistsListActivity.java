package com.yamblz.memoryleakssample.ui;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ArtistsListActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;
    int firstVisiblePosition;
    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawable(null);

        unbinder = ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, Artist[]>() {
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
        }.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
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
        recyclerView.scrollToPosition(firstVisiblePosition);
    }

    private void showArtistDetails(@NonNull Artist artist) {
        startActivity(ArtistDetailsActivity.newIntent(artist, getApplicationContext()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
