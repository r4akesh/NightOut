package com.nightout.handlers

import android.content.Intent
import com.nightout.model.LoginModel
import com.nightout.ui.activity.HomeActivity
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.OTPActivity
import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.LoginViewModel
import com.nightout.vendor.viewmodel.SideMenuViewModel

open class SideMenuHandler(val activity: HomeActivity) {
    private lateinit var sideMenuViewModl : SideMenuViewModel
  /*  fun onClickLogin(loginViewModel: LoginViewModel) {
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
    }*/



    fun doLogout(sideMenuViewModl : SideMenuViewModel) {
        this.sideMenuViewModl = sideMenuViewModl
        DialogCustmYesNo.getInstance().createDialog(activity,"Title","Are you sure you want to logout?",object:DialogCustmYesNo.Dialogclick{
            override fun onYES() {
                 logoutAPICall()
            }

            override fun onNO() {

            }

        })
    }

     private fun logoutAPICall(map: HashMap<String, Any>, activity: LoginActivity) {
        loginViewModel.login(map).observe(activity,{
            when (it.status) {
                Status.SUCCESS -> {
                    //progressBar.visibility = View.GONE
                   // it.data?.let { users -> renderList(users) }
                    it.data?.let {
                        var logModel: LoginModel.Data = it.data
                        PreferenceKeeper.instance.loginResponse = logModel
                        var vv = PreferenceKeeper.instance.loginResponse
                        var vv2 = PreferenceKeeper.instance.loginResponse
                    }
                   Util.showSnackBarOnError(activity.binding.loginPhno,it.data?.message!!,activity)
                    activity.startActivity(Intent(activity,OTPActivity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.MOBILENO,loginViewModel.PhNo!!))
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