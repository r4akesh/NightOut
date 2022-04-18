package com.nightout.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.nightout.R
import com.nightout.databinding.TopimgAdapterBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills


class TopImgVideoAdapter(
    var context: Context,
    var arrayList: MutableList<VenuDetailModel.VenueGallery>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<TopImgVideoAdapter.ViewHolder>() {
    private var exoPlayer: SimpleExoPlayer? = null
    private var playbackStateListener: PlaybackStateListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding2: TopimgAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.topimg_adapter, parent, false)
        return ViewHolder(binding2)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is LinearLayoutManager && itemCount > 0) {
            val llm = manager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    @NonNull recyclerView: RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePosition = llm.findFirstCompletelyVisibleItemPosition()
                    if (visiblePosition > -1) {
                        val v = llm.findViewByPosition(visiblePosition)
                        //do something
                        Log.d("TAG", "onScrolled: "+v)

                    }
                }
            })
        }

    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.arcLayout.visibility= View.GONE
          var playWhenReady = false
          var currentWindow = 0
          var playbackPosition: Long = 0
        var vPath= PreferenceKeeper.instance.imgPathSave+arrayList[position].image
        val uri = Uri.parse(vPath)
       // viewHolder.binding.videoviewInner.visibility= View.GONE
        if(arrayList[position].type == context.resources.getString(R.string.typeFile)){
            Log.d("onBindViewHolder2", "onBindViewHolder: "+position)
            playbackStateListener = PlaybackStateListener()
            playWhenReady = true
           // viewHolder.binding.videoviewInner.visibility = View.VISIBLE


            try {
                if (exoPlayer == null) {
                    val trackSelector = DefaultTrackSelector()
                    trackSelector.setParameters(
                        trackSelector.buildUponParameters().setMaxVideoSizeSd())
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
                }

                //viewHolder.binding.videoviewInner!!.player = exoPlayer
                val mediaSource = buildMediaSource(uri)
                exoPlayer!!.playWhenReady = playWhenReady
                exoPlayer!!.seekTo(currentWindow, playbackPosition)
                exoPlayer!!.addListener(playbackStateListener)
                exoPlayer!!.prepare(mediaSource, false, false)

            } catch (E: Exception) {
                Log.d("Video Error", E.toString())
            }

        }else{
            Log.d("onBindViewHolder1", "onBindViewHolder: "+position)
            playWhenReady = false
            viewHolder.binding.arcLayout.visibility= View.VISIBLE
            Utills.setImageNormal(context,viewHolder.binding.topImgAdpterimageView, arrayList[position].image)

            try {
                if (exoPlayer == null) {
                    val trackSelector = DefaultTrackSelector()
                    trackSelector.setParameters(
                        trackSelector.buildUponParameters().setMaxVideoSizeSd())
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
                }

              //  viewHolder.binding.videoviewInner!!.player = exoPlayer
                val mediaSource = buildMediaSource(uri)
                exoPlayer!!.playWhenReady = playWhenReady
                exoPlayer!!.seekTo(currentWindow, playbackPosition)
                exoPlayer!!.addListener(playbackStateListener)
                exoPlayer!!.prepare(mediaSource, false, false)

            } catch (E: Exception) {
                Log.d("Video Error", E.toString())
            }
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
//        viewHolder.binding.videoviewInner.setOnClickListener {
//            clickListener.onClick(position)
//        }
    }

    private class PlaybackStateListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean,
                                          playbackState: Int) {
            val stateString: String
            stateString = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }
            Log.d("PlayerActivity.TAG", "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady)
        }
    }
    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource(uri,
            DefaultDataSourceFactory(context, "Firebase"),
            DefaultExtractorsFactory(), null, null)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: TopimgAdapterBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: TopimgAdapterBinding = itemView
    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

