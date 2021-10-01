package com.nightout.handlers


import android.content.Intent
import android.util.Log
import com.nightout.model.LoginModel
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.OTPActivity

import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.RegViewModel


open class RegisterHandler(val activity: RegisterActivity) {
    private lateinit var regViewModel: RegViewModel
    private val progressDialog = CustomProgressDialog()
    fun onClickReg(regViewModel: RegViewModel) {
        this.regViewModel = regViewModel
        MyApp.hideSoftKeyboard(activity)
        if (regViewModel.isValidation(activity)) {
            val map = HashMap<String, String>()
            var mobNo = regViewModel.PhNo!!
            mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
            map["first_name"] = regViewModel.fName!!
            map["last_name"] = regViewModel.lName!!
            map["email"] = regViewModel.emailId!!
            map["phonenumber"] = mobNo
            map["device_id"] = "dhfkjdfh"
            map["device_type"] = "1"
            regCall(map, activity)
        }
    }


    private fun regCall(map: HashMap<String, String>, activity: RegisterActivity) {
        progressDialog.show(activity)
        regViewModel.register(map).observe(activity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {
                       /*  var regViewModel: LoginModel.Data = it.data
                        Log.d("TAG", "regCall: " + regViewModel)
                        PreferenceKeeper.instance.loginResponse = regViewModel
                        activity.startActivity(Intent(activity, LoginActivity::class.java))
                        activity.finish()*/
                        activity.startActivity(
                            Intent(activity, OTPActivity::class.java)
                                .putExtra(AppConstant.INTENT_EXTRAS.MOBILENO, regViewModel.PhNo!!)
                                .putExtra(AppConstant.INTENT_EXTRAS.EMAILID, it.data?.email))
                        

                    }
                    Utills.showSnackBarOnError(
                        activity.binding.registerRootLayout,
                        it.data?.message!!,
                        activity
                    )
                }
                Status.LOADING -> {
                    //progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(activity.binding.registerPhNo, it.message!!, activity)
                }
            }
        })
    }

    fun onFinishScreen(){
        activity.finish()
    }
}