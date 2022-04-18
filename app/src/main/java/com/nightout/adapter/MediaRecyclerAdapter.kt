package com.nightout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.nightout.R
import com.nightout.model.VenuDetailModel



class MediaRecyclerAdapter(
    mediaObjects: MutableList<VenuDetailModel.VenueGallery>,
    requestManager: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mediaObjects: MutableList<VenuDetailModel.VenueGallery>
    private val requestManager: RequestManager
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.topimg_adapter, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as PlayerViewHolder).onBind(mediaObjects[i], requestManager)
    }

    override fun getItemCount(): Int {
        return mediaObjects.size
    }

    init {
        this.mediaObjects = mediaObjects
        this.requestManager = requestManager
    }
}