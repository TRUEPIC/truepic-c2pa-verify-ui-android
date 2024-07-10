package com.truepic.lensverify.data.c2padata.assertions;

import com.google.gson.annotations.SerializedName;

public class ThumbnailAssertion extends DataInstance {

    @SerializedName("thumbnail_id")
    private String thumbnailID;

    public String getThumbnailID() {
        return thumbnailID;
    }
}
