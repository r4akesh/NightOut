package com.nightout.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.nightout.R
import com.nightout.databinding.DrinkItemBinding
import com.nightout.databinding.VenuTitleBotmsheetItemBinding
import com.nightout.model.DashboardModel
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import java.lang.Math.abs


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

        setUpMemberShip(viewHolder.binding.venuTitleBotmSeetSubRecyler,context,position, arrayList[position].sub_records)
//          venuBotmSheetAdapter = VenuBotmSheetAdapter(context,position, arrayList[position].sub_records, object : VenuBotmSheetAdapter.ClickListener {
//                override fun onClick(pos: Int) {
//                    clickListener.onClickSub(pos, position)
//                }
//
//            override fun onClickFav(pos: Int) {
//                clickListener.onClickFav(pos,position)
//            }
//
//        })


/*
        viewHolder.binding.venuTitleBotmSeetSubRecyler.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = venuBotmSheetAdapter
        }*/

        viewHolder.binding.venuTitleBotmShetRightImage.setOnClickListener {
            clickListener.onClickNext(position)

        }
    }

    private fun setUpMemberShip( viewPager2: ViewPager2,context: Context,position:Int, sub_records:ArrayList<DashboardModel.SubRecord>){
        viewPager2.adapter = VenuBotmSheetAdapter(context,position,sub_records,object : VenuBotmSheetAdapter.ClickListener{
            override fun onClick(pos: Int) {
                clickListener.onClickSub(pos, position)
            }

            override fun onClickFav(pos: Int) {
                clickListener.onClickFav(pos,position)
            }

        })
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

          val compositePageTransformer = CompositePageTransformer()
          compositePageTransformer.addTransformer(MarginPageTransformer(40))

          compositePageTransformer.addTransformer { page, position ->
              val r:Float =  1 - abs(position)
              page.scaleY = 0.85f + r * 0.15f
          }

        viewPager2.setPageTransformer(compositePageTransformer)

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

