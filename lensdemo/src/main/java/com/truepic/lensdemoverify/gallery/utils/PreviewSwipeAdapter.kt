package com.truepic.lensdemoverify.gallery.utils

import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ortiz.touchview.OnTouchImageViewListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.truepic.lensdemoverify.R
import com.truepic.lensdemoverify.databinding.PreviewItemBinding
import com.truepic.lensdemoverify.utils.Util
import java.util.Date


class PreviewSwipeAdapter(
    private val onZoom: (isZoomed: Boolean) -> Unit,
    private val onAudioVideoClicked: (path: String) -> Unit
) : RecyclerView.Adapter<PreviewSwipeAdapter.ViewHolder>() {
    private val differ = AsyncListDiffer(this, GalleryItemDiffUtil())

    /**
     * Sets list to adapter, should run in background
     *
     * @param list list of pictures
     */
    fun setList(list: List<GalleryItem>?, onFinished: () -> Unit) {
        differ.submitList(list) {
            onFinished.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PreviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(private val binding: PreviewItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: GalleryItem) {
            // load the image async
            if (item.type == GalleryItemType.AUDIO) {
                binding.previewImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.previewImage.context,
                        R.drawable.audio_preview
                    )
                )
                binding.previewImage.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.previewImage.post { repositionDate() }
            } else {
                Picasso.get().load("file://" + item.path).fit().centerInside().noFade()
                    .into(binding.previewImage, object : Callback {
                        override fun onSuccess() {
                            repositionDate()
                        }

                        override fun onError(e: Exception?) {}
                    })

            }

            binding.previewImage.setOnTouchImageViewListener(object : OnTouchImageViewListener {
                override fun onMove() {
                    val zoomed =
                        binding.previewImage.currentZoom > 1.005 // intentionally not using isZoomed to have extra threshold
                    onZoom(zoomed)

                    // hide date when zooming
                    binding.date.visibility = if (zoomed) View.GONE else View.VISIBLE

                    // reset margins to zero when zooming
                    val params = binding.previewImage.layoutParams as ConstraintLayout.LayoutParams
                    if (zoomed) {
                        params.bottomMargin = 0
                        params.topMargin = 0
                    } else {
                        params.bottomMargin =
                            binding.previewImage.resources.getDimensionPixelSize(R.dimen.toolbar_height)
                        params.topMargin =
                            binding.previewImage.resources.getDimensionPixelSize(R.dimen.toolbar_height)
                    }
                }
            })

            binding.date.text = Util.dateTimeFormat.format(Date(item.lastModified))
            binding.playImage.visibility =
                if (item.type == GalleryItemType.VIDEO || item.type == GalleryItemType.AUDIO) View.VISIBLE else View.GONE
            binding.previewImage.isZoomEnabled = item.type == GalleryItemType.PICTURE
            binding.playImage.setOnClickListener {
                onAudioVideoClicked.invoke(if (item.type == GalleryItemType.VIDEO) item.videoPath else item.path)
            }

        }

        /**
         * Calculates the position of the image inside the ImageView and positions date right under it
         * since we cannot user WRAP_CONTENT for it due to zooming limitations involved.
         */
        private fun repositionDate() {
            val imageMatrix = FloatArray(9)
            binding.previewImage.imageMatrix.getValues(imageMatrix)
            val y = imageMatrix[Matrix.MTRANS_Y]

            val params = binding.date.layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin = y.toInt()
            binding.date.layoutParams = params
        }
    }
}