package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class ValidationStatus {
    @SerializedName("code")
    public String code;

    @SerializedName("URI")
    public String URI;

    @SerializedName("explanation")
    public String explanation;

    @SerializedName("success")
    public boolean success;
}
