package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.FacilityItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.FacilityModel
import com.nightout.model.VenuDetailModel


class FacilityAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuDetailModel.VenueFacility>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FacilityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FacilityItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.facility_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.facilityItemTitle.text=arrayList[position].facility_detail.title


        if(arrayList[position].facility_detail.status.equals("1")){
            viewHolder.binding.facilityItemTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.right_white,0,0,0)
        }else{
            viewHolder.binding.facilityItemTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.cross_white,0,0,0)
        }
//        viewHolder.binding.weekCloseTime.setOnClickListener {
//            clickListener.onClickCloseTime(position)
//
//        }
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

