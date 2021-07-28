package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.FAQItemAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityFaqactivityBinding

class FAQActivity : BaseActivity() {
    lateinit var binding: ActivityFaqactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faqactivity)
        setToolBar()
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.faq)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE

        setUpView()
    }

    private fun setUpView() {
        binding.faqItemList.layoutManager = LinearLayoutManager(this)
        val faqItemAdapter = FAQItemAdapter(this)
        binding.faqItemList.adapter = faqItemAdapter
    }
}