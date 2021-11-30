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
import com.nightout.databinding.RowNotificationLayoutBinding
import com.nightout.model.*
import com.nightout.utils.Commons
import com.nightout.utils.MyApp
import com.nightout.utils.Utills


class NotificationAdpter(var context: Context, var arrayList: ArrayList<NotificationResponse.Data>, var clickListener: ClickListener, ) : RecyclerView.Adapter<NotificationAdpter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowNotificationLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_notification_layout, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.title.text=arrayList[position].subject
            viewHolder.binding.subTitle.text= arrayList[position].message
            var time = arrayList[position].created_at
           var vTime=  MyApp.dateZoneToTimeFormat(time)
            viewHolder.binding.timeValue.text= vTime
            vTime=  MyApp.dateZoneToDateFormat(time)
            viewHolder.binding.dateValue.text= vTime





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


    inner class ViewHolder(itemView: RowNotificationLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowNotificationLayoutBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

