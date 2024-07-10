package com.truepic.lensverify.data.c2padata.assertions.exif;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ExifData {

    @SerializedName("@context")
    private Map<String, String> context;

    @SerializedName(value = "EXIF:GPSLongitude", alternate = {"exif:GPSLongitude", "exif:gpslongitude"})
    private String longitude;

    @SerializedName(value = "EXIF:GPSLatitude", alternate = {"exif:GPSLatitude", "exif:gpslatitude"})
    private String latitude;

    @SerializedName(value = "EXIF:Make", alternate = {"exif:Make", "exif:make", "tiff:Make"})
    private String make;

    @SerializedName(value = "EXIF:Model", alternate = {"exif:Model", "tiff:Model", "tiff:model"})
    private String model;

    @SerializedName(value = "EXIF:GPSAltitude", alternate = {"exif:GPSAltitude", "exif:gpsaltitude"})
    private String gpsAltitude;

    @SerializedName(value = "EXIF:GPSHorizontalAccuracy", alternate = {"exif:GPSHorizontalAccuracy", "exif:gpshorizontalaccuracy"})
    private String gpsAccuracy;

    @SerializedName(value = "EXIF:GPSTimeStamp", alternate = {"exif:GPSTimeStamp", "exif:gpstimestamp"})
    private String gpsTimestamp;

    @SerializedName(value = "EXIF:DateTimeOriginal", alternate = {"exif:DateTimeOriginal", "exif:datetimeoriginal"})
    private String dateTimeOriginal;

    @SerializedName("truepic_id")
    private String truepicId;

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getGPSAltitude() {
        return gpsAltitude;
    }

    public String getGPSAccuracy() {
        return gpsAccuracy;
    }

    public String getGPSTimestamp() {
        return gpsTimestamp;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public String getTruepicId() {
        return truepicId;
    }

    public String getDateTimeOriginal() {
        return dateTimeOriginal;
    }
}
