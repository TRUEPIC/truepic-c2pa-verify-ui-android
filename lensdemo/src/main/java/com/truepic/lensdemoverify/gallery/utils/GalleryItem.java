package com.truepic.lensdemoverify.gallery.utils;

import androidx.annotation.Nullable;

public class GalleryItem {

    private final String date;
    private final String path;
    private final String videoPath;
    private final long size;
    private final GalleryItemType type;
    private final long durationSeconds;
    private C2PAStatus c2paStatus;
    private final long lastModified;

    public GalleryItem(String path, long size, C2PAStatus c2paStatus, long lastModified) {
        this.path = path;
        this.size = size;
        this.type = GalleryItemType.PICTURE;
        this.videoPath = null;
        this.durationSeconds = 0;
        this.date = "";
        this.c2paStatus = c2paStatus;
        this.lastModified = lastModified;
    }

    public GalleryItem(String path, String videoPath, long size, long durationSeconds, C2PAStatus c2paStatus, long lastModified) {
        this.path = path;
        this.videoPath = videoPath;
        this.size = size;
        this.type = GalleryItemType.VIDEO;
        this.durationSeconds = durationSeconds;
        this.date = "";
        this.c2paStatus = c2paStatus;
        this.lastModified = lastModified;
    }

    public GalleryItem(String audioPath, long size, long durationSeconds, C2PAStatus c2paStatus, long lastModified) {
        this.path = audioPath;
        this.size = size;
        this.type = GalleryItemType.AUDIO;
        this.videoPath = null;
        this.durationSeconds = durationSeconds;
        this.date = "";
        this.c2paStatus = c2paStatus;
        this.lastModified = lastModified;
    }

    public GalleryItem(String date) {
        type = GalleryItemType.DATE;
        size = 0;
        path = null;
        videoPath = null;
        durationSeconds = 0;
        this.date = date;
        c2paStatus = C2PAStatus.NON_C2PA;
        lastModified = 0;
    }

    public GalleryItem(GalleryItem item) {
        type = item.getType();
        size = item.getSize();
        path = item.getPath();
        videoPath = item.getVideoPath();
        durationSeconds = item.getDurationSeconds();
        date = item.getDate();
        c2paStatus = item.getC2PA();
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

    public C2PAStatus getC2PA() {
        return c2paStatus;
    }

    public void setC2PA(C2PAStatus c2paStatus) {
        this.c2paStatus = c2paStatus;
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
