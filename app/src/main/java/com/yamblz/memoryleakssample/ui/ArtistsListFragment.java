package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.SampleApplication;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArtistsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Artist[]> {
    private static final int SPAN_COUNT = 2;
    private static final int LOADER_ID = 1;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArtistsAdapter artistsAdapter;
    private Resources resources;
    private Context context;
    private Artist[] artists;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artisits_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        gridLayoutManager = new GridLayoutManager(context, SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

        context = getContext();
        resources = getResources();

        DividerItemDecoration decoration = new DividerItemDecoration(resources.getDrawable(R.drawable.divider),
                resources.getDimensionPixelSize(R.dimen.card_insets));
        recyclerView.addItemDecoration(decoration);

        if(artists == null && savedInstanceState == null){
            getLoaderManager().initLoader(
                    LOADER_ID,
                    null,
                    this).forceLoad();
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private void showContent() {
        hideProgress();
        artistsAdapter = new ArtistsAdapter(artists,
                Picasso.with(context),
                resources,
                this::showArtistDetails);

        recyclerView.setAdapter(artistsAdapter);
    }


    private void showArtistDetails(@NonNull Artist artist) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ArtistDetailsFragment.ARGUMENT_ARTIST, artist);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("details")
                .replace(R.id.flContainer, fragment)
                .commit();
    }

    @Override
    public Loader<Artist[]> onCreateLoader(int id, Bundle args) {
        if(isOnline()) {
            showProgress();
            return new AsyncTaskLoader<Artist[]>(context) {
                @Override
                public Artist[] loadInBackground() {
                    return SampleApplication.from(context).getApi().getArtists();
                }
            };
        }else{
            Toast.makeText(context,"No internet connection!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Artist[]> loader, Artist[] data) {
        artists = data;
        showContent();
    }

    @Override
    public void onLoaderReset(Loader<Artist[]> loader) {

    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
