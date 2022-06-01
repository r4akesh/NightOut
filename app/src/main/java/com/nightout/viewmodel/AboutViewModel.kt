package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.AboutModelResponse
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository

class AboutViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<AboutModelResponse>>


    fun aboutCms(): LiveData<Resource<AboutModelResponse>> {
     //   dashboardModel = webServiceRepository.aboutCMS()
        return dashboardModel
    }




}