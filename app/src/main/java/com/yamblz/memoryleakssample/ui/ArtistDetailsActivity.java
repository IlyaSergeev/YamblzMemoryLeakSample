package com.yamblz.memoryleakssample.ui;

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

import static com.yamblz.memoryleakssample.ui.ArtistsListActivity.ARTIST;

public class ArtistDetailsActivity extends AppCompatActivity {
    private Artist artist;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        ButterKnife.bind(this);
        this.artist = getIntent().getParcelableExtra(ARTIST);
        clearViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (artist != null) {
            updateArtistView(artist);
        } else {
            clearViews();
        }
    }

    private void clearViews() {
        posterImageView.setImageResource(android.R.color.white);
        nameTextView.setText("");
        albumsTextView.setText("");
        tracksTextView.setText("");
        descriptionTextView.setText("");
    }

    private void updateArtistView(@NonNull Artist artist) {
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
}
