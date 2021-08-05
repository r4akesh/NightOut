package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil


import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.OtpActvityBinding

class OTPActivity : BaseActivity() {

    lateinit var binding : OtpActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OTPActivity,R.layout.otp_actvity)
        initView()

        binding.otpPinView.setPasswordHidden(true);
        binding.otpPinView.isCursorVisible = false
        binding.otpPinView.setCursorColor(
            ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
        showTimer()

        binding.otpPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("TAG", "afterTextChanged: "+s.toString())
            }

        })

      /*  binding.otpPinView.setOnCompleteListener(PinView.OnCompleteListener { completed, pinResults -> //Do what you want
            if (completed) {
                startActivity(Intent(this@OTPActivity, HomeActivity::class.java))
                finish()


            }
        })*/
    }

    private fun showTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes: Long = millisUntilFinished / 1000 / 60
                val seconds: Long = millisUntilFinished / 1000 % 60
                binding.otpTimer.setText("" + minutes + ":" + seconds)
            }

            override fun onFinish() {
                Log.d("seconds remaining: ", "DONE")
            }
        }.start()
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