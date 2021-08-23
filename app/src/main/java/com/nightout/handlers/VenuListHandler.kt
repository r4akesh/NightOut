package com.nightout.handlers

import com.nightout.model.VenuModel
import com.nightout.ui.activity.HomeActivity
import com.nightout.utils.*

open class VenuListHandler(val activity: HomeActivity) {
    private lateinit var sideMenuViewModl : VenuModel
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



    fun doDummyClick(sideMenuViewModl : VenuModel) {
        this.sideMenuViewModl = sideMenuViewModl
        DialogCustmYesNo.getInstance().createDialog(activity,"Dummy","Are you sure you want to logout?",object:DialogCustmYesNo.Dialogclick{
            override fun onYES() {
                // logoutAPICall()
            }

            override fun onNO() {

            }

        })
    }

/*     private fun logoutAPICall(map: HashMap<String, Any>, activity: LoginActivity) {
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
    }*/

}