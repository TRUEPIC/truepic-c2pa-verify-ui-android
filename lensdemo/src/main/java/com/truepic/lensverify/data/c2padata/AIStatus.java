package com.truepic.lensverify.data.c2padata;

import com.google.gson.annotations.SerializedName;

public class AIStatus {
    @SerializedName("contains_ai")
    public boolean containsAI;

    @SerializedName("is_ai_generated")
    public boolean isAIGenerated;
}
