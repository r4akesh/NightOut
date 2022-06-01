package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.FilterMainItemBinding
import com.nightout.model.FillterRes


class FillterMainAdapter(
    var context: Context,
    var arrayList: ArrayList<FillterRes.FilterName>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FillterMainAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FilterMainItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.filter_main_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.filtermainTitel.text=arrayList[position].title

        if(arrayList[position].isSelected){
            viewHolder.binding.filtermainArrow.setImageResource(R.drawable.arrow_down_white)
            viewHolder.binding.filtermainRecycleSub.visibility= VISIBLE

        }else{
            viewHolder.binding.filtermainArrow.setImageResource(R.drawable.arrow_right_white)
            viewHolder.binding.filtermainRecycleSub.visibility= GONE
        }

        var subAdapter = FilterSubAdapter(context,arrayList[position].filter_options,object : FilterSubAdapter.ClickListener{
                override fun onClickChk(subPos: Int) {
                    clickListener.onClickSub(position,subPos)
                }

            })
            viewHolder.binding.filtermainRecycleSub.also {
                it.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                it.adapter = subAdapter
            }


        viewHolder.itemView.setOnClickListener {

            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: FilterMainItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: FilterMainItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)
        fun onClickSub(pos: Int,subPos: Int)


    }


}

