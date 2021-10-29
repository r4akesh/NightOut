package com.nightout.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.FAQItemAdapter
import com.nightout.adapter.NotificationAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityFaqactivityBinding
import com.nightout.databinding.ActivityNotificationBinding

class NotificationActivity : BaseActivity() {
    lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        setToolBar()
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.Notifications)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE

        setUpView()
    }

    private fun setUpView() {
        binding.notificationList.layoutManager = LinearLayoutManager(this)
        val notificationAdapter = NotificationAdapter(this)
        binding.notificationList.adapter = notificationAdapter
    }
}