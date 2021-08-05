package com.nightout.handlers

import android.content.Intent
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.MyApp
import com.nightout.utils.Util
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.LoginViewModel

open class LoginHandler(val activity: LoginActivity) {
    private lateinit var loginViewModel : LoginViewModel
    fun onClickLogin(loginViewModel: LoginViewModel) {
        this.loginViewModel = loginViewModel
        MyApp.hideSoftKeyboard(activity)
        if (loginViewModel.isValidation(activity)) {
            val map = HashMap<String, Any>()
            var mobNo=loginViewModel.PhNo!!
            mobNo=  mobNo.replace("(","").replace(")","").replace("-","").replace(" ","").trim()
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
        loginViewModel.login(map).observe(activity,{
            when (it.status) {
                Status.SUCCESS -> {
                    //progressBar.visibility = View.GONE
                   // it.data?.let { users -> renderList(users) }
                    Util.showSnackBarOnError(activity.binding.loginPhno,it.message!!,activity)
                }
                Status.LOADING -> {
                    //progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    // progressBar.visibility = View.GONE
                    Util.showSnackBarOnError(activity.binding.loginPhno,it.message!!,activity)
                }
            }
        })
    }

}