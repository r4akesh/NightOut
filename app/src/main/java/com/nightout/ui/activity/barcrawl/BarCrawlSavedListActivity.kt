package com.nightout.ui.activity.barcrawl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.SavedAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarssavedActivityBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.image_view_item.*

class BarCrawlSavedListActivity : BaseActivity() {

    lateinit var binding: BarssavedActivityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var getSavedListViewModel : CommonViewModel
     lateinit  var listtSaved : ArrayList<BarcrwalSavedRes.Data>
    var isFirstOpenScreen =true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSavedListActivity,R.layout.barssaved_activity)
        getSavedListViewModel = CommonViewModel(this@BarCrawlSavedListActivity)
        setToolBar()


    }

    override fun onResume() {
        super.onResume()
        if(isFirstOpenScreen){
            isFirstOpenScreen=false
            user_bar_crawl_listAPICall()
        }
        else if(PreferenceKeeper.instance.isUpdatedBarcrwalSuccesfully){
            //for update list after update the barcrwal
            PreferenceKeeper.instance.isUpdatedBarcrwalSuccesfully=false
            user_bar_crawl_listAPICall()
        }
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
                        if(listtSaved.size>0) {
                            binding.barcrwalSavedNoData.visibility= GONE
                            setListSaved()
                        }
                        else{
                            binding.barcrwalSavedNoData.visibility= VISIBLE
                        }
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    binding.barcrwalSavedNoData.visibility= VISIBLE
                    customProgressDialog.dialog.dismiss()
                  // Utills.showSnackBarOnError(binding.saveBarMainLayout, it.message!!, this@BarCrawlSavedListActivity)

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

            override fun onClick3Dot(pos: Int, imgview:ImageView) {
                openPopMenu(pos,imgview)
            }

        })

        binding.savedRecycleView.also {
            it.layoutManager = LinearLayoutManager(this@BarCrawlSavedListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = sharedAdapter
        }
    }

    private fun openPopMenu(pos: Int, imageView: ImageView) {
        val popup = PopupMenu(this@BarCrawlSavedListActivity, imageView)
        popup.menu.add("Edit")
        popup.menu.add("Delete")
        popup.setGravity(Gravity.END)
        popup.setOnMenuItemClickListener { item ->
            // item.title
            if(item.title.equals("Edit")){
                startActivity(Intent(this@BarCrawlSavedListActivity,BarcrawlListActivity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalID,listtSaved[pos].id)
                    .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_SAVEDLIST_Activity,true)
                    .putExtra(AppConstant.INTENT_EXTRAS.SAVEDLIST_Model,listtSaved[pos])
                    .putExtra(AppConstant.INTENT_EXTRAS.CITYNAME,listtSaved[pos].city)

                )
            }else{
                 delete_bar_crawlAPICall(pos)
            }
            true
        }
        popup.show()
    }
    private fun delete_bar_crawlAPICall(posList:Int) {
        customProgressDialog.show(this@BarCrawlSavedListActivity, "")
//        1- saved
//        2-shared
        var map = HashMap<String,String>()
        map["id"] = listtSaved[posList].id
        map["saved_shared"] = "1"

        getSavedListViewModel.delSharedList(map).observe(this@BarCrawlSavedListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    var listSize = listtSaved.size
                    listtSaved.removeAt(posList)
                    sharedAdapter.notifyItemRemoved(posList)
                    sharedAdapter.notifyItemRangeChanged(posList, listSize)
                  //  MyApp.popErrorMsg("",it.message!!,this@BarCrawlSavedListActivity)
                    if(listtSaved.size==0){
                        binding.barcrwalSavedNoData.visibility= VISIBLE
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    MyApp.popErrorMsg("",it.message!!,this@BarCrawlSavedListActivity)
                    //  Utills.showSnackBarOnError(binding.barcrwalSharedListMainLayoyt, it.message!!, this@BarCrawlShredListActivity)
                    // binding.barcrwalSharedListMainLayoyt.visibility= View.VISIBLE
                }
            }
        })
    }

}