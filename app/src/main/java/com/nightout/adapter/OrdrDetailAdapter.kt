package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RoworderdetailItemBinding
import com.nightout.model.LocalStreModel


class OrdrDetailAdapter(
    var context: Context,
    var arrayList: MutableList<LocalStreModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<OrdrDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RoworderdetailItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.roworderdetail_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.venuTitleBotmShetTitle.text = arrayList[position].title


        var ordrDetailSubAdapter = OrdrDetailSubAdapter(context, arrayList[position].prodct, object : OrdrDetailSubAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }
        })
        viewHolder.binding.venuTitleBotmSeetSubRecyler.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = ordrDetailSubAdapter
        }
        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RoworderdetailItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RoworderdetailItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

