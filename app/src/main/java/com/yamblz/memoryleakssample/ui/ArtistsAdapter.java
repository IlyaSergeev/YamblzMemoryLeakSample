package com.yamblz.memoryleakssample.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.ArtistsLoader;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i-sergeev on 01.07.16
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistVH> {
    @NonNull
    private final List<Artist> artists;

    @NonNull
    private final ArtistsAdapterListener listener;

    public ArtistsAdapter(@Nullable List<Artist> artists,
                          ArtistsAdapterListener listener) {

        if (artists == null) {
            artists = new LinkedList<>();
        }
        this.artists = artists;


        if (listener == null) {
            listener = ArtistsAdapterListener.NULL;
        }
        this.listener = listener;
    }

    @NonNull
    public Artist getArtist(int position) {
        return artists.get(position);
    }

    @Override
    public ArtistVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.artist_card, parent, false);
        final RecyclerView.ViewHolder h = new ArtistVH(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPos = h.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    listener.onClickArtist(getArtist(adapterPos));
                }
            }
        });
        return new ArtistVH(view);
    }


    @Override
    public void onBindViewHolder(ArtistVH holder, int position) {
        holder.bind(artists.get(position));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setDataset(List<Artist> artists) {
        if (artists != null && !this.artists.containsAll(artists)) {
            this.artists.clear();
            this.artists.addAll(artists);
            notifyDataSetChanged();
        }
    }


    public class ArtistVH extends RecyclerView.ViewHolder {
        @BindView(R.id.artist_poster)
        ImageView posterImageView;

        @BindView(R.id.artist_name)
        TextView nameTextView;

        @BindView(R.id.artist_albums)
        TextView albumsTextView;

        @BindView(R.id.artist_tracks)
        TextView songsTextView;

        public ArtistVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull Artist artist) {
            final Resources resources = posterImageView.getResources();

            Picasso.with(posterImageView.getContext())
                    .load(artist.getCover().getSmallImageUrl()).into(posterImageView);
            nameTextView.setText(artist.getName());
            albumsTextView.setText(resources.getQuantityString(R.plurals.artistAlbums,
                    artist.getAlbumsCount(),
                    artist.getAlbumsCount()));
            songsTextView.setText(resources.getQuantityString(R.plurals.artistTracks,
                    artist.getTracksCount(),
                    artist.getTracksCount()));
        }
    }

    public interface ArtistsAdapterListener {
        void onClickArtist(@NonNull Artist artist);

        public static ArtistsAdapterListener NULL = new ArtistsAdapterListener() {
            @Override
            public void onClickArtist(@NonNull Artist artist) {

            }
        };
    }
}
