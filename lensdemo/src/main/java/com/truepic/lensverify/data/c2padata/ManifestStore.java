package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ManifestStore {
    @SerializedName("URI")
    private String uri;
    @SerializedName("assertions")
    private Assertions assertions;
    @SerializedName("certificate")
    private Certificate certificate;
    @SerializedName("certificate_chain")
    private List<CertificateChain> certificateChain;
    @SerializedName("signature")
    private Signature signature;
    @SerializedName("claim")
    private Claim claim;
    @SerializedName("trusted_timestamp")
    private TrustedTimestamp trustedTimestamp;

    public String getUri() {
        return uri;
    }

    public Assertions getAssertions() {
        return assertions;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public List<CertificateChain> getCertificateChain() {
        return certificateChain;
    }

    public Signature getSignature() {
        return signature;
    }

    public Claim getClaim() {
        return claim;
    }
}
