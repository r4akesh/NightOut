package com.nightout.ui.activity.Review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.RatingListAdapter
import com.nightout.base.BaseActivity
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
    lateinit var ratingList: ArrayList<ReviewListRes.Data>
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
                        ratingList = ArrayList()
                        ratingList = it.data
                        setList( )
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
    var posSaveForItemNotify=0
    private fun setList( ) {
        ratingListAdapter = RatingListAdapter(this@RatingListActvity,ratingList,object:RatingListAdapter.ClickListener{
            override fun onClick(pos: Int) {
                posSaveForItemNotify=pos
                startForResultRating.launch(Intent(this@RatingListActvity,RatingActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.RATING_POJO,ratingList[pos]))
            }

        })
        binding.ratingActvityListRecycle.also {
            it.layoutManager = LinearLayoutManager(this@RatingListActvity,LinearLayoutManager.VERTICAL,false)
            it.adapter = ratingListAdapter
        }

    }

    val startForResultRating= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val listSize = ratingList.size
            ratingList.removeAt(posSaveForItemNotify)
            ratingListAdapter.notifyItemRemoved(posSaveForItemNotify)
            ratingListAdapter.notifyItemRangeChanged(posSaveForItemNotify, listSize)
            if(ratingList.size==0){
                finish()
                overridePendingTransition(0,0)
            }
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