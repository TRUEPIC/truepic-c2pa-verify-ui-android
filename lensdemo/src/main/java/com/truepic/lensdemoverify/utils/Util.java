package com.truepic.lensdemoverify.utils;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.truepic.lensdemoverify.LensApp;
import com.truepic.lensdemoverify.gallery.utils.GalleryItem;
import com.truepic.lensdemoverify.gallery.utils.GalleryItemType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL dd, yyyy", Locale.ENGLISH);
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("LLLL dd, yyyy, h:mm a", Locale.ENGLISH);
    public static String UPLOADED_ATTR = "uploaded";

    public static void saveLocallyFromResource(InputStream inputStream, String idName) {
        File picturesDir = new File(LensApp.getInstance().getAppPath());
        if (!picturesDir.exists()) {
            picturesDir.mkdirs();
        }
        try {
            File file = new File(LensApp.getInstance().getAppPath(), idName);
            if (file.exists()) return;

            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
            output.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts seconds to hh:mm:ss or mm:ss string
     *
     * @param seconds        amount of seconds
     * @param addTimePadding adds leading zero if minutes < 10 or hours < 10
     * @param addHours       adds hours to the front to get hh:mm:ss string instead of mm:ss
     * @return formatted mm:ss/hh:mm:ss string
     */
    public static String convertSecondsToDuration(long seconds, boolean addTimePadding, boolean addHours) {
        if (seconds > 0) {
            try {
                long secs = seconds % 60;
                long mins = addHours ? ((seconds / 60) % 60) : seconds / 60;
                long hours = seconds / 60 / 60;

                StringBuilder str = new StringBuilder();

                if (addHours) {
                    if (addTimePadding && hours < 10) {
                        str.append(0 + "").append(hours);
                    } else {
                        str.append(hours);
                    }
                    str.append(":");
                }

                if (addTimePadding && mins < 10) {
                    str.append(0 + "").append(mins);
                } else {
                    str.append(mins);
                }
                str.append(":");

                if (secs < 10) {
                    str.append(0 + "").append(secs);
                } else {
                    str.append(secs);
                }

                return str.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return addHours ? (addTimePadding ? "00:00:00" : "0:00:00") : (addTimePadding ? "00:00" : "0:00");
    }

    public static int getVideoDurationInSeconds(File videoFile) {
        int duration = -1;

        try {
            MediaPlayer mp = MediaPlayer.create(LensApp.getInstance(), Uri.fromFile(videoFile));
            duration = mp.getDuration();
            mp.release();
            return duration / 1000;
        } catch (Exception e) {
            Log.e(Util.class.getSimpleName(), "Duration couldn't be calculated: " + e.getMessage());
        }

        return duration;
    }

    /**
     * Gets audio duration from metadata
     *
     * @param file m4a
     * @return duration in seconds
     */
    public static int getAudioDuration(File file) {
        int duration = 0;

        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(file.getPath());
            String strDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(strDuration != null) duration = Math.round(Integer.parseInt(strDuration) / 1000f);
            mmr.close();
        } catch (Exception e) {
            Log.e(Util.class.getSimpleName(), "Audio duration couldn't be read: " + e.getMessage());
        }

        return duration;
    }

    /**
     * This Method gets all the images in the folder paths passed as a String to the method and returns
     * and ArrayList of PictureFacer a custom object that holds data of a given image
     */
    public static ArrayList<GalleryItem> getAllMediaFilesByFolder(boolean includeDateMarkers) {
        ArrayList<GalleryItem> images = new ArrayList<>();

        File directory = new File(LensApp.getInstance().getAppPath());
        File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(Constants.PictureExt) || pathname.getName().endsWith(Constants.AudioExt));
        if (files == null) return images;

        Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

        String lastDate = "";
        for (File file : files) {
            String currentDate = dateFormat.format(new Date(file.lastModified()));

            // insert date item if needed
            if (!currentDate.equalsIgnoreCase(lastDate) && includeDateMarkers) {
                images.add(new GalleryItem(currentDate));
                lastDate = currentDate;
            }

            GalleryItem pf;
            if (file.getName().contains(Constants.VideoExt)) { // video
                String videoPath = file.getPath().replace(file.getName(), "videos/" + file.getName().replace(Constants.PictureExt, ""));
                pf = new GalleryItem(file.getPath(), videoPath, file.length(), Util.getVideoDurationInSeconds(new File(videoPath)), false, file.lastModified());
            } else if(file.getName().contains(Constants.AudioExt)) { // audio
                pf = new GalleryItem(file.getPath(), file.length(), Util.getAudioDuration(file), false, file.lastModified());
            } else { // picture
                pf = new GalleryItem(file.getPath(), file.length(), false, file.lastModified());
            }

            // TODO: Access libc2pa - Load the C2PA data
            //pf.setC2PA(LensSecurityUtil.isC2PA(pf.getType() == GalleryItemType.VIDEO ? pf.getVideoPath() : pf.getPath()));
            images.add(pf);
        }

        return images;
    }

}
