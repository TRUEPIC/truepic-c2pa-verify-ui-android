package com.truepic.lensverify.data.c2padata.assertions.creativework;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreativeWorkData {

    @SerializedName("@context")
    private String context;

    @SerializedName("@type")
    private String type;

    private List<CreativeWorkAuthor> author;

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    public List<CreativeWorkAuthor> getAuthor() {
        return author;
    }
}
