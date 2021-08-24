package com.nightout.vendor.services

data class Resource<out T>(val status: Status, val data: T?, val message: String?,val imgPath: String?) {

    companion object {

        fun <T> success(data: T?,imgpath: String): Resource<T> {
            return Resource(Status.SUCCESS, data, null,imgpath)
        }

        fun <T> error(msg: String, data: T?,): Resource<T> {
            return Resource(Status.ERROR, data,msg,null)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null,null)
        }

    }

}