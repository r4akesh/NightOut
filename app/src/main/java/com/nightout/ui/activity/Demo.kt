package com.nightout.ui.activity

import android.annotation.SuppressLint
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
import android.graphics.Color
import android.graphics.drawable.Drawable
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

import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.demo.*

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class Demo : BaseActivity(), StoriesProgressView.StoriesListener{

    lateinit var binding : DemoBinding

    //private val resources = intArrayOf( R.drawable.venues1,    R.drawable.venues2,    R.drawable.venues3,    R.drawable.venues4,    )

    private val imagesList = mutableListOf( "https://www.fillmurray.com/640/360",
        "https://loremflickr.com/640/360",
        "https://www.placecage.com/640/360", "https://placekitten.com/640/360",
        "https://www.fillmurray.com/640/360",
        "https://loremflickr.com/640/360",
        "https://www.placecage.com/640/360", "https://placekitten.com/640/360")

    var pressTime = 0L
    var limit = 500L


  //  private val storiesProgressView: StoriesProgressView? = null


    // on below line we are creating a counter
    // for keeping count of our stories.
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this@Demo, com.nightout.R.layout.demo)

        binding.storiesProgressView.setStoriesCount(imagesList.size);
        binding.storiesProgressView.setStoryDuration(3000L);
        binding.storiesProgressView.setStoriesListener(this);
         binding.storiesProgressView.startStories(counter)
        setImageNormal(THIS!!,binding.imagePreview,imagesList[counter])

        binding.reverse.setOnTouchListener(onTouchListener);
        binding.skip.setOnTouchListener(onTouchListener);
        binding.reverse.setOnClickListener(this)
        binding.skip.setOnClickListener(this)
    }
    fun setImageNormal(context: Context?, imageView: ImageView?, url: String?) {
        binding.storiesProgressView.destroy()
        binding.progress.visibility= VISIBLE
      //  binding.storiesProgressView!!.pause()
        Glide.with(THIS!!)
            .load(url)
            .error(R.drawable.app_icon)
            .listener(object : RequestListener<Drawable?> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility= GONE
                   binding.storiesProgressView.startStories(counter)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility= GONE
                   return false
                }
            })
            .into(imageView!!)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.reverse){
            binding.storiesProgressView.reverse();
        }
        else if(v==binding.skip){
            binding.storiesProgressView.skip();
        }
    }

    // on below line we are creating a new method for adding touch listener
    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { v, event -> // inside on touch method we are
        // getting action on below line.
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                // on action down when we press our screen
                // the story will pause for specific time.
                pressTime = System.currentTimeMillis()

                // on below line we are pausing our indicator.
                binding.storiesProgressView!!.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {

                // in action up case when user do not touches
                // screen this method will skip to next image.
                val now = System.currentTimeMillis()

                // on below line we are resuming our progress bar for status.
                binding.storiesProgressView!!.resume()

                // on below line we are returning if the limit < now - presstime
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }
    override fun onNext() {

        setImageNormal(THIS!!,binding.imagePreview,imagesList[++counter])
        //binding.imagePreview.setImageResource(resources[++counter]);
    }

    override fun onPrev() {
        if ((counter - 1) < 0) return;

        // on below line we are setting image to image view
        setImageNormal(THIS!!,binding.imagePreview,imagesList[--counter])
      //  binding.imagePreview.setImageResource(resources[--counter]);
    }

    override fun onComplete() {
        overridePendingTransition(0,0)
        finish()
    }

    override fun onDestroy() {
        binding.storiesProgressView.destroy();
        super.onDestroy()
    }

}