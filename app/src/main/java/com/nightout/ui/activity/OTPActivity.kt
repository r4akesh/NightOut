package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
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
        initView()

        binding.otpPinView.isMaskPassword = true
      //  binding.otpPinView.split = "***"

        binding.otpPinView.setOnCompleteListener(PinView.OnCompleteListener { completed, pinResults -> //Do what you want
            if (completed) {
                startActivity(Intent(this@OTPActivity, HomeActivity::class.java))
                finish()


            }
        })
    }

    private fun initView() {
        setTouchNClick(binding.submitBtn)
        setTouchNClick(binding.otpActivitySendAgain)
        setTouchNClick(binding.otpActvityChange)
        setTouchNClick(binding.otpActivityBakBtn)
        var str1 = resources.getString(R.string.SendAgain)
        var settext = "<font color='#ffc800'><u>$str1 </u></font>"
        binding.otpActivitySendAgain.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

         str1 = resources.getString(R.string.Change)
          settext = "<font color='#ffc800'><u>$str1 </u></font>"
        binding.otpActvityChange.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.submitBtn){
            startActivity(Intent(this@OTPActivity, HomeActivity::class.java))
            finish()

        }
        else if(binding.otpActvityChange==v || binding.otpActivityBakBtn==v){
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

}