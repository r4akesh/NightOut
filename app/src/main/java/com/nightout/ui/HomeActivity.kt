package com.nightout.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.databinding.HomeActivityBinding
import com.nightout.utils.Util

class HomeActivity : AppCompatActivity() {

    lateinit var binding : HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.home_activity)
        binding = DataBindingUtil.setContentView(this@HomeActivity,R.layout.home_activity)
        appBarAndStatusBar()

        //sideMenu
        val widthRatio = Util.ratioOfScreen(this, 0.7f)
        Log.d("widthRatio_of_view", widthRatio.toString() + "")

    }

    private fun appBarAndStatusBar() {
        binding.header.headerTitle.setText("Hi User")
        binding.bottomMyProfile.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.primary_clr))
        binding.bottomTransport.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.primary_clr))
        binding.bottomHome.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.white))
    }
}