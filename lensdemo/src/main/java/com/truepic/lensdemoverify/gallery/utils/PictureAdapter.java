package com.truepic.lensdemoverify.gallery.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import com.truepic.lensdemoverify.databinding.DateItemBinding;
import com.truepic.lensdemoverify.databinding.PicHolderItemBinding;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // see https://developer.android.com/reference/androidx/recyclerview/widget/AsyncListDiffer for more info
    private final AsyncListDiffer<GalleryItem> differ = new AsyncListDiffer<>(this, new GalleryItemDiffUtil());

    private final GalleryItemClickListener picListener;

    /**
     * @param picListener An interface for listening to clicks on the RecyclerView's items
     */
    public PictureAdapter(GalleryItemClickListener picListener) {
        this.picListener = picListener;
    }

    /**
     * Sets list to adapter, should run in background
     *
     * @param list list of pictures
     */
    public void setList(ArrayList<GalleryItem> list) {
        differ.submitList(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());

        if (viewType == GalleryItemType.DATE.ordinal()) {
            return new DateHolder(DateItemBinding.inflate(inflater, container, false));
        } else {
            return new PicHolder(PicHolderItemBinding.inflate(inflater, container, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return differ.getCurrentList().get(position).getType().ordinal();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final GalleryItem item = differ.getCurrentList().get(position);

        if (holder.getItemViewType() == GalleryItemType.DATE.ordinal()) {
            DateHolder dateHolder = (DateHolder) holder;
            dateHolder.bind(item.getDate(), position == 0);
        } else {
            PicHolder picHolder = (PicHolder) holder;
            picHolder.bind(item, picListener);
        }
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
