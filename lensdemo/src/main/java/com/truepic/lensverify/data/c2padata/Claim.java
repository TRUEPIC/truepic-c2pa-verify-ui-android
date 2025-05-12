package com.truepic.lensverify.data.c2padata;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private JsonElement claimGeneratorInfo;

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

    public @Nullable List<ClaimGeneratorInfo> getClaimGeneratorInfo() {
        if (claimGeneratorInfo == null) return null;

        if (claimGeneratorInfo.isJsonArray()) { // pre 2.0 spec we have list of ClaimGeneratorInfo(s)
            Type listType = new TypeToken<List<ClaimGeneratorInfo>>(){}.getType();
            return (new Gson()).fromJson(claimGeneratorInfo, listType);
        } else { // 2.0 we have single ClaimGeneratorInfo
            ArrayList<ClaimGeneratorInfo> list = new ArrayList<>();
            list.add((new Gson()).fromJson(claimGeneratorInfo, ClaimGeneratorInfo.class));
            return list;
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
