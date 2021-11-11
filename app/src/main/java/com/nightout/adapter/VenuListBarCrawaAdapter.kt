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
import com.nightout.utils.PreferenceKeeper


class VenuListBarCrawaAdapter(
    var context: Context,
    var arrayList: ArrayList<AllBarCrwalListResponse.Barcrawl>,
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
        var venuesItemImage: ImageView = itemView.findViewById(R.id.venuesItemImage)
        var constrntLayoutMain: ConstraintLayout = itemView.findViewById(R.id.constrntLayoutMain)
        fun bind(position: Int) {
            val recyclerViewModel = arrayList[position]
            venuesItemTitle.text = recyclerViewModel.venue_detail.store_name
            if(recyclerViewModel.isSelected){
                constrntLayoutMain.setBackgroundResource(R.drawable.box_yelo_blnk)
            }else{
                constrntLayoutMain.setBackgroundResource(0)
            }
            Glide.with(context)
                .load(PreferenceKeeper.instance.imgPathSave + recyclerViewModel.venue_detail.store_logo)
                .error(R.drawable.no_image)
                .into(venuesItemImage)

        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var venuesItemTitle: TextView = itemView.findViewById(R.id.venuesItemTitle)
        var venuesItemImage: ImageView = itemView.findViewById(R.id.venuesItemImage)
        fun bind(position: Int) {
            val recyclerViewModel = arrayList[position]
            venuesItemTitle.text = recyclerViewModel.venue_detail.store_name
            var constrntLayoutMain: ConstraintLayout = itemView.findViewById(R.id.constrntLayoutMain)
            if(recyclerViewModel.isSelected){
                constrntLayoutMain.setBackgroundResource(R.drawable.box_yelo_blnk)
            }else{
                constrntLayoutMain.setBackgroundResource(0)
            }
            Glide.with(context)
                .load(PreferenceKeeper.instance.imgPathSave + recyclerViewModel.venue_detail.store_logo)
                .error(R.drawable.no_image)
                .into(venuesItemImage)
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

