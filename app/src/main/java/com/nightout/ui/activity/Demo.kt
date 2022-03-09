package com.nightout.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.media.Image
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuDemoAdapter
import com.nightout.base.BaseActivity

import com.nightout.model.VenuModel
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.view.animation.Animation

import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar
import com.nightout.databinding.DemoBinding
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills


import kotlinx.android.synthetic.main.demo.*

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener

import android.widget.VideoView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.teresaholfeld.stories.StoriesProgressView
import java.net.URI
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts



import java.text.ParseException
import java.text.SimpleDateFormat


class Demo : BaseActivity() {
    //https://githubmemory.com/repo/ravishankarsingh1996/StoriesProgressView
    lateinit var binding : DemoBinding

    //private val resources = intArrayOf( R.drawable.venues1,    R.drawable.venues2,    R.drawable.venues3,    R.drawable.venues4,    )

    private val imagesList = mutableListOf( "https://www.fillmurray.com/640/360",

        "https://loremflickr.com/640/360", "https://www.placecage.com/640/360", "https://placekitten.com/640/360"
        /* "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
   "https://nightout.ezxdemo.com/storage/uploads/store_media//1cOx5wH5VMCHnjsUuzL9lnRxCDcnOGwvHrht4YfJ.jpgooo",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//rY9JpCqmAQTlxkqpEssz808AaxA3hQ9F88GrGBHs.jpg",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//iz3XVb7QiNfRsZ4yha42vTgjegFt3DQ035SQVNWg.jpg",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//8rKCEmiu5n0Fm3riTmG4l5O9JYinmJraL5YiPEvK.jpg"*/
    )

    var pressTime = 0L
    var limit = 500L


  //  private val storiesProgressView: StoriesProgressView? = null


    // on below line we are creating a counter
    // for keeping count of our stories.
    private var counter = 0
    var exoPlayer: SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this@Demo, R.layout.demo)

       /* showStories()*/

        binding.btnPicker.setOnClickListener {
           // getPicker()
        }


    }

/*    private fun getPicker() {
        val intent = Lassi(this)
            .with(LassiOption.CAMERA_AND_GALLERY)
            .setMaxCount(4)
            .setGridSize(2)
            .setPlaceHolder(R.drawable.ic_image_placeholder)
            .setErrorDrawable(R.drawable.ic_image_placeholder)
            .setSelectionDrawable(R.drawable.ic_checked_media)
            .setStatusBarColor(R.color.colorPrimaryDark)
            .setToolbarColor(R.color.colorPrimary)
            .setToolbarResourceColor(android.R.color.white)
            .setProgressBarColor(R.color.colorAccent)
            .setCropType(CropImageView.CropShape.OVAL)
            .setCropAspectRatio(1, 1)
            .setCompressionRation(10)
            .setMinFileSize(0)
            .setMaxFileSize(1000)
            .enableActualCircleCrop()
            .setSupportedFileTypes("jpg", "jpeg", "png", "webp", "gif")
            .enableFlip()
            .enableRotate()
            .build()
        receiveData.launch(intent)
    }*/



   /* private val receiveData =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedMedia = it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                if (!selectedMedia.isNullOrEmpty()) {
                    Log.d("pic", "onActivityResult: "+selectedMedia)
                }
            }
        }*/

/*
    fun showStories() {
        val myStories: ArrayList<MyStory> = ArrayList()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        try {
            val story1 = MyStory(
                "https://media.pri.org/s3fs-public/styles/story_main/public/images/2019/09/092419-germany-climate.jpg?itok=P3FbPOp-",
                simpleDateFormat.parse("20-10-2019 10:00:00")
            )
            myStories.add(story1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            val story2 = MyStory(
                "http://i.imgur.com/0BfsmUd.jpg",
                simpleDateFormat.parse("26-10-2019 15:00:00"),
                "#TEAM_STANNIS"
            )
            myStories.add(story2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val story3 = MyStory(
            "https://mfiles.alphacoders.com/681/681242.jpg"
        )
        myStories.add(story3)
        StoryView.Builder(supportFragmentManager)
            .setStoriesList(myStories)
            .setStoryDuration(5000)
            .setTitleText("Hamza Al-Omari")
            .setSubtitleText("Damascus")
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {
                    Toast.makeText(
                        this@Demo,
                        "Clicked: " + myStories[position].description,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onTitleIconClickListener(position: Int) {}
            })
            .setOnStoryChangedCallback { position ->
                Toast.makeText(
                    this@Demo,
                    position.toString() + "",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setStartingIndex(0)
            //.setRtl(true)
            .build()
            .show()
    }*/






}