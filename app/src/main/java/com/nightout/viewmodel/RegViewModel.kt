package com.nightout.vendor.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel

import com.nightout.ui.activity.RegisterActivity
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import com.nightout.vendor.services.ApiSampleResource


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository



class RegViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    var PhNo: String? = ""
    var fName: String? = ""
    var lName: String? = ""
    var emailId: String? = ""

    private lateinit var regResponseModel: LiveData<ApiSampleResource<LoginModel>>

    fun isValidation(activity: RegisterActivity): Boolean {
        val isFormValidated: Boolean
        when {
            activity.binding.registerFName.text.toString().isNullOrBlank() -> {
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.registerFName, activity.resources.getString(R.string.please_enter_fname), activity)
            }

            activity.binding.registerLName.text.toString().isNullOrBlank() -> {
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.registerLName, activity.resources.getString(R.string.please_enter_lname), activity)
            }
            activity.binding.registerEmail.text.toString().isNullOrBlank() -> {
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.registerLName, activity.resources.getString(R.string.please_enter_email), activity)
            }
            !MyApp.isValidEmail(activity.binding.registerEmail.text.toString())  -> {
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.registerLName, activity.resources.getString(R.string.please_enter_validemail), activity)
            }
            activity.binding.registerPhNo.text.toString().isNullOrBlank() -> {
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.registerLName, activity.resources.getString(R.string.please_enter_phno), activity)
            }

            activity.binding.registerPhNo.text.toString().length<14->{
                isFormValidated = false
                Utills.showSnackBarOnError(activity.binding.registerPhNo, activity.resources.getString(R.string.please_enter_valid_phno), activity)
            }

            else -> {
                isFormValidated = true
            }
        }

        return isFormValidated
    }


    fun register(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        regResponseModel = webServiceRepository.register(map)
        return regResponseModel
    }


}