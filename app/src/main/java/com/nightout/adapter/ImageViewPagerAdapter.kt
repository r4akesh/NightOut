package com.nightout.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.nightout.R
import com.nightout.databinding.ImageViewItemBinding
import com.nightout.model.VenuDetailModel
import com.nightout.ui.activity.VideoPlayActvity
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills


class ImageViewPagerAdapter(private val context: Context, imageArray: MutableList<VenuDetailModel.VenueGallery>) :
    PagerAdapter() {
    private var imageArray: MutableList<VenuDetailModel.VenueGallery> = imageArray
    var vPath=""
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ImageViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.image_view_item, container, false
        )

        binding.dVenuGalyViewModel = imageArray[position]
        if(imageArray[position].type == context.resources.getString(R.string.typeFile)){
            //0 pic ,  1 video
            Utills.setImageNormal(context,binding.imageView, imageArray[position].thumbnail)
            var vPath=PreferenceKeeper.instance.imgPathSave+imageArray[position].image

           var vv = container
            binding.imageView.visibility=GONE
            binding.videoviewInner.visibility= VISIBLE
            playVideo(vPath,binding.videoviewInner)
        }else{
            binding.imageView.visibility=VISIBLE
            binding.videoviewInner.visibility=GONE
            Utills.setImageNormal(context,binding.imageView, imageArray[position].image)
        }


         binding.postThumIv.setOnClickListener {
               vPath=PreferenceKeeper.instance.imgPathSave+imageArray[position].image
             if( vPath.isNullOrBlank()) {
                 MyApp.popErrorMsg("","Video Not Found!!",context)
             }
             else{
                 context.startActivity(Intent(context, VideoPlayActvity::class.java)
                     .putExtra(AppConstant.INTENT_EXTRAS.VIDEO_URL, vPath))

             }
          //  context.callVideo(imageArray[position].image)
        }

        container.addView(binding.root)
        return binding.root
    }

    var exoPlayer: SimpleExoPlayer? = null
    private fun playVideo(videoURL: String, videoviewInner: SimpleExoPlayerView) {
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val trackSelector: TrackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        val videouri: Uri = Uri.parse(videoURL) //for url
        //  val videouri: Uri = Uri.fromFile( File(videoURL))
        val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
        val mediaSource: MediaSource = ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null)
        videoviewInner.player = exoPlayer

        exoPlayer!!.prepare(mediaSource)
        exoPlayer!!.playbackState
        startPlayer()


    }
    public fun pausePlayer() {
        if(exoPlayer!!.isPlaying) {
            exoPlayer?.setPlayWhenReady(false)
            exoPlayer?.getPlaybackState()
        }
    }

    public fun startPlayer() {
        if(exoPlayer!!.playbackState== ExoPlayer.STATE_ENDED){
            exoPlayer!!.seekTo(0    )
        }

        exoPlayer?.setPlayWhenReady(true)
        exoPlayer?.getPlaybackState()
    }



    override fun getItemPosition(`object`: Any): Int {
        Log.d("ViewPager4", "getItemPosition: "+`object`)
        return super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageArray.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

}