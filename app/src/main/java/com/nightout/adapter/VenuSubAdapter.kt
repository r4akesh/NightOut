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

import com.nightout.model.VenuListModel
import com.nightout.model.VenuModel
import com.nightout.utils.Commons
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills


class VenuSubAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuListModel.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenusubItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venusub_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.venusubitemTitle.text=arrayList[position].store_name
            viewHolder.binding.venusubitemSubTitle.text=arrayList[position].store_address
            viewHolder.binding.venusubitemOpenclosetime.text="Close : "+arrayList[position].close_time
            viewHolder.binding.venusubitemRating.text=arrayList[position].rating.avg_rating

            val latitude: Double = Commons.strToDouble(arrayList[position].store_lattitude)
            val longitude: Double = Commons.strToDouble(arrayList[position].store_longitude)

            viewHolder.binding.venusubitemKm.text= "${MyApp.getDestance(latitude,longitude,PreferenceKeeper.instance.currentLat!!,PreferenceKeeper.instance.currentLong!!)} Km away"

            Glide.with(context)
                .load(PreferenceKeeper.instance.imgPathSave+arrayList[position].store_logo)
                .error(R.drawable.no_image)
                .into(viewHolder.binding.venusubitemTopimg)

            if(arrayList[position].store_type.toLowerCase() == "food" || arrayList[position].store_type.toLowerCase() == "event"){
                viewHolder.binding.venusubitemSaveToBarvrawl.visibility=GONE
            }else{
                viewHolder.binding.venusubitemSaveToBarvrawl.visibility= VISIBLE
            }

            if(arrayList[position].favrouite == "1"){
                viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_selected)
            }else{
                viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_unselected)
            }

            viewHolder.binding.venusubitemFav.setOnClickListener{
                if(arrayList[position].favrouite.equals("1")){

                    arrayList[position].favrouite = "0"
                    viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_unselected)
                }else{

                    arrayList[position].favrouite = "1"
                    viewHolder.binding.venusubitemFav.setImageResource(R.drawable.fav_selected)
                }
                viewHolder.binding.venusubitemFav.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce))
                clickListener.onClickFav(position)
            }

            if(arrayList[position].barcrawl == "1"){
                viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.save_fav,0,0,0)
            }else{
                viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_unseleted_barcrwl,0,0,0)
            }
            viewHolder.binding.venusubitemSaveToBarvrawl.setOnClickListener {
                if(arrayList[position].barcrawl.equals("1")){
                    arrayList[position].barcrawl = "0"
                    viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_unseleted_barcrwl,0,0,0)
                }else{
                    arrayList[position].barcrawl = "1"
                    viewHolder.binding.venusubitemSaveToBarvrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.save_fav,0,0,0)
                }
                clickListener.onClikSaveToBarcrewal(position)
            }
            viewHolder.binding.venusubitemSubTitle.setOnClickListener {
                clickListener.onClikAddrs(position)
            }
            viewHolder.itemView.setOnClickListener {
                clickListener.onClick(position)

            }
        } catch (e: Exception) {
        }

    }

    fun filterList(filterllist: ArrayList<VenuListModel.Data>) {
        // below line is to add our filtered
        // list in our course array list.
        arrayList = filterllist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
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
        fun onClikSaveToBarcrewal(pos: Int)
        fun onClikAddrs(pos: Int)


    }


}

