package com.truepic.lensverify.data.c2padata.assertions.actions;


import com.truepic.lensverify.data.c2padata.assertions.DataInstance;

import java.util.List;

public class C2PAActionData extends DataInstance {

    private List<C2PAActionDataActions> actions;

    private C2PAActionDataMetadata metadata;

    public List<C2PAActionDataActions> getActions() {
        return actions;
    }

    public C2PAActionDataMetadata getMetadata() {
        return metadata;
    }
}
