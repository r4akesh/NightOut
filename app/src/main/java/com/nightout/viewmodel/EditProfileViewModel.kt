package com.nightout.vendor.viewmodel


import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.LoginModel
import com.nightout.ui.activity.EditProfileActivity
import com.nightout.utils.Utills
import com.nightout.vendor.services.ApiSampleResource
import com.nightout.vendor.services.WebServiceRepository
import okhttp3.MultipartBody


class EditProfileViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)

    @get:Bindable
    var fName: String = ""

    @get:Bindable
    var lName: String = ""

    @get:Bindable
    var addrs1: String = ""

    @get:Bindable
    var addrs2: String = ""

    @get:Bindable
    var aboutMe: String = ""

    @get:Bindable
    var profilePic: MultipartBody.Part? = null
    var profilePicPath: String = ""

    private lateinit var loginResponseModel: LiveData<ApiSampleResource<LoginModel>>

    fun updateProfile(requestBody: MultipartBody): LiveData<ApiSampleResource<LoginModel>> {
        loginResponseModel = webServiceRepository.updateProfile(requestBody)
        return loginResponseModel
    }

    fun isValidate(activity: EditProfileActivity): Boolean {
        when {
            activity.binding.etFName.toString() == "" -> {
                Utills.showIconToast(
                    activity,
                    activity.resources.getString(R.string.please_enter_fName),

                )
                return false
            }
            activity.binding.etLName.text.toString() == "" -> {
                Utills.showIconToast(
                    activity,
                    activity.resources.getString(R.string.please_enter_lName)

                )
                return false
            }
        }
        return true
    }


}