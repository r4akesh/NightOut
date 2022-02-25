package com.nightout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.databinding.StoryItemBinding
import com.nightout.model.DashboardModel


import com.nightout.utils.PreferenceKeeper


class FeatureBarcrwlAdapter(
    var context: Context,
    var arrayList: ArrayList<DashboardModel.FeatureBarCrawl>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FeatureBarcrwlAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: StoryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.story_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.storyitemTitle.text = arrayList[position].name

            Glide
                .with(context)
                .load(PreferenceKeeper.instance.imgPathSave+arrayList[position].image)
                 .error(R.drawable.no_image)
                .into(viewHolder.binding.storyitemImg)
            viewHolder.itemView.setOnClickListener {
                clickListener.onClick(position)

            }
        } catch (e: Exception) {
            Log.d("ok", "$e.toString()")
        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: StoryItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: StoryItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

