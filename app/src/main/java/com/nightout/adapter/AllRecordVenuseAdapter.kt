package com.nightout.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.nightout.R
import com.nightout.databinding.VenuTitleItemBinding
import com.nightout.model.LostItemChooseVenuResponse


class AllRecordVenuseAdapter(
    var context: Context,
    var arrayList: ArrayList<LostItemChooseVenuResponse.AllRecord>,
    var clickListener: ClickListener,
) : RecyclerView.Adapter<AllRecordVenuseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuTitleItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_title_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if(position==0) {
            viewHolder.binding.venuTitleBotmShetTitle.visibility= View.GONE
            viewHolder.binding.venuTitleBotmShetTitle.text = arrayList[position].title
        }
        else {
            viewHolder.binding.venuTitleBotmShetTitle.visibility= View.VISIBLE
            viewHolder.binding.venuTitleBotmShetTitle.text = arrayList[position].title
        }




       var venuBotmSheetAdapter = VenuesAdapter(context, arrayList[position].records, object : VenuesAdapter.ClickListener {
                override fun onClick(subPos: Int) {
                    clickListener.onClickSub(subPos, position)
                    if(arrayList[position].records[subPos].isChk){
                        arrayList[position].records[subPos].isChk=false
                    }else{
                        arrayList[position].records[subPos].isChk=true
                    }
                    notifyDataSetChanged()
                }

            })
        (viewHolder.binding?.venuTitleBotmSeetSubRecyler.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false//stop animation
        viewHolder.binding.venuTitleBotmSeetSubRecyler.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = venuBotmSheetAdapter
        }


    }


    override fun getItemCount(): Int {
         return if (null != arrayList) arrayList!!.size else 0
//        if(arrayList!=null && arrayList.size>=3)
//        return  3
//        else return arrayList.size
    }


    inner class ViewHolder(itemView: VenuTitleItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuTitleItemBinding = itemView

    }

    interface ClickListener {
        fun onClickNext(pos: Int)
        fun onClickSub(subpos: Int, Pos: Int)


    }


}

