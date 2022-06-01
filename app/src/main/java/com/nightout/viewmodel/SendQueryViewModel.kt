package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.BaseModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository

class SendQueryViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<BaseModel>>


   /* fun sendQuery(map : HashMap<String, Any>): LiveData<Resource<BaseModel>> {
        try {
            dashboardModel = webServiceRepository.sendQuery(map)
            return dashboardModel
        } catch (e: Exception) {
            return dashboardModel
        }
    }*/




}