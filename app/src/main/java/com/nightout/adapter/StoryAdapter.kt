package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.StoryItemBinding
import com.nightout.databinding.VenuItemBinding
import com.nightout.databinding.VenusubItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.StoryModel
import com.nightout.model.VenuListModel
import com.nightout.model.VenuModel


class StoryAdapter(
    var context: Context,
    var arrayList: ArrayList<StoryModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: StoryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.story_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.storyitemTitle.text=arrayList[position].title
        viewHolder.binding.storyitemImg.setImageResource(arrayList[position].imgProfile)



        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

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

