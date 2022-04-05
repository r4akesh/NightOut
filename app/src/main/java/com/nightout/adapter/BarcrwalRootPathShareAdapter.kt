package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.BarcrwalRootpathBinding
import com.nightout.model.SharedBarcrwalRes


class BarcrwalRootPathShareAdapter(
    var context: Context,
    var arrayList: ArrayList<SharedBarcrwalRes.Venue>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<BarcrwalRootPathShareAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: BarcrwalRootpathBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.barcrwal_rootpath, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

      //  var myPos = position
       // var valueInt: Int = myPos+65
        val valueAlpha1 = arrayList[position].store_name
        //next value
       // valueInt+=1
        var valueAlpha2 = arrayList[position+1].store_name
     ///  var dist= MyApp.getDestance(Commons.strToDouble(arrayList[position].store_lattitude),Commons.strToDouble(arrayList[position].store_longitude),arrayList[position+1].store_lattitude,arrayList[position+1].store_longitude,)
        var dist=arrayList[position].distance
        var dur=arrayList[position].durration
        viewHolder.binding.textSource.text= "$valueAlpha1 to $valueAlpha2 ($dist) Duration : ($dur)"
        //viewHolder.binding.textDest.text=arrayList[position].subTitle

        if(position==arrayList.size-2) {
            viewHolder.binding.imgLine.visibility = INVISIBLE
            viewHolder.binding.imgSource.setImageResource(R.drawable.pin_dest_ic)
        }
        else {
            viewHolder.binding.imgLine.visibility = VISIBLE
            viewHolder.binding.imgSource.setImageResource(R.drawable.pin_source_ic)
        }


        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size-1 else 0
    }


    inner class ViewHolder(itemView: BarcrwalRootpathBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: BarcrwalRootpathBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

