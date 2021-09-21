package com.nightout.adapter

import android.content.Context
import android.util.Log

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
import com.nightout.model.ContactFillterModel
import com.nightout.model.ContactNoModel
import com.nightout.model.GroupListModel


class ContactListAdapter(var context: Context, var arrayList: ArrayList<ContactFillterModel.Data>, var clickListener: ClickListener, ) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GroupItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.group_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        try {
            viewHolder.binding.grupItemTitle.text=arrayList[position].name
            viewHolder.binding.grupItemSubTitle.text=arrayList[position].phonenumber

            viewHolder.binding.grupItemProfile.setImageResource(R.drawable.user_default_ic)

            if (arrayList[position].isChk) {
                viewHolder.binding.grupItemChk.setImageResource(R.drawable.chk_box)
            } else {
                viewHolder.binding.grupItemChk.setImageResource(R.drawable.unchk_box)
            }



            viewHolder.itemView.setOnClickListener {
                clickListener.onClickChk(position)

            }
        } catch (e: Exception) {
            Log.d("ok", "onBindViewHolder: "+e.toString())
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

