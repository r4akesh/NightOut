package com.nightout.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.ui.activity.EmergencyContactListActivity
import com.nightout.ui.activity.TaxiListActivity


class SomeTaxiListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<SomeTaxiListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.taxi_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { mContext.startActivity(Intent(mContext,TaxiListActivity::class.java)) }
    }

    override fun getItemCount(): Int = 5

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}