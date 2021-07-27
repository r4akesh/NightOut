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
import com.nightout.databinding.GroupItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.GroupListModel


class GroupListAdapter(
    var context: Context,
    var arrayList: ArrayList<GroupListModel>,
    var isVisibleChkBox : Boolean,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GroupItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.group_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.grupItemTitle.text=arrayList[position].title
        viewHolder.binding.grupItemSubTitle.text=arrayList[position].subTitle

        viewHolder.binding.grupItemProfile.setImageResource(arrayList[position].imgProfile)

        if(isVisibleChkBox) {
            if (arrayList[position].isChk) {
                viewHolder.binding.grupItemChk.setImageResource(R.drawable.chk_box)
            } else {
                viewHolder.binding.grupItemChk.setImageResource(R.drawable.unchk_box)
            }
        }
        else{
            viewHolder.binding.grupItemChk.visibility=GONE
        }

        viewHolder.binding.grupItemChk.setOnClickListener {
            clickListener.onClickChk(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: GroupItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: GroupItemBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)


    }


}

