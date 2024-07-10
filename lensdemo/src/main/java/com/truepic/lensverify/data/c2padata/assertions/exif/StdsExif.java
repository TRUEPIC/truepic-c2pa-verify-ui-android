package com.truepic.lensverify.data.c2padata.assertions.exif;

import com.google.gson.annotations.SerializedName;
import com.truepic.lensverify.data.c2padata.assertions.DataInstance;

public class StdsExif extends DataInstance {
    @SerializedName("data")
    private ExifData exifData;
    @SerializedName("truepic_id")
    private String truepicId;

    public ExifData getExifData() {
        return exifData;
    }

    public String getTruepicId() {
        return truepicId;
    }

}
