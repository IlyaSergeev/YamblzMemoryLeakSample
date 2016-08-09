package com.yamblz.memoryleakssample.ui;

import android.content.res.Resources;
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

public class ArtistDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ARTIST =
            "com.yamblz.memoryleakssample.ui.ArtistDetailsActivity.EXTRA_ARTIST";
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

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(EXTRA_ARTIST)){
            Artist artist = (Artist) extras.getSerializable(EXTRA_ARTIST);

            if(artist!=null){
                updateArtistView(artist);
            }
        }
    }

    private void updateArtistView(@NonNull Artist artist) {
        Resources resources = getResources();
        Picasso.with(this).load(artist.getCover().getBigImageUrl()).into(posterImageView);
        nameTextView.setText(artist.getName());
        int albumsCount = artist.getAlbumsCount();
        int tracksCount = artist.getTracksCount();

        albumsTextView.setText(resources.getQuantityString(R.plurals.artistAlbums,
                albumsCount,
                albumsCount));
        tracksTextView.setText(resources.getQuantityString(R.plurals.artistTracks,
                tracksCount,
                tracksCount));
        descriptionTextView.setText(artist.getDescription());
    }

}
