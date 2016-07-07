package com.yamblz.memoryleakssample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Cover implements Parcelable
{
    @SerializedName("small")
    private final String smallImageUrl;

    @SerializedName("big")
    private final String bigImageUrl;

    public Cover(String smallImageUrl, String bigImageUrl)
    {
        this.smallImageUrl = smallImageUrl;
        this.bigImageUrl = bigImageUrl;
    }

    public String getSmallImageUrl()
    {
        return smallImageUrl;
    }

    public String getBigImageUrl()
    {
        return bigImageUrl;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(smallImageUrl);
        parcel.writeString(bigImageUrl);
    }

    private Cover(Parcel source)
    {
        smallImageUrl = source.readString();
        bigImageUrl = source.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>()
    {
        @Override
        public Cover createFromParcel(Parcel parcel)
        {
            return new Cover(parcel);
        }

        @Override
        public Cover[] newArray(int i)
        {
            return new Cover[i];
        }
    };
}
