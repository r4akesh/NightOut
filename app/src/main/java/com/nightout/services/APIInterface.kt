package com.nightout.vendor.services

import com.nightout.model.LoginModel
import retrofit2.Response
import retrofit2.http.Body
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

}