package com.truepic.lensverify.data.c2padata.assertions.odometry;

import com.google.gson.annotations.SerializedName;
import com.truepic.lensverify.data.c2padata.assertions.DataInstance;

public class CustomOdometry extends DataInstance {
    @SerializedName("data")
    private OdometryData data;

    public OdometryData getData() {
        return data;
    }
}
