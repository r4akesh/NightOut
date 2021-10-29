package com.nightout.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.DrinkItemBinding
import com.nightout.databinding.VenuTitleBotmsheetItemBinding
import com.nightout.model.DashboardModel
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.Utills


class AllRecordAdapter(
    var context: Context,
    var arrayList: ArrayList<DashboardModel.AllRecord>,
    var clickListener: ClickListener,
) : RecyclerView.Adapter<AllRecordAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuTitleBotmsheetItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_title_botmsheet_item, parent, false
        )
        return ViewHolder(binding)
    }



    lateinit var venuBotmSheetAdapter :VenuBotmSheetAdapter
    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        androidx.core.view.ViewCompat.setNestedScrollingEnabled( viewHolder.binding.venuTitleBotmSeetSubRecyler, false)//for scroll issue botmSheet
        viewHolder.binding.venuTitleBotmShetTitle.text = arrayList[position].title


          venuBotmSheetAdapter = VenuBotmSheetAdapter(context,position, arrayList[position].sub_records, object : VenuBotmSheetAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    clickListener.onClickSub(pos, position)
                }

            override fun onClickFav(pos: Int) {
                clickListener.onClickFav(pos,position)
            }

        })
        viewHolder.binding.venuTitleBotmSeetSubRecyler.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = venuBotmSheetAdapter
        }

        viewHolder.binding.venuTitleBotmShetRightImage.setOnClickListener {
            clickListener.onClickNext(position)

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
        fun onClickNext(pos: Int)
        fun onClickSub(pos: Int, subPos: Int)
        fun onClickFav(subPos: Int, mainPos: Int)


    }


}

