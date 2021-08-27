package com.nightout.adapter

import android.R.attr
import android.content.Context

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.RecyclerView
import com.nightout.R

import com.nightout.databinding.VenuItemBinding
import com.nightout.model.ChatModel
import com.nightout.model.VenuModel
import com.nightout.vendor.viewmodel.VenuListViewModel
import android.R.attr.data
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable


class VenuAdapterAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuAdapterAdapter.ViewHolder>(MyDiffUtilCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: "+position)

        viewHolder.binding.venuItemTitle.text=arrayList[position].title

        if(arrayList[position].isSelected){
            viewHolder.binding.venuItemTitle.setBackgroundResource(R.drawable.border_yello)
            viewHolder.binding.venuItemTitle.setTextColor(context.resources.getColor(R.color.text_yello))
        }else{
            viewHolder.binding.venuItemTitle.setBackgroundResource(R.drawable.border_primaryclr)
            viewHolder.binding.venuItemTitle.setTextColor(context.resources.getColor(R.color.white))
        }



        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenuItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)


    }

    fun getData(): ArrayList<VenuModel> {
        return arrayList
    }

    fun setData(newData: ArrayList<VenuModel>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffUtilCallBack(newData, arrayList))
        //arrayList.clear()
       // arrayList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    class MyDiffUtilCallBack(newList: ArrayList<VenuModel>?, oldList: ArrayList<VenuModel>?) : DiffUtil.Callback() {
        var newList: ArrayList<VenuModel>? = newList
        var oldList: ArrayList<VenuModel>? = oldList

        override fun getOldListSize(): Int {
            return if (oldList != null) oldList!!.size else 0
        }

        override fun getNewListSize(): Int {
            return if (newList != null) newList!!.size else 0
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList!![newItemPosition].id == oldList!![oldItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            var oldEmployee = oldList?.get(oldItemPosition)
            var newEmployee = newList?.get(newItemPosition)

            return oldEmployee?.title.equals(newEmployee?.title)
        }

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            /*val newModel: VenuModel = newList!![newItemPosition]
            val oldModel: VenuModel = oldList!![oldItemPosition]
            val diff = Bundle()
            if (newModel.price !== oldModel.price) {
                diff.putInt("price", newModel.price)
            }
            return if (diff.size() == 0) {
                null
            } else diff*/
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }


    }
}

