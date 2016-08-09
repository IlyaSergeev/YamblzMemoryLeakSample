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
import com.yamblz.memoryleakssample.util.asynctask.GettingArtistsAsyncTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ArtistsListActivity extends AppCompatActivity
{
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;

    private AsyncTask<Void, Void, Artist[]> gettingArtistTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        unbinder = ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gettingArtistTask = new GettingArtistsAsyncTask(this::showProgress, this::showContent);
        gettingArtistTask.execute();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        int firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        Artist firstVisibleArtist = artistsAdapter.getArtist(firstVisiblePosition);
        ((SampleApplication)getApplication()).setFirstVisibleArtistInListActivity(firstVisibleArtist);
        gettingArtistTask.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
                                            (artist) -> showArtistDetails(artist));
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();

        Artist firstVisibleArtist = ((SampleApplication)getApplication()).getFirstVisibleArtistInListActivity();
        if (firstVisibleArtist != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                if (data[i].getId().equals(firstVisibleArtist.getId()))
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
        intent.putExtra(artist.getClass().getCanonicalName(), artist);
        startActivity(intent);
    }
}
