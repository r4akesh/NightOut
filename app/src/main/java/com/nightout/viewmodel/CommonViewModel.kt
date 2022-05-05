package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.nightout.model.*
import com.nightout.vendor.services.ApiSampleResource
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository
import okhttp3.MultipartBody
import org.json.JSONObject

class CommonViewModel (activity: Activity) : BaseObservable() {
    private var webServiceRepository = WebServiceRepository(activity)
    lateinit var baseModel: LiveData<ApiSampleResource<BaseModel>>
    private lateinit var cmsResponse: LiveData<ApiSampleResource<AboutModelResponse>>
    private lateinit var contactListResponse: LiveData<ApiSampleResource<ContactFillterModel>>
    lateinit var getEmergencyModel: LiveData<ApiSampleResource<GetEmergencyModel>>
    lateinit var getLostItem: LiveData<ApiSampleResource<GetLostItemListModel>>
    lateinit var getSharedViewModel: LiveData<ApiSampleResource<SharedBarcrwalRes>>
    lateinit var getSavedViewModel: LiveData<ApiSampleResource<BarcrwalSavedRes>>
    lateinit var dsahModel: LiveData<ApiSampleResource<DashboardModel>>
    lateinit var venuDetailModel: LiveData<ApiSampleResource<VenuDetailModel>>
    lateinit var addFavModel: LiveData<ApiSampleResource<AddFavModel>>
    lateinit var addRemBarCrwl: LiveData<ApiSampleResource<AddRemveBarCrawlModel>>
    lateinit var venuListModel: LiveData<ApiSampleResource<VenuListModel>>
    lateinit var reviewListModel: LiveData<ApiSampleResource<ReviewListRes>>
    lateinit var addReviewModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var submitLostItemModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var delItemModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var foundBaseModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var saveEmrgencyModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var delEmngcyModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var sendQueryModel: LiveData<ApiSampleResource<BaseModel>>
    lateinit var bookEventMdl: LiveData<ApiSampleResource<BookEventMdlResponse>>
    lateinit var favListModelRes: LiveData<ApiSampleResource<FavListModelRes>>
    lateinit var invitedBarCrwlViewMOdel: LiveData<ApiSampleResource<InvitedBarCrwlResponse>>
    lateinit var barCrwlListModel: LiveData<ApiSampleResource<AllBarCrwalListResponse>>
    lateinit var userDeviceModel: LiveData<ApiSampleResource<BaseModel>>

    lateinit var lostItemChooseVenuResponse: LiveData<ApiSampleResource<LostItemChooseVenuResponse>>
    lateinit var searchCityResponse: LiveData<ApiSampleResource<SearchCityResponse>>
    lateinit var notificationResponse: LiveData<ApiSampleResource<NotificationResponse>>
    lateinit var panicHistoryRes: LiveData<ApiSampleResource<PanicHistoryRes>>
    lateinit var createUpdateBarcrwalResponse: LiveData<ApiSampleResource<CreateUpdateBarcrwalResponse>>
    lateinit var setEndLocModel: LiveData<ApiSampleResource<SetEndLocModel>>
    lateinit var barcrwalCreatedViewModel: LiveData<ApiSampleResource<BarcrwalCreatedRes>>
    lateinit var notifEmilSettingViewModel: LiveData<ApiSampleResource<NotifEmilSettingRes>>
    lateinit var prebookedViewModel: LiveData<ApiSampleResource<PrebookedlistResponse>>
    lateinit var myOrderResViewModel: LiveData<ApiSampleResource<MyOrderRes>>
    lateinit var prebookedCancelViewModel: LiveData<ApiSampleResource<PreBookCancelRes>>
    lateinit var fillterResViewModel: LiveData<ApiSampleResource<FillterRes>>
    lateinit var allUserResViewModel: LiveData<ApiSampleResource<AllUserRes>>
    lateinit var doPayment: LiveData<ApiSampleResource<PlaceOrderResponse>>
    lateinit var doPaymentStatus: LiveData<ApiSampleResource<BaseModel>>
    lateinit var chatUploadImg: LiveData<ApiSampleResource<ChatImgUploadResponse>>

