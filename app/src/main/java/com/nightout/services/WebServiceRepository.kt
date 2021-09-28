package com.nightout.vendor.services

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightout.model.*
import com.nightout.utils.AppConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class WebServiceRepository(application: Activity) {
    private var apiInterface: APIInterface = APIClient.makeRetrofitService()
    private var apiInterfaceHeader: APIInterface = APIClient.makeRetrofitServiceHeader()
    private var networkHelper: NetworkHelper = NetworkHelper(application)
    var application = application

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }


    fun login(map: HashMap<String, Any>): LiveData<Resource<LoginModel>> {
        val userLogin = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            userLogin.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterface.loginAPI(map)
                if (request.isSuccessful) {
                    userLogin.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userLogin.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        userLogin.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else userLogin.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
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
                    userReg.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userReg.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )

                    } catch (e: Exception) {
                        userReg.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else userReg.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
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
                    userOtp.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userOtp.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )

                    } catch (e: Exception) {
                        userOtp.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else userOtp.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
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
                    userOtp.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("json error>>>>>>>>>>>$jsonObj")
                        userOtp.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        userOtp.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else userOtp.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return userOtp
    }


    fun dashBoard(): LiveData<Resource<DashboardModel>> {
        val dashboardRes = MutableLiveData<Resource<DashboardModel>>()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                dashboardRes.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    val request = apiInterfaceHeader.dashboardAPI()
                    if (request.isSuccessful) {
                        dashboardRes.postValue(
                            Resource.success(
                                request.code(),
                                request.body()!!.message,
                                request.body(),
                                request.body()!!.image_path
                            )
                        )
                    } else {
                        try {
                            val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                            println("ok jsonObj$jsonObj")
                            dashboardRes.postValue(
                                Resource.error(
                                    request.code(),
                                    jsonObj.getString("message"),
                                    null
                                )
                            )
                        } catch (e: Exception) {
                            dashboardRes.postValue(Resource.error(0, e.toString(), null))
                        }

                    }
                } else dashboardRes.postValue(
                    Resource.error(
                        AppConstant.PrefsName.NO_INTERNET,
                        "No internet connection",
                        null
                    )
                )
            }
        } catch (e: Exception) {
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
                    dashboardRes.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else
                dashboardRes.postValue(
                    Resource.error(
                        AppConstant.PrefsName.NO_INTERNET,
                        "No internet connection",
                        null
                    )
                )
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
                    dashboardRes.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else
                dashboardRes.postValue(
                    Resource.error(
                        AppConstant.PrefsName.NO_INTERNET,
                        "No internet connection",
                        null
                    )
                )
        }
        return dashboardRes
    }

  /*  fun userVenueList2(map: HashMap<String, String>): LiveData<Resource<VenuListModel>> {
        val dashboardRes = MutableLiveData<Resource<VenuListModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            dashboardRes.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.venuListAPI(map)
                if (request.isSuccessful) {
                    dashboardRes.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )

                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else
                dashboardRes.postValue(
                    Resource.error(
                        AppConstant.PrefsName.NO_INTERNET,
                        "No internet connection",
                        null
                    )
                )
        }
        return dashboardRes
    }*/

    fun userVenueList(map: HashMap<String, String>): LiveData<Resource<VenuListModel>> {
        val openCloseResponseModel = MutableLiveData<Resource<VenuListModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.venuListAPI(map)
                request.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val data = response.body()?.string()
                        if (data != null) {
                            val dataResponse = fromJson<VenuListModel>(data)
                            openCloseResponseModel.postValue(Resource.success(response.code(), response!!.message(), dataResponse, ""))
                        } else {
                            openCloseResponseModel.postValue(Resource.success(response.code(), response.message(), null, ""))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        if (t is IOException) {
                            openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.INTERNAL_ERROR, "Network Failure,Please try again", null))
                        } else {
                            openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.PARSING_ERROR, "Something went wrong. Please contact with admin.", null))
                        }
                    }

                })
            } else {
                openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.NO_INTERNET, "No internet connection", null))
            }
        }
        return openCloseResponseModel
    }
    fun updateProfile(requestBody: MultipartBody): LiveData<Resource<LoginModel>> {
        val updateProfileResponse = MutableLiveData<Resource<LoginModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.updateProfileAPI(requestBody)
                if (request.isSuccessful) {
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return updateProfileResponse
    }

    fun submitLostItem(requestBody: MultipartBody): LiveData<Resource<BaseModel>> {
        val updateProfileResponse = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.addlostitemAPI(requestBody)
                if (request.isSuccessful) {
                    var vv = request.code()
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
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
                    dashboardRes.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        println("ok jsonObj$jsonObj")
                        dashboardRes.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        dashboardRes.postValue(Resource.error(0, e.toString(), null))
                    }

                }
            } else dashboardRes.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return dashboardRes
    }

    fun delLostItem(map: HashMap<String, String>): LiveData<Resource<BaseModel>> {
        val updateProfileResponse = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.delItemsAPI(map)
                if (request.isSuccessful && request.code() == 200) {
                    var vv = request.code()
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return updateProfileResponse
    }

    fun foundLostItem(map: HashMap<String, String>): LiveData<Resource<BaseModel>> {
        val updateProfileResponse = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.foundItemsAPI(map)
                if (request.isSuccessful) {
                    var vv = request.code()
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return updateProfileResponse
    }

    fun getContactFilter(jsonObject: JSONObject): LiveData<Resource<ContactFillterModel>> {
        val dashboardRes = MutableLiveData<Resource<ContactFillterModel>>()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                dashboardRes.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        jsonObject.toString()
                    )
                    val request = apiInterfaceHeader.contactListFilter(body)
                    if (request.isSuccessful) {
                        if (request.code() == 200) {
                            dashboardRes.postValue(
                                Resource.success(
                                    request.code(),
                                    request.body()!!.message,
                                    request.body(),
                                    ""
                                )
                            )
                        } else {
                            try {
                                val jsonObj =
                                    JSONObject(request.errorBody()!!.charStream().readText())
                                dashboardRes.postValue(
                                    Resource.error(
                                        request.code(),
                                        jsonObj.getString("message"),
                                        null
                                    )
                                )
                            } catch (e: Exception) {
                                dashboardRes.postValue(Resource.error(0, e.toString(), null))
                            }
                        }
                    } else {
                        try {
                            val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                            println("ok jsonObj$jsonObj")
                            dashboardRes.postValue(
                                Resource.error(
                                    request.code(),
                                    jsonObj.getString("message"),
                                    null
                                )
                            )
                        } catch (e: Exception) {
                            dashboardRes.postValue(Resource.error(0, e.toString(), null))
                        }
                    }
                } else dashboardRes.postValue(
                    Resource.error(
                        AppConstant.PrefsName.NO_INTERNET,
                        "No internet connection",
                        null
                    )
                )
            }
        } catch (e: Exception) {
        }
        return dashboardRes
    }


    fun saveEmergency(map: HashMap<String, String>): LiveData<Resource<BaseModel>> {
        val updateProfileResponse = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.saveEmergencyAPI(map)
                if (request.isSuccessful) {
                    var vv = request.code()
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return updateProfileResponse
    }

    fun getEmergency(): LiveData<Resource<GetEmergencyModel>> {
        val updateProfileResponse = MutableLiveData<Resource<GetEmergencyModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.getEmergencyAPI()
                if (request.isSuccessful) {
                    request.code()
                    var vv = request.code()
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return updateProfileResponse
    }

    fun delEmergency(map: HashMap<String, String>): LiveData<Resource<BaseModel>> {
        val updateProfileResponse = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.delEmergencyAPI(map)
                if (request.isSuccessful) {
                    var vv = request.code()
                    updateProfileResponse.postValue(
                        Resource.success(
                            request.code(),
                            request.body()!!.message,
                            request.body(),
                            ""
                        )
                    )
                } else {
                    try {
                        val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                        updateProfileResponse.postValue(
                            Resource.error(
                                request.code(),
                                jsonObj.getString("message"),
                                null
                            )
                        )
                    } catch (e: Exception) {
                        updateProfileResponse.postValue(Resource.error(0, e.toString(), null))
                    }
                }
            } else updateProfileResponse.postValue(
                Resource.error(
                    AppConstant.PrefsName.NO_INTERNET,
                    "No internet connection",
                    null
                )
            )
        }
        return updateProfileResponse
    }
    /*  fun sendQueryOld(map: HashMap<String, Any>): LiveData<Resource<BaseModel>> {
          val dashboardRes = MutableLiveData<Resource<BaseModel>>()
          try {
              CoroutineScope(Dispatchers.IO).launch {
                  dashboardRes.postValue(Resource.loading(null))
                  if (networkHelper.isNetworkConnected()) {
                     // val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonObject.toString())
                      val request = apiInterfaceHeader.sendQueryAPI(map)
                      if (request.isSuccessful) {
                              dashboardRes.postValue(Resource.success(request.code(),request.body()!!.message,request.body(),""))

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
          } catch (e: Exception) {
          }
          return dashboardRes
      }*/

    fun sendQuery(map: HashMap<String, Any>): LiveData<Resource<BaseModel>> {
        val openCloseResponseModel = MutableLiveData<Resource<BaseModel>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.sendQueryAPI(map)
                request.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val data = response.body()?.string()
                        if (data != null) {
                            val dataResponse = fromJson<BaseModel>(data)
                            openCloseResponseModel.postValue(Resource.success(response.code(), response!!.message(), dataResponse, ""))
                        } else {
                            openCloseResponseModel.postValue(Resource.success(response.code(), response.message(), null, ""))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        if (t is IOException) {
                            openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.INTERNAL_ERROR, "Network Failure,Please try again", null))
                        } else {
                            openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.PARSING_ERROR, "Something went wrong. Please contact with admin.", null))
                        }
                    }
                })
            } else {
                openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.NO_INTERNET, "No internet connection", null))
            }
        }
        return openCloseResponseModel
    }

    fun aboutCMS(): LiveData<Resource<AboutModelResponse>> {
        val dashboardRes = MutableLiveData<Resource<AboutModelResponse>>()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                dashboardRes.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    val request = apiInterfaceHeader.userPagesAPI()
                    if (request.isSuccessful) {
                        dashboardRes.postValue(
                            Resource.success(request.code(), request.body()!!.message, request.body(), ""))
                    } else {
                        try {
                            val jsonObj = JSONObject(request.errorBody()!!.charStream().readText())
                            println("ok jsonObj$jsonObj")
                            dashboardRes.postValue(Resource.error(request.code(), jsonObj.getString("message"), null))
                        } catch (e: Exception) {
                            dashboardRes.postValue(Resource.error(0, e.toString(), null))
                        }
                    }
                } else dashboardRes.postValue(
                    Resource.error(AppConstant.PrefsName.NO_INTERNET, "No internet connection", null))
            }
        } catch (e: Exception) {
        }
        return dashboardRes
    }
    fun eventBook(map: HashMap<String, String>): LiveData<Resource<BookEventMdlResponse>> {
        val openCloseResponseModel = MutableLiveData<Resource<BookEventMdlResponse>>()
        CoroutineScope(Dispatchers.IO).launch {
            if (networkHelper.isNetworkConnected()) {
                val request = apiInterfaceHeader.bookEventTicketAPI(map)
                request.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val data = response.body()?.string()
                        if (data != null) {
                            val dataResponse = fromJson<BookEventMdlResponse>(data)
                            openCloseResponseModel.postValue(Resource.success(response.code(), response!!.message(), dataResponse, ""))
                        } else {
                            openCloseResponseModel.postValue(Resource.success(response.code(), response.message(), null, ""))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        if (t is IOException) {
                            openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.INTERNAL_ERROR, "Network Failure,Please try again", null))
                        } else {
                            openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.PARSING_ERROR, "Something went wrong. Please contact with admin.", null))
                        }
                    }
                })
            } else {
                openCloseResponseModel.postValue(Resource.error(AppConstant.PrefsName.NO_INTERNET, "No internet connection", null))
            }
        }
        return openCloseResponseModel
    }

}


