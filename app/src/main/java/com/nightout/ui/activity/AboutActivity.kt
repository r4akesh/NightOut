package com.nightout.ui.activity

import android.os.Bundle
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.AboutActviityBinding

class AboutActivity : BaseActivity() {
    lateinit var  binding : AboutActviityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@AboutActivity,R.layout.about_actviity)
        setToolBar()
    }

    private fun setToolBar() {
         binding.venulistingToolBar.toolbarTitle.setText("About Us")
        binding.venulistingToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.venulistingToolBar.toolbar3dot.visibility=GONE
        binding.venulistingToolBar.toolbarBell.visibility=GONE
    }
}