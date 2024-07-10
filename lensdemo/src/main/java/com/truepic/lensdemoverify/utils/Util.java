package com.truepic.lensdemoverify.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.google.gson.Gson;
import com.truepic.lensdemoverify.LensApp;
import com.truepic.lensdemoverify.gallery.utils.C2PAStatus;
import com.truepic.lensdemoverify.gallery.utils.GalleryItem;
import com.truepic.lensdemoverify.gallery.utils.GalleryItemType;
import com.truepic.lensverify.data.c2padata.C2PAData;
import com.truepic.lensverify.data.c2padata.ManifestStore;
import com.truepic.lensverify.data.c2padata.ValidationStatus;
import com.truepic.lenssdkverify.LensSecurityUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Util {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL dd, yyyy", Locale.ENGLISH);
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("LLLL dd, yyyy, HH:mm a", Locale.ENGLISH);

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getOutputDir(String dir) {
        final File truepicDir = new File(dir);
        if (!truepicDir.exists()) {
            truepicDir.mkdirs();
        }
        return truepicDir;
    }

    public static File getImageByUUID(UUID uuid) {
        return new File(Util.getOutputDir(LensApp.getInstance().getAppPath()), uuid + Constants.PictureExt);
    }

    public static File getAudioByUUID(UUID uuid) {
        return new File(Util.getOutputDir(LensApp.getInstance().getAppPath()), uuid + Constants.AudioExt);
    }

    public static void saveVideo(String uuid, Uri videoUri) {
        File outputFile = new File(Util.getOutputDir(LensApp.getInstance().getVideoPath()), uuid + Constants.VideoExt);
        if (outputFile.exists()) {
            return;
        }
        File previewFile = new File(Util.getOutputDir(LensApp.getInstance().getAppPath()), outputFile.getName() + Constants.PictureExt);

        InputStream stream;
        try {
            stream = LensApp.getInstance().getContentResolver().openInputStream(videoUri);
            OutputStream output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = stream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();

            // create preview
            FileOutputStream fos = new FileOutputStream(previewFile.getPath());
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(outputFile.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            // attach duration to preview
            Util.setDurationAttribute(previewFile, Util.getVideoDurationInSeconds(outputFile));

            stream.close();
        } catch (Exception e) {
            Log.d(Util.class.getName(), "Exception in saving the video file: " + e);
        }
    }

    public static void saveLocallyFromResource(InputStream inputStream, String idName) {
        if (idName.endsWith(".mp4")) {
            File mediaDir = new File(LensApp.getInstance().getVideoPath());
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
            idName = idName.substring(0, idName.length() - ".mp4".length());
            File tempFile = new File(LensApp.getInstance().getCacheDir(), "temp_video.mp4");
            saveRawResourceToFile(inputStream, tempFile);
            saveVideo(idName, Uri.fromFile(tempFile));
            return;
        }

        File mediaDir = new File(LensApp.getInstance().getAppPath());
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
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

    public static void saveRawResourceToFile(InputStream inputStream, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e){
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

    /**
     * Gets video duration from exif attribute of the input file
     *
     * @param file jpg
     * @return duration in seconds
     */
    public static int getVideoDurationAttribute(File file) {
        int duration = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(file);
            String durationString = exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT);
            if (durationString != null) duration = Integer.parseInt(durationString);
        } catch (Exception e) {
            Log.e(Util.class.getSimpleName(), "Duration couldn't be read: " + e.getMessage());
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
    public static ArrayList<GalleryItem> getAllMediaFilesByFolder(boolean includeDateMarkers, Map<String, C2PAStatus> c2paMetadata) {
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
                pf = new GalleryItem(file.getPath(), videoPath, file.length(), Util.getVideoDurationAttribute(file), C2PAStatus.NON_C2PA, file.lastModified());
            } else if(file.getName().contains(Constants.AudioExt)) { // audio
                pf = new GalleryItem(file.getPath(), file.length(), Util.getAudioDuration(file), C2PAStatus.NON_C2PA, file.lastModified());
            } else { // picture
                pf = new GalleryItem(file.getPath(), file.length(), C2PAStatus.NON_C2PA, file.lastModified());
            }

            C2PAStatus c2paStatusData = c2paMetadata.get(file.getPath());
            pf.setC2PA(c2paStatusData != null ? c2paStatusData : C2PAStatus.NON_C2PA);

            images.add(pf);
        }

        return images;
    }

    public static int getVideoDurationInSeconds(File videoFile) {
        int duration = 0;

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
     * Saves video duration to exif attribute of the input file
     *
     * @param file jpg
     */
    public static void setDurationAttribute(File file, int duration) {
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, duration + "");
            exifInterface.saveAttributes();
        } catch (Exception e) {
            Log.e(Util.class.getSimpleName(), "Duration couldn't be saved: " + e.getMessage());
        }
    }

    /**
     * Updates c2pa flags for the gallery items
     *
     * @param items existing items to be updated
     * @return new list of updated items
     */
    public static ArrayList<GalleryItem> updateMediaC2PAFlags(List<GalleryItem> items, Map<String, C2PAStatus> c2paMetadata) {
        ArrayList<GalleryItem> updatedItems = new ArrayList<>(items.size());
        for (GalleryItem item : items) {
            if (item.getType() != GalleryItemType.DATE) {
                GalleryItem updated = new GalleryItem(item);
                // TODO: accessing libc2pa
                String json = LensSecurityUtil.verifyJson((item.getType() == GalleryItemType.VIDEO ? item.getVideoPath() : item.getPath()));
                updated.setC2PA(getC2PAStatus(jsonToC2PAData(json)));
                updatedItems.add(updated);
                c2paMetadata.put(updated.getPath(), updated.getC2PA());
            } else {
                updatedItems.add(item);
            }
        }

        return updatedItems;
    }

    public static C2PAData jsonToC2PAData(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, C2PAData.class);
    }

    public static C2PAData jsonToC2PADataWithThumbnails(String json, Map<String, byte[]> thumbnails) {
        Gson gson = new Gson();
        C2PAData data = gson.fromJson(json, C2PAData.class);
        data.setThumbnailStore(thumbnails);
        return data;
    }

    public static C2PAStatus getC2PAStatus(C2PAData data) {
        try {
            boolean isInvalidHash = false;

            for (ManifestStore manifestStore : data.getManifestStore()) {
                for(ValidationStatus validationStatus : manifestStore.getValidationStatuses()) {
                    if (validationStatus.code.contains("signingCredential.") && !validationStatus.success) {
                        return C2PAStatus.C2PA_INVALID_SIGNATURE;
                    }

                    if (!validationStatus.success) {
                        isInvalidHash = true;
                    }
                }
            }

            return isInvalidHash ? C2PAStatus.C2PA_INVALID_HASH : C2PAStatus.C2PA;
        } catch (Exception ex) {
            return C2PAStatus.NON_C2PA;
        }
    }
}
