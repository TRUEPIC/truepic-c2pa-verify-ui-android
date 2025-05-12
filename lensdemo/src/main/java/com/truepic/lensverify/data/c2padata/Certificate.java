package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class Certificate {

    @SerializedName("cert_der")
    private String certDer;

    @SerializedName("issuer_name")
    private String issuerName;

    @SerializedName("organization_name")
    private String organizationName;

    @SerializedName("organization_unit_name")
    private String organizationUnitName;

    @SerializedName("status")
    private String status;

    @SerializedName("status_reason")
    private String statusReason;

    @SerializedName("subject_name")
    private String subjectName;

    @SerializedName("valid_not_after")
    private String validNotAfter;

    @SerializedName("valid_not_before")
    private String validNotBefore;

    public String getSubjectName() {
        return subjectName;
    }

    public String getCertDer() {
        return certDer;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public String getValidNotAfter() {
        return validNotAfter;
    }

    public String getValidNotBefore() {
        return validNotBefore;
    }

}
