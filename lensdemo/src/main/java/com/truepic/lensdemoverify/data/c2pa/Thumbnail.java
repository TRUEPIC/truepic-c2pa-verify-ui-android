package com.truepic.lensdemoverify.data.c2pa;

import com.google.gson.annotations.SerializedName;

public class Thumbnail {
    @SerializedName("MaxDimension")
    String maxDimension;
    @SerializedName("Quality")
    String quality;

    public Thumbnail(int maxDimension, int quality) {
        this.maxDimension = Integer.toString(maxDimension);
        this.quality = Integer.toString(quality);
    }
}
