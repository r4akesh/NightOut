package com.nightout.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.databinding.DrinkItemBinding
import com.nightout.databinding.RowEmergencyContactBinding
import com.nightout.databinding.RowEmergencyItemBinding
import com.nightout.model.GetEmergencyModel



class EmergencyContactAdapter(private val mContext: Context,var arrayList:ArrayList<GetEmergencyModel.Data>, var clickListener: ClickListenerr) :
    RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowEmergencyContactBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_emergency_contact, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.contactName.setText(arrayList[position].user_detail.name)
        holder.binding.contactNumber.setText(arrayList[position].user_detail.phonenumber)
        holder.binding.emergyDel.setOnClickListener {
          clickListener.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList!!.size else 0
    }

    inner class ViewHolder(itemView: RowEmergencyContactBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowEmergencyContactBinding = itemView

    }




    interface  ClickListenerr{
        fun onClick(pos:Int)

    }

}