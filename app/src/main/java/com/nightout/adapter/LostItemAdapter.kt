package com.nightout.adapter

import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.LostItemBinding
import com.nightout.model.GetLostItemListModel
import com.nightout.model.LostItemModel
import com.nightout.utils.Utills


class LostItemAdapter(
    var context: Context,
    var arrayList: ArrayList<GetLostItemListModel.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<LostItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LostItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lost_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.lostItemTitle.text=arrayList[position].product_name
        viewHolder.binding.lostItemSubTitle.text=Utills.dateZonetoDateFormat2(arrayList[position].created_at)

        Utills.setImageNormal(context, viewHolder.binding.lostItemProfile,arrayList[position].image)
        if(arrayList[position].status.equals("0")){
            viewHolder.binding.lostItemStatus.setTextColor(context.resources.getColor(R.color.red_clr))
            viewHolder.binding.lostItemStatus.text ="Status : Not Found"
        }else{
            viewHolder.binding.lostItemStatus.setTextColor(context.resources.getColor(R.color.green_clr_light))
            viewHolder.binding.lostItemStatus.text ="Status : Found"
        }
        viewHolder.itemView.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ")
            clickListener.onClick(position)

        }

        viewHolder.binding.lostItem3Dot.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ")
            clickListener.onClickSetting(position, viewHolder.binding.lostItem3Dot)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: LostItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: LostItemBinding = itemView

    }

    interface ClickListener {
        fun onClickSetting(pos: Int, lostItem3Dot: ImageView)
        fun onClick(pos: Int)


    }


}

