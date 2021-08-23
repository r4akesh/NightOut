package com.nightout.vendor.services

import android.app.Application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nightout.R
import com.nightout.model.DashboardModel

import com.nightout.model.LoginModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class WebServiceRepository(application: Application) {
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
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("json error>>>>>>>>>>>$jsonObj")
                    userLogin.postValue(Resource.error(jsonObj.getString("message"), null)
                )}
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
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("json error>>>>>>>>>>>$jsonObj")
                    userReg.postValue(Resource.error(jsonObj.getString("message"), null)
                    )}
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
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("json error>>>>>>>>>>>$jsonObj")
                    userOtp.postValue(Resource.error(jsonObj.getString("message"), null)
                    )}
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
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("json error>>>>>>>>>>>$jsonObj")
                    userOtp.postValue(Resource.error(jsonObj.getString("message"), null)
                    )}
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
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("ok jsonObj$jsonObj")
                    dashboardRes.postValue(Resource.error(jsonObj.getString("message"), null)
                    )}
            }else dashboardRes.postValue(Resource.error(application.resources.getString(R.string.No_Internet), null))
        }
        return dashboardRes
    }
}


