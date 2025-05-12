package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class CertificateChain {
    @SerializedName("cert_der")
    private String certDer;

    public String getCertDer() {
        return certDer;
    }
}
