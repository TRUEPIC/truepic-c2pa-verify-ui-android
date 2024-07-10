package com.truepic.lensverify.data.c2padata.assertions.actions;

import com.google.gson.annotations.SerializedName;

public class C2PAAction {

    @SerializedName("data")
    private C2PAActionData data;

    public C2PAActionData getData() {
        return data;
    }
}

