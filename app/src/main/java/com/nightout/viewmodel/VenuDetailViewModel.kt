package com.nightout.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nightout.model.DashboardModel
import com.nightout.model.LoginModel
import com.nightout.model.VenuDetailModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.Status
import com.nightout.vendor.services.WebServiceRepository

class VenuDetailViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var userVenueDetailModel: LiveData<Resource<VenuDetailModel>>


    fun userVenueDetail(map: HashMap<String, Any>): LiveData<Resource<VenuDetailModel>> {
        userVenueDetailModel = webServiceRepository.userVenueDetail(map)
        return userVenueDetailModel
    }




}