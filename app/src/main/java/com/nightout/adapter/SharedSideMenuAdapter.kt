package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.SharedSidemenuItemBinding
import com.nightout.model.InvitedBarCrwlResponse
import com.nightout.utils.Utills


class SharedSideMenuAdapter(
    var context: Context,
    var arrayList: ArrayList<InvitedBarCrwlResponse.Data>,
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


        try {
            viewHolder.binding.sharedItemDate.text = arrayList[position].bar_crawl.date
            viewHolder.binding.sharedItemTitle.text = arrayList[position].bar_crawl.name
            Utills.setImageNormal(context,viewHolder.binding.sharedItemImg,arrayList[position].bar_crawl.image)
        } catch (e: Exception) {
        }


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

