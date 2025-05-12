package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class Signature {

    private String status;

    @SerializedName("signed_by")
    private String signedBy;

    @SerializedName("signed_on")
    private String signedOn;

    public String getStatus() {
        return status;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public String getSignedOn() {
        return signedOn;
    }
}
