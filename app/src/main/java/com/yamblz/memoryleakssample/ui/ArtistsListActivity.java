package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.communication.LoadArtistsTask;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity implements ArtistsAdapter.ArtistsAdapterListener, LoadArtistsTask.Callbacks {

    private static final String FIRST_ARTIST_EXTRA = "first_artist";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private LoadArtistsTask mLoadArtistsTask;
    private Artist mFirstVisibleArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        if(savedInstanceState != null) {
            mFirstVisibleArtist = savedInstanceState.getParcelable(FIRST_ARTIST_EXTRA);
        }

        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FIRST_ARTIST_EXTRA, mFirstVisibleArtist);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mLoadArtistsTask = new LoadArtistsTask(this, getApi());
        mLoadArtistsTask.execute();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mLoadArtistsTask.cancel(false);

        int visible = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        if(visible != RecyclerView.NO_POSITION) {
            mFirstVisibleArtist = artistsAdapter.getArtist(visible);
        }
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
                                            this);
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();

        if(mFirstVisibleArtist != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                if (data[i].getId().equals(mFirstVisibleArtist.getId()))
                {
                    recyclerView.scrollToPosition(i);
                    break;
                }
            }
        }
    }

    private void showArtistDetails(@NonNull Artist artist)
    {
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra(ArtistDetailsActivity.ARTIST_EXTRA, artist);
        startActivity(intent);
    }

    @Override
    public void onClickArtist(@NonNull Artist artist)
    {
        showArtistDetails(artist);
    }

    private Api getApi()
    {
        return ((SampleApplication)getApplicationContext()).getApi();
    }

    @Override
    public void onPre() {
        showProgress();
    }

    @Override
    public void onPost(Artist[] artists) {
        showContent(artists);
    }
}
