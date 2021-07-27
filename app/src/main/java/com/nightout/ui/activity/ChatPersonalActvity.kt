package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ChatpersonalActvityBinding

class ChatPersonalActvity : BaseActivity() {
    lateinit var binding : ChatpersonalActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.chatpersonal_actvity)
        binding = DataBindingUtil.setContentView(this@ChatPersonalActvity,R.layout.chatpersonal_actvity)
        initView()
    }

    private fun initView() {
         setTouchNClick(binding.chatpersonalClose)
         setTouchNClick(binding.chatpersonalImage)
         setTouchNClick(binding.chatpersonalTitle)
         setTouchNClick(binding.chatpersonalSubTitle)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(binding.chatpersonalClose==v){
            finish()
        }
       else  if(v==binding.chatpersonalImage || v==binding.chatpersonalSubTitle || v==binding.chatpersonalTitle){
            startActivity(Intent(this@ChatPersonalActvity,GroupInfoActvity::class.java))
        }
    }
}