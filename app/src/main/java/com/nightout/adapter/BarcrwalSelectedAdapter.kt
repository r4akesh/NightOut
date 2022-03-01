package com.nightout.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.databinding.RowSelectedbarcrwalvenuBinding
import com.nightout.model.AllBarCrwalListResponse
import com.nightout.utils.PreferenceKeeper


class BarcrwalSelectedAdapter(
    var context: Context,
    var arrayList: ArrayList<AllBarCrwalListResponse.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<BarcrwalSelectedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowSelectedbarcrwalvenuBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_selectedbarcrwalvenu, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.commentItemTitle.text=arrayList[position].store_name
        Glide.with(context)
            .load(PreferenceKeeper.instance.imgPathSave + arrayList[position].store_logo)
            .error(R.drawable.no_image)
            .into(viewHolder.binding.commentItemProfile)

            viewHolder.binding.commentItemCloseBtn.setOnClickListener {
            clickListener.onClickClose(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowSelectedbarcrwalvenuBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowSelectedbarcrwalvenuBinding = itemView

    }

    interface ClickListener {
        fun onClickClose(pos: Int)


    }


}

