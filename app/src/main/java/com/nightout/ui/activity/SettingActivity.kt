package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivitySettingBinding
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class SettingActivity : BaseActivity() {
    lateinit var binding: ActivitySettingBinding
    private var customProgressDialog = CustomProgressDialog()
    private lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        commonViewModel = CommonViewModel(THIS!!)
        setToolBar()

        binding.switchNotification.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                user_noti_email_statusAPICAll("1","1")
            } else {
                user_noti_email_statusAPICAll("1","0")
            }
        })
        binding.switchEmail.setOnCheckedChangeListener({bb,isChecked->
            if (isChecked) {
                user_noti_email_statusAPICAll("0","1")
            } else {
                user_noti_email_statusAPICAll("0","0")
            }
        })
        var lognRes= PreferenceKeeper.instance.loginResponse
        binding.switchNotification.isChecked = if(lognRes?.user_notification_setting?.status.equals("0")) false else true
        binding.switchEmail.isChecked = if(lognRes?.user_email_setting?.status.equals("0")) false else true
    }

    private fun user_noti_email_statusAPICAll(type:String,status:String) {
       // type : REQUIRED [0=>Email, 1=>Notification]
        //status: REQUIRED [0=>Off, 1=>On]
        customProgressDialog.show(this@SettingActivity, "")
        var map = HashMap<String, String>()
        map["type"] = type
        map["status"] = status

        commonViewModel.emaiLSettings(map).observe(this@SettingActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                       var nofctionValue = myData.data.notificationsetting.status
                       var emailValue = myData.data.emailsetting.status
                        var mdl = PreferenceKeeper.instance.loginResponse
                        mdl?.user_email_setting?.status = emailValue
                        mdl?.user_notification_setting?.status = nofctionValue
                        PreferenceKeeper.instance.loginResponse = mdl
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@SettingActivity,
                        it.message!!

                    )
                }
            }
        })
    }


    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.Settings)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE
    }

}