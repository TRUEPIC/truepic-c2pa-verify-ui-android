package com.truepic.lensdemoverify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.truepic.lensdemoverify.databinding.ActivityGalleryNgBinding;
import com.truepic.lensdemoverify.gallery.utils.C2PAStatus;
import com.truepic.lensdemoverify.gallery.utils.GalleryItem;
import com.truepic.lensdemoverify.gallery.utils.GalleryItemClickListener;
import com.truepic.lensdemoverify.gallery.utils.PictureAdapter;
import com.truepic.lensdemoverify.utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryActivityNg extends AppCompatActivity implements GalleryItemClickListener {

    private ActivityGalleryNgBinding binding;
    private final int longClickDuration = 2000;
    private boolean isLongPress = false;

    private final Map<String, C2PAStatus> c2paMetadata = new HashMap<>();

    private final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private PictureAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private FileObserver fileObserver;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGalleryNgBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        binding.recycler.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(this);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setItemAnimator(null);

        // Add 3 pre-capture files
        add3PreCapturedFiles();

        // https://stackoverflow.com/questions/9958418/change-long-click-delay
        binding.headerText.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                isLongPress = true;
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (isLongPress) {
                        logout();
                    }
                }, longClickDuration);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                isLongPress = false;
            }
            return true;
        });

        binding.logoutButton.setOnClickListener(view -> logout());

        binding.galleryAction.setOnClickListener(view -> {
            launchFilePicker();
        });

        binding.drawerButton.setOnClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));

        fileObserver = new FileObserver(LensApp.getInstance().getAppPath()) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                if (event == FileObserver.MODIFY) runOnUiThread(() -> load());
            }
        };
    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimetypes = {"image/*", "video/*", "audio/mp4"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() == null) {
                        Toast.makeText(GalleryActivityNg.this, R.string.file_picker_error, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Uri uri = result.getData().getData();

                    if(uri == null) {
                        Toast.makeText(GalleryActivityNg.this, R.string.file_picker_error, Toast.LENGTH_SHORT).show();
                    } else {
                        String mime = getContentResolver().getType(uri);
                        if (mime != null && mime.toLowerCase().contains("image")) {
                            handlePicker(uri, Util.getImageByUUID(UUID.randomUUID()));
                        } else if (mime != null && mime.toLowerCase().contains("video")) {
                            Util.saveVideo(UUID.randomUUID().toString(), uri);
                        } else if(mime != null && mime.toLowerCase().contains("audio")) {
                            handlePicker(uri, Util.getAudioByUUID(UUID.randomUUID()));
                        } else {
                            Toast.makeText(GalleryActivityNg.this, R.string.file_picker_type_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void handlePicker(Uri result, File outputFile) {
        // Save file to local storage
        InputStream stream;
        try {
            stream = getContentResolver().openInputStream(result);

            OutputStream output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = stream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
            stream.close();
        } catch (Exception e) {
            // ignore
        }
    }

    private void add3PreCapturedFiles() {
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t2), "t2.jpg");
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t3), "t3.jpg");
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t4), "t4.jpg");
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t5), "t5.jpg");
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t6), "t6.m4a");
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t7), "t7.mp4");
        Util.saveLocallyFromResource(getResources().openRawResource(R.raw.t8), "t8.jpg");
    }

    private void logout() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        LensApp.getInstance().clearAll();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        fileObserver.startWatching();
        binding.drawerLayout.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fileObserver.stopWatching();
    }

    private void load() {
        if (adapter.getItemCount() == 0) {
            binding.loader.setVisibility(View.VISIBLE);
        }

        executor.execute(() -> {
            ArrayList<GalleryItem> items = Util.getAllMediaFilesByFolder(true, c2paMetadata);

            runOnUiThread(() -> {
                if (items.size() > 0) {
                    binding.recycler.setVisibility(View.VISIBLE);
                    binding.desc.setVisibility(View.GONE);
                    binding.title.setVisibility(View.GONE);
                    adapter.setList(items);

                    // update c2pa flags separately afterwards to speed up loading
                    executor.execute(() -> adapter.setList(Util.updateMediaC2PAFlags(items, c2paMetadata)));
                } else {
                    binding.recycler.setVisibility(View.GONE);
                    binding.desc.setVisibility(View.VISIBLE);
                    binding.title.setVisibility(View.VISIBLE);
                }

                binding.loader.setVisibility(View.GONE);
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancel running task(s) to avoid memory leaks
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @Override
    public void onPicClicked(String path) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra(PreviewActivity.FilePathExtra, path);
        startActivity(intent);
    }

    @Override
    public void onLongPicClicked(String path) {
        C2PARawDialogFragment dialog = new C2PARawDialogFragment(path);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onInfoClicked(String path) {
        Intent intent = new Intent(this, C2PAActivity.class);
        intent.putExtra(C2PAActivity.FilePathExtra, path);
        startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.bottom_up, R.anim.stay).toBundle());
    }

    private void showNoNetworkDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_network_title)
                .setMessage(R.string.no_network_message)
                .setNegativeButton(R.string.no_network_cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.no_network_settings, (dialog, which) -> startActivity(new Intent(Settings.ACTION_SETTINGS))).show();
    }

    @Override
    public boolean onSupportNavigateUp() { // for the back icon
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
