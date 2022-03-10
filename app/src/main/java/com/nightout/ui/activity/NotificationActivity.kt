package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.NotificationReadAdpter
import com.nightout.adapter.NotificationUnreadAdpter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityNotificationBinding
import com.nightout.model.NotificationResponse
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.PreferenceKeeper
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
        //change Status
        PreferenceKeeper.instance.isNotificationOpen =true
        setToolBar()
        user_notificationAPICall()
    }

    private fun user_notificationAPICall() {
        customProgressDialog.show(this@NotificationActivity, "")
        notificationViewMode.notificationList().observe(this@NotificationActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {
                        if(it.data.unread.size>0) {
                            binding.notificationUnread.visibility=VISIBLE
                            setListUnread(it.data.unread)
                        }
                          if(it.data.read.size>0) {
                            binding.notificationread.visibility=VISIBLE
                            setListRead(it.data.read)
                        }
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@NotificationActivity,
                        it.message!!

                    )
                }
            }
        })
    }
    lateinit var notificationReadAdpter: NotificationReadAdpter
    private fun setListRead(readList: ArrayList<NotificationResponse.Read>) {
        notificationReadAdpter = NotificationReadAdpter(this@NotificationActivity,readList,object:NotificationReadAdpter.ClickListener{
            override fun onClickChk(pos: Int) {

            }

        })

        binding.notificationRecyleread.also {
            it.layoutManager= LinearLayoutManager(this@NotificationActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = notificationReadAdpter
        }
    }

    lateinit var notificationAdpter: NotificationUnreadAdpter
    private fun setListUnread(dataList: ArrayList<NotificationResponse.Unread>) {
        notificationAdpter = NotificationUnreadAdpter(this@NotificationActivity,dataList,object:NotificationUnreadAdpter.ClickListener{
            override fun onClickChk(pos: Int) {

            }

        })

        binding.notificationRecyleUnread.also {
            it.layoutManager= LinearLayoutManager(this@NotificationActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = notificationAdpter
        }

    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.Notifications)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE


    }


}