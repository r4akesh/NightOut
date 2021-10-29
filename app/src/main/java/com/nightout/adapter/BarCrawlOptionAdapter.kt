package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.AllbarcrawalGridItemBinding
import com.nightout.databinding.RowBarcrwlOptionitemBinding
import com.nightout.model.BarCrwlListModel


class BarCrawlOptionAdapter(
    var context: Context,
    var arrayList: ArrayList<BarCrwlListModel.BarcrawlOption>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<BarCrawlOptionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowBarcrwlOptionitemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_barcrwl_optionitem, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
       // viewHolder.binding.rowbarcrwlTitle.text = arrayList[position].title
        viewHolder.binding.data= arrayList[position]
        if(arrayList[position].isSelected){
            viewHolder.binding.rowbarcrwlTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.chk_box,0,0,0)
        }else{
            viewHolder.binding.rowbarcrwlTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.unchk_box,0,0,0)
        }
        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowBarcrwlOptionitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowBarcrwlOptionitemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

