package com.nightout.vendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel
import com.nightout.ui.activity.OTPActivity

import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.MyApp
import com.nightout.utils.Util


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository



class OtpViewModel(application: Application) : AndroidViewModel(application) {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(application)
    var otp: String? = ""
    //var phno: String? = ""


    private lateinit var regResponseModel: LiveData<Resource<LoginModel>>

    fun isValidation(activity: OTPActivity): Boolean {
        val isFormValidated: Boolean
        when {
            activity.binding.otpPinView.text.toString().isNullOrBlank() -> {
                isFormValidated = false
                Util.showSnackBarOnError(activity.binding.otpPinView, activity.resources.getString(R.string.please_enter_otp), activity)
            }
            activity.binding.otpPinView.text.toString().length<4->{
                isFormValidated = false
                Util.showSnackBarOnError(activity.binding.otpPinView, activity.resources.getString(R.string.please_enter_complete_otp), activity)
            }
            else -> {
                isFormValidated = true
            }
        }
        return isFormValidated
    }


    fun otp(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        regResponseModel = webServiceRepository.otp(map)
        return regResponseModel
    }

    fun otpResend(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        regResponseModel = webServiceRepository.otpResend(map)
        return regResponseModel
    }
}