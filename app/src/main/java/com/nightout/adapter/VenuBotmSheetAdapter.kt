package com.nightout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.model.DashboardModel
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.Utills

//
class VenuBotmSheetAdapter(
    var context: Context,
    var mainPos: Int,
    var arrayList: ArrayList<DashboardModel.SubRecord>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //RecyclerView.Adapter<VenuBotmSheetAdapter.ViewHolder>() {


    companion object {
        var VIEW_TYPE_ONE = 0
        var VIEW_TYPE_TWO = 1
    }

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var venuBottmShhetItemClostTime: TextView = itemView.findViewById(R.id.venuBottmShhetItemClostTime)
        var venusubitem_rating: TextView = itemView.findViewById(R.id.venusubitem_rating)
        var venuBottmShhetItem_title: TextView = itemView.findViewById(R.id.venuBottmShhetItem_title)
        var venuBottmShhetItem_subTitle: TextView = itemView.findViewById(R.id.venuBottmShhetItem_subTitle)
        var venuBottmShhetItem_img: ImageView = itemView.findViewById(R.id.venuBottmShhetItem_img)
        var venuBottmShhetItem_left: ImageView = itemView.findViewById(R.id.venuBottmShhetItem_left)
        var hsview: HorizontalScrollView = itemView.findViewById(R.id.hsview)
        var venuBottmShhetItem_right: ImageView = itemView.findViewById(R.id.venuBottmShhetItem_right)
        var venuBottmShhetItemFavBtn: ImageView = itemView.findViewById(R.id.venuBottmShhetItemFavBtn)
        var venuBottmShhetItemPrimeBtn: ImageView = itemView.findViewById(R.id.venuBottmShhetItemPrimeBtn)

        fun bind(position: Int) {
            venuBottmShhetItem_title.text = arrayList[position].store_name
            venuBottmShhetItem_subTitle.text = arrayList[position].store_address
            venuBottmShhetItemClostTime.text = "Close : "+arrayList[position].close_time
            venusubitem_rating.text = arrayList[position].rating.avg_rating
            Utills.setImageNormal(context, venuBottmShhetItem_img, arrayList[position].store_logo)

            if(arrayList[position].favrouite == "1"){
                venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_selected72)
            }else{
                venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_unselected72)
            }

            venuBottmShhetItem_left.setOnClickListener {
                hsview.scrollTo(hsview.getScrollX() as Int - 80, hsview.getScrollY() as Int)
            }
            venuBottmShhetItem_right.setOnClickListener {
                hsview.scrollTo(hsview.getScrollX() as Int + 80, hsview.getScrollY() as Int)
            }
            if(arrayList[position].is_prime == "1"){
                venuBottmShhetItemPrimeBtn.setImageResource(R.drawable.ic_prime72)
            }else{
                venuBottmShhetItemPrimeBtn.setImageResource(0)
            }
            venuBottmShhetItemFavBtn.setOnClickListener {
                clickListener.onClickFav(position)
                if(arrayList[position].favrouite.equals("1")){
                    venuBottmShhetItemFavBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce))
                    arrayList[position].favrouite = "0"
                    venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_unselected72)
                }else{
                    venuBottmShhetItemFavBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce))
                    arrayList[position].favrouite = "1"
                    venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_selected72)
                }

            }
            itemView.setOnClickListener {

                clickListener.onClick(position)

            }
        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var venuBottmShhetItem_title: TextView = itemView.findViewById(R.id.venuBottmShhetItem_title)
        var venuBottmShhetItem_subTitle: TextView = itemView.findViewById(R.id.venuBottmShhetItem_subTitle)
        var venuBottmShhetItemClostTime: TextView = itemView.findViewById(R.id.venuBottmShhetItemClostTime)
        var venusubitem_rating: TextView = itemView.findViewById(R.id.venusubitem_rating)
        var venuBottmShhetItem_img: ImageView = itemView.findViewById(R.id.venuBottmShhetItem_img)
        var venuBottmShhetItem_left: ImageView = itemView.findViewById(R.id.venuBottmShhetItem_left)
        var hsview: HorizontalScrollView = itemView.findViewById(R.id.hsview)
        var venuBottmShhetItem_right: ImageView = itemView.findViewById(R.id.venuBottmShhetItem_right)
        var venuBottmShhetItemFavBtn: ImageView = itemView.findViewById(R.id.venuBottmShhetItemFavBtn)
        var venuBottmShhetItemPrimeBtn: ImageView = itemView.findViewById(R.id.venuBottmShhetItemPrimeBtn)

        fun bind(position: Int) {
            venuBottmShhetItem_title.text = arrayList[position].store_name
            venuBottmShhetItem_subTitle.text = arrayList[position].store_address
            venuBottmShhetItemClostTime.text = "Close : "+arrayList[position].close_time
            venusubitem_rating.text = arrayList[position].rating.avg_rating

            Utills.setImageNormal(context, venuBottmShhetItem_img, arrayList[position].store_logo)
            venuBottmShhetItem_left.setOnClickListener {
                hsview.scrollTo(hsview.getScrollX() as Int - 80, hsview.getScrollY() as Int)
            }
            venuBottmShhetItem_right.setOnClickListener {
                hsview.scrollTo(hsview.getScrollX() as Int + 80, hsview.getScrollY() as Int)
            }
            if(arrayList[position].is_prime == "1"){
                venuBottmShhetItemPrimeBtn.setImageResource(R.drawable.ic_prime72)
            }else{
                venuBottmShhetItemPrimeBtn.setImageResource(0)
            }
            if(arrayList[position].favrouite == "1"){
                venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_selected72)
            }else{
                venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_unselected72)
            }

            venuBottmShhetItemFavBtn.setOnClickListener {
                clickListener.onClickFav(position)
                if(arrayList[position].favrouite.equals("1")){

                    arrayList[position].favrouite = "0"
                    venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_unselected72)
                }else{

                    arrayList[position].favrouite = "1"
                    venuBottmShhetItemFavBtn.setImageResource(R.drawable.fav_selected72)
                }
                venuBottmShhetItemFavBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce))
            }

            itemView.setOnClickListener {
                clickListener.onClick(position)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.venu_botmsheet_item, parent, false)
            )
        } else {
            View2ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.venu_botmsheet_item2, parent, false)
            )
        }
        Log.d("TAG", "onCreateViewHolder: " + viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {


        if (mainPos % 2 == 0)
            (viewHolder as View1ViewHolder).bind(position)
        else
            (viewHolder as View2ViewHolder).bind(position)
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    override fun getItemViewType(position: Int): Int {

       if (mainPos % 2 == 0) {
            return VIEW_TYPE_ONE
        } else {
            return VIEW_TYPE_TWO
        }

    }


    interface ClickListener {
        fun onClick(pos: Int)
        fun onClickFav(pos: Int)
    }


}

