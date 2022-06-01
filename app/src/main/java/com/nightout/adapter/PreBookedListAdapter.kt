package com.nightout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.LostItemBinding
import com.nightout.model.PrebookedlistResponse
import com.nightout.utils.Utills


class PreBookedListAdapter(
    var context: Context,
    var arrayList: ArrayList<PrebookedlistResponse.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<PreBookedListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LostItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lost_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.lostItemTitle.text = arrayList[position].venue_detail.store_name
        viewHolder.binding.lostItemSubTitle.text = arrayList[position].date

        Utills.setImageNormal(
            context,
            viewHolder.binding.lostItemProfile,
            arrayList[position].venue_detail.store_logo
        )
        // 1=>Pending, 2=>Completed, 3=>Cancelled
        when (arrayList[position].status) {
            "1" -> {
                viewHolder.binding.lostItemCancel.text = "Cancel"
                viewHolder.binding.lostItemCancel.visibility= VISIBLE
                viewHolder.binding.lostItemCancel.setBackgroundResource(R.drawable.box_btn_bg_yello)
                viewHolder.binding.lostItemCancel.setTextColor(context.resources.getColor(R.color.black))
            }
            "2" -> {
                viewHolder.binding.lostItemCancel.visibility= GONE
            }
            "3" -> {


                viewHolder.binding.lostItemCancel.text = "Cancelled"
                viewHolder.binding.lostItemCancel.visibility= VISIBLE
                viewHolder.binding.lostItemCancel.setBackgroundResource(R.drawable.box_bookticket)
                viewHolder.binding.lostItemCancel.setTextColor(context.resources.getColor(R.color.text_hint))
            }
        }

        viewHolder.binding.lostItemStatus.setTextColor(context.resources.getColor(R.color.white_second))
        viewHolder.binding.lostItemStatus.text = arrayList[position].time
        viewHolder.binding.lostItem3Dot.visibility = GONE

        viewHolder.itemView.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ")
            clickListener.onClick(position)

        }


        viewHolder.binding.lostItemCancel.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ")
            if(arrayList[position].status=="1") {
                clickListener.onClickCancel(position, viewHolder.binding.lostItem3Dot)
            }

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
        fun onClickCancel(pos: Int, lostItem3Dot: ImageView)
        fun onClick(pos: Int)


    }


}

