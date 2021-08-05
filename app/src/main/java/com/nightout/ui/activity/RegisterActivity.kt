package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.RegisterActivityBinding
import com.nightout.model.RegisterModel
import com.nightout.utils.MyApp
import com.nightout.vendor.viewmodel.RegViewModel

class RegisterActivity : BaseActivity() {
    lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RegisterActivity, R.layout.register_activity)
        initView()
    }

    private fun initView() {
        setTouchNClick(binding.registerSaveBtn)
        setTouchNClick(binding.reigisterActivityBakBtn)
        binding.registerHandler = MyApp.getRegHandler(this)
        binding.regviewmodel = ViewModelProviders.of(this).get(RegViewModel::class.java)
    }

  /*  override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.registerSaveBtn) {
            startActivity(Intent(this@RegisterActivity, OTPActivity::class.java))
            overridePendingTransition(0, 0)
        } else if (v == binding.reigisterActivityBakBtn) {
            finish()
        }
    }*/
}