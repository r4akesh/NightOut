package com.nightout.vendor.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel
import com.nightout.ui.activity.LoginActivity
import com.nightout.utils.Utills


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository



class LoginViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    var PhNo: String? = ""
   // var password: String? = ""
    private lateinit var loginResponseModel: LiveData<Resource<LoginModel>>

    fun isValidation(activity: LoginActivity): Boolean {
        val isFormValidated: Boolean
        when {
            activity.binding.loginPhno.text.toString() == "" -> {
                isFormValidated = false
                Utills.showSnackBarOnError(
                    activity.binding.loginPhno,
                    activity.resources.getString(R.string.please_enter_phno),
                    activity
                )
            }
            activity.binding.loginPhno.text.toString().length<14->{
                isFormValidated = false
                Utills.showSnackBarOnError(
                    activity.binding.loginPhno,
                    activity.resources.getString(R.string.please_enter_valid_phno),
                    activity
                )
            }

            else -> {
                isFormValidated = true
            }
        }

        return isFormValidated
    }


    fun login(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        loginResponseModel = webServiceRepository.login(map)
        return loginResponseModel
    }


}