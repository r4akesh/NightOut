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
import com.nightout.adapter.SharedAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarshredActivityBinding
import com.nightout.model.SharedBarcrwalRes
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import java.lang.invoke.ConstantCallSite

class BarCrawlShredListActivity : BaseActivity() {

    lateinit var binding: BarshredActivityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var getSharedListViewModel : CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlShredListActivity,R.layout.barshred_activity)
        setToolBar()
        getSharedListViewModel = CommonViewModel(this@BarCrawlShredListActivity)
        bar_crawl_invitation_listAPICall()
    }

    override fun onResume() {
        super.onResume()
        if(PreferenceKeeper.instance.isUpdatedBarcrwalSuccesfully){
            //for update list after update the barcrwal
            PreferenceKeeper.instance.isUpdatedBarcrwalSuccesfully=false
            bar_crawl_invitation_listAPICall()
        }
    }
    lateinit var listtShared : ArrayList<SharedBarcrwalRes.Data>
    private fun bar_crawl_invitation_listAPICall() {
        customProgressDialog.show(this@BarCrawlShredListActivity, "")
        getSharedListViewModel.getSharedList().observe(this@BarCrawlShredListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        listtShared =  ArrayList()
                        listtShared = myData.data.reversed() as ArrayList<SharedBarcrwalRes.Data>

                        setListShared( )
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    // Utills.showSnackBarOnError(binding.lostConstrentToolbar, it.message!!, this@LostitemActivity)
                    binding.barcrwalSharedListMainLayoyt.visibility= View.VISIBLE
                }
            }
        })
    }

    private fun setToolBar() {
        binding.sharedBarCrawlToolBar.toolbarTitle.setText("Shared")
        setTouchNClick( binding.sharedBarCrawlToolBar.toolbarBack)
        binding.sharedBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.sharedBarCrawlToolBar.toolbarCreateGrop.visibility=GONE
        binding.sharedBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.sharedBarCrawlToolBar.toolbar3dot.visibility=GONE

    }

    lateinit var  sharedAdapter: SharedAdapter
    private fun setListShared( ) {

        sharedAdapter = SharedAdapter(this@BarCrawlShredListActivity,listtShared,object : SharedAdapter.ClickListener{
            override fun onClick3Dot(pos: Int, imgview : ImageView) {
                openPopMenu(imgview,pos)
            }

        })

        binding.sharedRecycle.also {
            it.layoutManager = LinearLayoutManager(this@BarCrawlShredListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = sharedAdapter
        }
    }

    private fun openPopMenu(imgview: ImageView,pos:Int) {
        val popup = PopupMenu(this@BarCrawlShredListActivity, imgview)
        popup.menu.add("Edit")
        popup.menu.add("Delete")
        popup.setGravity(Gravity.END)
        popup.setOnMenuItemClickListener { item ->
            // item.title
            if(item.title.equals("Edit")){
                 startActivity(Intent(this@BarCrawlShredListActivity,BarcrawlListActivity::class.java)
                     .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalID,listtShared[pos].id))
            }else{
                delete_bar_crawlAPICall(pos)
            }
            true
        }
        popup.show()
    }

    private fun delete_bar_crawlAPICall(posList:Int) {
        customProgressDialog.show(this@BarCrawlShredListActivity, "")
        var map = HashMap<String,String>()
        map["id"] = listtShared[posList].id

        getSharedListViewModel.delSharedList(map).observe(this@BarCrawlShredListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    var listSize = listtShared.size
                    listtShared.removeAt(posList)
                    sharedAdapter.notifyItemRemoved(posList)
                    sharedAdapter.notifyItemRangeChanged(posList, listSize)
                    MyApp.popErrorMsg("",it.message!!,this@BarCrawlShredListActivity)
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    MyApp.popErrorMsg("",it.message!!,this@BarCrawlShredListActivity)
                    //  Utills.showSnackBarOnError(binding.barcrwalSharedListMainLayoyt, it.message!!, this@BarCrawlShredListActivity)
                   // binding.barcrwalSharedListMainLayoyt.visibility= View.VISIBLE
                }
            }
        })
    }
}