package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.AddbarcrawlActivityBinding
import com.nightout.databinding.FragmentBarcrawlnewBinding

class AddBarCrawlActvity : BaseActivity() {


    lateinit var binding: AddbarcrawlActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AddBarCrawlActvity,R.layout.addbarcrawl_activity)
        setToolBar()
        initView()
    }

    private fun initView() {
        setTouchNClick(R.id.createBtn)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.createBtn){
            startActivity( Intent (this@AddBarCrawlActvity, BarCrawlSaveActivity::class.java))
            finish()
        }
    }

    private fun setToolBar() {
        setTouchNClick(binding.addBarCrawlToolBar.toolbarBack)
        setTouchNClick(binding.addBarCrawlToolBar.toolbarCreateGrop)

        binding.addBarCrawlToolBar.toolbarTitle.setText("Add Bar Crawl")
        binding.addBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.addBarCrawlToolBar.toolbar3dot.visibility=GONE
        binding.addBarCrawlToolBar.toolbarCreateGrop.visibility= VISIBLE
        binding.addBarCrawlToolBar.toolbarCreateGrop.setText("View List")

        binding.addBarCrawlToolBar.toolbarBack.setOnClickListener{
            finish()
        }

        binding.addBarCrawlToolBar.toolbarCreateGrop.setOnClickListener {
            startActivity(Intent(this@AddBarCrawlActvity,AllBarCrawalNewActivity::class.java))
        }
    }

}