package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.GrupchatimgItemBinding
import com.nightout.model.GroupChatImgModel
import com.nightout.utils.Utills


class GroupChatImageAdapter(
    var context: Context,
    var arrayList: ArrayList<String>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<GroupChatImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GrupchatimgItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.grupchatimg_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Utills.setImageFullPath(context,viewHolder.binding.grupchatimgitemImage,arrayList[position])
        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: GrupchatimgItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: GrupchatimgItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

