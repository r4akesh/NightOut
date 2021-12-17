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
import com.nightout.databinding.VenuesItemBinding
import com.nightout.model.*
import com.nightout.utils.Utills


class VenuesAdapter(
    var context: Context,
    var arrayList: ArrayList<LostItemChooseVenuResponse.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuesItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venues_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


//        viewHolder.binding.venuesItemTitle.text=arrayList[position].store_name
//        viewHolder.binding.venuesItemSubTitle.text=arrayList[position].store_address
//      //  viewHolder.binding.venuesItemDistence.text=arrayList[position].store_address
//
//        Utills.setImageNormal(context,  viewHolder.binding.venuesItemImage,arrayList[position].store_logo)
//
//        if(arrayList[position].isChk){
//            viewHolder.binding.venuesItemChk.setImageResource(R.drawable.chk_box)
//        }else{
//            viewHolder.binding.venuesItemChk.setImageResource(R.drawable.unchk_box)
//        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenuesItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuesItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

