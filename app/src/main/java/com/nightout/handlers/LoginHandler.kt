package com.nightout.handlers

import android.content.Intent
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.OTPActivity
import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.LoginViewModel

open class LoginHandler(val activity: LoginActivity) {
    private lateinit var loginViewModel: LoginViewModel
    private val progressDialog = CustomProgressDialog()
    private var lastClickTime = 0;
    fun onClickLogin(loginViewModel: LoginViewModel) {
        this.loginViewModel = loginViewModel
        MyApp.hideSoftKeyboard(activity)

        if (loginViewModel.isValidation(activity)) {
            val map = HashMap<String, String>()
            var mobNo = loginViewModel.PhNo!!
            mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
           map["phonenumber"] = mobNo
            map["device_id"] = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
            map["device_type"] = "1"
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000){//prevent double tap
                return
            }
            lastClickTime = SystemClock.elapsedRealtime().toInt()
            loginCall(map, activity)
        }
    }


    fun onClickSignUp() {
        activity.startActivity(Intent(activity, RegisterActivity::class.java))
    }

    private fun loginCall(map: HashMap<String, String>, activity: LoginActivity) {
        progressDialog.show(activity)
        loginViewModel.doLogin(map).observe(activity) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    activity.startActivity(
                        Intent(activity, OTPActivity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.MOBILENO, loginViewModel.PhNo!!)
                            .putExtra(AppConstant.INTENT_EXTRAS.EMAILID, it.data?.data?.email)
                    )
                }
                Status.LOADING -> {
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    // progressBar.visibility = View.GONE
                    Utills.showErrorToast(activity, it.message!!)
                }
            }
        }
    }



}