package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.FacilityItemBinding
import com.nightout.databinding.SavedItemBinding
import com.nightout.databinding.SharedItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.FacilityModel
import com.nightout.model.SharedModel
import com.nightout.model.VenuDetailModel


class SavedAdapter(
    var context: Context,
    var arrayList: ArrayList<SharedModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<SavedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SavedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.saved_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.sharedItemImg.setImageResource(arrayList[position].imgProfile)
        viewHolder.binding.sharedItemDate.setText(arrayList[position].subTitle)
        viewHolder.binding.sharedItemTitle.setText(arrayList[position].title)
        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)
        }


    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: SavedItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: SavedItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

