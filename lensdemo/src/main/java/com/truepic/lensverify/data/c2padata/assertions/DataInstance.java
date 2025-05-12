package com.truepic.lensverify.data.c2padata.assertions;

import com.google.gson.annotations.SerializedName;

public class DataInstance {
    @SerializedName("instance_index")
    private int instanceIndex;
    @SerializedName("status")
    private String status;

    public int getInstanceIndex() {
        return instanceIndex;
    }

    public String getStatus() {
        return status;
    }
}
