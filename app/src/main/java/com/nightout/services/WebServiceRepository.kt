package com.nightout.vendor.services

import android.app.Application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nightout.model.LoginModel
import com.nightout.model.RegisterModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class WebServiceRepository(application: Application) {
    private var apiInterface: APIInterface = APIClient.makeRetrofitService()
    private var networkHelper:NetworkHelper = NetworkHelper(application)


    fun login(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        val userLogin = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userLogin.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.loginAPI(map)
                if (request.isSuccessful) {
                    userLogin.postValue(Resource.success(request.body()))
                } else {
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("json error>>>>>>>>>>>$jsonObj")
                    userLogin.postValue(Resource.error(jsonObj.getString("message"), null)
                )}
            }else userLogin.postValue(Resource.error("No internet connection", null))
        }
        return userLogin
    }

    fun register(map: HashMap<String, Any>): LiveData<Resource<RegisterModel>> {
        val userReg = MutableLiveData<Resource<RegisterModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userReg.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.regAPI(map)
                if (request.isSuccessful) {
                    userReg.postValue(Resource.success(request.body()))
                } else {
                    val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                    println("json error>>>>>>>>>>>$jsonObj")
                    userReg.postValue(Resource.error(jsonObj.getString("message"), null)
                    )}
            }else userReg.postValue(Resource.error("No internet connection", null))
        }
        return userReg
    }
}


