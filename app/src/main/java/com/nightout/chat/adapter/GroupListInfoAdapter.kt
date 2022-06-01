package com.nightout.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.GroupItemBinding
import com.nightout.model.FSUsersModel
import com.nightout.utils.Utills


class GroupListInfoAdapter(
    var context: Context,
    var arrayList: ArrayList<FSUsersModel>,
    var isVisibleChkBox : Boolean,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<GroupListInfoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GroupItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.group_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.grupItemTitle.text=arrayList[position].name
       // viewHolder.binding.grupItemSubTitle.text=arrayList[position].userprofile.about_me

        Utills.setImageNormal(context, viewHolder.binding.grupItemProfile,arrayList[position].profile_image)


            viewHolder.binding.grupItemChk.visibility=GONE
            viewHolder.binding.grupItemSubTitle.visibility=GONE

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

