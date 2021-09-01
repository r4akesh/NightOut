package com.nightout.vendor.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel
import com.nightout.ui.activity.EditProfileActivity
import com.nightout.utils.Utills


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository
import okhttp3.MultipartBody


class EditProfileViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    var fName: String = ""
    var lName: String = ""
    var addrs1: String = ""
    var addrs2: String = ""
    var aboutMe: String = ""
    var profilePic: MultipartBody.Part? = null
    var profilePicPath: String=""

    private lateinit var loginResponseModel: LiveData<Resource<LoginModel>>
    fun updateProfile(requestBody: MultipartBody): LiveData<Resource<LoginModel>> {
        loginResponseModel = webServiceRepository.updateProfile(requestBody)
        return loginResponseModel
    }

    fun isValidate(activity: EditProfileActivity): Boolean {
            when{
                activity.binding.etFName.toString() == "" ->{
                    Utills.showSnackBarOnError(activity.binding.etRootLayout, activity.resources.getString(R.string.please_enter_fName), activity)
                    return false
                }
                activity.binding.etLName.text.toString() == "" ->{
                    Utills.showSnackBarOnError(activity.binding.etRootLayout, activity.resources.getString(R.string.please_enter_lName), activity)
                    return false
                }
            }
        return true
    }


}