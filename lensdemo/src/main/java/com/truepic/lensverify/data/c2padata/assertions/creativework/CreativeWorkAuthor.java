package com.truepic.lensverify.data.c2padata.assertions.creativework;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreativeWorkAuthor {

    @SerializedName("@type")
    private String type;

    private List<CreativeWorkCredential> credential;

    private String identifier;

    private String name;

    public String getType() {
        return type;
    }

    public List<CreativeWorkCredential> getCredential() {
        return credential;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

}
