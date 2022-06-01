package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.databinding.VenusubItemBinding
import com.nightout.model.FavListModelRes
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
        viewHolder.binding.venusubitemTitle.text = arrayList[position].venue_detail.store_name
        viewHolder.binding.venusubitemSubTitle.text = arrayList[position].venue_detail.store_address
        viewHolder.binding.venusubitemOpenclosetime.text =
            "Open : " + arrayList[position].venue_detail.open_time + " " + "Close : " + arrayList[position].venue_detail.close_time
        viewHolder.binding.venusubitemRating.text =
            arrayList[position].venue_detail.rating.avg_rating

        Glide.with(context)
            .load(PreferenceKeeper.instance.imgPathSave + arrayList[position].venue_detail.store_logo)
            .error(R.drawable.no_image)
            .into(viewHolder.binding.venusubitemTopimg)

        viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_selected)

        if (arrayList[position].venue_detail.store_type.toLowerCase() == "food" || arrayList[position].venue_detail.store_type.toLowerCase() == "event") {
            viewHolder.binding.venusubitemSaveToBarvrawl.visibility = GONE
        } else {
            viewHolder.binding.venusubitemSaveToBarvrawl.visibility = VISIBLE
        }

        if (arrayList[position].venue_detail.barcrawl == "1") {
            viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.save_fav,
                0,
                0,
                0
            )
        } else {
            viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_unseleted_barcrwl,
                0,
                0,
                0
            )
        }


        viewHolder.binding.venusubitemFav.setOnClickListener {
            viewHolder.binding.venusubitemFav.startAnimation(
                AnimationUtils.loadAnimation(
                    context!!,
                    R.anim.bounce
                )
            )
            clickListener.onClickFav(position)
        }

        viewHolder.binding.venusubitemSaveToBarvrawl.setOnClickListener {
            clickListener.onClickSaveToBarcrwal(position)
            if (arrayList[position].venue_detail.barcrawl == "1") {
                arrayList[position].venue_detail.barcrawl = "0"
                viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_unseleted_barcrwl,
                    0,
                    0,
                    0
                )
            } else {
                arrayList[position].venue_detail.barcrawl = "1"
                viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.save_fav,
                    0,
                    0,
                    0
                )
            }
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
        fun onClickSaveToBarcrwal(pos: Int)


    }


}

