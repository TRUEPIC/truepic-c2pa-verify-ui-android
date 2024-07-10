package com.truepic.lensverify.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Util {

    private static Rect getUpdatedBounds(BitmapFactory.Options options, int size) {
        int width, height;
        if (options.outWidth > options.outHeight)
        {
            width = size;
            height = width * options.outHeight / options.outWidth;
        }
        else {
            height = size;
            width = height * options.outWidth / options.outHeight;
        }
        return new Rect(0, 0, width, height);
    }

    /**
     * Gets down scaled bitmap where longest axis is 1000px
     *
     * @param data image data
     * @param size to be used for sampling
     * @return sampled bitmap
     */
    public static Bitmap getSampledBitmap(byte[] data, int size) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        Rect bounds = getUpdatedBounds(options, size);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        return Bitmap.createScaledBitmap( bitmap, bounds.width(), bounds.height(), true);
    }

    /**
     * Scales bitmap according to the max size using sampling (more memory efficient)
     * @param bitmap to be scaled
     * @param maxSize maximum size of the longer edge
     * @return scaled bitmap
     */
    public static Bitmap getScaledBitmapFromBuffer(byte[] bitmap, int maxSize, int rotation) {
        Bitmap sampled = getSampledBitmap(bitmap, maxSize);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap resized = Bitmap.createBitmap(sampled, 0, 0, sampled.getWidth(), sampled.getHeight(), matrix, true);
        sampled.recycle();
        return resized;
    }


    public static int getDegreesFromExifOrientation(int orientation) {
        switch (orientation) {
            case 1:
            case 2: // mirrored
                return 0;
            case 3:
            case 4: // mirrored
                return 180;
            case 5:
            case 6: // mirrored
                return 90;
            case 7:
            case 8: // mirrored
                return 270;
            default:
                return 0;
        }
    }

}
