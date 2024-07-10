package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class LibData {
    @SerializedName("git_hash")
    private String gitHash;

    @SerializedName("lib_name")
    private String libName;

    @SerializedName("lib_version")
    private String libVersion;

    @SerializedName("target_spec_version")
    private String targetSpecVersion;

    public String getGitHash() {
        return gitHash;
    }

    public String getLibName() {
        return libName;
    }

    public String getLibVersion() {
        return libVersion;
    }

    public String getTargetSpecVersion() {
        return targetSpecVersion;
    }
}
