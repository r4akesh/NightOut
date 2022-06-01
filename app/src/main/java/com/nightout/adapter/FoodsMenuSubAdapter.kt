package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RowBarmenuSubitemBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.Utills


class FoodsMenuSubAdapter(
    var context: Context,
    var arrayList: MutableList<VenuDetailModel.Product>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<FoodsMenuSubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowBarmenuSubitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_barmenu_subitem, parent, false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.binding.drinkSubItemTitle.text = arrayList[position].title
        viewHolder.binding.drinkSubItemSubTitle.text = arrayList[position].description
        viewHolder.binding.drinkSubItemPrice.text = context.resources.getString(R.string.Price)+" : "+context.resources.getString(R.string.currency_sumbol)+arrayList[position].price
        Utills.setImageNormal(context,viewHolder.binding.drinkSubItemImg,arrayList[position].image)
        viewHolder.binding.drinkSubItemQtyVlue.setText(""+arrayList[position].quantityLocal)
        viewHolder.binding.drinkSubItemDiscunt.setText(""+arrayList[position].discount+"% off")
//        if (arrayList[position].isChekd) {
//            viewHolder.binding.drinkSubItemChk.setImageResource(R.drawable.chk_box)
//        } else {
//            viewHolder.binding.drinkSubItemChk.setImageResource(R.drawable.unchk_box)
//        }
        viewHolder.binding.drinkSubItemPlusBtn.setOnClickListener{
            clickListener.onClickPluse(position)
        }
        viewHolder.binding.drinkSubItemMinusBtn.setOnClickListener{
            clickListener.onClickMinus(position)
        }

        viewHolder.binding.drinkSubItemChk.setOnClickListener {
            clickListener.onClickChk(position)

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
        fun onClickChk(pos: Int)
        fun onClickPluse(pos: Int)
        fun onClickMinus(pos: Int)

    }


}

