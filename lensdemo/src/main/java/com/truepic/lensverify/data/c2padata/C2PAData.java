package com.truepic.lensverify.data.c2padata;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;


public class C2PAData {
    @SerializedName("manifest_store")
    private ArrayList<ManifestStore> manifestStore;

    private Map<String, byte[]> thumbnails;

    public ArrayList<ManifestStore> getManifestStore() {
        return manifestStore;
    }

    public @Nullable Map<String, byte[]> getThumbnailStore() {
        return thumbnails;
    }

    public void setThumbnailStore(Map<String, byte[]> thumbnails) {
        this.thumbnails = thumbnails;
    }
}

