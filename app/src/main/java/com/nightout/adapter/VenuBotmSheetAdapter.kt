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
import com.nightout.model.*


class VenuBotmSheetAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuBotmSheetModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuBotmSheetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuBotmsheetItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_botmsheet_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
      viewHolder.binding.venuBottmShhetItemTitle.text=arrayList[position].title
         viewHolder.binding.venuBottmShhetItemSubTitle.text=arrayList[position].subTitle



        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenuBotmsheetItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuBotmsheetItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

