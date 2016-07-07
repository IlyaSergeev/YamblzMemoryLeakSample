package com.yamblz.memoryleakssample.ui.artists_list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;
import com.yamblz.memoryleakssample.ui.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Artist[]>
{
    private static final int ARTISTS_LOADER_ID = 101;

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

        showProgress();
        getSupportLoaderManager().initLoader(
                ARTISTS_LOADER_ID,
                null,
                this).forceLoad();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public Loader<Artist[]> onCreateLoader(int id, Bundle args)
    {
        return new ArtistsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Artist[]> loader, Artist[] data)
    {
        showContent(data);
    }

    @Override
    public void onLoaderReset(Loader<Artist[]> loader)
    {

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
                                                            //TODO
                                                        }
                                                    });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
