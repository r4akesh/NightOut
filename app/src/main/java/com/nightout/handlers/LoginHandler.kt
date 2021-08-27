package com.nightout.handlers

import android.content.Intent
import android.util.Log
import com.nightout.model.LoginModel
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.OTPActivity
import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.LoginViewModel

open class LoginHandler(val activity: LoginActivity) {
    private lateinit var loginViewModel: LoginViewModel
    private val progressDialog = CustomProgressDialog()

    fun onClickLogin(loginViewModel: LoginViewModel) {
        this.loginViewModel = loginViewModel
        MyApp.hideSoftKeyboard(activity)
        if (loginViewModel.isValidation(activity)) {
            val map = HashMap<String, Any>()
            var mobNo = loginViewModel.PhNo!!
            mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
            map["phonenumber"] = mobNo
            map["device_id"] = "dhfkjdfh"
            map["device_type"] = "1"
            loginCall(map, activity)
        }
    }


    fun onClickSignUp() {
        activity.startActivity(Intent(activity, RegisterActivity::class.java))
    }

    private fun loginCall(map: HashMap<String, Any>, activity: LoginActivity) {
        progressDialog.show(activity)
        loginViewModel.login(map).observe(activity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
//                    it.data?.let {
//
//                    }

                    activity.startActivity(
                        Intent(activity, OTPActivity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.MOBILENO, loginViewModel.PhNo!!)
                    )
                }
                Status.LOADING -> {
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    // progressBar.visibility = View.GONE
                    Utills.showSnackBarOnError(activity.binding.loginActvityRoot, it.message!!, activity)
                }
            }
        })
    }

}