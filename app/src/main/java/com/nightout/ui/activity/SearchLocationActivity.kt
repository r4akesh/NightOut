package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.SerchlocationActivityBinding

class SearchLocationActivity : BaseActivity() {
    lateinit var binding : SerchlocationActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SearchLocationActivity,R.layout.serchlocation_activity);
        setToolBar()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.toolbarBack){
            finish()
            overridePendingTransition(0,0)

        }
    }
    private fun setToolBar() {
         setTouchNClick(binding.toolbarBack)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }
}