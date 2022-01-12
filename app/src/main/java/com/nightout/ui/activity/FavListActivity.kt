package com.nightout.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.FavVenuAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.FavlistActivityBinding
import com.nightout.model.FavListModelRes
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import com.pranavpandey.android.dynamic.toasts.DynamicToast

class FavListActivity : BaseActivity() {
    lateinit var binding : FavlistActivityBinding
    private var customProgressDialog = CustomProgressDialog()
    private lateinit var commonViewModel: CommonViewModel
    lateinit var favVenuAdapter: FavVenuAdapter
    lateinit var doAddBarCrawlModel : CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@FavListActivity,R.layout.favlist_activity)
        setToolBar()
        inItView()
        favouriteListAPICall()
    }

    private fun inItView() {
        commonViewModel = CommonViewModel(this@FavListActivity)
        doAddBarCrawlModel = CommonViewModel(this@FavListActivity)
    }
   lateinit var dataList : ArrayList<FavListModelRes.Data>
    private fun favouriteListAPICall() {
        customProgressDialog.show(this@FavListActivity, "")
        commonViewModel.favList().observe(this@FavListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        binding
                          dataList = ArrayList()
                        dataList.addAll(myData.data)
                        if(dataList!=null && dataList.size>0){
                            binding.favVenuesNoDataConstrent.visibility=GONE
                        }else{
                            binding.favVenuesNoDataConstrent.visibility=VISIBLE
                        }
                        setList()
                        //  binding.aboutActvityText.setText(myData.data[0].content)
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@FavListActivity,
                        it.message!!,

                    )
                }
            }
        })
    }



    private fun setToolBar() {
        binding.favListToolBar.toolbarTitle.text = resources.getString(R.string.Favourite)
        binding.favListToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.favListToolBar.toolbar3dot.visibility= View.GONE
        binding.favListToolBar.toolbarBell.visibility= View.GONE
    }

    val REQCODE_STOREDETAILACTIVITY = 1002
    var posSave=0 //for list update

    private fun setList() {
        favVenuAdapter = FavVenuAdapter(this@FavListActivity,dataList,object:FavVenuAdapter.ClickListener{
            override fun onClick(pos: Int) {
                posSave = pos
                if(dataList[pos].venue_detail.store_type == "5"){
                   startActivityForResult(
                        Intent(this@FavListActivity, EventDetailActivity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                        .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + dataList[pos].venue_detail.id)
                        .putExtra(AppConstant.INTENT_EXTRAS.FAVROUITE_VALUE,  "1")
                        .putExtra(AppConstant.INTENT_EXTRAS.StoreType,  dataList[pos].venue_detail.store_type)
                       ,REQCODE_STOREDETAILACTIVITY)


                }else {
                    startActivityForResult(
                        Intent(this@FavListActivity, StoreDetailActvity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                            .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + dataList[pos].venue_detail.id)
                            .putExtra(AppConstant.INTENT_EXTRAS.FAVROUITE_VALUE, "1")
                            .putExtra(AppConstant.INTENT_EXTRAS.StoreType, dataList[pos].venue_detail.store_type)
                        ,REQCODE_STOREDETAILACTIVITY)


                }
            }

            override fun onClickFav(pos: Int) {
               // showAlertUnFav(pos)

                add_favouriteAPICALL(pos)

            }

            override fun onClickSaveToBarcrwal(pos: Int) {
                addRemoveBarCrawlAPICall(pos)
            }

        })

        binding.favListRecycle.also{
            it.layoutManager = LinearLayoutManager(this@FavListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = favVenuAdapter
        }

    }
    private fun addRemoveBarCrawlAPICall(pos: Int) {
        //addBarCrawlStatus = if(venuDataList[pos].barcrawl == "0") "0" else "1"
        // progressDialog.show(this@VenuListActvity, "")
        var map = HashMap<String, String>()
        map["venue_id"] = dataList[pos].id
        map["vendor_id"] = dataList[pos].venue_detail.id
        map["status"] =dataList[pos].venue_detail.barcrawl
        map["store_type"] =dataList[pos].venue_detail.store_type

        doAddBarCrawlModel.doAddBarCrawl(map).observe(this@FavListActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    //   progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            /* Log.d("ok", "add_favouriteAPICALL: " + detailData.data.status)
                             if (detailData.data.status == "1") {
                                 addBarCrawlStatus = "0"
                                 binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)

                             } else {
                                 addBarCrawlStatus = "1"
                                 binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)
                             }*/
                            // MyApp.ShowTost(this@VenuListActvity,detailData.message)
                        } catch (e: Exception) {
                            // MyApp.popErrorMsg("StoreDetail",""+e.toString(),this@VenuListActvity)
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    // progressDialog.dialog.dismiss()
                    // Utills.showErrorToast(THIS!!,it.message!!)
                    //  Utills.showSnackBarOnError(binding.rootLayoutStorDetail, it.message!!, this@VenuListActvity)
                }
            }
        })

    }

    private fun showAlertUnFav(pos: Int) {
         DialogCustmYesNo.getInstance().createDialog(this@FavListActivity,resources.getString(R.string.app_name),resources.getString(R.string.areUSureUnFav), object : DialogCustmYesNo.Dialogclick{
             override fun onYES() {
                 add_favouriteAPICALL(pos)
             }

             override fun onNO() {

             }

         })
    }

    private fun add_favouriteAPICALL(pos:Int) {
        customProgressDialog.show(this@FavListActivity, "")
        var map = HashMap<String, String>()
        map["venue_id"] = dataList[pos].venue_id
        map["vendor_id"] = dataList[pos].venue_detail.user_id
        map["status"] = "0"


        commonViewModel.doFavItem(map).observe(this@FavListActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            val listSize = dataList.size
                            dataList.removeAt(pos)
                            favVenuAdapter.notifyItemRemoved(pos)
                            favVenuAdapter.notifyItemRangeChanged(pos, listSize)
                            if(dataList.size==0){
                                binding.favVenuesNoDataConstrent.visibility=VISIBLE
                            }
                             Utills.showDefaultToast(THIS!!,"Venue removed from favourite")

                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    customProgressDialog.dialog.dismiss()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQCODE_STOREDETAILACTIVITY && resultCode== Activity.RESULT_OK){
            try {
                var favValue = data?.getStringExtra("result")!!
                if(favValue == "0") {
                    val listSize = dataList.size
                    dataList.removeAt(posSave)
                    favVenuAdapter.notifyItemRemoved(posSave)
                    favVenuAdapter.notifyItemRangeChanged(posSave, listSize)

                }
            } catch (e: Exception) {
            }
        }
    }
}