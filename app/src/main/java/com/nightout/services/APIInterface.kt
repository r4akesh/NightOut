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




















//    @POST("user_pages")
//    suspend fun userPagesAPI(): Response<AboutModelResponse>



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



    @POST("venue_type_list")
    fun venuListAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("profile_update")
      fun updateProfileAPI(@Body requestBody: MultipartBody): Call<ResponseBody>

    @POST("add_update_lost_item")
      fun addlostitemAPI(@Body requestBody: MultipartBody): Call<ResponseBody>

    @POST("delete_lost_item")
      fun delItemsAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("found_lost_item")
      fun foundItemsAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("add_emergency_contact")
      fun saveEmergencyAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("delete_emergency_contact")
      fun delEmergencyAPI(@Body params: HashMap<String, String>): Call<ResponseBody>


    @POST("send_query")
    fun sendQueryAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("book_event_ticket")
    fun bookEventTicketAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @GET("favourite_list")
    fun favouriteListAPi( ): Call<ResponseBody>

    @POST("add_remove_bar_crawl")
    fun addRemBrCrwlAPI(@Body params: HashMap<String, String>):  Call<ResponseBody>

    @GET("bar_crawl_list")
    fun bar_crawl_listAPI():  Call<ResponseBody>
}