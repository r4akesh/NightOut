package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.PreviousEmergencyInfoAdatper
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityEmergencyContactBinding

class EmergencyContactListActivity : BaseActivity() {
    lateinit var binding: ActivityEmergencyContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency_contact_list)
        setToolBar()
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.Emergency_Contact)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE
        init()
    }

    private fun init(){
        binding.emergencyList.layoutManager = LinearLayoutManager(this)
        val previousEmergencyInfoAdatper = PreviousEmergencyInfoAdatper(this)
        binding.emergencyList.adapter = previousEmergencyInfoAdatper
    }
}