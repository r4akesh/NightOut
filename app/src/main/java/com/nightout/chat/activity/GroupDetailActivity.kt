package com.nightout.chat.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.databinding.GroupdetailActivityBinding

class GroupDetailActivity: AppCompatActivity() {
    lateinit var binding : GroupdetailActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@GroupDetailActivity, R.layout.groupdetail_activity)
        binding.backBtnImage.setOnClickListener { finish() }
    }


}