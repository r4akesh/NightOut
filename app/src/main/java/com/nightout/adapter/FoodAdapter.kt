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
import com.nightout.databinding.DrinkItemBinding
import com.nightout.model.FoodStoreModel
import com.nightout.model.StoreDetailDrinksModel
import com.nightout.model.SubFoodModel


class FoodAdapter(
    var context: Context,
    var arrayList: ArrayList<FoodStoreModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: DrinkItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.drink_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.drinkItemTitel.text=arrayList[position].title
        if(arrayList[position].isSelected){
            viewHolder.binding.drinkItemImageArrow.setImageResource(R.drawable.arrow_down_white)
            viewHolder.binding.drinkItemRecycleSub.visibility=VISIBLE
            var subAdapter = FoodSubAdapter(context,arrayList[position].list,object : FoodSubAdapter.ClickListener{
                override fun onClickChk(subPos: Int) {
                    clickListener.onClickSub(position,subPos)
                }

            })
            viewHolder.binding.drinkItemRecycleSub.also {
                it.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                it.adapter = subAdapter
            }
        }else{
            viewHolder.binding.drinkItemRecycleSub.visibility=GONE
            viewHolder.binding.drinkItemImageArrow.setImageResource(R.drawable.arrow_right_white)
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: DrinkItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: DrinkItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)
        fun onClickSub(pos: Int,subPos: Int)


    }


}

