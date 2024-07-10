package com.truepic.lensverify.data.c2padata.assertions;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class C2PAIngriedientData {

    private String title;
    private String thumbnailID;
    private String instanceID;
    private String format;

    @SerializedName("ingredient_manifest")
    private String ingredientManifest;

    public String getTitle() {
        return title;
    }

    public String getThumbnailID() {
        return thumbnailID;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public String getFormat() {
        return format;
    }

    public @Nullable String getIngredientManifest() {
        return ingredientManifest;
    }
}