    fun aboutCms(): LiveData<ApiSampleResource<AboutModelResponse>> {
        cmsResponse = webServiceRepository.aboutCMS()
        return cmsResponse
    }
    fun getContactFilter(jsonObject: JSONObject): LiveData<ApiSampleResource<ContactFillterModel>> {
        contactListResponse = webServiceRepository.getContactFilter(jsonObject)
        return contactListResponse
    }
    fun getContactAll(jsonObject: JSONObject): LiveData<ApiSampleResource<ContactFillterModel>> {
        contactListResponse = webServiceRepository.getContactAll(jsonObject)
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

    fun dashBoard(jbj:JSONObject): LiveData<ApiSampleResource<DashboardModel>> {
        dsahModel = webServiceRepository.dashBoard(jbj)
        return dsahModel
    }

    fun doPayment(jbj:JSONObject): LiveData<ApiSampleResource<PlaceOrderResponse>> {
        doPayment = webServiceRepository.doPayment(jbj)
        return doPayment
    }
    fun doPaymentChkStatus(jbj:JSONObject): LiveData<ApiSampleResource<BaseModel>> {
        doPaymentStatus = webServiceRepository.doPaymentStaus(jbj)
        return doPaymentStatus
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
    fun venuListBarCrwl(map: HashMap<String, String>): LiveData<ApiSampleResource<AllBarCrwalListResponse>> {
        barCrwlListModel = webServiceRepository.barCrwlVenuList(map)
        return barCrwlListModel
    }
    fun userDevice(map:HashMap<String,String>): LiveData<ApiSampleResource<BaseModel>> {
        userDeviceModel = webServiceRepository.userDevice(map)
        return userDeviceModel
    }
    fun panic( map:HashMap<String,String>): LiveData<ApiSampleResource<BaseModel>> {
        userDeviceModel = webServiceRepository.panicNoty(map)
        return userDeviceModel
    }

    fun lostChooseVenues( ): LiveData<ApiSampleResource<LostItemChooseVenuResponse>> {
        lostItemChooseVenuResponse = webServiceRepository.lostChooseVenues()
        return lostItemChooseVenuResponse
    }

    fun searchCity( ): LiveData<ApiSampleResource<SearchCityResponse>> {
        searchCityResponse = webServiceRepository.searchCity()
        return searchCityResponse
    }

    fun notificationList( ): LiveData<ApiSampleResource<NotificationResponse>> {
        notificationResponse = webServiceRepository.notificationList()
        return notificationResponse
    }
     fun panicHistory( ): LiveData<ApiSampleResource<PanicHistoryRes>> {
         panicHistoryRes = webServiceRepository.panicHisrtyList()
        return panicHistoryRes
    }

    fun createBarCrwalWidImg(requestBody: MultipartBody): LiveData<ApiSampleResource<CreateUpdateBarcrwalResponse>> {
        createUpdateBarcrwalResponse = webServiceRepository.createBarCrwalWidImg(requestBody)
        return createUpdateBarcrwalResponse
    }

    fun shareBarCrwal( map:HashMap<String,String>): LiveData<ApiSampleResource<BarcrwalCreatedRes>> {
        barcrwalCreatedViewModel = webServiceRepository.shareBarCrwal(map)
        return barcrwalCreatedViewModel
    }



    fun setEndLoc(map: HashMap<String, String>): LiveData<ApiSampleResource<SetEndLocModel>> {
        setEndLocModel = webServiceRepository.setEndLoc(map)
        return setEndLocModel
    }

    fun getEndLoc(): LiveData<ApiSampleResource<SetEndLocModel>> {
        setEndLocModel = webServiceRepository.getEndLoc()
        return setEndLocModel
    }
    fun getSharedList(): LiveData<ApiSampleResource<SharedBarcrwalRes>> {
        getSharedViewModel = webServiceRepository.getSharedBarCrwalList()
        return getSharedViewModel
    }
    fun delSharedList(map: HashMap<String, String>): LiveData<ApiSampleResource<BaseModel>> {
        baseModel = webServiceRepository.delSharedBarCrwalList(map)
        return baseModel
    }
    fun getSavedList(map: HashMap<String, String>): LiveData<ApiSampleResource<BarcrwalSavedRes>> {
        getSavedViewModel = webServiceRepository.savedBarCrwalList(map)
        return getSavedViewModel
    }
    fun logoutUser(map: HashMap<String, String>): LiveData<ApiSampleResource<BaseModel>> {
        baseModel = webServiceRepository.logoutUser(map)
        return baseModel
    }

    fun preBook(jsonObject: JSONObject): LiveData<ApiSampleResource<BaseModel>> {
        baseModel = webServiceRepository.preeBook(jsonObject)
        return baseModel
    }

    fun emaiLSettings(map: HashMap<String, String>): LiveData<ApiSampleResource<NotifEmilSettingRes>> {
        notifEmilSettingViewModel = webServiceRepository.emaiLSetting(map)
        return notifEmilSettingViewModel
    }

    fun barCrwalInvitedList(): LiveData<ApiSampleResource<InvitedBarCrwlResponse>> {
        invitedBarCrwlViewMOdel = webServiceRepository.invitedBarCrwalList()
        return invitedBarCrwlViewMOdel
    }

    fun prebookedList(): LiveData<ApiSampleResource<PrebookedlistResponse>> {
        prebookedViewModel = webServiceRepository.preBookedList()
        return prebookedViewModel
    }
    fun prebookedCancel(map: HashMap<String, String>): LiveData<ApiSampleResource<PreBookCancelRes>> {
        prebookedCancelViewModel = webServiceRepository.preBookedCancel(map)
        return prebookedCancelViewModel
    }
    fun getFiterList(): LiveData<ApiSampleResource<FillterRes>> {
        fillterResViewModel = webServiceRepository.filterList()
        return fillterResViewModel
    }
    fun getAllUserList(): LiveData<ApiSampleResource<AllUserRes>> {
        allUserResViewModel = webServiceRepository.allUserList()
        return allUserResViewModel
    }
    fun ratingList(): LiveData<ApiSampleResource<ReviewListRes>> {
        reviewListModel = webServiceRepository.reviewList()
        return reviewListModel
    }

    fun addRating(map : HashMap<String, String>): LiveData<ApiSampleResource<BaseModel>> {
        addReviewModel = webServiceRepository.addReview(map)
        return addReviewModel
    }
    fun orderList(): LiveData<ApiSampleResource<MyOrderRes>> {
        myOrderResViewModel = webServiceRepository.myOrder()
        return myOrderResViewModel
    }
    fun uploadChatImg(requestBody: MultipartBody): LiveData<ApiSampleResource<ChatImgUploadResponse>> {
        chatUploadImg = webServiceRepository.chatUploadImg(requestBody)
        return chatUploadImg
    }

}