package com.truepic.lensverify.data.c2padata.assertions.odometry;

import com.google.gson.annotations.SerializedName;
import com.truepic.lensverify.data.c2padata.assertions.DataInstance;

import java.util.List;

public class OdometryData extends DataInstance {

    private List<Attitude> attitude;
    private List<Geomagnetism> geomagnetism;
    private List<Gravity> gravity;
    private List<Pressure> pressure;
    private String lens;

    @SerializedName("rotation_rate")
    private List<RotationRate> rotationRate;

    @SerializedName("user_acceleration")
    private List<UserAcceleration> userAcceleration;

    public String getLens() {
        return lens;
    }

    public List<Attitude> getAttitude() {
        return attitude;
    }

    public List<Geomagnetism> getGeomagnetism() {
        return geomagnetism;
    }

    public List<Gravity> getGravity() {
        return gravity;
    }

    public List<Pressure> getPressure() {
        return pressure;
    }

    public List<RotationRate> getRotationRate() {
        return rotationRate;
    }

    public List<UserAcceleration> getUserAcceleration() {
        return userAcceleration;
    }
}
