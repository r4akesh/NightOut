package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.AllTaxiListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityTaxiListBinding

class TaxiListActivity : BaseActivity() {
    private lateinit var binding: ActivityTaxiListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_taxi_list)
        setToolBar()

    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.select_taxi)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE
        setUpTaxiView()
    }

    private fun setUpTaxiView(){
        binding.taxiList.layoutManager = LinearLayoutManager(this)
        val allTaxiListAdapter = AllTaxiListAdapter(this)
        binding.taxiList.adapter = allTaxiListAdapter
    }
}