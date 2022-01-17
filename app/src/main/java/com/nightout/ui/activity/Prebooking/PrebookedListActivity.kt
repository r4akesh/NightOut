package com.nightout.ui.activity.Prebooking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.PreBookedListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.PrebookedlistActvityBinding
import com.nightout.model.PrebookedlistResponse
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class PrebookedListActivity : BaseActivity() {
    lateinit var binding: PrebookedlistActvityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var prebooedViewModel : CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(THIS!!,R.layout.prebookedlist_actvity)
       setToolbar()
        prebooedViewModel = CommonViewModel(THIS!!)
        pre_booking_listAPICALL()
    }

    private fun pre_booking_listAPICALL() {
        customProgressDialog.show(this@PrebookedListActivity, "")
        prebooedViewModel.prebookedList().observe(this@PrebookedListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                            setList(myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    // Utills.showSnackBarOnError(binding.lostConstrentToolbar, it.message!!, this@LostitemActivity)

                }
            }
        })
    }

    lateinit var preBookedListAdapter: PreBookedListAdapter

    private fun setList(dataList: ArrayList<PrebookedlistResponse.Data>) {
        preBookedListAdapter =PreBookedListAdapter(THIS!!,dataList,object:PreBookedListAdapter.ClickListener{
            override fun onClickSetting(pos: Int, lostItem3Dot: ImageView) {

            }

            override fun onClick(pos: Int) {
                startActivity(Intent(THIS!!,PrebookedDetail::class.java).putExtra(AppConstant.INTENT_EXTRAS.PreBookDetail,dataList[pos].pre_booking_detail))
            }

        })

        binding.prebookedlistRecycle.also {
            it.layoutManager = LinearLayoutManager(THIS!!,LinearLayoutManager.VERTICAL,false)
            it.adapter = preBookedListAdapter
        }

    }

    private fun setToolbar() {

        binding.prebookedlistToolBar.toolbarBell.visibility=GONE
        binding.prebookedlistToolBar.toolbar3dot.visibility=GONE
        binding.prebookedlistToolBar.toolbarTitle.text = resources.getString(R.string.BookedVenue)
        binding.prebookedlistToolBar.toolbarBack.setOnClickListener {
            finish()
        }
    }
}