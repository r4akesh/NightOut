package com.nightout.vendor.services

import com.nightout.model.LoginModel
import com.nightout.model.RegisterModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface APIInterface {
    @POST("login")
    suspend fun loginAPI(@Body params: HashMap<String, Any>): Response<LoginModel>


    @POST("register")
    suspend fun regAPI(@Body params: HashMap<String, Any>): Response<RegisterModel>

}