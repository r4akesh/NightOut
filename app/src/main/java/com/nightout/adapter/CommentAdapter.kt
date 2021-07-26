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
import com.nightout.model.ChatModel
import com.nightout.model.CommentModel


class CommentAdapter(
    var context: Context,
    var arrayList: ArrayList<CommentModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CommentItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.comment_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.commentItemTitle.text=arrayList[position].title
        viewHolder.binding.commentItemDays.text=arrayList[position].days

        viewHolder.binding.commentItemProfile.setImageResource(arrayList[position].imgProfile)


//        viewHolder.binding.weekCloseTime.setOnClickListener {
//            clickListener.onClickCloseTime(position)
//
//        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: CommentItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: CommentItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

