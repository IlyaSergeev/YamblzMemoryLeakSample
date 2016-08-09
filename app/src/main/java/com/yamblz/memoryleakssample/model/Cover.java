package com.yamblz.memoryleakssample.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Cover implements Serializable{
    @SerializedName("small")
    private final String smallImageUrl;

    @SerializedName("big")
    private final String bigImageUrl;

    public Cover(String smallImageUrl, String bigImageUrl) {
        this.smallImageUrl = smallImageUrl;
        this.bigImageUrl = bigImageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }
}
