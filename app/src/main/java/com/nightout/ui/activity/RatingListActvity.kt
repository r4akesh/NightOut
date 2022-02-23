package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.ProgressBar
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
import com.nightout.adapter.RatingListAdapter
import com.nightout.databinding.RatinglistActvityBinding
import com.nightout.model.ReviewListRes
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel


class RatingListActvity : BaseActivity() {
    lateinit var binding : RatinglistActvityBinding
    lateinit var customProgressDialog: CustomProgressDialog
    lateinit var ratingListViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RatingListActvity,R.layout.ratinglist_actvity)
        ratingListViewModel = CommonViewModel(this@RatingListActvity)
        customProgressDialog = CustomProgressDialog()
        setToolBar()
        getRatingListAPICAll()


    }

    private fun getRatingListAPICAll() {
        customProgressDialog.show(this@RatingListActvity)

        ratingListViewModel.ratingList().observe(this@RatingListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.hide()
                    it.data?.let {
                        setList(it.data)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {

                }
            }
        })
    }
    lateinit var ratingListAdapter: RatingListAdapter

    private fun setList(dataList: ArrayList<ReviewListRes.Data>) {
        ratingListAdapter = RatingListAdapter(this@RatingListActvity,dataList,object:RatingListAdapter.ClickListener{
            override fun onClick(pos: Int) {
                    startActivity(Intent(this@RatingListActvity,RatingActvity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.RATING_POJO,dataList[pos]))
            }

        })
        binding.ratingActvityListRecycle.also {
            it.layoutManager = LinearLayoutManager(this@RatingListActvity,LinearLayoutManager.VERTICAL,false)
            it.adapter = ratingListAdapter
        }

    }



    private fun setToolBar() {
        binding.RatingActvityToolBar.toolbarTitle.setText("Rating")
        binding.RatingActvityToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.RatingActvityToolBar.toolbar3dot.visibility=GONE
        binding.RatingActvityToolBar.toolbarBell.visibility=GONE
    }
}