package com.nightout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.RowPakgeBinding
import com.nightout.model.VenuDetailModel


class PackageAdapter(
    var context: Context,
    var arrayList: MutableList<VenuDetailModel.Record>,
    var clickListener: ClickListener,
) :
    RecyclerView.Adapter<PackageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowPakgeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_pakge, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.drinkSubItemTitle.text = arrayList[position].title
        viewHolder.binding.drinkSubItemSubTitle.text = ""+arrayList[position].description
        viewHolder.binding.drinkSubItemImgText.text = context.resources.getString(R.string.currency_sumbol) +""+arrayList[position].price
        viewHolder.binding.drinkSubItemPrice.text = ""+arrayList[position].free
        viewHolder.binding.drinkSubItemQtyVlue.setText(""+arrayList[position].quantityLocal)


        if (arrayList[position].isSelected) {
            viewHolder.binding.drinkSubItemChk.setImageResource(R.drawable.chk_box)
        } else {
            viewHolder.binding.drinkSubItemChk.setImageResource(R.drawable.unchk_box)
        }

        viewHolder.binding.drinkSubItemImgDis.setText(arrayList[position].discount+"% off")

       /* viewHolder.binding.drinkSubItemChk.setOnClickListener {
            clickListener.onClickChk(position)

        }*/
        viewHolder.binding.drinkSubItemMinusBtn.setOnClickListener {
            clickListener.onClickMinus(position)

        }
        viewHolder.binding.drinkSubItemPlusBtn.setOnClickListener {
            clickListener.onClickPlus(position)

        }
    }


    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }


    inner class ViewHolder(itemView: RowPakgeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowPakgeBinding = itemView

    }

    interface ClickListener {
        fun onClickChk(pos: Int)
        fun onClickPlus(pos: Int)
        fun onClickMinus(pos: Int)


    }


}

