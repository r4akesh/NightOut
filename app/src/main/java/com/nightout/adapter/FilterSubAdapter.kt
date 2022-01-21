package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.DrinkItemBinding
import com.nightout.databinding.DrinkSubitemBinding
import com.nightout.databinding.FilterSubitemBinding
import com.nightout.databinding.FoodSubitemBinding
import com.nightout.model.FillterRes
import com.nightout.model.SubFoodModel


class FilterSubAdapter(
    var context: Context,
    var arrayList: ArrayList<FillterRes.FilterOption>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FilterSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FilterSubitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.filter_subitem, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.filterSubItemTitle.text = arrayList[position].title


        if (arrayList[position].isChekd) {
            viewHolder.binding.filterSubItemChk.setImageResource(R.drawable.chk_box)
            viewHolder.binding.filterSubItemTitle.setTextColor(context.resources.getColor(R.color.text_yello))

        } else {
            viewHolder.binding.filterSubItemChk.setImageResource(R.drawable.unchk_box)
            viewHolder.binding.filterSubItemTitle.setTextColor(context.resources.getColor(R.color.white))

        }
//        if(position == arrayList.size-1){
//            viewHolder.binding.filterSubViewLine.visibility= GONE
//        }else{
//            viewHolder.binding.filterSubViewLine.visibility= VISIBLE
//        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClickChk(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: FilterSubitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: FilterSubitemBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

