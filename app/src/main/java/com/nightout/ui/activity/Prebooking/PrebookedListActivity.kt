package com.nightout.ui.activity.Prebooking

import android.content.Intent
import android.os.Bundle
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
       // 1=>Pending, 2=>Completed, 3=>Cancelled
    }

    private fun pre_booking_listAPICALL() {
        customProgressDialog.show(this@PrebookedListActivity, "")
        prebooedViewModel.prebookedList().observe(this@PrebookedListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        dataList = ArrayList()
                        dataList = myData.data
                            setList()
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
    lateinit var dataList: ArrayList<PrebookedlistResponse.Data>
    private fun setList( ) {
        preBookedListAdapter =PreBookedListAdapter(THIS!!,dataList,object:PreBookedListAdapter.ClickListener{
            override fun onClickCancel(pos: Int, lostItem3Dot: ImageView) {
                        cancelAPICAll(dataList[pos].id,pos)
            }

            override fun onClick(pos: Int) {
                startActivity(Intent(THIS!!,PrebookedDetail::class.java).putExtra(AppConstant.INTENT_EXTRAS.PreBookDetail,dataList[pos]))
            }

        })

        binding.prebookedlistRecycle.also {
            it.layoutManager = LinearLayoutManager(THIS!!,LinearLayoutManager.VERTICAL,false)
            it.adapter = preBookedListAdapter
        }

    }

    private fun cancelAPICAll(bookingID: String,pos:Int) {
        var map = HashMap<String,String>()
        map["id"] = bookingID
        customProgressDialog.show(this@PrebookedListActivity, "")
        prebooedViewModel.prebookedCancel(map).observe(this@PrebookedListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        dataList[pos].status ="3"
                        preBookedListAdapter.notifyItemChanged(pos)
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

    private fun setToolbar() {

        binding.prebookedlistToolBar.toolbarBell.visibility=GONE
        binding.prebookedlistToolBar.toolbar3dot.visibility=GONE
        binding.prebookedlistToolBar.toolbarTitle.text = resources.getString(R.string.BookedVenue)
        binding.prebookedlistToolBar.toolbarBack.setOnClickListener {
            finish()
        }
    }
}