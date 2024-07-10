package com.truepic.lensdemoverify.gallery.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class GalleryItemDiffUtil : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        // called only if areItemsTheSame == true, so we already know that paths are equal
        return oldItem.size == newItem.size && oldItem.type == newItem.type && oldItem.c2PA == newItem.c2PA
    }
}