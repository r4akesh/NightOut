package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.DrinkSubitemBinding
import com.nightout.model.SubFoodModel


class DrinksSubAdapter(
    var context: Context,
    var arrayList: ArrayList<SubFoodModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<DrinksSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: DrinkSubitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.drink_subitem, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if (arrayList[position].imgProfile.equals(0)) {
            viewHolder.binding.drinkSubItemImg.visibility = GONE
            viewHolder.binding.drinkSubItemImgText.visibility = VISIBLE
        } else {
            viewHolder.binding.drinkSubItemImg.visibility = VISIBLE
            viewHolder.binding.drinkSubItemImgText.visibility = GONE
        }

        viewHolder.binding.drinkSubItemTitle.text = arrayList[position].title
        viewHolder.binding.drinkSubItemSubTitle.text = arrayList[position].subTitle
        viewHolder.binding.drinkSubItemPrice.text = arrayList[position].price
        viewHolder.binding.drinkSubItemImg.setImageResource(arrayList[position].imgProfile)

        if (arrayList[position].isChekd) {
            viewHolder.binding.drinkSubItemChk.setImageResource(R.drawable.chk_box)
        } else {
            viewHolder.binding.drinkSubItemChk.setImageResource(R.drawable.unchk_box)
        }

        viewHolder.binding.drinkSubItemChk.setOnClickListener {
            clickListener.onClickChk(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: DrinkSubitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: DrinkSubitemBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

