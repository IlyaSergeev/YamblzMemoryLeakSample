package com.yamblz.memoryleakssample.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistDetailsFragment extends Fragment {
    public static final String ARGUMENT_ARTIST =
            "com.yamblz.memoryleakssample.ui.ArtistDetailsActivity.ARGUMENT_ARTIST";
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARGUMENT_ARTIST)) {
            Artist artist = (Artist) arguments.getSerializable(ARGUMENT_ARTIST);

            if (artist != null) {
                updateArtistView(artist);
            }
        }
    }

    private void updateArtistView(@NonNull Artist artist) {
        Resources resources = getResources();
        Picasso.with(getContext()).load(artist.getCover().getBigImageUrl()).into(posterImageView);
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
