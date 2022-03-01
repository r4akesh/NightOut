package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.*
import com.nightout.model.VenuDetailModel


class OrdrDetailSubAdapter(
    var context: Context,
    var arrayList: MutableList<VenuDetailModel.Product>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<OrdrDetailSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowOrderdetailsubItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_orderdetailsub_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.itemText.text = arrayList[position].title
        viewHolder.binding.itemPrice.text = context.resources.getString(R.string.currency_sumbol)+arrayList[position].price
        viewHolder.binding.itemQty.text = arrayList[position].quantityLocal.toString()
        if(arrayList[position].discount.equals("") || arrayList[position].discount.equals("0")){
            viewHolder.binding.itemPer.visibility=GONE
        }else{
            viewHolder.binding.itemPer.visibility= VISIBLE
            viewHolder.binding.itemPer.text = arrayList[position].discount+"% off"
        }

        viewHolder.binding.itemTotal.text = arrayList[position].totPriceLocal.toString()
        if(position==0){
            viewHolder.binding.itemLinerHead.visibility= VISIBLE
        }else{
            viewHolder.binding.itemLinerHead.visibility= GONE
        }
        if(position==arrayList.size-1){
            viewHolder.binding.itemViewBtm.visibility=GONE
        }else{
            viewHolder.binding.itemViewBtm.visibility= VISIBLE
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowOrderdetailsubItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowOrderdetailsubItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

