package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.VenuTitleBotmsheetItemBinding
import com.nightout.model.VenuBotmSheetTitleModel


class VenuTitleBotmSheetAdapter(
    var context: Context,
    var arrayList: ArrayList<VenuBotmSheetTitleModel>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<VenuTitleBotmSheetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuTitleBotmsheetItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_title_botmsheet_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.venuTitleBotmShetTitle.text=arrayList[position].venuName

    /*    var venuBotmSheetAdapter =  VenuBotmSheetAdapter(context,arrayList[position].list,object : VenuBotmSheetAdapter.ClickListener{
            override fun onClick(pos: Int) {
                clickListener.onWholeClickdd(pos,position)
            }

        })

        viewHolder.binding.venuTitleBotmSeetSubRecyler.also {
            it.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            it.adapter= venuBotmSheetAdapter
        }
*/
        viewHolder.binding.venuTitleBotmShetRightImage.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: VenuTitleBotmsheetItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuTitleBotmsheetItemBinding = itemView

    }

    interface ClickListener {
        fun onClick(pos: Int)
        fun onWholeClickdd(subPos: Int,mainPos : Int)


    }


}

