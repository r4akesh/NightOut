package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.DashboardModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository

class HomeViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<DashboardModel>>


   /* fun dashBoard(): LiveData<Resource<DashboardModel>> {
        try {
            dashboardModel = webServiceRepository.dashBoard()
            return dashboardModel
        } catch (e: Exception) {
            return dashboardModel
        }
    }*/




}