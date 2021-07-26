package com.nightout.ui.activity

import android.os.Bundle
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.TermncondActviityBinding

import kotlinx.android.synthetic.main.termncond_actviity.*


class TermsNCondActivity : BaseActivity() {
    lateinit var  binding : TermncondActviityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@TermsNCondActivity,R.layout.termncond_actviity)
        setToolBar()
    }

    private fun setToolBar() {
         binding.termCondToolBar.toolbarTitle.setText( "Terms and Condition")
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility=GONE
        binding.termCondToolBar.toolbarBell.visibility=GONE
    }
}