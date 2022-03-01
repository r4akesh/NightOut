package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RowRatinglistBinding
import com.nightout.model.ReviewListRes
import com.nightout.utils.Utills


class RatingListAdapter(
    var context: Context,
    var arrayList: ArrayList<ReviewListRes.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<RatingListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowRatinglistBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_ratinglist, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.ratingItemTitle.text=arrayList[position].store_name
        viewHolder.binding.ratingItemSubTitle.text=context.resources.getString(R.string.currency_sumbol)+arrayList[position].amount

        Utills.setImageNormal(context,viewHolder.binding.ratingItemProfile,arrayList[position].store_logo)




        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowRatinglistBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowRatinglistBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

