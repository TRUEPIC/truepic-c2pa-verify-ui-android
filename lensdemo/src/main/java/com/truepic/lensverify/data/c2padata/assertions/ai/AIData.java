package com.truepic.lensverify.data.c2padata.assertions.ai;

import com.google.gson.annotations.SerializedName;

public class AIData {

    @SerializedName("model_name")
    private String modelName;

    @SerializedName("model_version")
    private String modelVersion;

    public String getModelName() {
        return modelName;
    }

    public String getModelVersion() {
        return modelVersion;
    }
}
