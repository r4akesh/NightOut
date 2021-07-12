package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dpizarro.pinview.library.PinView
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.OtpActvityBinding

class OTPActivity : BaseActivity() {

    lateinit var binding : OtpActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OTPActivity,R.layout.otp_actvity)
        binding.otpPinView.setOnCompleteListener(PinView.OnCompleteListener { completed, pinResults -> //Do what you want
            if (completed) {
               // otpValue = pinResults;
                startActivity(Intent(this@OTPActivity, HomeActivity::class.java))
                overridePendingTransition(0,0)


            }
        })
    }


}