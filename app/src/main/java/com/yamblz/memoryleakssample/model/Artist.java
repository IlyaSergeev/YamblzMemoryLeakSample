package com.yamblz.memoryleakssample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Artist implements Parcelable {
    @SerializedName("id")
    private final long id;

    @SerializedName("name")
    private final String name;

    @SerializedName("genres")
    private final List<String> genres;

    @SerializedName("tracks")
    private final int tracksCount;

    @SerializedName("albums")
    private final int albumsCount;

    @SerializedName("link")
    private final String webCite;

    @SerializedName("description")
    private final String description;

    @SerializedName("cover")
    private final Cover cover;

    public Artist(long id,
                  String name,
                  List<String> genres,
                  int tracksCount,
                  int albumsCount,
                  String webCite, String description, Cover cover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracksCount = tracksCount;
        this.albumsCount = albumsCount;
        this.webCite = webCite;
        this.description = description;
        this.cover = cover;
    }

    public Artist(Parcel in) {
        id = in.readLong();
        name = in.readString();
        genres = in.createStringArrayList();
        tracksCount = in.readInt();
        albumsCount = in.readInt();
        webCite = in.readString();
        description = in.readString();
        cover = new Cover(in.readString(), in.readString());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public int getAlbumsCount() {
        return albumsCount;
    }

    public String getWebCite() {
        return webCite;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeStringList(genres);
        parcel.writeInt(tracksCount);
        parcel.writeInt(albumsCount);
        parcel.writeString(webCite);
        parcel.writeString(description);
        parcel.writeString(cover.getBigImageUrl());
        parcel.writeString(cover.getSmallImageUrl());
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
