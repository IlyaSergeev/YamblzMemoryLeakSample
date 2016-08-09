package com.yamblz.memoryleakssample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Cover implements Parcelable
{
    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };
    @SerializedName("small")
    private final String smallImageUrl;
    @SerializedName("big")
    private final String bigImageUrl;

    public Cover(String smallImageUrl, String bigImageUrl)
    {
        this.smallImageUrl = smallImageUrl;
        this.bigImageUrl = bigImageUrl;
    }

    @SuppressWarnings("WeakerAccess")
    protected Cover(Parcel in) {
        smallImageUrl = in.readString();
        bigImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(smallImageUrl);
        dest.writeString(bigImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSmallImageUrl()
    {
        return smallImageUrl;
    }

    public String getBigImageUrl()
    {
        return bigImageUrl;
    }
}
