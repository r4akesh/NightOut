package com.nightout.ui.activity.Prebooking

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.PrebookDetailListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.PrebookdetailActivityBinding
import com.nightout.model.PrebookedlistResponse
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.Utills

class PrebookedDetail : BaseActivity() {
    lateinit var binding: PrebookdetailActivityBinding
   lateinit var prebookedDetail : PrebookedlistResponse.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(THIS!!, R.layout.prebookdetail_activity)

        setToolBar()
          prebookedDetail= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.PreBookDetail) as PrebookedlistResponse.Data
        if(prebookedDetail!=null) {
            setData()
        }
    }

    private fun setData() {
        try {
            binding.prebookVenuName.text = prebookedDetail.venue_detail.store_name
            binding.prebookDate.text = "Date: ${prebookedDetail.date}"
            binding.prebookTime.text = "Time: ${prebookedDetail.time}"
            binding.preBookEntryTimeValue.text = "${prebookedDetail.time}"
            binding.preBookedTotPersonValue.text = "${prebookedDetail.people}"
            binding.preBookEntryPaidAmountValue.text = ""+resources.getString(R.string.currency_sumbol)+prebookedDetail.amount

            Utills.setImageNormal(this@PrebookedDetail,binding.prebookProfile,prebookedDetail.venue_detail.store_logo)
            if(prebookedDetail.description.isBlank()){
                binding.preBookedReq.visibility=GONE
                binding.preBookedReqValue.visibility=GONE
            }else{
                binding.preBookedReq.visibility= VISIBLE
                binding.preBookedReqValue.visibility=VISIBLE
                binding.preBookedReqValue.text = "${prebookedDetail.description}"
            }
            if(prebookedDetail.pre_booking_detail.size>0){
                setListItem(prebookedDetail.pre_booking_detail)
            }
        } catch (e: Exception) {
            MyApp.ShowTost(this@PrebookedDetail,e.toString())
        }


    }

    lateinit var prebookDetailListAdapter: PrebookDetailListAdapter

    private fun setListItem(list: ArrayList<PrebookedlistResponse.PreBookingDetail>) {
        prebookDetailListAdapter= PrebookDetailListAdapter(this@PrebookedDetail,list,object:PrebookDetailListAdapter.ClickListener{
            override fun onClickPluse(pos: Int) {

            }

            override fun onClickMinus(pos: Int) {

            }

        })

        binding.preBookedRecycle.also {
            it.layoutManager = LinearLayoutManager(this@PrebookedDetail,LinearLayoutManager.VERTICAL,false)
            it.adapter = prebookDetailListAdapter
        }

    }

    private fun setToolBar() {
        binding.prebookeddetailToolBar.toolbarBell.visibility= View.GONE
        binding.prebookeddetailToolBar.toolbar3dot.visibility= View.GONE
        binding.prebookeddetailToolBar.toolbarTitle.text = resources.getString(R.string.VenueDetail)
        binding.prebookeddetailToolBar.toolbarBack.setOnClickListener {
            finish()
        }
    }
}