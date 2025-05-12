package com.truepic.lensdemoverify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.truepic.lensdemoverify.databinding.ActivityGalleryNgBinding;
import com.truepic.lensdemoverify.gallery.utils.GalleryItemClickListener;
import com.truepic.lensdemoverify.gallery.utils.PictureAdapter;
import com.truepic.lensdemoverify.utils.Util;
import com.truepic.lensdemoverify.viewmodels.GalleryViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryActivityNg extends AppCompatActivity implements GalleryItemClickListener {

    private ActivityGalleryNgBinding binding;
    private final int longClickDuration = 2000;
    private boolean isLongPress = false;

    private final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private PictureAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private GalleryViewModel viewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = ActivityGalleryNgBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        viewModel.setItemsListener(items -> {
            if (adapter.getItemCount() == 0) {
                binding.loader.setVisibility(View.VISIBLE);
            }

            if (!items.isEmpty()) {
                binding.recycler.setVisibility(View.VISIBLE);
                binding.desc.setVisibility(View.GONE);
                binding.title.setVisibility(View.GONE);
                adapter.setList(items);
            } else {
                binding.recycler.setVisibility(View.GONE);
                binding.desc.setVisibility(View.VISIBLE);
                binding.title.setVisibility(View.VISIBLE);
            }

            binding.loader.setVisibility(View.GONE);

            return null;
        });

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

        binding.drawerButton.setOnClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));
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
        viewModel.resume();
        binding.drawerLayout.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
