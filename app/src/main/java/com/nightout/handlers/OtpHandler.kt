package com.nightout.handlers


import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import com.nightout.model.LoginModel
import com.nightout.ui.activity.HomeActivity
import com.nightout.ui.activity.OTPActivity
import com.nightout.utils.CustomProgressDialog

import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.OtpViewModel


open class OtpHandler(val activity: OTPActivity, var mobNo: String,var email: String) {
    private lateinit var regViewModel: OtpViewModel
    private val progressDialog = CustomProgressDialog()

    fun onClickSubmit(regViewModel: OtpViewModel) {
        this.regViewModel = regViewModel
        MyApp.hideSoftKeyboard(activity)
        if (regViewModel.isValidation(activity)) {
            Log.d("TAG", "isValide done: ")
            val map = HashMap<String, String>()
            var mobNo = mobNo
            mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
            map["otp"] = regViewModel.otp!!
            map["phonenumber"] = mobNo
            map["device_id"] = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
            map["device_type"] = "1"
            map["email"] = email
            otpCall(map, activity)
        }
    }


    private fun otpCall(map: HashMap<String, String>, activity: OTPActivity) {
        progressDialog.show(activity)
        regViewModel.otp(map).observe(activity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    //progressBar.visibility = View.GONE
                       it.data?.let {
                           var logModel: LoginModel.Data = it.data
                           PreferenceKeeper.instance.bearerTokenSave = logModel.token
                           PreferenceKeeper.instance.loginResponse = logModel
                           PreferenceKeeper.instance.isUserLogin = true

                          activity.startActivity(Intent(activity, HomeActivity::class.java))
                          activity.finish()
                      }
                   /* Utills.showSnackBarOnError(
                        activity.binding.otpRootLyout,
                        it.data?.message!!,
                        activity
                    )*/
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(activity.binding.otpRootLyout, it.message!!, activity)
                }
            }
        })
    }


      fun sendAgain(regViewModel: OtpViewModel){
          progressDialog.show(activity)
          activity.binding.otpActivitySendAgain.visibility = View.GONE
          activity.binding.otpDidnot.visibility = View.GONE
          activity.showTimer()
          val map = HashMap<String, String>()
          var mobNo = mobNo
          mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
          map["phonenumber"] = mobNo
          map["device_id"] = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
          map["device_type"] = "1"
          map["email"] = email
          regViewModel.otpResend(map).observe(activity, {
              when (it.status) {
                  Status.SUCCESS -> {
                      progressDialog.dialog.dismiss()
                      it.data?.let {

                      }
                      Utills.showSnackBarOnError(
                          activity.binding.otpRootLyout,
                          it.data?.message!!,
                          activity
                      )
                  }
                  Status.LOADING -> {
                      //progressBar.visibility = View.VISIBLE

                  }
                  Status.ERROR -> {
                      progressDialog.dialog.dismiss()
                      Utills.showSnackBarOnError(activity.binding.otpRootLyout, it.message!!, activity)
                  }
              }
          })
    }

    fun bakPressd(){
        activity.finish()
    }

}