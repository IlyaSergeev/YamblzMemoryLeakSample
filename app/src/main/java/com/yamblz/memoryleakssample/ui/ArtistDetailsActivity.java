package com.yamblz.memoryleakssample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamblz.memoryleakssample.App;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistDetailsActivity extends AppCompatActivity {
    @BindView(R.id.artist_name) TextView name;
    @BindView(R.id.artist_cover) ImageView cover;
    @BindView(R.id.artist_albums) TextView albums;
    @BindView(R.id.artist_tracks) TextView tracks;
    @BindView(R.id.artist_description) TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        ButterKnife.bind(this);

        Artist artist = getIntent().getParcelableExtra(Artist.KEY_ARTIST);
        updateViews(artist);
    }

    private void updateViews(@NonNull Artist artist) {
        Api api = App.getApi();
        name.setText(artist.getName());
        description.setText(artist.getDescription());
        albums.setText(api.getPlurals(R.plurals.artistAlbums, artist.getAlbumsCount()));
        tracks.setText(api.getPlurals(R.plurals.artistTracks, artist.getTracksCount()));
        api.loadCover(cover, artist.getCover().getBigImageUrl());
    }
}
