package com.nightout.vendor.services

import com.nightout.model.AddFavModel
import com.nightout.model.BaseModel
import com.nightout.model.LoginModel
import com.nightout.model.VenuDetailModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface APIInterface {



    @POST("venue_type_list")
    fun venuListAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("profile_update")
    suspend fun updateProfileAPI(@Body requestBody: MultipartBody): Response<LoginModel>



    @POST("add_update_lost_item")
    suspend fun addlostitemAPI(@Body requestBody: MultipartBody): Response<BaseModel>


    @POST("delete_lost_item")
    suspend fun delItemsAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("add_emergency_contact")
    suspend fun saveEmergencyAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("found_lost_item")
    suspend fun foundItemsAPI(@Body params: HashMap<String, String>): Response<BaseModel>


    @POST("delete_emergency_contact")
    suspend fun delEmergencyAPI(@Body params: HashMap<String, String>): Response<BaseModel>

    @POST("send_query")
    fun sendQueryAPI(@Body params: HashMap<String, Any>): Call<ResponseBody>

//    @POST("user_pages")
//    suspend fun userPagesAPI(): Response<AboutModelResponse>

    @POST("book_event_ticket")
    fun bookEventTicketAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("user_pages")
    fun userPagesAPI(): Call<ResponseBody>

    @POST("contact_list")
    fun contactListFilter(@Body params: RequestBody): Call<ResponseBody>

    @GET("emergency_contact_list")
    fun getEmergencyAPI(): Call<ResponseBody>

    @POST("user_lost_items")
    fun userlostitemsAPI(): Call<ResponseBody>

    @POST("login")
    fun loginAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("register")
    fun regAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("otp")
    fun otpAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("resend_otp")
    fun otpResendAPI(@Body params: HashMap<String, String>): Call<ResponseBody>


    @GET("dashboard")
    fun dashboardAPI(): Call<ResponseBody>


    @POST("user_venue_detail")
      fun userVenueDetailAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("add_favourite")
      fun addFavouriteAPI(@Body params: HashMap<String, String>):  Call<ResponseBody>
}