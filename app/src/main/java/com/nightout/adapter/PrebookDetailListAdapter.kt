package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RowBarmenuSubitemBinding
import com.nightout.model.PrebookedlistResponse
import com.nightout.utils.Utills


class PrebookDetailListAdapter(
    var context: Context,
    var arrayList: ArrayList<PrebookedlistResponse.PreBookingDetail>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<PrebookDetailListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowBarmenuSubitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_barmenu_subitem, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.binding.drinkSubItemTitle.text = arrayList[position].product_detail.title
            viewHolder.binding.drinkSubItemSubTitle.text = arrayList[position].product_detail.description
            viewHolder.binding.drinkSubItemPrice.text = context.resources.getString(R.string.Price)+" : "+context.resources.getString(R.string.currency_sumbol)+arrayList[position].product_detail.price
            Utills.setImageNormal(context,viewHolder.binding.drinkSubItemImg,arrayList[position].product_detail.image)
            //viewHolder.binding.drinkSubItemQtyVlue.setText(""+arrayList[position].quantityLocal)
            if(arrayList[position].product_detail.discount == "" || arrayList[position].product_detail.discount == "0"){
                viewHolder.binding.drinkSubItemDiscunt.visibility=GONE
            }else{
                viewHolder.binding.drinkSubItemDiscunt.visibility=VISIBLE
                viewHolder.binding.drinkSubItemDiscunt.text = ""+arrayList[position].product_detail.discount+"% off"
            }
            viewHolder.binding.drinkSubItemRelplusMinus.visibility=GONE
            viewHolder.binding.drinkSubItemQty.visibility= VISIBLE
            viewHolder.binding.drinkSubItemQty.text = "Qty : "+arrayList[position].qty

        } catch (e: Exception) {
        }


    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowBarmenuSubitemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowBarmenuSubitemBinding = itemView

    }

    interface ClickListener {
       // fun onClickChk(pos: Int)
        fun onClickPluse(pos: Int)
        fun onClickMinus(pos: Int)


    }


}

