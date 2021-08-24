package com.nightout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.databinding.VenuBotmsheetItemBinding
import com.nightout.model.DashboardModel
import com.nightout.model.Food

import com.nightout.utils.PreferenceKeeper


class FoodsAdapter(
    var context: Context,
    var arrayList: ArrayList<Food>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FoodsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuBotmsheetItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_botmsheet_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.venuBottmShhetItemTitle.text=arrayList[position].store_name
            viewHolder.binding.venuBottmShhetItemSubTitle.text=arrayList[position].store_address
            Glide.with(context)
                .load(PreferenceKeeper.instance.imgPathSave+arrayList[position].store_logo)
                .error(R.drawable.no_image)
                .into(viewHolder.binding.venuBottmShhetItemImg)

            viewHolder.binding.venuBottmShhetItemLeft.setOnClickListener{
                viewHolder.binding.hsview.scrollTo(viewHolder.binding.hsview.getScrollX() as Int - 100, viewHolder.binding.hsview.getScrollY() as Int)
            }
            viewHolder.binding.venuBottmShhetItemRight.setOnClickListener{
                viewHolder.binding.hsview.scrollTo(viewHolder.binding.hsview.getScrollX() as Int + 100, viewHolder.binding.hsview.getScrollY() as Int)
            }

            viewHolder.itemView.setOnClickListener {
                clickListener.onClick(position)

            }
        } catch (e: Exception) {
            Log.d("ok", "$e.toString()")
        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenuBotmsheetItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuBotmsheetItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

