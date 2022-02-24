package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.AllbarcrawalGridItemBinding
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.RowOrderitemFirstBinding
import com.nightout.databinding.VenuesItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.MyOrderRes
import com.nightout.model.VenuesModel
import com.nightout.utils.Commons
import com.nightout.utils.Utills


class MyOrderAdapter(
    var context: Context,
    var arrayList: ArrayList<MyOrderRes.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowOrderitemFirstBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_orderitem_first, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.binding.myOrderFirstDate.text= Utills.dateZonetoDateFormat2(arrayList[position].created_at)

        var myOrderSubAdapter=  MyOrderSubAdapter(context,arrayList[position].transaList,object:MyOrderSubAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })
        viewHolder.binding.myOrderFirstRecycle.also {
            it.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            it.adapter = myOrderSubAdapter
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowOrderitemFirstBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowOrderitemFirstBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

