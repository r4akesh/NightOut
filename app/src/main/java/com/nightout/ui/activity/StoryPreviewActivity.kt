package com.nightout.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity

import android.content.Context
import android.net.Uri
import android.util.Log


import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.WindowManager

import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.demo.*

import java.util.*
import android.media.MediaPlayer

import android.widget.VideoView
import com.nightout.databinding.StoryPreviewActvityBinding

import com.nightout.model.DashboardModel
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper


class StoryPreviewActivity : BaseActivity(), StoriesProgressView.StoriesListener{
    //https://githubmemory.com/repo/ravishankarsingh1996/StoriesProgressView
    lateinit var binding : StoryPreviewActvityBinding

    //private val resources = intArrayOf( R.drawable.venues1,    R.drawable.venues2,    R.drawable.venues3,    R.drawable.venues4,    )

    /*private val imagesList = mutableListOf( "https://www.fillmurray.com/640/360",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://loremflickr.com/640/360", "https://www.placecage.com/640/360", "https://placekitten.com/640/360",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//1cOx5wH5VMCHnjsUuzL9lnRxCDcnOGwvHrht4YfJ.jpgooo",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//rY9JpCqmAQTlxkqpEssz808AaxA3hQ9F88GrGBHs.jpg",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//iz3XVb7QiNfRsZ4yha42vTgjegFt3DQ035SQVNWg.jpg",
    "https://nightout.ezxdemo.com/storage/uploads/store_media//8rKCEmiu5n0Fm3riTmG4l5O9JYinmJraL5YiPEvK.jpg")*/

    var pressTime = 0L
    var limit = 500L


  //  private val storiesProgressView: StoriesProgressView? = null


    // on below line we are creating a counter
    // for keeping count of our stories.
    private var counter = 0
    lateinit var liststory : ArrayList<DashboardModel.Storydetail>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this@StoryPreviewActivity, R.layout.story_preview_actvity)
          liststory = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.STORY_LIST) as ArrayList<DashboardModel.Storydetail>
        binding.storiesProgressView.setStoriesCount(liststory.size);
        binding.storiesProgressView.setStoryDuration(3000L);
        binding.storiesProgressView.setStoriesListener(this);
         binding.storiesProgressView.startStories(counter)
       // var vv=MyApp.getUrlExtention(liststory[++counter].image)
        if(MyApp.getUrlExtention(liststory[counter].image).equals(".jpg") || MyApp.getUrlExtention(liststory[counter].image).equals(".png")
            || MyApp.getUrlExtention(liststory[counter].image).equals(".JPEG")) {
            setImageNormal(THIS!!, binding.imagePreview, PreferenceKeeper.instance.imgPathSave+liststory[counter].image)
        }else{
            setVideo(THIS!!, binding.videoView,PreferenceKeeper.instance.imgPathSave+liststory[counter].image)
        }

        binding.reverse.setOnTouchListener(onTouchListener);
        binding.skip.setOnTouchListener(onTouchListener);
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
                    binding.storiesProgressView.resume()
                    Log.d("TAG", "onResourceReady: ")
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("TAG", "onLoadFailed: ")
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
        Log.d("TAG", "setVideo111: ")
        videoview?.setVideoURI(Uri.parse(url))
        try {
            videoview?.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.setOnInfoListener(MediaPlayer.OnInfoListener { mediaPlayer, i, i1 ->
                    Log.d("TAG", "onInfo: =============>>>>>>>>>>>>>>>>>>>$i")
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
                Log.d("TAG", "setVideo: "+url)
                binding.progress.setVisibility(GONE)
              //  binding.storiesProgressView.setStoryDuration(mediaPlayer.duration.toLong())
                // binding.storiesProgressView.setStoryDuration(30000)
                binding.storiesProgressView.startStories(counter)
            }
        } catch (e: Exception) {
            Log.d("TAG", "setVideo2222: "+e)
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
      //  Log.d("TAG", "{$cnt ----$counter}")

        if(MyApp.getUrlExtention(liststory[cnt].image).equals(".jpg") || MyApp.getUrlExtention(liststory[cnt].image).equals(".png")
               || MyApp.getUrlExtention(liststory[cnt].image).equals(".JPEG")) {
               binding.videoView.stopPlayback();
               setImageNormal(THIS!!, binding.imagePreview, PreferenceKeeper.instance.imgPathSave+liststory[cnt].image)
           }else{
               setVideo(THIS!!, binding.videoView,PreferenceKeeper.instance.imgPathSave+liststory[cnt].image)
           }
    }

    override fun onPrev() {
        if ((counter - 1) < 0) return;
        var cnt =--counter
        if(MyApp.getUrlExtention(liststory[cnt].image).equals(".jpg") || MyApp.getUrlExtention(liststory[cnt].image).equals(".png")
            || MyApp.getUrlExtention(liststory[cnt].image).equals(".JPEG")) {
            binding.videoView.stopPlayback();
            setImageNormal(THIS!!, binding.imagePreview, PreferenceKeeper.instance.imgPathSave+liststory[cnt].image)
        }else{
            setVideo(THIS!!, binding.videoView,PreferenceKeeper.instance.imgPathSave+liststory[cnt].image)
        }
      //  setImageNormal(THIS!!,binding.imagePreview,imagesList[--counter])

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