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
import com.nightout.databinding.RowOrderitemSecondBinding
import com.nightout.model.MyOrderRes
import com.nightout.utils.Utills


class MyOrderSubAdapter(
    var context: Context,
    var arrayList: ArrayList<MyOrderRes.Transa>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<MyOrderSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowOrderitemSecondBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_orderitem_second, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.myOrderScndTitle.text=arrayList[position].venue_detail.store_name
        viewHolder.binding.myOrderScndSubTitle.text="Table# "+arrayList[position].table_number
        viewHolder.binding.myOrderScndSubAddrs.text=arrayList[position].venue_detail.store_address
        Utills.setImageNormal(context,viewHolder.binding.myOrderScndImage,arrayList[position].venue_detail.store_logo)

        viewHolder.binding.myOrderSecondServiceValue.text = context.resources.getString(R.string.currency_sumbol)+arrayList[position].service_charge
        viewHolder.binding.myOrderSecondTotAmtValue.text = context.resources.getString(R.string.currency_sumbol)+arrayList[position].amount

        if(arrayList[position].isSelected){
            viewHolder.binding.myOrderSecondRecycle.visibility = VISIBLE
            viewHolder.binding.myOrderScndImageArrow.setImageResource(R.drawable.arrow_down_white)
            viewHolder.binding.myOrderconstrentService.visibility= VISIBLE
        }else{
            viewHolder.binding.myOrderSecondRecycle.visibility = GONE
            viewHolder.binding.myOrderScndImageArrow.setImageResource(R.drawable.arrow_right_21)
            viewHolder.binding.myOrderconstrentService.visibility= GONE
        }

        viewHolder.binding.myOrderConstrentArrow.setOnClickListener{
            arrayList[position].isSelected = !arrayList[position].isSelected
            notifyItemChanged(position)
        }

        var myOrderSubChildAdapter =MyOrderSubChildAdapter(context,arrayList[position].order_list,object:MyOrderSubChildAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })
        viewHolder.binding.myOrderSecondRecycle.also {
            it.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            it.adapter = myOrderSubChildAdapter
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowOrderitemSecondBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowOrderitemSecondBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

