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
import com.nightout.databinding.VenuItemBinding
import com.nightout.databinding.VenusubItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.VenuListModel
import com.nightout.model.VenuModel


class VenuSubAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuListModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenusubItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venusub_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.venusubitemTitle.text=arrayList[position].title
        viewHolder.binding.venusubitemTopimg.setImageResource(arrayList[position].imgProfile)

        if(position==0){
            viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_selected)
        }else{
            viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_unselected)
        }

//        viewHolder.binding.weekCloseTime.setOnClickListener {
//            clickListener.onClickCloseTime(position)
//
//        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenusubItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenusubItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

