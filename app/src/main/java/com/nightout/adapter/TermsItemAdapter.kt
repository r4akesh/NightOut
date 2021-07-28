package com.nightout.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.nightout.R
import kotlinx.android.synthetic.main.row_terms_cms_layout.view.*


class TermsItemAdapter(private val mContext:Context):RecyclerView.Adapter<TermsItemAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.row_terms_cms_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.spendBtn.setOnClickListener {
            transitionAnim(holder.itemView.contentLayout,holder.itemView.contextText,holder.itemView.spendBtn)
        }
    }

    override fun getItemCount(): Int  = 4

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun transitionAnim(view: ViewGroup, mainContainerView: View, button: ImageView) {
        if (mainContainerView.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(view, AutoTransition())
            val rotate = ObjectAnimator.ofFloat(button, "rotation", 0f, 180f)
            rotate.duration = 500
            rotate.start()
            mainContainerView.visibility = View.VISIBLE
        } else {
            val rotate = ObjectAnimator.ofFloat(button, "rotation", 180f, 0f)
            rotate.duration = 500
            rotate.start()
            mainContainerView.visibility = View.GONE
        }
    }
}