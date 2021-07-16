package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.LoginActivityBinding

class LoginActivity : BaseActivity() {
    lateinit var binding : LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity,R.layout.login_activity)
        initView()
    }

    private fun initView() {
        setTouchNClick(binding.loginBtn)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.loginBtn){
            startActivity(Intent(this@LoginActivity, OTPActivity::class.java))
            overridePendingTransition(0,0)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }
}