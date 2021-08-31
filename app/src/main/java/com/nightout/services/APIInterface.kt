package com.nightout.vendor.services

import com.nightout.model.DashboardModel
import com.nightout.model.LoginModel
import com.nightout.model.VenuDetailModel
import com.nightout.model.VenuListModel
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
}