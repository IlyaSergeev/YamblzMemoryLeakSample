package com.yamblz.memoryleakssample.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;

public class ArtistDetailsActivity extends AppCompatActivity
{
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

    @State Artist artist;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        unbinder = ButterKnife.bind(this);

        artist = (Artist) getIntent().getExtras().getSerializable(Artist.class.getCanonicalName());

        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (artist != null)
        {
            updateArtistView(artist);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void updateArtistView(@NonNull Artist artist)
    {
        Picasso.with(this).load(artist.getCover().getBigImageUrl()).into(posterImageView);
        nameTextView.setText(artist.getName());
        albumsTextView.setText(getResources().getQuantityString(R.plurals.artistAlbums,
                                                                artist.getAlbumsCount(),
                                                                artist.getAlbumsCount()));
        tracksTextView.setText(getResources().getQuantityString(R.plurals.artistTracks,
                                                                artist.getTracksCount(),
                                                                artist.getTracksCount()));
        descriptionTextView.setText(artist.getDescription());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
