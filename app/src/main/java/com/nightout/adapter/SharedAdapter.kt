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
import com.nightout.databinding.SharedItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.FacilityModel
import com.nightout.model.SharedModel
import com.nightout.model.VenuDetailModel


class SharedAdapter(
    var context: Context,
    var arrayList: ArrayList<SharedModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<SharedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SharedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shared_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.sharedItemImg.setImageResource(arrayList[position].imgProfile)
        viewHolder.binding.sharedItem3Dot.setOnClickListener {
            clickListener.onClick3Dot(position, viewHolder.binding.sharedItem3Dot)
        }


    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: SharedItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: SharedItemBinding = itemView

    }

    interface ClickListener {
        fun onClick3Dot(pos: Int,img:ImageView)


    }


}

