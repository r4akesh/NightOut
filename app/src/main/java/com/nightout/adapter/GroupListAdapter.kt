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
import com.nightout.model.AllUserRes
import com.nightout.model.ChatModel
import com.nightout.model.GetLostItemListModel
import com.nightout.model.GroupListModel
import com.nightout.utils.Utills


class GroupListAdapter(
    var context: Context,
    var arrayList: ArrayList<AllUserRes.Data>,
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


        viewHolder.binding.grupItemTitle.text=arrayList[position].name
        viewHolder.binding.grupItemSubTitle.text=arrayList[position].userprofile.about_me

        Utills.setImageNormal(context, viewHolder.binding.grupItemProfile,arrayList[position].profile)

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
        if(position==arrayList.size-1){
            viewHolder.binding.grupItemViewLine.visibility= GONE
        }else{
            viewHolder.binding.grupItemViewLine.visibility= VISIBLE
        }

        viewHolder.itemView.setOnClickListener {
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

