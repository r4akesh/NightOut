package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.ChatItemBinding
import com.nightout.databinding.CommentItemBinding
import com.nightout.databinding.RowSelectedbarcrwalvenuBinding
import com.nightout.model.BarCrwalVenuesModel
import com.nightout.model.ChatModel
import com.nightout.model.CommentModel


class BarcrwalSelectedAdapter(
    var context: Context,
    var arrayList: ArrayList<BarCrwalVenuesModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<BarcrwalSelectedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowSelectedbarcrwalvenuBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_selectedbarcrwalvenu, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.commentItemTitle.text=arrayList[position].title
        viewHolder.binding.commentItemProfile.setImageResource(arrayList[position].img)
    //        viewHolder.binding.weekCloseTime.setOnClickListener {
//            clickListener.onClickCloseTime(position)
//
//        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowSelectedbarcrwalvenuBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowSelectedbarcrwalvenuBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

