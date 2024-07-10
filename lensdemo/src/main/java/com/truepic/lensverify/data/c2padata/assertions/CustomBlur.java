package com.truepic.lensverify.data.c2padata.assertions;

import com.google.gson.annotations.SerializedName;

public class CustomBlur extends DataInstance {
    @SerializedName("data")
    private double data;

    public double getData() {
        return data;
    }
}
