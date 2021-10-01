package com.nightout.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nightout.model.AddFavModel
import com.nightout.model.DashboardModel
import com.nightout.model.LoginModel
import com.nightout.model.VenuDetailModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.Status
import com.nightout.vendor.services.WebServiceRepository

class DoFavViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var userVenueDetailModel: LiveData<Resource<AddFavModel>>


    /*fun doFavItem(map: HashMap<String, Any>): LiveData<Resource<AddFavModel>> {
        userVenueDetailModel = webServiceRepository.userAddFav(map)
        return userVenueDetailModel
    }*/




}