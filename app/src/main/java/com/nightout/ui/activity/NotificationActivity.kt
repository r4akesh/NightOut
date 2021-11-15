package com.nightout.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.FAQItemAdapter
import com.nightout.adapter.NotificationAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityFaqactivityBinding
import com.nightout.databinding.ActivityNotificationBinding
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class NotificationActivity : BaseActivity() {
    lateinit var binding: ActivityNotificationBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var notificationViewMode: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        notificationViewMode = CommonViewModel(this@NotificationActivity)
        setToolBar()
        user_notificationAPICall()
    }

    private fun user_notificationAPICall() {
        customProgressDialog.show(this@NotificationActivity, "")
        notificationViewMode aboutCms().observe(this@NotificationActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.venulistingToolBar.toolbarTitle.text = resources.getString(R.string.About)
                            binding.aboutActvityText.setText(Html.fromHtml(myData.data.about_us[0].content, Html.FROM_HTML_MODE_COMPACT))
                        } else {
                            binding.venulistingToolBar.toolbarTitle.text = resources.getString(R.string.About)
                            binding.aboutActvityText.setText(Html.fromHtml(myData.data.about_us[0].content))
                        }
                        //  binding.aboutActvityText.setText(myData.data[0].content)
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(
                        binding.rootLayoutAbout,
                        it.message!!,
                        this@AboutActivity
                    )
                }
            }
        })
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