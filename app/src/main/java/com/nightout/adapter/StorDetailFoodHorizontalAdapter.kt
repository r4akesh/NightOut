package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.FoodItemBinding
import com.nightout.model.VenuDetailModel


class StorDetailFoodHorizontalAdapter(
    var context: Context,
    var arrayList: MutableList<VenuDetailModel.AllProduct>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<StorDetailFoodHorizontalAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FoodItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.food_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.foodItemTitle.text=arrayList[position].type
        if(arrayList[position].isSelected){
            viewHolder.binding.foodItemTitle.setTextColor(context.resources.getColor(R.color.text_yello))
            viewHolder.binding.foodItemView.visibility= VISIBLE
        }else{
            viewHolder.binding.foodItemTitle.setTextColor(context.resources.getColor(R.color.text_gray))
            viewHolder.binding.foodItemView.visibility= GONE
        }


        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: FoodItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: FoodItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

