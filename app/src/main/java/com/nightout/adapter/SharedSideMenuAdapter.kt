package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.FacilityItemBinding
import com.nightout.databinding.SharedItemBinding
import com.nightout.databinding.SharedSidemenuItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.FacilityModel
import com.nightout.model.SharedModel
import com.nightout.model.VenuDetailModel


class SharedSideMenuAdapter(
    var context: Context,
    var arrayList: ArrayList<SharedModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<SharedSideMenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SharedSidemenuItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shared_sidemenu_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.sharedItemImg.setImageResource(arrayList[position].imgProfile)



    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: SharedSidemenuItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: SharedSidemenuItemBinding = itemView

    }

    interface ClickListener {
        fun onClick3Dot(pos: Int,img:ImageView)


    }


}

