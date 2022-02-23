package com.nightout.ui.activity

import android.os.Bundle
import android.util.Log
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
import com.nightout.model.ReviewListRes
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel


class RatingActvity : BaseActivity() {
    lateinit var binding: RatingActvityBinding
    var ratingValue = "0"
    lateinit var ratingData: ReviewListRes.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RatingActvity, R.layout.rating_actvity)
        initView()
        setToolBar()
        setDummyList()
        binding.ratingActvityStar.rating = 0.0F

        binding.ratingActvityStar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, v, b ->
                if (ratingBar.rating.toInt() == 1) {
                    binding.ratingActvityExcellent.text = "Terrible"
                    ratingValue = "1"
                } else if (ratingBar.rating.toInt() == 2) {
                    binding.ratingActvityExcellent.text = "Bad"
                    ratingValue = "2"
                } else if (ratingBar.rating.toInt() == 3) {
                    binding.ratingActvityExcellent.text = "Okay"
                    ratingValue = "3"
                } else if (ratingBar.rating.toInt() == 4) {
                    binding.ratingActvityExcellent.text = "Good"
                    ratingValue = "4"
                } else if (ratingBar.rating.toInt() == 5) {
                    binding.ratingActvityExcellent.text = "Great"
                    ratingValue = "5"
                }
            }


    }

    private fun initView() {
        ratingData = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.RATING_POJO) as ReviewListRes.Data
        binding.ratingActvityConsterntRecyle.visibility = GONE
        setTouchNClick(binding.ratingActvitySend)
        customProgressDialog = CustomProgressDialog()
        giveRatingViewModel = CommonViewModel(this@RatingActvity)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.ratingActvitySend) {
            if (isValidInput())
                addVenueReviewAPICall()
        }
    }

    private fun isValidInput(): Boolean {
        when {
            ratingValue == "0" -> {
                MyApp.popErrorMsg("", resources.getString(R.string.Please_give_rating), THIS!!)
                return false
            }
            binding.ratingActvityReviewEdit.text.toString().trim().isBlank() -> {
                MyApp.popErrorMsg("", resources.getString(R.string.Please_give_review), THIS!!)
                return false
            }
            else -> return true
        }
    }

    lateinit var customProgressDialog: CustomProgressDialog
    lateinit var giveRatingViewModel: CommonViewModel

    private fun addVenueReviewAPICall() {
        customProgressDialog.show(this@RatingActvity)
        var map = HashMap<String, String>()
        map["venue_id"] = ratingData.venue_id
        map["vendor_id"] = ratingData.vendor_id
        map["rating"] = ratingValue
        map["review"] = binding.ratingActvityReviewEdit.getText().toString()
        giveRatingViewModel.addRating(map).observe(this@RatingActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.hide()
                    it.data?.let {
                        Utills.showDefaultToast(this@RatingActvity, "Thanks for the review")
                        finish()
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    customProgressDialog.dialog.hide()
                    Utills.showDefaultToast(this@RatingActvity, it.message!!)
                }
            }
        })
    }

    lateinit var commentAdapter: CommentAdapter
    private fun setDummyList() {
        var list = ArrayList<CommentModel>()
        list.add(CommentModel("Darrell Steward", "2 dyas", R.drawable.comment1))
        list.add(CommentModel("Darrell Steward", "5 dyas", R.drawable.comment2))
        list.add(CommentModel("Darrell Steward", "6 dyas", R.drawable.comment3))
        list.add(CommentModel("Darrell Steward", "7 dyas", R.drawable.comment1))
        list.add(CommentModel("Darrell Steward", "8 dyas", R.drawable.comment2))
        list.add(CommentModel("Darrell Steward", "9 dyas", R.drawable.comment3))

        commentAdapter =
            CommentAdapter(this@RatingActvity, list, object : CommentAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    TODO("Not yet implemented")
                }

            })
        binding.ratingActvityRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@RatingActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = commentAdapter
        }
    }

    private fun setToolBar() {
        binding.RatingActvityToolBar.toolbarTitle.setText("Feedback")
        binding.RatingActvityToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.RatingActvityToolBar.toolbar3dot.visibility = GONE
        binding.RatingActvityToolBar.toolbarBell.visibility = GONE
    }
}