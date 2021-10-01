package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.*
import com.nightout.vendor.services.ApiSampleResource
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository
import org.json.JSONObject

class CommonViewModel (activity: Activity) : BaseObservable() {
    private var webServiceRepository = WebServiceRepository(activity)
    private lateinit var cmsResponse: LiveData<ApiSampleResource<AboutModelResponse>>
    private lateinit var contactListResponse: LiveData<ApiSampleResource<ContactFillterModel>>
    lateinit var getEmergencyModel: LiveData<ApiSampleResource<GetEmergencyModel>>
    lateinit var getLostItem: LiveData<ApiSampleResource<GetLostItemListModel>>
    lateinit var dsahModel: LiveData<ApiSampleResource<DashboardModel>>
    lateinit var venuDetailModel: LiveData<ApiSampleResource<VenuDetailModel>>
    lateinit var addFavModel: LiveData<ApiSampleResource<AddFavModel>>

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


    fun getLostItemList(): LiveData<ApiSampleResource<GetLostItemListModel>> {
        getLostItem = webServiceRepository.getLostItemList()
        return getLostItem
    }

    fun dashBoard(): LiveData<ApiSampleResource<DashboardModel>> {
        dsahModel = webServiceRepository.dashBoard()
        return dsahModel
    }

    fun userVenueDetail(map: HashMap<String, String>): LiveData<ApiSampleResource<VenuDetailModel>> {
        venuDetailModel = webServiceRepository.userVenueDetail(map)
        return venuDetailModel
    }

    fun doFavItem(map: HashMap<String, String>): LiveData<ApiSampleResource<AddFavModel>> {
        addFavModel = webServiceRepository.userAddFav(map)
        return addFavModel
    }

}