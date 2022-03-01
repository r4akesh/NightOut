package com.nightout.vendor.services

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nightout.BuildConfig
import com.nightout.utils.PreferenceKeeper
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object APIClient {
    private const val BASE_URL_CHAT = "http://testapi.newdevpoint.in/"
    const val IMAGE_URL = "http://testapi.newdevpoint.in/"
    const val BASE_URL_WEB_SOCKET = "ws://sschat-react.herokuapp.com/V1"
    private var retrofit: Retrofit? = null







    //chat
    fun getClient(): Retrofit? {
        if (retrofit == null) {
            val httpBuilder = OkHttpClient.Builder()
            httpBuilder.connectTimeout(60, TimeUnit.SECONDS)
            httpBuilder.readTimeout(10, TimeUnit.MINUTES)
            httpBuilder.writeTimeout(10, TimeUnit.MINUTES)
            httpBuilder.retryOnConnectionFailure(true)
            //            httpBuilder.addInterceptor(new CustomInterceptor(""));
            val okHttpClient = httpBuilder.build()

            //init retrofit
            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL_CHAT) //                    .addConverterFactory(ScalarsConverterFactory.signUp())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    object KeyConstant {
        const val REQUEST_TYPE_KEY = "request"
        const val REQUEST_TYPE_LOGIN = "login"
        const val REQUEST_TYPE_CREATE_CONNECTION = "create_connection"
        const val REQUEST_TYPE_ROOM = "room"
        const val REQUEST_TYPE_GROUP_ROOM = "group"
        const val REQUEST_TYPE_USERS = "users"
        const val REQUEST_TYPE_MESSAGE = "message"
        const val REQUEST_TYPE_BLOCK_USER = "block_user"
    }


}