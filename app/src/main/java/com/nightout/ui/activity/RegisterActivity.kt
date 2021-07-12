package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.RegisterActivityBinding

class RegisterActivity : BaseActivity() {
    lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RegisterActivity,R.layout.register_activity)
        initView()
    }

    private fun initView() {
         setTouchNClick(binding.registerSaveBtn)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.registerSaveBtn){
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }
}