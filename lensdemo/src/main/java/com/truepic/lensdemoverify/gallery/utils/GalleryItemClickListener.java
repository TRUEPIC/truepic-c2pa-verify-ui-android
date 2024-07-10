package com.truepic.lensdemoverify.gallery.utils;

public interface GalleryItemClickListener {

    /**
     * Called when a picture is clicked
     */
    void onPicClicked(String path);

    void onLongPicClicked(String path);

    void onInfoClicked(String path);
}
