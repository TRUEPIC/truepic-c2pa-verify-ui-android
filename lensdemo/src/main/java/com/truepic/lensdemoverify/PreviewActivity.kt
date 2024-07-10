package com.truepic.lensdemoverify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.truepic.lensdemoverify.databinding.ActivityPreviewBinding
import com.truepic.lensdemoverify.gallery.utils.C2PAStatus
import com.truepic.lensdemoverify.gallery.utils.PreviewSwipeAdapter
import com.truepic.lensdemoverify.viewmodels.PreviewViewModel

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private lateinit var adapter: PreviewSwipeAdapter
    private lateinit var viewModel: PreviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PreviewViewModel::class.java]

        viewModel.c2paCallback = { c2pa, path ->
            binding.infoImage.visibility = if (c2pa != C2PAStatus.NON_C2PA) View.VISIBLE else View.GONE
            binding.infoImage.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.infoImage.context,
                    if (c2pa == C2PAStatus.C2PA) R.drawable.ic_c2pa_new else R.drawable.ic_c2pa_invalid_new
                )
            )
            binding.infoImage.setOnClickListener {
                // open from the bottom up
                startActivity(Intent(this, C2PAActivity::class.java).apply {
                    putExtra(C2PAActivity.FilePathExtra, path)
                }, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.bottom_up, R.anim.stay).toBundle())
            }
        }

        adapter = PreviewSwipeAdapter({ zoomed ->
            // disable swiping when zoomed-in
            binding.previewContainer.isUserInputEnabled = zoomed.not()

            // hide toolbar when zooming
            binding.toolbar.visibility = if (zoomed) View.GONE else View.VISIBLE
        }) {
            // open from the bottom up
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.FilePathExtra, it)
            }, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.bottom_up, R.anim.stay).toBundle())
        }

        binding.previewContainer.adapter = adapter
        binding.previewContainer.offscreenPageLimit = 4 // keep two slides on each side
        binding.previewContainer.setPageTransformer { _, _ -> } // empty page transformer to disable view animation on notifyitemchanged
        binding.previewContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.onPageSelected(position)
            }
        })

        binding.lensToolbarBackImageView.setOnClickListener { finish() }

        intent.getStringExtra(FilePathExtra)?.let { currentPath ->
            viewModel.load(currentPath) { list, currentIndex ->
                adapter.setList(list) {
                    binding.previewContainer.setCurrentItem(currentIndex, false)
                }
            }
        }
    }

    companion object {
        const val FilePathExtra = "filePathExtra"
    }

}