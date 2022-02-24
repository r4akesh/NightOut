package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.*
import com.nightout.model.ChatModel
import com.nightout.model.MyOrderRes
import com.nightout.model.VenuesModel


class MyOrderSubAdapter(
    var context: Context,
    var arrayList: ArrayList<MyOrderRes.Transa>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<MyOrderSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowOrderitemSecondBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_orderitem_second, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.myOrderScndTitle.text=arrayList[position].venue_detail.store_name
        viewHolder.binding.myOrderScndSubTitle.text=arrayList[position].venue_detail.store_address



        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowOrderitemSecondBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowOrderitemSecondBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

