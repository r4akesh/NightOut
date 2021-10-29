package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nightout.R

import com.nightout.databinding.VenusubItemBinding
import com.nightout.model.FavListModelRes

import com.nightout.model.VenuListModel
import com.nightout.model.VenuModel
import com.nightout.utils.PreferenceKeeper


class FavVenuAdapter(
    var context: Context,
    var arrayList: ArrayList<FavListModelRes.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FavVenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenusubItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venusub_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.venusubitemTitle.text=arrayList[position].venue_detail.store_name
        viewHolder.binding.venusubitemSubTitle.text=arrayList[position].venue_detail.store_address
        viewHolder.binding.venusubitemOpenclosetime.text="Open : "+arrayList[position].venue_detail.open_time+" "+"Close : "+arrayList[position].venue_detail.close_time
        viewHolder.binding.venusubitemRating.text=arrayList[position].venue_detail.rating.avg_rating

        Glide.with(context)
            .load(PreferenceKeeper.instance.imgPathSave+arrayList[position].venue_detail.store_logo)
            .error(R.drawable.no_image)
            .into(viewHolder.binding.venusubitemTopimg)

        viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_selected)


        viewHolder.binding.venusubitemFav.setOnClickListener{
            clickListener.onClickFav(position)
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenusubItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenusubItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)
        fun onClickFav(pos: Int)


    }


}

