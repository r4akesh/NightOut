package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.EventdetailActvityBinding

class EventDetail : BaseActivity() {

    lateinit var binding :EventdetailActvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //setContentView(R.layout.eventdetail_actvity)
        binding = DataBindingUtil.setContentView(this@EventDetail,R.layout.eventdetail_actvity)

        setTouchNClick(binding.eventDetailPlaceOrder)
        setTouchNClick(binding.eventDetailBakBtn)



    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.eventDetailPlaceOrder){
            startActivity(Intent (this@EventDetail,BookTicketActivity::class.java))
        }
        else if(v==binding.eventDetailBakBtn){
           finish()
        }
    }
}