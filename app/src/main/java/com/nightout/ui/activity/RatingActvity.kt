package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.CommentAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.RatingActvityBinding
import com.nightout.model.CommentModel
import android.widget.Toast

import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener


class RatingActvity : BaseActivity() {
    lateinit var binding : RatingActvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RatingActvity,R.layout.rating_actvity)
        setToolBar()
        setDummyList()
        binding.ratingActvityStar.rating = 0.0F

        binding.ratingActvityStar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, v, b ->
                if(ratingBar.rating.toInt() ==1){
                    binding.ratingActvityExcellent.text = "Terrible"
                }
                else if(ratingBar.rating.toInt() ==2){
                    binding.ratingActvityExcellent.text = "Bad"
                }
                else if(ratingBar.rating.toInt() ==3){
                    binding.ratingActvityExcellent.text = "Okay"
                }
                else if(ratingBar.rating.toInt() ==4){
                    binding.ratingActvityExcellent.text = "Good"
                }
                else if(ratingBar.rating.toInt() ==5){
                    binding.ratingActvityExcellent.text = "Great"
                }
            }



    }

    lateinit var  commentAdapter : CommentAdapter
    private fun setDummyList() {
        var list = ArrayList<CommentModel>()
        list.add(CommentModel("Darrell Steward","2 dyas", R.drawable.comment1))
        list.add(CommentModel("Darrell Steward","5 dyas", R.drawable.comment2))
        list.add(CommentModel("Darrell Steward","6 dyas", R.drawable.comment3))
        list.add(CommentModel("Darrell Steward","7 dyas", R.drawable.comment1))
        list.add(CommentModel("Darrell Steward","8 dyas", R.drawable.comment2))
        list.add(CommentModel("Darrell Steward","9 dyas", R.drawable.comment3))

        commentAdapter = CommentAdapter(this@RatingActvity,list , object : CommentAdapter.ClickListener{
            override fun onClick(pos: Int) {
                TODO("Not yet implemented")
            }

        })
        binding.ratingActvityRecycle.also {
            it.layoutManager = LinearLayoutManager(this@RatingActvity,LinearLayoutManager.VERTICAL,false)
            it.adapter = commentAdapter
        }
    }

    private fun setToolBar() {
        binding.RatingActvityToolBar.toolbarTitle.setText("Feedback")
        binding.RatingActvityToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.RatingActvityToolBar.toolbar3dot.visibility=GONE
        binding.RatingActvityToolBar.toolbarBell.visibility=GONE
    }
}