package com.truepic.lensdemoverify.gallery.utils;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.truepic.lensdemoverify.R;
import com.truepic.lensdemoverify.databinding.PicHolderItemBinding;
import com.truepic.lensdemoverify.utils.Util;

import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class PicHolder extends RecyclerView.ViewHolder {

    private final PicHolderItemBinding binding;

    PicHolder(@NonNull PicHolderItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(GalleryItem item, GalleryItemClickListener listener) {
        binding.mediaDuration.setVisibility(item.getType() != GalleryItemType.PICTURE ? View.VISIBLE : View.GONE);
        binding.infoImage.setVisibility(item.getC2PA() != C2PAStatus.NON_C2PA ? View.VISIBLE : View.GONE);
        binding.infoImage.setImageDrawable(ContextCompat.getDrawable(binding.infoImage.getContext(),
                item.getC2PA() == C2PAStatus.C2PA ? R.drawable.ic_c2pa_new : R.drawable.ic_c2pa_invalid_new));
        binding.date.setText(Util.timeFormat.format(new Date(item.getLastModified())));

        if (item.getType() != GalleryItemType.PICTURE) {
            binding.mediaDuration.setText(Util.convertSecondsToDuration(item.getDurationSeconds(), true, false));

            if (item.getType() == GalleryItemType.AUDIO) {
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(binding.mediaDuration, ContextCompat.getDrawable(binding.mediaDuration.getContext(), R.drawable.ic_audio), null, null, null);
            } else {
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(binding.mediaDuration, ContextCompat.getDrawable(binding.mediaDuration.getContext(), R.drawable.ic_video_padded), null, null, null);
            }
        }

        if (item.getType() == GalleryItemType.AUDIO) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(binding.image.getResources().getDimensionPixelSize(R.dimen.thumbnail_corners));
            binding.image.setImageDrawable(ContextCompat.getDrawable(binding.image.getContext(), R.drawable.audio_preview));
            binding.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            binding.image.setBackground(drawable);
            binding.image.setClipToOutline(true);
        } else {
            Picasso.get()
                    .load("file://" + item.getPath())
                    .fit()
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(binding.image.getResources().getDimensionPixelSize(R.dimen.thumbnail_corners), 0))
                    .into(binding.image);
        }


        binding.image.setOnClickListener(v -> listener.onPicClicked(item.getPath()));
        binding.image.setOnLongClickListener(v -> {
            listener.onLongPicClicked(item.getType() == GalleryItemType.VIDEO ? item.getVideoPath() : item.getPath());
            return false;
        });
        binding.infoImage.setOnClickListener(v -> listener.onInfoClicked(item.getType() == GalleryItemType.VIDEO ? item.getVideoPath() : item.getPath()));
    }
}
