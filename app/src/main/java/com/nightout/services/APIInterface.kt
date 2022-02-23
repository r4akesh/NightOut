package com.nightout.vendor.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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

    @POST("all_contact_list")
    fun all_contact_listAPI(@Body params: RequestBody): Call<ResponseBody>


    @POST("dashboard")
    fun dashboardAPI(@Body params: RequestBody): Call<ResponseBody>

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




    @POST("user_venue_detail")
    fun userVenueDetailAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("add_favourite")
    fun addFavouriteAPI(@Body params: HashMap<String, String>): Call<ResponseBody>


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
    fun favouriteListAPi(): Call<ResponseBody>

    @POST("add_remove_bar_crawl")
    fun addRemBrCrwlAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

//    @GET("bar_crawl_list")
//    fun bar_crawl_listAPI(): Call<ResponseBody>

    @POST("user_device")
    fun user_deviceAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("panic_notification")
    fun panic_notificationAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("lost_item_venues")
    fun lost_item_venuesAPI(): Call<ResponseBody>

    @POST("user_notification")
    fun user_notificationAPI(): Call<ResponseBody>

    @GET("panic_history")
    fun panic_historyAPI(): Call<ResponseBody>

    @POST("create_update_bar_crawl")
    fun create_update_bar_crawlAPI(@Body requestBody: MultipartBody): Call<ResponseBody>

    @POST("bar_crawl_invitation")
    fun bar_crawl_invitationAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("set_end_location")
    fun set_end_locationAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @GET("get_end_location")
    fun get_end_locationAPI(): Call<ResponseBody>

    @GET("bar_crawl_invitation_list")
    fun bar_crawl_invitation_listAPI(): Call<ResponseBody>

    @GET("user_bar_crawl_list")
    fun getSavedBarCrawlList(): Call<ResponseBody>

    @POST("delete_bar_crawl")
    fun delete_bar_crawlAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("logout")
    fun logoutAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("pre_booking")
    fun pre_bookingAPI(@Body params: RequestBody): Call<ResponseBody>


    @GET("city_list")
    fun city_listAPI(): Call<ResponseBody>


    @POST("user_noti_email_status")
    fun user_noti_email_statusAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("all_venue_list")
    fun bar_crawl_listAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @GET("bar_crawl_invited_list")
    fun barcrawlinvitedlistAPI(): Call<ResponseBody>

    @GET("pre_booking_list")
    fun pre_booking_listAPI(): Call<ResponseBody>

    @GET("filter_list")
    fun filter_listAPI(): Call<ResponseBody>

    @GET("all_users")
    fun all_usersAPI(): Call<ResponseBody>

    @POST("pre_booking_cancel")
    fun pre_booking_cancelAPI(@Body params: HashMap<String, String>): Call<ResponseBody>

    @POST("make_payment")
    fun make_paymentAPI(@Body params: RequestBody): Call<ResponseBody>

    @GET("venue_reviews_remaning")
    fun venue_reviews_remaningAPI(): Call<ResponseBody>

    @GET("orders")
    fun ordersAPI(): Call<ResponseBody>

    @POST("add_venue_review")
    fun add_venue_reviewAPI(@Body params: HashMap<String, String>): Call<ResponseBody>
}