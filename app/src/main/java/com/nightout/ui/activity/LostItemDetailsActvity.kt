package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity

import com.nightout.databinding.LostitemdetailsActivityBinding

class LostItemDetailsActvity : BaseActivity() {
    lateinit var binding : LostitemdetailsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this@LostItemDetailsActvity,R.layout.lostitemdetails_activity)
        setToolBar()
        initView()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.lostActvityChooseVenues){
            startActivity(Intent(this@LostItemDetailsActvity,ChooseVenuseActivity::class.java))
        }
    }
    private fun initView() {
        setTouchNClick(binding.lostActvityChooseVenues)
    }

    private fun setToolBar() {
         setTouchNClick(binding.lostItemToolBar.toolbarBack)
        binding.lostItemToolBar.toolbarBack.setOnClickListener { finish() }
        binding.lostItemToolBar.toolbarTitle.setText("Lost Item Details")
        binding.lostItemToolBar.toolbar3dot.visibility=GONE
        binding.lostItemToolBar.toolbarBell.visibility=GONE
    }
}