package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArtistsListActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    @Inject Api api;

    private GridLayoutManager gridLayoutManager;
    private int firstVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SampleApplication) getApplication()).component().inject(this);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawable(null);

        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    // TODO: Не стоит это делать в onResume?
    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        Observable.fromCallable(() -> api.getArtists())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showContent,
                        Throwable::printStackTrace);
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

        ArtistsAdapter artistsAdapter = new ArtistsAdapter(data,
                Picasso.with(this),
                getResources(),
                this::showArtistDetails);
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(firstVisiblePosition);
    }

    private void showArtistDetails(@NonNull Artist artist) {
        Intent artistIntent = new Intent(this, ArtistDetailsActivity.class);
        artistIntent.putExtra("artist", artist);
        startActivity(artistIntent);
    }
}
