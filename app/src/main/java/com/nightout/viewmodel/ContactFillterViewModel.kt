package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.ContactFillterModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository
import org.json.JSONObject

class ContactFillterViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<ContactFillterModel>>


    fun getContactFilter(jsonObject: JSONObject): LiveData<Resource<ContactFillterModel>> {
        try {
         //   dashboardModel = webServiceRepository.getContactFilter(jsonObject)
            return dashboardModel
        } catch (e: Exception) {
            return dashboardModel
        }
    }




}