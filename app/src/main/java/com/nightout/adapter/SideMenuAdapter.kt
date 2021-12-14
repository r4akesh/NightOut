package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
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
        viewHolder.binding.sideMenuTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(arrayList[position].menuIcon,0,0,0)

        if(arrayList[position].isSelected){
            viewHolder.binding.sideMenuTitle.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            if(arrayList[position].menuTitle== context.resources.getString(R.string.CMS_Pages)){
                 if (viewHolder.binding.subMenuContainer.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(viewHolder.binding.mainRowSidemenu, AutoTransition())
                    viewHolder.binding.subMenuContainer.visibility = View.VISIBLE
                }else {
                    viewHolder.binding.subMenuContainer.visibility = View.GONE
                }
            }else{
                viewHolder.binding.subMenuContainer.visibility = View.GONE
            }

        }else{
            viewHolder.binding.sideMenuTitle.setBackgroundResource(0)
            viewHolder.binding.subMenuContainer.visibility = View.GONE
        }


        viewHolder.itemView.setOnClickListener {
            for(i in 0 until arrayList.size){
                if(position == i){
                    arrayList[i].isSelected =true
                }else{
                    arrayList[i].isSelected =false
                }
            }
            clickListener.onMenuSelect(arrayList[position].menuTitle)
            notifyDataSetChanged()
        }

        viewHolder.binding.aboutUSTV.setOnClickListener{
            clickListener.onMenuSelect(context.resources.getString(R.string.About))
            notifyDataSetChanged()
        }
        viewHolder.binding.FAQTV.setOnClickListener{
            clickListener.onMenuSelect(context.resources.getString(R.string.FAQ))
            notifyDataSetChanged()
        }
        viewHolder.binding.TermTv.setOnClickListener{
            clickListener.onMenuSelect(context.resources.getString(R.string.Trems_Condition))
            notifyDataSetChanged()
        }
        viewHolder.binding.ContactUsTV.setOnClickListener{
            clickListener.onMenuSelect(context.resources.getString(R.string.Contact_Us))
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowSidemenuListBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowSidemenuListBinding = itemView

    }




}

