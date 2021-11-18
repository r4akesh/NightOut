package com.nightout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RowPanichistryItemBinding
import com.nightout.model.PanicHistoryRes
import com.nightout.utils.MyApp


class PanicHistryAdpter(
    var context: Context,
    var arrayList: ArrayList<PanicHistoryRes.Data>,
    var clickListener: ClickListener,
) : RecyclerView.Adapter<PanicHistryAdpter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowPanichistryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_panichistry_item, parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            try {
                viewHolder.binding.panicHitryItemTitle.text = arrayList[position].user_detail.name
                viewHolder.binding.panicHitryItemSubTitle.text = arrayList[position].address
                var time = arrayList[position].created_at
                var vvT = MyApp.dateZoneToTimeFormat(time)
                viewHolder.binding.panicHitryItemTime.text = vvT
                var vvD = MyApp.dateZoneToDateFormat(time)
                viewHolder.binding.panicHitryItemDate.text = vvD
            } catch (e: Exception) {
            }




            viewHolder.itemView.setOnClickListener {
                clickListener.onClickChk(position)

            }
        } catch (e: Exception) {
            Log.d("ok", "onBindViewHolder: " + e.toString())
        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowPanichistryItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowPanichistryItemBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

