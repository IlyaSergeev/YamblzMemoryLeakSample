package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class ArtistDetailsActivity extends AppCompatActivity {
    private static final String KEY_ARTIST = "ArtistDetailsActivity#KEY_ARTIST";

    @BindView(R.id.artist_poster)
    ImageView posterImageView;

    @BindView(R.id.artist_name)
    TextView nameTextView;

    @BindView(R.id.artist_albums)
    TextView albumsTextView;

    @BindView(R.id.artist_tracks)
    TextView tracksTextView;

    @BindView(R.id.artist_description)
    TextView descriptionTextView;

    @NonNull
    @State
    Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        artist = getIntent().getExtras().getParcelable(KEY_ARTIST);
        if (artist == null)
            throw new IllegalArgumentException("Artist can't be null");
        showArtist(artist);
    }

    private void showArtist(@NonNull Artist artist) {
        Picasso.with(this)
                .load(artist.getCover().getBigImageUrl())
                .placeholder(R.color.white)
                .into(posterImageView);
        nameTextView.setText(artist.getName());
        albumsTextView.setText(getResources().getQuantityString(R.plurals.artistAlbums,
                artist.getAlbumsCount(),
                artist.getAlbumsCount()));
        tracksTextView.setText(getResources().getQuantityString(R.plurals.artistTracks,
                artist.getTracksCount(),
                artist.getTracksCount()));
        descriptionTextView.setText(artist.getDescription());
    }

    public static final Intent newIntent(@NonNull Context context,
                                         @NonNull Artist artist){
        Intent intent = new Intent(new Intent(context, ArtistDetailsActivity.class));
        intent.putExtra(KEY_ARTIST, artist);
        return intent;
    }
}
