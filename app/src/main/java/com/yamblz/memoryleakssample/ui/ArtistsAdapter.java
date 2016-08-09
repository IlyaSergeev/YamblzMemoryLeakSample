package com.yamblz.memoryleakssample.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.App;
import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;
import com.yamblz.memoryleakssample.ui.ArtistsAdapter.ArtistHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i-sergeev on 01.07.16
 */
public class ArtistsAdapter extends Adapter<ArtistHolder> {
    private final ArtistSelectedListener listener;

    private Artist[] artists = new Artist[0];

    public interface ArtistSelectedListener {
        void onArtistSelected(Artist artist);
    }


    public ArtistsAdapter(ArtistSelectedListener listener) {
        this.listener = listener;
    }


    public void setArtists(Artist[] artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }


    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.artist_card, parent, false);
        return new ArtistHolder(view);
    }


    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        holder.bind(artists[position]);
    }


    @Override
    public int getItemCount() {
        return artists.length;
    }


    public class ArtistHolder extends ViewHolder {
        @BindView(R.id.artist_name) TextView name;
        @BindView(R.id.artist_cover) ImageView cover;
        @BindView(R.id.artist_albums) TextView albums;
        @BindView(R.id.artist_tracks) TextView tracks;

        public ArtistHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArtistSelected(artists[getAdapterPosition()]);
                }
            });
        }

        public void bind(@NonNull Artist artist) {
            Api api = App.getApi();
            name.setText(artist.getName());
            albums.setText(api.getPlurals(R.plurals.artistAlbums, artist.getAlbumsCount()));
            tracks.setText(api.getPlurals(R.plurals.artistTracks, artist.getTracksCount()));
            api.loadCover(cover, artist.getCover().getSmallImageUrl());
        }
    }
}
