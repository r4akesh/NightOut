package com.nightout.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityEditProfileBinding

class EditProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        init()
    }

    private fun init(){
        binding.toolbarBack.setOnClickListener { finish() }
    }
}