package com.nightout.vendor.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel
import com.nightout.ui.activity.LoginActivity
import com.nightout.utils.Utills
import com.nightout.vendor.services.ApiSampleResource


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository



class LoginViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    var PhNo: String? = ""

   // var password: String? = ""
    private lateinit var loginResponseModel: LiveData<ApiSampleResource<LoginModel>>

    fun isValidation(activity: LoginActivity): Boolean {
        val isFormValidated: Boolean
        when {
            activity.binding.loginPhno.text.toString() == "" -> {
                isFormValidated = false
                //Utills.showSnackBarOnError(activity.binding.loginPhno, activity.resources.getString(R.string.please_enter_phno), activity)
                //Utills.showSnakBarCstm(activity.binding.loginPhno, activity.resources.getString(R.string.please_enter_phno), activity)
                Utills.showErrorToast(activity,activity.resources.getString(R.string.please_enter_phno))
            }
            activity.binding.loginPhno.text.toString().length<14->{
                isFormValidated = false
              //  Utills.showSnackBarOnError(activity.binding.loginPhno, activity.resources.getString(R.string.please_enter_valid_phno), activity)
                Utills.showErrorToast(activity,activity.resources.getString(R.string.please_enter_valid_phno))
            }

            else -> {
                isFormValidated = true
            }
        }

        return isFormValidated
    }


    fun doLogin(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        loginResponseModel = webServiceRepository.doLogin(map)
        return loginResponseModel
    }


}