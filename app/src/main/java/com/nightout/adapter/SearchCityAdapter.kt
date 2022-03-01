package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RawCityitemBinding
import com.nightout.model.SearchCityResponse


class SearchCityAdapter(
    var context: Context,
    var arrayList: MutableList<SearchCityResponse.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<SearchCityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RawCityitemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.raw_cityitem, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.cityItemText.text=arrayList[position].title



        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RawCityitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RawCityitemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

