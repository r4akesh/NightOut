package com.nightout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.VenuItemBinding
import com.nightout.model.VenuModel

class PromotionAdapter(private val items: List<VenuModel>) : RecyclerView.Adapter<PromotionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VenuItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venu_item, parent, false
        )


        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])


    inner class ViewHolder(itemView: VenuItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: VenuItemBinding = itemView
        fun bind(item: VenuModel) {
            with(itemView) {
                binding.venuModel=item
                binding.venuItemTitle.text=item.title
               // binding.promotion_item_special_label.visibility = if (item.isSpecial) View.VISIBLE else View.GONE
                binding.executePendingBindings()
            }
        }


    }
}