package com.truepic.lensdemoverify.gallery.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.truepic.lensdemoverify.R;
import com.truepic.lensdemoverify.databinding.DateItemBinding;

public class DateHolder extends RecyclerView.ViewHolder {

    private final DateItemBinding binding;

    DateHolder(@NonNull DateItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(String text, boolean isFirst) {
        binding.date.setPadding(0, isFirst ? binding.date.getResources().getDimensionPixelSize(R.dimen.default_margin_small) : 0, 0, 0);
        binding.date.setText(text);
    }
}
