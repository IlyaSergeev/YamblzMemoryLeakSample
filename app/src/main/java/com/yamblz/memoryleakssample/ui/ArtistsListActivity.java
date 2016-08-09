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
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;
import com.yamblz.memoryleakssample.ui.ArtistsAdapter.ArtistsAdapterListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.artists_recycler_view) RecyclerView recyclerView;
    private AsyncTask<Void, Void, Artist[]> load;
    private GridLayoutManager gridLayoutManager;

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
        if(recyclerView.getAdapter()!=null){
            //already load
        }else{
            Api api=((SampleApplication)getApplication()).getApi();
            if(api.isArtistLoad()){
                showContent(api.getArtists());
            }else{
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
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
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

        ArtistsAdapter artistsAdapter = new ArtistsAdapter(data,
                Picasso.with(this),
                getResources(),
                new ArtistsAdapterListener() {
                    @Override
                    public void onClickArtist(@NonNull Artist artist, int id) {
                        showArtistDetails(id);
                    }
                });
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();

    }

    private void showArtistDetails(int id) {
        Intent intent=new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",gridLayoutManager.findFirstVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            int position=savedInstanceState.getInt("position");
            gridLayoutManager.scrollToPosition(position);
        }

    }
}
