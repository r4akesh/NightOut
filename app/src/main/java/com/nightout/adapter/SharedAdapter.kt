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
import com.nightout.model.*
import com.nightout.utils.Commons
import com.nightout.utils.MyApp
import com.nightout.utils.Utills


class SharedAdapter(
    var context: Context,
    var arrayList: ArrayList<SharedBarcrwalRes.Data>,
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


        try {
            viewHolder.binding.sharedItemTitle.text = arrayList[position].bar_crawl.name
          // var vDate= MyApp.dateZoneToDateFormat(arrayList[position].bar_crawl.created_at)
         //  var vTime= MyApp.dateZoneToTimeFormat(arrayList[position].bar_crawl.created_at)

         //   var vDate = Commons.millsToDate(Commons.strToLong(arrayList[position].da))
          //  viewHolder.binding.sharedItemDate.text = vDate


            viewHolder.binding.sharedItemDate.text = arrayList[position].bar_crawl.date
          //  viewHolder.binding.sharedItemTime.text = vTime
            Utills.setImageNormal(context, viewHolder.binding.sharedItemImg,arrayList[position].bar_crawl.image)
            viewHolder.binding.sharedItem3Dot.setOnClickListener {
                clickListener.onClick3Dot(position, viewHolder.binding.sharedItem3Dot)
            }
        } catch (e: Exception) {

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

