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
import com.nightout.model.VenuDetailModel


class FoodsMenuAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuDetailModel.CategoryFoodMdl>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FoodsMenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: DrinkItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.drink_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.drinkItemTitel.text=arrayList[position].category
        if(arrayList[position].isSelected){
            viewHolder.binding.drinkItemImageArrow.setImageResource(R.drawable.arrow_down_white)
            viewHolder.binding.drinkItemRecycleSub.visibility=VISIBLE
            var subAdapter = FoodsMenuSubAdapter(context,arrayList[position].products,object : FoodsMenuSubAdapter.ClickListener{
                override fun onClickChk(subPos: Int) {
                    clickListener.onClickSub(position,subPos)
                }
                override fun onClickPluse(subPos: Int) {
                    clickListener.onClickPluse(position,subPos)
                }

                override fun onClickMinus(subPos: Int) {
                    clickListener.onClickMinus(position,subPos)
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
        fun onClickPluse(pos: Int,subPos: Int)
        fun onClickMinus(pos: Int,subPos: Int)

    }


}

