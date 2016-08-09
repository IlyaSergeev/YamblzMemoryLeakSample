package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ArtistsListActivity extends AppCompatActivity
{
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;

    private Subscription subscription;
    private Artist[] artists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        if (getLastCustomNonConfigurationInstance() == null) {
            showProgress();
            subscription = Observable.fromCallable(new Callable<Artist[]>() {
                @Override
                public Artist[] call() throws Exception {
                    return new Api(ArtistsListActivity.this).getArtists();
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Artist[]>() {
                        @Override
                        public void call(Artist[] artists) {
                            subscription = null;
                            ArtistsListActivity.this.artists = artists;
                            showContent(artists);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            subscription = null;
                            Log.d(ArtistsListActivity.class.getSimpleName(),
                                    "error during data loading");
                        }
                    });
        } else {
            artists = (Artist[]) getLastCustomNonConfigurationInstance();
            showContent(artists);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return artists;
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription = null;
        }
        super.onDestroy();
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
    }

    private void showArtistDetails(@NonNull Artist artist)
    {
        ArtistDetailsActivity.artist = artist;
        startActivity(new Intent(this, ArtistDetailsActivity.class));
    }
}
