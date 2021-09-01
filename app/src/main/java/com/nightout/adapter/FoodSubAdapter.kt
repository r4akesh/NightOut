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
import com.nightout.databinding.FoodSubitemBinding
import com.nightout.model.DashboardModel
import com.nightout.model.SubFoodModel


class FoodSubAdapter(
    var context: Context,
    var arrayList: ArrayList<DashboardModel.SubRecord>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FoodSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FoodSubitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.food_subitem, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.foodSubItemTitle.text = arrayList[position].store_name
        viewHolder.binding.foodSubItemPrice.text = arrayList[position].price

        /*if (arrayList[position].isChekd) {
            viewHolder.binding.foodSubItemChk.setImageResource(R.drawable.chk_box)
            viewHolder.binding.foodSubItemTitle.setTextColor(context.resources.getColor(R.color.text_yello))
            viewHolder.binding.foodSubItemPrice.setTextColor(context.resources.getColor(R.color.text_yello))
        } else {
            viewHolder.binding.foodSubItemChk.setImageResource(R.drawable.unchk_box)
            viewHolder.binding.foodSubItemTitle.setTextColor(context.resources.getColor(R.color.white))
            viewHolder.binding.foodSubItemPrice.setTextColor(context.resources.getColor(R.color.white))
        }*/
        if(position == arrayList.size-1){
            viewHolder.binding.foodSubViewLine.visibility= GONE
        }else{
            viewHolder.binding.foodSubViewLine.visibility= VISIBLE
        }

        viewHolder.binding.foodSubItemChk.setOnClickListener {
            clickListener.onClickChk(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: FoodSubitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: FoodSubitemBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

