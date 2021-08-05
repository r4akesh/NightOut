package com.nightout.handlers

import android.content.Intent
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.MyApp
import com.nightout.utils.Util
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.RegViewModel


open class RegisterHandler(val activity: RegisterActivity) {
    private lateinit var regViewModel : RegViewModel
    fun onClickReg(regViewModel: RegViewModel) {
        this.regViewModel = regViewModel
        MyApp.hideSoftKeyboard(activity)
        if (regViewModel.isValidation(activity)) {
            val map = HashMap<String, Any>()
            var mobNo=regViewModel.PhNo!!
            mobNo=  mobNo.replace("(","").replace(")","").replace("-","").replace(" ","").trim()
            map["first_name"] = regViewModel.fName!!
            map["last_name"] = regViewModel.lName!!
            map["email"] = regViewModel.emailId!!
            map["phonenumber"] = mobNo
            map["device_id"] = "dhfkjdfh"
            map["device_type"] = "1"
            regCall(map, activity)
        }
    }





    private fun regCall(map: HashMap<String, Any>, activity: RegisterActivity) {
        regViewModel.register(map).observe(activity,{
            when (it.status) {
                Status.SUCCESS -> {
                    //progressBar.visibility = View.GONE
                   // it.data?.let { users -> renderList(users) }
                    Util.showSnackBarOnError(activity.binding.registerPhNo,it.message!!,activity)
                }
                Status.LOADING -> {
                    //progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    // progressBar.visibility = View.GONE
                    Util.showSnackBarOnError(activity.binding.registerPhNo,it.message!!,activity)
                }
            }
        })
    }

}