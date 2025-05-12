package com.truepic.lensverify.data.c2padata.assertions;

import com.google.gson.annotations.SerializedName;
import com.truepic.lensverify.data.c2padata.LibData;

public class LibC2PA extends DataInstance {
    @SerializedName("data")
    private LibData libData;

    public LibData getLibData() {
        return libData;
    }
}
