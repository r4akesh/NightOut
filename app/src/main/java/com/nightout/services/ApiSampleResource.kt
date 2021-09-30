package com.nightout.vendor.services

import com.nightout.BuildConfig


data class ApiSampleResource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(httpCode: Int, msg: String, data: T?): ApiSampleResource<T> {
            var message = ""
            when (httpCode) {
                200 -> message = "Successfully"
                204 -> message = "Sorry Records Not Found."
                409 -> message = msg
            }

            return if (BuildConfig.DEBUG) {
                ApiSampleResource(Status.SUCCESS, data, message)
            } else {
                ApiSampleResource(Status.SUCCESS, data, msg)
            }

        }

        fun <T> error(httpCode: Int, msg: String, data: T?): ApiSampleResource<T> {
            var message = ""
            when (httpCode) {
                502 -> message = "No Internet connection,Please check Internet connection."
                500 -> message = "Server seems busy.Please try after sometime."
                400 -> message = "Something went wrong."
                201 -> message = "Your are registered successfully"
                401 -> message = msg
              //  204 -> message = "no content"
                204 -> message = msg
                504 -> message = "Parsing Problem Please contact to backend Developer"


            }

            return if (BuildConfig.DEBUG) {
                ApiSampleResource(Status.ERROR, data, message)
            } else {
                ApiSampleResource(Status.ERROR, data, msg)
            }

        }



    }

}