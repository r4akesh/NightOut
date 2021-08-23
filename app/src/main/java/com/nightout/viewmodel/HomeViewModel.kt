package com.nightout.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nightout.model.DashboardModel
import com.nightout.model.LoginModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.Status
import com.nightout.vendor.services.WebServiceRepository

class HomeViewModel (application: Application) : AndroidViewModel(application) {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(application)
    lateinit var dashboardModel: LiveData<Resource<DashboardModel>>


    fun dashBoard(): LiveData<Resource<DashboardModel>> {
        dashboardModel = webServiceRepository.dashBoard()
        return dashboardModel
    }




}