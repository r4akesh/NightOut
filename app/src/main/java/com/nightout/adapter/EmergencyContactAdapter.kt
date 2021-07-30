package com.nightout.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.ui.activity.EmergencyContactListActivity


class EmergencyContactAdapter(private val mContext: Context) :
    RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.row_emergency_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
         //   mContext.startActivity(Intent(mContext,EmergencyContactListActivity::class.java))
        }
    }

    override fun getItemCount(): Int = 1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}