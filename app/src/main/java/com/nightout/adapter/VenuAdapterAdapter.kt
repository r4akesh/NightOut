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
import com.nightout.model.ChatModel
import com.nightout.model.VenuModel


class VenuAdapterAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuAdapterAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.venuItemTitle.text=arrayList[position].title
        if(position==0){
            viewHolder.binding.venuItemTitle.setBackgroundResource(R.drawable.border_yello)
        }else{
            viewHolder.binding.venuItemTitle.setBackgroundResource(R.drawable.border_primaryclr)
        }

//        viewHolder.binding.weekCloseTime.setOnClickListener {
//            clickListener.onClickCloseTime(position)
//
//        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenuItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

