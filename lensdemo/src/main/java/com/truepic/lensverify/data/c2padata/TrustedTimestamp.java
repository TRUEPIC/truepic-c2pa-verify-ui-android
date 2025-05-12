package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class TrustedTimestamp {

    private String status;

    private String timestamp;

    @SerializedName("TSA")
    private String tsa;

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTsa() {
        return tsa;
    }
}
