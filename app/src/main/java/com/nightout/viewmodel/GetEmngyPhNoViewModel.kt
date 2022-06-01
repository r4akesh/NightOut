package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.GetEmergencyModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository

class GetEmngyPhNoViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<GetEmergencyModel>>


   /* fun getEmngcy(): LiveData<Resource<GetEmergencyModel>> {
        dashboardModel = webServiceRepository.getEmergency()
        return dashboardModel
    }*/




}