package com.truepic.lensdemoverify

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.truepic.lensdemoverify.databinding.ActivityPlayerBinding
import com.truepic.lensdemoverify.utils.Constants
import kotlin.math.absoluteValue

@OptIn(UnstableApi::class)
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null
    private var y1 = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // hide system bars for more immersive experience
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        binding.playerView.controllerAutoShow = false
        binding.closeButton.setOnClickListener { finish() }

        // swipe down to finish
        binding.playerView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> y1 = event.y
                MotionEvent.ACTION_UP -> {
                    val delta = event.y - y1

                    if (delta.absoluteValue > 100 && delta > 0) { // swipe down
                        finish()
                    }
                }
            }

            false
        }
    }

    private fun initializePlayer() {
        if (player == null) {

            player = ExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    intent.getStringExtra(FilePathExtra)?.let {
                        binding.playerView.player = exoPlayer
                        exoPlayer.setMediaItem(MediaItem.fromUri(it))
                        exoPlayer.playWhenReady = true
                        exoPlayer.prepare()

                        if (it.endsWith(Constants.AudioExt)) {
                            binding.playerView.showController() // show controller for audio files
                        } else {
                            binding.audioPreview.visibility = View.GONE
                        }
                    }
                }
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            binding.playerView.player = null
            player = null
        }
    }


    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
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

    companion object {
        const val FilePathExtra = "filePathExtra"
    }

}