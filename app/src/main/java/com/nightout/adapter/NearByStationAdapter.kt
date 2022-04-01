package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.FacilityItemBinding
import com.nightout.model.NearByPlaceModel
import com.nightout.model.VenuDetailModel


class NearByStationAdapter(
    var context: Context,
    var arrayList: ArrayList<NearByPlaceModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<NearByStationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FacilityItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.facility_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.facilityItemTitle.text=arrayList[position].name

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: FacilityItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: FacilityItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

