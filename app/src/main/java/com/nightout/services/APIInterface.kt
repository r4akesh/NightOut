package com.nightout.vendor.services

import com.nightout.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface APIInterface {
    @POST("login")
    suspend fun loginAPI(@Body params: HashMap<String, Any>): Response<LoginModel>


    @POST("register")
    suspend fun regAPI(@Body params: HashMap<String, Any>): Response<LoginModel>

    @POST("otp")
    suspend fun otpAPI(@Body params: HashMap<String, Any>): Response<LoginModel>

    @POST("resend_otp")
    suspend fun otpResendAPI(@Body params: HashMap<String, Any>): Response<LoginModel>

    @GET("dashboard")
    suspend fun dashboardAPI(): Response<DashboardModel>

    @POST("user_venue_detail")
    suspend fun userVenueDetailAPI(@Body params: HashMap<String, Any>): Response<VenuDetailModel>

    @POST("venue_type_list")
    fun venuListAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("profile_update")
    suspend fun updateProfileAPI(@Body requestBody: MultipartBody): Response<LoginModel>

    @POST("add_favourite")
    suspend fun addFavouriteAPI(@Body params : HashMap<String, Any>): Response<AddFavModel>

    @POST("add_update_lost_item")
    suspend fun addlostitemAPI(@Body requestBody: MultipartBody): Response<BaseModel>

    @POST("user_lost_items")
    suspend fun userlostitemsAPI(): Response<GetLostItemListModel>

    @POST("delete_lost_item")
    suspend fun delItemsAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("add_emergency_contact")
    suspend fun saveEmergencyAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("found_lost_item")
    suspend fun foundItemsAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("contact_list")
    suspend fun contactListFilter(@Body params: RequestBody): Response<ContactFillterModel>

    @GET("emergency_contact_list")
    suspend fun getEmergencyAPI(): Response<GetEmergencyModel>

    @POST("delete_emergency_contact")
    suspend fun delEmergencyAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("send_query")
     fun sendQueryAPI(@Body params: HashMap<String, Any>): Call<ResponseBody>

    @POST("user_pages")
    suspend fun userPagesAPI(): Response<AboutModelResponse>


}