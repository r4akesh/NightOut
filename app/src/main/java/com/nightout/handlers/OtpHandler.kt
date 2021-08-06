package com.nightout.handlers


import android.content.Intent
import android.util.Log
import com.nightout.model.LoginModel
import com.nightout.ui.activity.HomeActivity
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.OTPActivity

import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Util
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.OtpViewModel
import com.nightout.vendor.viewmodel.RegViewModel


open class OtpHandler(val activity: OTPActivity, var mobNo: String) {
    private lateinit var regViewModel: OtpViewModel
    fun onClickSubmit(regViewModel: OtpViewModel) {
        this.regViewModel = regViewModel
        MyApp.hideSoftKeyboard(activity)
        if (regViewModel.isValidation(activity)) {
            Log.d("TAG", "isValide done: ")
            val map = HashMap<String, Any>()
            var mobNo = mobNo
            mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
            map["otp"] = regViewModel.otp!!
            map["phonenumber"] = mobNo
            map["device_id"] = "dhfkjdfh"
            map["device_type"] = "1"
            otpCall(map, activity)
        }
    }


    private fun otpCall(map: HashMap<String, Any>, activity: OTPActivity) {
        regViewModel.otp(map).observe(activity, {
            when (it.status) {
                Status.SUCCESS -> {
                    //progressBar.visibility = View.GONE
                       it.data?.let {
                          var regViewModel: LoginModel.Data = it.data
                          Log.d("TAG", "regCall: " + regViewModel)
                          PreferenceKeeper.instance.loginResponse = regViewModel
                          activity.startActivity(Intent(activity, HomeActivity::class.java))
                          activity.finish()
                      }
                    Util.showSnackBarOnError(
                        activity.binding.otpRootLyout,
                        it.data?.message!!,
                        activity
                    )
                }
                Status.LOADING -> {
                    //progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    // progressBar.visibility = View.GONE
                    Util.showSnackBarOnError(activity.binding.otpRootLyout, it.message!!, activity)
                }
            }
        })
    }


      fun sendAgain(regViewModel: OtpViewModel){
          val map = HashMap<String, Any>()
          var mobNo = mobNo
          mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
          map["phonenumber"] = mobNo
          map["device_id"] = "dhfkjdfh"
          map["device_type"] = "1"
          regViewModel.otpResend(map).observe(activity, {
              when (it.status) {
                  Status.SUCCESS -> {
                      //progressBar.visibility = View.GONE
                      it.data?.let {

                      }
                      Util.showSnackBarOnError(
                          activity.binding.otpRootLyout,
                          it.data?.message!!,
                          activity
                      )
                  }
                  Status.LOADING -> {
                      //progressBar.visibility = View.VISIBLE

                  }
                  Status.ERROR -> {
                      // progressBar.visibility = View.GONE
                      Util.showSnackBarOnError(activity.binding.otpRootLyout, it.message!!, activity)
                  }
              }
          })
    }

    fun bakPressd(){
        activity.finish()
    }

}