package com.nightout.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.SimpleExoPlayer
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.VideoplayActvityBinding
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.source.ExtractorMediaSource

import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory

import com.google.android.exoplayer2.extractor.ExtractorsFactory

import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.nightout.utils.AppConstant


class VideoPlayActvity : BaseActivity() {

    // url of video which we are loading.
    var videoURL = ""
    // creating a variable for exoplayerview.
   // var exoPlayerView: SimpleExoPlayerView? = null
    // creating a variable for exoplayer
    var exoPlayer: SimpleExoPlayer? = null

    lateinit var binding : VideoplayActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@VideoPlayActvity,R.layout.videoplay_actvity)
        videoURL= intent.getStringExtra(AppConstant.INTENT_EXTRAS.VIDEO_URL)!!
        initializePlayer()
    }

    private fun initializePlayer() {
        try {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

            val videouri: Uri = Uri.parse(videoURL)

            val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")

            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()


            val mediaSource: MediaSource =
                ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null)


            binding.videoview.setPlayer(exoPlayer)


            exoPlayer!!.prepare(mediaSource)

            // we are setting our exoplayer
            // when it is ready.

            // we are setting our exoplayer
            // when it is ready.
            exoPlayer!!.playWhenReady = true
        } catch (e: Exception) {
            Log.e("TAG", "Error : " + e.toString());
        }

    }
}