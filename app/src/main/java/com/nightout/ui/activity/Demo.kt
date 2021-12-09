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
import com.teresaholfeld.stories.StoriesProgressView
import java.net.URI


class Demo : BaseActivity(), StoriesProgressView.StoriesListener{
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this@Demo, R.layout.demo)

        binding.storiesProgressView.setStoriesCount(imagesList.size)
        binding.storiesProgressView.setStoryDuration(5000L)
        binding.storiesProgressView.setStoriesListener(this)
         binding.storiesProgressView.startStories(counter)

        setImageNormal(THIS!!,binding.imagePreview,imagesList[counter])

        binding.reverse.setOnTouchListener(onTouchListener)
        binding.skip.setOnTouchListener(onTouchListener)
        binding.reverse.setOnClickListener(this)
        binding.skip.setOnClickListener(this)
    }
    fun setImageNormal(context: Context?, imageView: ImageView?, url: String?) {
        binding.imagePreview.visibility= VISIBLE
        binding.relVideoView.visibility= GONE
        binding.progress.visibility= VISIBLE
        Handler(Looper.getMainLooper()).post(Runnable {
            if (binding.storiesProgressView != null)
                binding.storiesProgressView.pause()
        })

        Glide.with(THIS!!)
            .load(url)
            .error(R.drawable.no_image)
            .listener(object : RequestListener<Drawable?> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility= GONE
                    Handler(Looper.getMainLooper()).post(Runnable {
                        if (binding.storiesProgressView != null)
                            binding.storiesProgressView.resume()
                    })

                    Log.e("TAG", "onResourceReady: ")
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("TAG", "onLoadFailed: ")
                    binding.progress.visibility= GONE
                   // binding.storiesProgressView.skip()

                   return false
                }
            })
            .into(imageView!!)
    }


    fun setVideo(context: Context?, videoview: VideoView?, url: String?)  {
        binding.relVideoView.visibility= VISIBLE
        binding.imagePreview.visibility= GONE
        Handler(Looper.getMainLooper()).post(Runnable {
            if (binding.storiesProgressView != null)
                binding.storiesProgressView.pause()
        })
      //  Log.e("TAG", "setVideo111: ")
        videoview?.setVideoURI(Uri.parse(url))
        try {
            videoview?.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.setOnInfoListener(MediaPlayer.OnInfoListener { mediaPlayer, i, i1 ->
                    Log.e("TAG", "onInfo: =============>>>>>>>>>>>>>>>>>>>$i")
                    when (i) {
                        MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                            binding.progress.setVisibility(GONE)
                            storiesProgressView.resume()
                            return@OnInfoListener true
                        }
                        MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                            binding.progress.setVisibility(VISIBLE)
                            Handler(Looper.getMainLooper()).post(Runnable {
                                if (binding.storiesProgressView != null)
                                    binding.storiesProgressView.pause()
                            })
                            return@OnInfoListener true
                        }
                        MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                            binding.progress.setVisibility(VISIBLE)
                            Handler(Looper.getMainLooper()).post(Runnable {
                                if (binding.storiesProgressView != null)
                                    binding.storiesProgressView.pause()
                            })
                            return@OnInfoListener true
                        }
                        MediaPlayer.MEDIA_ERROR_TIMED_OUT -> {
                            binding.progress.setVisibility(VISIBLE)
                            Handler(Looper.getMainLooper()).post(Runnable {
                                if (binding.storiesProgressView != null)
                                    binding.storiesProgressView.pause()
                            })
                            return@OnInfoListener true
                        }
                        MediaPlayer.MEDIA_INFO_AUDIO_NOT_PLAYING -> {
                            binding.progress.setVisibility(VISIBLE)
                            Handler(Looper.getMainLooper()).post(Runnable {
                                if (binding.storiesProgressView != null)
                                    binding.storiesProgressView.pause()
                            })
                            return@OnInfoListener true
                        }
                    }
                    false
                })
                videoview.start()
                Log.e("TAG", "setVideo: "+url)
                binding.progress.setVisibility(GONE)
              //  binding.storiesProgressView.setStoryDuration(mediaPlayer.duration.toLong())
                // binding.storiesProgressView.setStoryDuration(30000)
                binding.storiesProgressView.startStories(counter)
            }
        } catch (e: Exception) {
            Log.e("TAG", "setVideo2222: "+e)
        }
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
        var cnt =++counter
//        Log.e("TAG", "{$cnt ----$counter}")
//       if(cnt==1){
//           setVideo(THIS!!,binding.videoView,imagesList[cnt])
//       }else{
//           binding.videoView.stopPlayback();
//           setImageNormal(THIS!!,binding.imagePreview,imagesList[cnt])
//       }

        setImageNormal(THIS!!,binding.imagePreview,imagesList[cnt])

    }

    override fun onPrev() {
        if ((counter - 1) < 0) return;


        setImageNormal(THIS!!,binding.imagePreview,imagesList[--counter])

    }

    override fun onComplete() {
        try {
            binding.storiesProgressView.destroy()
            finish()
        } catch (e: Exception) {
            Log.e("TAG", "onDestroy: ")
        }
        Log.e("TAG", "onDestroy: ")
    }

    override fun onDestroy() {
        try {
            binding.storiesProgressView.destroy()
        } catch (e: Exception) {
            Log.e("TAG", "onDestroy: ")
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        try {
            binding.storiesProgressView.destroy();
        } catch (e: Exception) {
            Log.e("TAG", "onBackPressed: ")
        }
        super.onBackPressed()
    }

}