package com.nightout.vendor.services

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightout.R
import com.nightout.model.*
import com.nightout.utils.AppConstant
import com.nightout.utils.AppConstant.PrefsName.INTERNAL_ERROR
import com.nightout.utils.AppConstant.PrefsName.NO_INTERNET
import com.nightout.utils.AppConstant.PrefsName.PARSING_ERROR
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


    fun getContactFilter(jsonObject: JSONObject): LiveData<ApiSampleResource<ContactFillterModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<ContactFillterModel>>()
        if (networkHelper.isNetworkConnected()) {
            val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonObject.toString())
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.contactListFilter(body)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<ContactFillterModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Parsing_Problem), null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue( ApiSampleResource.error(response.code(), "You have no contact", null))
                        }
                        401->{
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(response.code(), application.resources.getString(R.string.Internal_server_error), null))
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(
            ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null)
        )
        return venueListResponseModel
    }

    fun aboutCMS(): LiveData<ApiSampleResource<AboutModelResponse>> {
        val venueListResponseModel =
            MutableLiveData<ApiSampleResource<AboutModelResponse>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.userPagesAPI()
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<AboutModelResponse>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,application.resources.getString(R.string.Parsing_Problem),                                    null))
                            }
                        }
                        401->{
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            venueListResponseModel.postValue(ApiSampleResource.error(
                                response.code(),
                                jsonObj.getString("message"),
                                null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error) ,
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(
                            ApiSampleResource.error(
                                INTERNAL_ERROR,
                                application.resources.getString(R.string.Network_Failure),
                                null
                            )
                        )
                    } else {
                        venueListResponseModel.postValue(
                            ApiSampleResource.error(
                                PARSING_ERROR,
                                application.resources.getString(R.string.Something_went_wrong),
                                null
                            )
                        )
                    }
                }

            })
        } else venueListResponseModel.postValue(
            ApiSampleResource.error(
                NO_INTERNET,
                application.resources.getString(R.string.No_Internet),
                null
            )
        )
        return venueListResponseModel
    }

    fun getEmergency(): LiveData<ApiSampleResource<GetEmergencyModel>> {
        val venueListResponseModel =
            MutableLiveData<ApiSampleResource<GetEmergencyModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.getEmergencyAPI()
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<GetEmergencyModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        401->{
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            venueListResponseModel.postValue(ApiSampleResource.error(
                                response.code(),
                                jsonObj.getString("message"),
                                null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(
            ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }

    fun getLostItemList(): LiveData<ApiSampleResource<GetLostItemListModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<GetLostItemListModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.userlostitemsAPI()
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<GetLostItemListModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    "Parsing Problem",
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue( ApiSampleResource.error(response.code(), "No Data Found!!", null))
                        }
                        401->{
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            venueListResponseModel.postValue(ApiSampleResource.error(
                                response.code(),
                                jsonObj.getString("message"),
                                null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                "Internal server error",
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, "Network Failure,Please try again", null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, "Something went wrong. Please contact with admin.", null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, "No internet connection", null))
        return venueListResponseModel
    }

    fun doLogin(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<LoginModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.loginAPI(map)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<LoginModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        201,205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }


    fun register(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<LoginModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.regAPI(map)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<LoginModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        201,205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }

    fun otp(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<LoginModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.otpAPI(map)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<LoginModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        201,205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }

    fun otpResend(map: HashMap<String, String>): LiveData<ApiSampleResource<LoginModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<LoginModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.otpResendAPI(map)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<LoginModel>(data)
                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        201,205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }

    fun dashBoard(): LiveData<ApiSampleResource<DashboardModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<DashboardModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.dashboardAPI()
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<DashboardModel>(data)

                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        201,205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }

    fun userVenueDetail(map: HashMap<String, String>): LiveData<ApiSampleResource<VenuDetailModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<VenuDetailModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.userVenueDetailAPI(map)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<VenuDetailModel>(data)

                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                        201,205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }

    fun userAddFav(map: HashMap<String, String>): LiveData<ApiSampleResource<AddFavModel>> {
        val venueListResponseModel = MutableLiveData<ApiSampleResource<AddFavModel>>()
        if (networkHelper.isNetworkConnected()) {
            val responseBody: Call<ResponseBody> = apiInterfaceHeader.addFavouriteAPI(map)
            responseBody.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when (response.code()) {
                        200 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<AddFavModel>(data)

                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }
                        201 -> {
                            val data = response.body()?.string()!!
                            try {
                                val dataResponse = fromJson<AddFavModel>(data)

                                venueListResponseModel.postValue(ApiSampleResource.success(response.code(),response.message(),dataResponse))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                venueListResponseModel.postValue(ApiSampleResource.error(
                                    PARSING_ERROR,
                                    application.resources.getString(R.string.Parsing_Problem),
                                    null))
                            }
                        }

                        204->{
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), application.resources.getString(R.string.No_data_found), null))
                        }
                         205,400,401,408,409-> {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            var vv=jsonObj.getString("message")
                            venueListResponseModel.postValue(ApiSampleResource.error(response.code(), jsonObj.getString("message"), null))
                        }
                        500->{
                            venueListResponseModel.postValue( ApiSampleResource.error(
                                response.code(),
                                application.resources.getString(R.string.Internal_server_error),
                                null))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t is IOException) {
                        venueListResponseModel.postValue(ApiSampleResource.error(INTERNAL_ERROR, application.resources.getString(R.string.Network_Failure), null))
                    } else {
                        venueListResponseModel.postValue(ApiSampleResource.error(PARSING_ERROR, application.resources.getString(R.string.Something_went_wrong), null))
                    }
                }

            })
        } else venueListResponseModel.postValue(ApiSampleResource.error(NO_INTERNET, application.resources.getString(R.string.No_Internet), null))
        return venueListResponseModel
    }


  /*  fun userAddFav(map: HashMap<String, Any>): LiveData<Resource<AddFavModel>> {
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
    }*/
}


