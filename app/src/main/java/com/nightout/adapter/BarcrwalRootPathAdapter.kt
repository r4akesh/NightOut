package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.AllbarcrawalGridItemBinding
import com.nightout.databinding.BarcrwalRootpathBinding
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.VenuesItemBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.model.ChatModel
import com.nightout.model.VenuesModel
import com.nightout.utils.Commons
import com.nightout.utils.MyApp


class BarcrwalRootPathAdapter(
    var context: Context,
    var arrayList: ArrayList<BarcrwalSavedRes.Venue>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<BarcrwalRootPathAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: BarcrwalRootpathBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.barcrwal_rootpath, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
       /* if(position==arrayList.size-1) {
            viewHolder.binding.consterntPath.visibility=GONE
            return
        }else{
            viewHolder.binding.consterntPath.visibility= VISIBLE
        }*/
        var myPos = position
        var valueInt: Int = myPos+65
        val valueAlpha1 = valueInt.toChar()
        //next value
        valueInt+=1
        var valueAlpha2 = valueInt.toChar()
     ///  var dist= MyApp.getDestance(Commons.strToDouble(arrayList[position].store_lattitude),Commons.strToDouble(arrayList[position].store_longitude),arrayList[position+1].store_lattitude,arrayList[position+1].store_longitude,)
        var dist=arrayList[position].distance
        var dur=arrayList[position].durration
        viewHolder.binding.textSource.text= "Point $valueAlpha1 to Point $valueAlpha2 ($dist) Duration : ($dur)"
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

