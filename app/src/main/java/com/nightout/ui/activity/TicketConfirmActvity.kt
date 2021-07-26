package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.TicketconfirmActvityBinding

class TicketConfirmActvity : BaseActivity() {
    lateinit var  binding : TicketconfirmActvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.ticketconfirm_actvity)
        binding = DataBindingUtil.setContentView(this@TicketConfirmActvity,R.layout.ticketconfirm_actvity)
        initView()
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