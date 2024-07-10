package com.truepic.lensverify.data.c2padata.assertions.actions;

import java.util.Map;

public class C2PAActionDataActions {

    private String action;
    private String softwareAgent;
    private String when;
    private Map<String, String> parameters;

    public String getAction() {
        return action;
    }

    public String getSoftwareAgent() {
        return softwareAgent;
    }

    public String getWhen() {
        return when;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
