package com.nightout.vendor.services

import android.app.Activity
import android.app.Application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nightout.R
import com.nightout.model.*
import com.nightout.model.BaseModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject


class WebServiceRepository(application: Activity) {
    private var apiInterface: APIInterface = APIClient.makeRetrofitService()
    private var apiInterfaceHeader: APIInterface = APIClient.makeRetrofitServiceHeader()
    private var networkHelper:NetworkHelper = NetworkHelper(application)
    var application = application

    fun login(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        val userLogin = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userLogin.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.loginAPI(map)
                if (request.isSuccessful) {
                    userLogin.postValue(Resource.success(request.body(),""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userLogin.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        userLogin.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else userLogin.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return userLogin
    }

    fun register(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        val userReg = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userReg.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.regAPI(map)
                if (request.isSuccessful) {
                    userReg.postValue(Resource.success(request.body(),""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userReg.postValue(Resource.error(jsonObj.getString("message"), null)
                        )
                    } catch (e: Exception) {
                        userReg.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else userReg.postValue(Resource.error("No internet connection", null))
        }
        return userReg
    }

    fun otp(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        val userOtp = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userOtp.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.otpAPI(map)
                if (request.isSuccessful) {
                    userOtp.postValue(Resource.success(request.body(),""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userOtp.postValue(Resource.error(jsonObj.getString("message"), null)
                        )
                    } catch (e: Exception) {
                        userOtp.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else userOtp.postValue(Resource.error("No internet connection", null))
        }
        return userOtp
    }

    fun otpResend(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        val userOtp = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userOtp.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.otpResendAPI(map)
                if (request.isSuccessful) {
                    userOtp.postValue(Resource.success(request.body(),""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userOtp.postValue(Resource.error(jsonObj.getString("message"), null)
                        )
                    } catch (e: Exception) {
                        userOtp.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else userOtp.postValue(Resource.error("No internet connection", null))
        }
        return userOtp
    }


    fun dashBoard(): LiveData<Resource<DashboardModel>> {
        val dashboardRes = MutableLiveData<Resource<DashboardModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            dashboardRes.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.dashboardAPI()
                if (request.isSuccessful) {
                    dashboardRes.postValue(Resource.success(request.body(), request.body()!!.image_path))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(e.toString(), null))
                    }

                }
            }else dashboardRes.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return dashboardRes
    }



    fun userVenueDetail(map: HashMap<String, Any>): LiveData<Resource<VenuDetailModel>> {
        val dashboardRes = MutableLiveData<Resource<VenuDetailModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            dashboardRes.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.userVenueDetailAPI(map)
                if (request.isSuccessful) {
                    dashboardRes.postValue(Resource.success(request.body(), ""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else
                dashboardRes.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return dashboardRes
    }

    fun userAddFav(map: HashMap<String, Any>): LiveData<Resource<AddFavModel>> {
        val dashboardRes = MutableLiveData<Resource<AddFavModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            dashboardRes.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.addFavouriteAPI(map)
                if (request.isSuccessful) {
                    dashboardRes.postValue(Resource.success(request.body(), ""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else
                dashboardRes.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return dashboardRes
    }

    fun userVenueList(map: HashMap<String, String>): LiveData<Resource<VenuListModel>> {
        val dashboardRes = MutableLiveData<Resource<VenuListModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            dashboardRes.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.venuListAPI(map)
                if (request.isSuccessful) {
                    dashboardRes.postValue(Resource.success(request.body(), ""))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(Resource.error(jsonObj.getString("message"), null)
                        )
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else
                dashboardRes.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return dashboardRes
    }

    fun updateProfile(requestBody: MultipartBody): LiveData<Resource<LoginModel>> {
        val updateProfileResponse = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.updateProfileAPI(requestBody)
                if (request.isSuccessful) {
                    updateProfileResponse.postValue(Resource.success(request.body(),""))
                } else
                {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else updateProfileResponse.postValue(Resource.error("No internet connection", null))
        }
        return updateProfileResponse
    }

    fun submitLostItem(requestBody: MultipartBody): LiveData<Resource<BaseModel>> {
        val updateProfileResponse = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.addlostitemAPI(requestBody)
                if (request.isSuccessful && request.code()==200) {
                   var vv = request.code()
                    updateProfileResponse.postValue(Resource.success(request.body(),""))
                } else
                {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(e.toString(), null))
                    }
                }
            }else updateProfileResponse.postValue(Resource.error("No internet connection", null))
        }
        return updateProfileResponse
    }

    fun getLostItemList(): LiveData<Resource<GetLostItemListModel>> {
        val dashboardRes = MutableLiveData<Resource<GetLostItemListModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            dashboardRes.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.userlostitemsAPI()
                if (request.isSuccessful) {
                    dashboardRes.postValue(Resource.success(request.body(), request.body()!!.image_path))
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(Resource.error(jsonObj.getString("message"), null))
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(e.toString(), null))
                    }

                }
            }else dashboardRes.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return dashboardRes
    }
}


