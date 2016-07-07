package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity
{
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new AsyncTask<Void, Void, Artist[]>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Artist[] doInBackground(Void... voids)
            {
                return SampleApplication.getApi().getArtists();
            }

            @Override
            protected void onPostExecute(Artist[] artists)
            {
                super.onPostExecute(artists);
                showContent(artists);
            }
        }.execute();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        return super.onCreateView(name, context, attrs);
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

        ArtistsAdapter adapter = new ArtistsAdapter(data,
                                                    Picasso.with(this),
                                                    getResources(),
                                                    new ArtistsAdapter.ArtistsAdapterListener()
                                                    {
                                                        @Override
                                                        public void onClickArtist(@NonNull Artist artist)
                                                        {
                                                            //TODO show artist activity
                                                        }
                                                    });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
