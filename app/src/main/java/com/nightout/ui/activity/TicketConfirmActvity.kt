package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.TicketconfirmActvityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import com.nightout.utils.PreferenceKeeper

class TicketConfirmActvity : BaseActivity() {
    lateinit var  binding : TicketconfirmActvityBinding
    lateinit var pojoEvntDetl : VenuDetailModel.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@TicketConfirmActvity,R.layout.ticketconfirm_actvity)
        pojoEvntDetl= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.EVENTDETAIL_POJO) as VenuDetailModel.Data
        initView()
        setData()
    }

    private fun setData() {
        try {
            binding.eventName.text = pojoEvntDetl.store_name
            binding.eventName.text = "Start at :  ${pojoEvntDetl.open_time} To ${pojoEvntDetl.close_time}"
            binding.eventDate.text =  pojoEvntDetl.event_date
            binding.eventAddrs.text =  pojoEvntDetl.store_address
            binding.eventPrice.text =  "$"+intent.getStringExtra(AppConstant.INTENT_EXTRAS.TOTAL_AMT)
            binding.eventUser.text =  PreferenceKeeper.instance.loginResponse?.name
            binding.tickeOrderID.text =  "Order ID : "+intent.getStringExtra(AppConstant.INTENT_EXTRAS.ORDER_ID)
            binding.transID.text =  "Transaction ID : "+intent.getStringExtra(AppConstant.INTENT_EXTRAS.TRANSACTION_ID)
        } catch (e: Exception) {
        }
    }

    private fun initView() {
         setTouchNClick(binding.ticketConfrmdGiveFeedback)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.ticketConfrmdGiveFeedback){
            startActivity(Intent(this@TicketConfirmActvity,CongrtulationActvity::class.java))
        }
    }
}