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
import com.yamblz.memoryleakssample.ui.ArtistsAdapter.ArtistsAdapterListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.artists_recycler_view) RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private AsyncTask<Void, Void, Artist[]> load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);
        ButterKnife.bind(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        load = new AsyncTask<Void, Void, Artist[]>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Artist[] doInBackground(Void... voids) {
                return ((SampleApplication) getApplication()).getApi().getArtists();
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
        int firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        Artist firstVisibleArtist = artistsAdapter.getArtist(firstVisiblePosition);
        ((SampleApplication) getApplication()).setFirstVisibleArtistInListActivity(firstVisibleArtist);
        if(load!=null){
            load.cancel(true);
        }
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
                new ArtistsAdapterListener() {
                    @Override
                    public void onClickArtist(@NonNull Artist artist, int id) {
                        showArtistDetails(artist,id);
                    }
                });
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();

    }

    private void showArtistDetails(@NonNull Artist artist,int id) {
        //todo
        // ArtistDetailsActivity.artist = artist;
        Intent intent=new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
