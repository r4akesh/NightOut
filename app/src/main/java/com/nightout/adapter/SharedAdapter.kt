package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.SharedItemBinding
import com.nightout.model.SharedBarcrwalRes
import com.nightout.utils.Utills


class SharedAdapter(
    var context: Context,
    var arrayList: ArrayList<SharedBarcrwalRes.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<SharedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SharedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shared_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.sharedItemTitle.text = arrayList[position].bar_crawl.name
            if(arrayList[position].bar_crawl.public_private=="1"){
                viewHolder.binding.sharedItemPubPriImg.setImageResource(R.drawable.ic_public)
            }else{
                viewHolder.binding.sharedItemPubPriImg.setImageResource(R.drawable.ic_private)
            }


            viewHolder.binding.sharedItemDate.text = arrayList[position].bar_crawl.date
          //  viewHolder.binding.sharedItemTime.text = vTime
            Utills.setImageNormal(context, viewHolder.binding.sharedItemImg,arrayList[position].bar_crawl.image)
            viewHolder.binding.sharedItem3Dot.setOnClickListener {
                clickListener.onClick3Dot(position, viewHolder.binding.sharedItem3Dot)
            }
            viewHolder.itemView.setOnClickListener {
                clickListener.onClick(position)
            }
        } catch (e: Exception) {

        }


    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: SharedItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: SharedItemBinding = itemView

    }

    interface ClickListener {
        fun onClick3Dot(pos: Int,img:ImageView)
        fun onClick(pos: Int)


    }


}

