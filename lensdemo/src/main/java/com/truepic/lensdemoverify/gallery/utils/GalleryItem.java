package com.truepic.lensdemoverify.gallery.utils;

import androidx.annotation.Nullable;

public class GalleryItem {

    private final String date;
    private final String path;
    private final String videoPath;
    private final long size;
    private final GalleryItemType type;
    private final long durationSeconds;
    private boolean isC2PA;
    private final long lastModified;

    public GalleryItem(String path, long size, boolean isC2PA, long lastModified) {
        this.path = path;
        this.size = size;
        this.type = GalleryItemType.PICTURE;
        this.videoPath = null;
        this.durationSeconds = 0;
        this.date = "";
        this.isC2PA = isC2PA;
        this.lastModified = lastModified;
    }

    public GalleryItem(String path, String videoPath, long size, long durationSeconds, boolean isC2PA, long lastModified) {
        this.path = path;
        this.videoPath = videoPath;
        this.size = size;
        this.type = GalleryItemType.VIDEO;
        this.durationSeconds = durationSeconds;
        this.date = "";
        this.isC2PA = isC2PA;
        this.lastModified = lastModified;
    }

    public GalleryItem(String audioPath, long size, long durationSeconds, boolean isC2PA, long lastModified) {
        this.path = audioPath;
        this.size = size;
        this.type = GalleryItemType.AUDIO;
        this.videoPath = null;
        this.durationSeconds = durationSeconds;
        this.date = "";
        this.isC2PA = isC2PA;
        this.lastModified = lastModified;
    }

    public GalleryItem(String date) {
        type = GalleryItemType.DATE;
        size = 0;
        path = null;
        videoPath = null;
        durationSeconds = 0;
        this.date = date;
        isC2PA = false;
        lastModified = 0;
    }

    public GalleryItem(GalleryItem item) {
        type = item.getType();
        size = item.getSize();
        path = item.getPath();
        videoPath = item.getVideoPath();
        durationSeconds = item.getDurationSeconds();
        date = item.getDate();
        isC2PA = item.isC2PA();
        lastModified = item.getLastModified();
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public GalleryItemType getType() {
        return type;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getDate() {
        return date;
    }

    public boolean isC2PA() {
        return isC2PA;
    }

    public void setC2PA(boolean isC2PA) {
        this.isC2PA = isC2PA;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof GalleryItem) {


            if (((GalleryItem)obj).getPath() == null || getPath() == null) {
                return false;
            }

            return ((GalleryItem)obj).getPath().equals(getPath());

        }

        return false;
    }
}
