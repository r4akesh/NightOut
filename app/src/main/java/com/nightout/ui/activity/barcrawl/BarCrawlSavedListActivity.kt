package com.nightout.ui.activity.barcrawl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.SavedAdapter
import com.nightout.adapter.SharedAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarshredActivityBinding
import com.nightout.databinding.BarssavedActivityBinding
import com.nightout.databinding.FragmentBarcrawlnewBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.model.SharedBarcrwalRes
import com.nightout.model.SharedModel
import com.nightout.ui.activity.BarCrawlSavedMapActivity
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class BarCrawlSavedListActivity : BaseActivity() {

    lateinit var binding: BarssavedActivityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var getSavedListViewModel : CommonViewModel
     lateinit  var listtSaved : ArrayList<BarcrwalSavedRes.Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSavedListActivity,R.layout.barssaved_activity)
        getSavedListViewModel = CommonViewModel(this@BarCrawlSavedListActivity)
        setToolBar()

        user_bar_crawl_listAPICall()
    }

    private fun user_bar_crawl_listAPICall() {
        customProgressDialog.show(this@BarCrawlSavedListActivity, "")
        var map= HashMap<String,String>()
        map["saved_shared"] = "1"
        getSavedListViewModel.getSavedList(map).observe(this@BarCrawlSavedListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        listtSaved =  ArrayList()
                       // listtSaved = myData.data.reversed() as ArrayList<BarcrwalSavedRes.Data>
                        listtSaved.addAll(myData.data)

                        setListSaved( )
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                   Utills.showSnackBarOnError(binding.saveBarMainLayout, it.message!!, this@BarCrawlSavedListActivity)

                }
            }
        })
    }



    private fun setToolBar() {
        binding.savedBarCrawlToolBar.toolbarTitle.setText("Saved")
        setTouchNClick( binding.savedBarCrawlToolBar.toolbarBack)
        binding.savedBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.savedBarCrawlToolBar.toolbarCreateGrop.visibility=GONE
        binding.savedBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.savedBarCrawlToolBar.toolbar3dot.visibility=GONE

    }

    lateinit var  sharedAdapter: SavedAdapter
    private fun setListSaved() {
        sharedAdapter = SavedAdapter(this@BarCrawlSavedListActivity,listtSaved,object : SavedAdapter.ClickListener{
            override fun onClick(pos: Int) {

                startActivity(Intent(this@BarCrawlSavedListActivity, BarCrawlSavedMapActivity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalList,listtSaved[pos]))
            }

        })

        binding.savedRecycleView.also {
            it.layoutManager = LinearLayoutManager(this@BarCrawlSavedListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = sharedAdapter
        }
    }


}