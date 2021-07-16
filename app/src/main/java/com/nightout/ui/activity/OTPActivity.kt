package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dpizarro.pinview.library.PinView
import com.dpizarro.pinview.library.PinViewSettings
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.OtpActvityBinding

class OTPActivity : BaseActivity() {

    lateinit var binding : OtpActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OTPActivity,R.layout.otp_actvity)
        setTouchNClick(binding.submitBtn)
        binding.otpPinView.setMaskPassword(true)
        binding.otpPinView.split = "***"
        binding.otpPinView.setOnCompleteListener(PinView.OnCompleteListener { completed, pinResults -> //Do what you want
            if (completed) {

                startActivity(Intent(this@OTPActivity, HomeActivity::class.java))
                finish()
                overridePendingTransition(0,0)


            }
        })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.submitBtn){
            startActivity(Intent(this@OTPActivity, HomeActivity::class.java))
            finish()
            overridePendingTransition(0,0)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

}