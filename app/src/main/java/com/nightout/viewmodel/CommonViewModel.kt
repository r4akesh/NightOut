package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.*
import com.nightout.vendor.services.ApiSampleResource
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository
import okhttp3.MultipartBody
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
    lateinit var addRemBarCrwl: LiveData<ApiSampleResource<AddRemveBarCrawlModel>>
    lateinit var venuListModel: LiveData<ApiSampleResource<VenuListModel>>
    lateinit var submitLostItemModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var delItemModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var foundBaseModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var saveEmrgencyModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var delEmngcyModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var sendQueryModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var bookEventMdl: LiveData<ApiSampleResource<BookEventMdlResponse>>
    lateinit var favListModelRes: LiveData<ApiSampleResource<FavListModelRes>>
    lateinit var barCrwlListModel: LiveData<ApiSampleResource<AllBarCrwalListResponse>>

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

    fun doAddBarCrawl(map: HashMap<String, String>): LiveData<ApiSampleResource<AddRemveBarCrawlModel>> {
        addRemBarCrwl = webServiceRepository.userAddBarCrawl(map)
        return addRemBarCrwl
    }
    fun venulistData(storeType : HashMap<String,String>): LiveData<ApiSampleResource<VenuListModel>> {
        venuListModel = webServiceRepository.userVenueList(storeType)
        return venuListModel
    }

    fun submitLostItem(requestBody: MultipartBody): LiveData<ApiSampleResource<BaseModel>> {
        submitLostItemModel = webServiceRepository.submitLostItem(requestBody)
        return submitLostItemModel
    }

    fun delItem(map: HashMap<String, String>): LiveData<ApiSampleResource<BaseModel>> {
        delItemModel = webServiceRepository.delLostItem(map)
        return delItemModel
    }


    fun foundLostItem(storeType : HashMap<String,String>): LiveData<ApiSampleResource<BaseModel>> {
        foundBaseModel = webServiceRepository.foundLostItem(storeType)
        return foundBaseModel
    }

    fun saveEmngcy(map: HashMap<String, String>): LiveData<ApiSampleResource<BaseModel>> {
        saveEmrgencyModel = webServiceRepository.saveEmergency(map)
        return saveEmrgencyModel
    }

    fun delEmngcy(map:HashMap<String,String>): LiveData<ApiSampleResource<BaseModel>> {
        delEmngcyModel = webServiceRepository.delEmergency(map)
        return delEmngcyModel
    }

    fun sendQuery(map : HashMap<String, String>): LiveData<ApiSampleResource<BaseModel>> {
        sendQueryModel = webServiceRepository.sendQuery(map)
            return sendQueryModel
    }


    fun bookEvent(storeType : HashMap<String,String>): LiveData<ApiSampleResource<BookEventMdlResponse>> {
        bookEventMdl = webServiceRepository.eventBook(storeType)
        return bookEventMdl
    }
    fun favList(): LiveData<ApiSampleResource<FavListModelRes>> {
        favListModelRes = webServiceRepository.favList()
        return favListModelRes
    }
 /*   fun barCrwlList(): LiveData<ApiSampleResource<BarCrwlListModel>> {
        barCrwlListModel = webServiceRepository.barCrwlList()
        return barCrwlListModel
    }*/
    fun venuListBarCrwl(): LiveData<ApiSampleResource<AllBarCrwalListResponse>> {
        barCrwlListModel = webServiceRepository.barCrwlVenuList()
        return barCrwlListModel
    }
}