package com.nightout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.SavedItemBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.utils.Utills


class SavedAdapter(
    var context: Context,
    var arrayList: ArrayList<BarcrwalSavedRes.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<SavedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SavedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.saved_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.sharedItemTitle.setText(arrayList[position].name)
            //plz show the date value instead of created_at
          //  var vDate = Commons.millsToDate(Commons.strToLong(arrayList[position].date))
            viewHolder.binding.sharedItemDate.text = arrayList[position].date

            Utills.setImageNormal(context, viewHolder.binding.sharedItemImg,arrayList[position].image)
            viewHolder.itemView.setOnClickListener {
                clickListener.onClick(position)
            }
            if(arrayList[position].public_private=="1"){
                viewHolder.binding.sharedItemPubPriImg.setImageResource(R.drawable.ic_public)
            }else{
                viewHolder.binding.sharedItemPubPriImg.setImageResource(R.drawable.ic_private)
            }

            viewHolder.binding.sharedItem3Dott.setOnClickListener {
                clickListener.onClick3Dot(position, viewHolder.binding.sharedItem3Dott)
            }
        } catch (e: Exception) {
            Log.d("TAG", "Exception: $e")
        }


    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: SavedItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: SavedItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)
        fun onClick3Dot(pos: Int,img:ImageView)


    }


}

