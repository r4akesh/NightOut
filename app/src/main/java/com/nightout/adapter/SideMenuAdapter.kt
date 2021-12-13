package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.AllbarcrawalGridItemBinding
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.RowSidemenuListBinding
import com.nightout.databinding.VenuesItemBinding
import com.nightout.interfaces.OnSideMenuSelectListener
import com.nightout.model.ChatModel
import com.nightout.model.SideMenuModel
import com.nightout.model.VenuesModel


class SideMenuAdapter(
    var context: Context,
    var arrayList: MutableList<SideMenuModel>,
    var clickListener: OnSideMenuSelectListener,
) :
    RecyclerView.Adapter<SideMenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowSidemenuListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_sidemenu_list, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.sideMenuTitle.text=arrayList[position].menuTitle



        viewHolder.itemView.setOnClickListener {
            clickListener.onMenuSelect(arrayList[position].menuTitle)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowSidemenuListBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowSidemenuListBinding = itemView

    }

//    interface ClickListener {
//        fun onClick(pos: Int)
//
//
//    }


}

