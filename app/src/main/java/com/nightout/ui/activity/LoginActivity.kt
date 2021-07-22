package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.LoginActivityBinding
import kotlinx.android.synthetic.main.discount_desc.view.*

class LoginActivity : BaseActivity() {
    lateinit var binding : LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity,R.layout.login_activity)
        initView()
    }

    private fun initView() {
        setTouchNClick(binding.loginBtn)
        setTouchNClick(binding.loginActvitySignUp)
        val str1 = resources.getString(R.string.Sign_Up)
        var settext = "<font color='#ffc800'><u>$str1 </u></font>"
//        val str1 = resources.getString(R.string.discount10)
//        var str2 = resources.getString(R.string.firsLine)
//        var settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
        binding.loginActvitySignUp.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.loginBtn){
            startActivity(Intent(this@LoginActivity, OTPActivity::class.java))

        }
        else if(v==binding.loginActvitySignUp){
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }
}