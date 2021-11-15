package com.nightout.adapter

import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.GroupItemBinding
import com.nightout.databinding.RowStringitemBinding
import com.nightout.model.*
import com.nightout.utils.Utills


class VenuListDetailAdapter(var context: Context, var arrayList: ArrayList<GetLostItemListModel.Venue>, var clickListener: ClickListener, ) : RecyclerView.Adapter<VenuListDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowStringitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_stringitem, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        try {
            viewHolder.binding.rowitemStringTitle.text=arrayList[position].store_name
            viewHolder.itemView.setOnClickListener {
                clickListener.onClickChk(position)

            }
        } catch (e: Exception) {
            Log.d("ok", "onBindViewHolder: "+e.toString())
        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowStringitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowStringitemBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

