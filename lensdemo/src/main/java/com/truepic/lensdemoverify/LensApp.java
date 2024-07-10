package com.truepic.lensdemoverify;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.truepic.lensdemoverify.utils.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class LensApp extends Application {

    private static LensApp _appContext = null;

    public LensApp() {
        _appContext = this;
    }

    public static LensApp getInstance() {
        return _appContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public String getAppPath() {
        return getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Constants.TruepicLensDir;
    }

    public String getVideoPath() {
        return getAppPath() + "/" + Constants.VideoDir;
    }

    public void clearAll() {
        try {
            File dir = new File(getAppPath());
            FileUtils.deleteDirectory(dir);
        } catch (Exception e) {
        }
    }
}
