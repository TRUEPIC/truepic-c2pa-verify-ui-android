package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Claim {

    @SerializedName("dc:title")
    private String dcTitle;

    @SerializedName("dc:format")
    private String dcFormat;

    private String instanceID;

    @SerializedName("claim_generator")
    private String claimGenerator;

    @SerializedName("claim_generator_info")

    private List<ClaimGeneratorInfo> claimGeneratorInfo;

    private boolean isActive;

    public String getDcTitle() {
        return dcTitle;
    }

    public String getDcFormat() {
        return dcFormat;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public String getClaimGenerator() {
        return claimGenerator;
    }

    public List<ClaimGeneratorInfo> getClaimGeneratorInfo() {
        return claimGeneratorInfo;
    }

    public boolean isActive() {
        return isActive;
    }
}
