package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.LostitemeditActvityBinding


class LostItemEditActvity : BaseActivity() {

    lateinit var  binding : LostitemeditActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@LostItemEditActvity,R.layout.lostitemedit_actvity)
        setToolBar()
    }


    private fun setToolBar() {
        binding.lostItemEditToolBar.toolbarTitle.text = "Lost Items"
        binding.lostItemEditToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.lostItemEditToolBar.toolbar3dot.visibility= View.GONE
        binding.lostItemEditToolBar.toolbarBell.visibility= View.GONE
    }
}