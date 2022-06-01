package com.nightout.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.nightout.R
import com.nightout.databinding.RowTermsCmsLayoutBinding
import com.nightout.model.AboutModelResponse
import kotlinx.android.synthetic.main.row_terms_cms_layout.view.*


class TermsItemAdapter(private val mContext:Context, private val list : ArrayList<AboutModelResponse.Term>):
    RecyclerView.Adapter<TermsItemAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : RowTermsCmsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.row_terms_cms_layout,parent,false)
       // val view: View = LayoutInflater.from(mContext).inflate(R.layout.row_terms_cms_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.termTitle.setText(Html.fromHtml(list[position].subject, Html.FROM_HTML_MODE_COMPACT))
                holder.binding.contextText.setText(Html.fromHtml(list[position].content, Html.FROM_HTML_MODE_COMPACT))

            } else {
                holder.binding.termTitle.setText(Html.fromHtml(list[position].subject))
                holder.binding.contextText.setText(Html.fromHtml(list[position].content))
            }
        } catch (e: Exception) {
            Log.d("TAG", "onBindViewHolder: "+e.toString())
        }


        holder.itemView.setOnClickListener {
            transitionAnim(holder.itemView.contentLayout,holder.itemView.contextText,holder.itemView.spendBtn)
        }
    }

    override fun getItemCount(): Int  = list.size

   inner class ViewHolder(itemView: RowTermsCmsLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root){
        var binding :RowTermsCmsLayoutBinding = itemView
    }

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