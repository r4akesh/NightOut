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
import com.nightout.model.ChatModel


class ChatAdapter(
    var context: Context,
    var arrayList: ArrayList<ChatModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ChatItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.chat_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.chatItemTitle.text=arrayList[position].title

        viewHolder.binding.chatItemProfile.setImageResource(arrayList[position].imgProfile)
        if(position%3==0){
            viewHolder.binding.chatItemCount.visibility=VISIBLE
        }else{
            viewHolder.binding.chatItemCount.visibility= GONE
        }

//        viewHolder.binding.weekCloseTime.setOnClickListener {
//            clickListener.onClickCloseTime(position)
//
//        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: ChatItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ChatItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }


}

