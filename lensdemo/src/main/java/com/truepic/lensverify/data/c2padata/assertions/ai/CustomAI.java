package com.truepic.lensverify.data.c2padata.assertions.ai;

import com.google.gson.annotations.SerializedName;
import com.truepic.lensverify.data.c2padata.assertions.DataInstance;

public class CustomAI extends DataInstance {
    @SerializedName("data")
    private AIData data;

    public AIData getData() {
        return data;
    }
}
