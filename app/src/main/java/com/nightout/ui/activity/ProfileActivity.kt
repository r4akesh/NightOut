package com.nightout.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.databinding.ActivityFaqactivityBinding
import com.nightout.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        setToolBar()
    }

    private fun setToolBar() {
        binding.toolbarBack.setOnClickListener {
            finish()
        }

        binding.editProfileBtn.setOnClickListener {
            startActivity(Intent(this,EditProfileActivity::class.java))
        }
    }
}