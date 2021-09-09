package com.nightout.vendor.services

import com.nightout.model.*
import okhttp3.MultipartBody
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
    suspend fun venuListAPI(@Body params: HashMap<String, String>): Response<VenuListModel>

    @POST("profile_update")
    suspend fun updateProfileAPI(@Body requestBody: MultipartBody): Response<LoginModel>

    @POST("add_favourite")
    suspend fun addFavouriteAPI(@Body params : HashMap<String, Any>): Response<AddFavModel>

    @POST("add_lost_item")
    suspend fun addlostitemAPI(@Body requestBody: MultipartBody): Response<BaseModel>

    @POST("user_lost_items")
    suspend fun userlostitemsAPI(): Response<GetLostItemListModel>
}