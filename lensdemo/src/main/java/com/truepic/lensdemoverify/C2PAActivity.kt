package com.truepic.lensdemoverify

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truepic.lensdemoverify.databinding.ActivityC2paBinding
import com.truepic.lensdemoverify.databinding.C2paDetailsBinding
import com.truepic.lensdemoverify.gallery.utils.C2PAStatus
import com.truepic.lensdemoverify.viewmodels.C2PAViewModel
import com.truepic.lensverify.utils.C2PAPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class C2PAActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding: ActivityC2paBinding
    private lateinit var viewModel: C2PAViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityC2paBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[C2PAViewModel::class.java]
        binding.back.setOnClickListener { v: View? -> finish() }
    }

    override fun onStart() {
        super.onStart()

        binding.progress.visibility = View.VISIBLE
        viewModel.load(intent.getStringExtra(FilePathExtra), onLoadComplete = { status, list ->
            binding.progress.visibility = View.GONE

            if(status == C2PAStatus.C2PA) {
                val adapter = Adapter(list)
                binding.message.visibility = View.GONE
                binding.list.adapter = adapter
                binding.list.layoutManager = LinearLayoutManager(this@C2PAActivity)
                val itemDecorator =
                    DividerItemDecoration(this@C2PAActivity, DividerItemDecoration.VERTICAL)
                itemDecorator.setDrawable(
                    Objects.requireNonNull<Drawable?>(
                        ContextCompat.getDrawable(
                            this@C2PAActivity,
                            R.drawable.divider_item
                        )
                    )
                )
                binding.list.addItemDecoration(itemDecorator)
            } else if(status == C2PAStatus.C2PA_INVALID_HASH) {
                binding.list.visibility = View.GONE
                binding.message.visibility = View.VISIBLE
                binding.message.text = getText(R.string.c2pa_invalid_hash)
            } else if(status == C2PAStatus.C2PA_INVALID_SIGNATURE) {
                binding.list.visibility = View.GONE
                binding.message.visibility = View.VISIBLE
                binding.message.text = getText(R.string.c2pa_invalid_signature)
            } else { // shouldn't happen
                binding.list.visibility = View.GONE
                binding.message.visibility = View.GONE
            }
        }) {
            if (it.isNullOrEmpty().not()) {
                Toast.makeText(this@C2PAActivity, it, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    companion object {
        const val FilePathExtra = "filePathExtra"
    }

    private inner class Adapter(private val list: List<C2PAViewModel.Item>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                C2paDetailsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position], position)
        }


        private inner class ViewHolder(private val binding: C2paDetailsBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(item: C2PAViewModel.Item, position: Int) {
                binding.title.visibility = if (position == 1) View.VISIBLE else View.GONE
                binding.progress.visibility = View.VISIBLE

                lifecycleScope.launch(Dispatchers.Default) {

                    withContext(Dispatchers.Main) {
                        if (item.thumbnail != null) {
                            binding.thumbnail.setImageBitmap(item.thumbnail)
                        } else {
                            val thumbnail = when (item.type) {
                                C2PAPresenter.Type.Audio -> R.drawable.no_thumbnail_audio
                                C2PAPresenter.Type.Video -> R.drawable.no_thumbnail_video
                                else -> R.drawable.no_thumbnail_image
                            }
                            binding.thumbnail.setImageDrawable(
                                ContextCompat.getDrawable(
                                    binding.thumbnail.context,
                                    thumbnail
                                )
                            )

                        }

                        binding.locationLabel.visibility = View.GONE
                        binding.locationText.visibility = View.GONE

                        item.address?.let {
                            binding.locationLabel.visibility = View.VISIBLE
                            binding.locationText.visibility = View.VISIBLE
                            binding.locationText.text = it
                        }

                        binding.thumbnailDesc.text = item.descriptor
                        binding.thumbnailType.text = item.typeLabel
                        binding.capturedWithText.text = item.capturedWith
                        binding.capturedWithLabel.text = item.capturedWithLabel
                        binding.capturedLabel.text = item.capturedLabel
                        binding.aiWarning.visibility =
                            if (item.isAiGenerated) View.VISIBLE else View.GONE

                        if (item.modifications > 0) {
                            binding.modificationsLabel.visibility = View.VISIBLE
                            binding.modificationsText.visibility = View.VISIBLE
                            binding.modificationsText.text = item.modifications.toString()
                            binding.signedWithLabel.visibility = View.GONE
                            binding.signedWithText.visibility = View.GONE
                        } else {
                            binding.modificationsLabel.visibility = View.GONE
                            binding.modificationsText.visibility = View.GONE
                            binding.signedWithLabel.visibility = View.VISIBLE
                            binding.signedWithText.visibility = View.VISIBLE
                        }

                        binding.capturedText.text = item.capturedDateText
                        binding.signedByText.text = item.signedByText

                        if (item.signedWithText.isNullOrEmpty()) {
                            binding.signedWithText.visibility = View.GONE
                            binding.signedWithLabel.visibility = View.GONE
                        } else {
                            binding.signedWithText.text = item.signedWithText
                        }

                        binding.progress.visibility = View.GONE
                    }
                }
            }
        }

    }

    override fun finish() {
        super.finish()
        // close from up to the bottom
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, R.anim.stay, R.anim.up_bottom, 0)
        } else {
            overridePendingTransition(R.anim.stay, R.anim.up_bottom)
        }
    }
}