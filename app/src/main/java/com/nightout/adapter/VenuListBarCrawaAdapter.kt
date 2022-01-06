package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.databinding.RowVenuebarcrawalGridBinding
import com.nightout.model.AllBarCrwalListResponse
import com.nightout.model.BarCrwalVenuesModel
import com.nightout.utils.Commons
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import java.text.DecimalFormat


class VenuListBarCrawaAdapter(
    var context: Context,
    var arrayList: ArrayList<AllBarCrwalListResponse.Data>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var venuesItemTitle: TextView = itemView.findViewById(R.id.venuesItemTitle)
      //  var venuesItemRating: TextView = itemView.findViewById(R.id.venuesItemRating)
        var venuesItemDistence: TextView = itemView.findViewById(R.id.venuesItemDistence)
        var venuesItemImage: ImageView = itemView.findViewById(R.id.venuesItemImage)
        var constrntLayoutMain: ConstraintLayout = itemView.findViewById(R.id.constrntLayoutMain)
        fun bind(position: Int) {
            try {
                val recyclerViewModel = arrayList[position]
                venuesItemTitle.text = recyclerViewModel.store_name
                var crntLat = Commons.strToDouble(PreferenceKeeper.instance.currentLat!!)
                var crntLong = Commons.strToDouble(PreferenceKeeper.instance.currentLong!!)
                var eventLat = (recyclerViewModel.store_lattitude)
                var eventLong = (recyclerViewModel.store_longitude)
                var vv =MyApp.getDestance(crntLat,crntLong,eventLat,eventLong)*0.621371
                venuesItemDistence.text =""+ DecimalFormat("##.##").format(vv)+" miles"

                if(recyclerViewModel.isSelected){
                    constrntLayoutMain.setBackgroundResource(R.drawable.box_yelo_blnk)
                }else{
                    constrntLayoutMain.setBackgroundResource(0)
                }
                Glide.with(context)
                    .load(PreferenceKeeper.instance.imgPathSave + recyclerViewModel.store_logo)
                    .error(R.drawable.no_image)
                    .into(venuesItemImage)
            } catch (e: Exception) {
            }

        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var venuesItemTitle: TextView = itemView.findViewById(R.id.venuesItemTitle)
        var venuesItemImage: ImageView = itemView.findViewById(R.id.venuesItemImage)
        var venuesItemDistence: TextView = itemView.findViewById(R.id.venuesItemDistence)
        fun bind(position: Int) {
            try {
                val recyclerViewModel = arrayList[position]
                venuesItemTitle.text = recyclerViewModel.store_name
                var crntLat = Commons.strToDouble(PreferenceKeeper.instance.currentLat!!)
                var crntLong = Commons.strToDouble(PreferenceKeeper.instance.currentLong!!)
                var eventLat = (recyclerViewModel.store_lattitude)
                var eventLong = (recyclerViewModel.store_longitude)
                var vv =MyApp.getDestance(crntLat,crntLong,eventLat,eventLong)*0.621371
                venuesItemDistence.text =""+ DecimalFormat("##.##").format(vv)+" miles"
                var constrntLayoutMain: ConstraintLayout = itemView.findViewById(R.id.constrntLayoutMain)
                if(recyclerViewModel.isSelected){
                    constrntLayoutMain.setBackgroundResource(R.drawable.box_yelo_blnk)
                }else{
                    constrntLayoutMain.setBackgroundResource(0)
                }
                Glide.with(context)
                    .load(PreferenceKeeper.instance.imgPathSave + recyclerViewModel.store_logo)
                    .error(R.drawable.no_image)
                    .into(venuesItemImage)
            } catch (e: Exception) {
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        /* val binding: RowVenuebarcrawalGridBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_venuebarcrawal_grid,
            parent,
            false
        )
        return ViewHolder(binding)*/

        if (viewType == VIEW_TYPE_ONE) {
            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_grid_bigg, parent, false)
            )
        } else  {
            return View2ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_grid_smalll, parent, false)
            )
        }

    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
      /*  viewHolder.binding.venuesItemTitle.text = arrayList[position].title
        viewHolder.binding.venuesItemImage.setImageResource(arrayList[position].img)
        if (arrayList[position].isChk) {
            viewHolder.binding.constrntLayoutMain.setBackgroundResource(R.drawable.box_yelo_blnk)
        } else {
            viewHolder.binding.constrntLayoutMain.setBackgroundResource(0)
        }

        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }*/
        if (arrayList[position].viewType === VIEW_TYPE_ONE) {
            (viewHolder as View1ViewHolder).bind(position)
        } else {
            (viewHolder as View2ViewHolder).bind(position)
        }
        viewHolder.itemView.setOnClickListener {
            clickListener.onClick(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return arrayList[position].viewType
    }



    interface ClickListener {
        fun onClick(pos: Int)
    }


}

