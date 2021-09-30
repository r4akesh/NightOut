package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.AboutModelResponse
import com.nightout.model.ContactFillterModel
import com.nightout.model.GetEmergencyModel
import com.nightout.vendor.services.ApiSampleResource
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository
import org.json.JSONObject

class CommonViewModel (activity: Activity) : BaseObservable() {
    private var webServiceRepository = WebServiceRepository(activity)
    private lateinit var cmsResponse: LiveData<ApiSampleResource<AboutModelResponse>>
    private lateinit var contactListResponse: LiveData<ApiSampleResource<ContactFillterModel>>
    lateinit var getEmergencyModel: LiveData<ApiSampleResource<GetEmergencyModel>>

    fun aboutCms(): LiveData<ApiSampleResource<AboutModelResponse>> {
        cmsResponse = webServiceRepository.aboutCMS()
        return cmsResponse
    }
    fun getContactFilter(jsonObject: JSONObject): LiveData<ApiSampleResource<ContactFillterModel>> {
        contactListResponse = webServiceRepository.getContactFilter(jsonObject)
        return contactListResponse
    }

    fun getEmngcy(): LiveData<ApiSampleResource<GetEmergencyModel>> {
        getEmergencyModel = webServiceRepository.getEmergency()
        return getEmergencyModel
    }
}