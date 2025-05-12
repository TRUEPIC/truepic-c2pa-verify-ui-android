package com.truepic.lensverify.data.c2padata.assertions.creativework;

import java.util.List;

public class CreativeWorkCredential {

    private String alg;

    private List<Integer> hash;

    private String url;

    public String getAlg() {
        return alg;
    }

    public List<Integer> getHash() {
        return hash;
    }

    public String getUrl() {
        return url;
    }
}
