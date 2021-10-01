package com.nightout.vendor.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel
import com.nightout.ui.activity.OTPActivity

import com.nightout.utils.Utills
import com.nightout.vendor.services.ApiSampleResource


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository



class OtpViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    var otp: String? = ""
    //var phno: String? = ""


    private lateinit var regResponseModel: LiveData<ApiSampleResource<LoginModel>>

    fun isValidation(activity: OTPActivity): Boolean {
        val isFormValidated: Boolean
        when {
            activity.binding.otpPinView.text.toString().isNullOrBlank() -> {
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.otpPinView, activity.resources.getString(R.string.please_enter_otp), activity)
            }
            activity.binding.otpPinView.text.toString().length<4->{
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.otpPinView, activity.resources.getString(R.string.please_enter_complete_otp), activity)
            }
            else -> {
                isFormValidated = true
            }
        }
        return isFormValidated
    }


    fun otp(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        regResponseModel = webServiceRepository.otp(map)
        return regResponseModel
    }

    fun otpResend(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        regResponseModel = webServiceRepository.otpResend(map)
        return regResponseModel
    }
}