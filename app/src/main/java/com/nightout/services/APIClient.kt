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
    const val BASE_URL = "https://nightout.ezxdemo.com/api/"

    private const val BASE_URL_CHAT = "http://testapi.newdevpoint.in/"
    const val IMAGE_URL = "http://testapi.newdevpoint.in/"
    const val BASE_URL_WEB_SOCKET = "ws://sschat-react.herokuapp.com/V1"
    private var retrofit: Retrofit? = null




    fun makeRetrofitServiceHeader(): APIInterface {
        //val token = PreferenceKeeper.instance.loginResponse?.token
        val token = PreferenceKeeper.instance.bearerTokenSave
        Log.d("ok", "Bearer $token")
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer $token")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        val client = httpClient
            .retryOnConnectionFailure(true)
            .connectTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(APIInterface::class.java)


    }


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