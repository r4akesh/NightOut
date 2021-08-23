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

class VenuDetailViewModel (application: Application) : AndroidViewModel(application) {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(application)
    lateinit var dashboardModel: LiveData<Resource<VenuDetailModel>>


    fun dashBoard(): LiveData<Resource<VenuDetailModel>> {
        dashboardModel = webServiceRepository.dashBoard()
        return dashboardModel
    }




}