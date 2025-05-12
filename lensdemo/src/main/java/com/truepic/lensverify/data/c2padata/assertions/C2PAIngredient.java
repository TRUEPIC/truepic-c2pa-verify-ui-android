package com.truepic.lensverify.data.c2padata.assertions;

import com.google.gson.annotations.SerializedName;

public class C2PAIngredient {

    @SerializedName("data")
    private C2PAIngriedientData data;

    public C2PAIngriedientData getData() {
        return data;
    }
}

