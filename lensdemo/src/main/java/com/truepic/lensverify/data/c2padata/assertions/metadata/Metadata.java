package com.truepic.lensverify.data.c2padata.assertions.metadata;

import com.google.gson.annotations.SerializedName;
import com.truepic.lensverify.data.c2padata.assertions.DataInstance;

public class Metadata extends DataInstance {
    @SerializedName("data")
    private MetadataDetails data;
    @SerializedName("truepic_id")
    private String truepicId;

    public MetadataDetails getData() {
        return data;
    }

    public String getTruepicId() {
        return truepicId;
    }

}
