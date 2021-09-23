package com.nightout.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nightout.model.BaseModel
import com.nightout.model.DashboardModel
import com.nightout.model.LoginModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.Status
import com.nightout.vendor.services.WebServiceRepository
import okhttp3.MultipartBody

class LostItemSubmitViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<BaseModel>>


    fun submitLostItem(requestBody: MultipartBody): LiveData<Resource<BaseModel>> {
        dashboardModel = webServiceRepository.submitLostItem(requestBody)
        return dashboardModel
    }




}