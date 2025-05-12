package com.truepic.lensverify.data.c2padata.assertions.odometry;

import com.google.gson.annotations.SerializedName;

public class Attitude {
    @SerializedName("azimuth")
    private double azimuth;
    @SerializedName("pitch")
    private double pitch;
    @SerializedName("roll")
    private double roll;
    @SerializedName("timestamp")
    private String timestamp;

    public double getAzimuth() {
        return azimuth;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
