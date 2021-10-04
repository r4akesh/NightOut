package com.nightout.vendor.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.R
import com.nightout.model.DashboardModel
import com.nightout.model.LoginModel
import com.nightout.model.VenuListModel
import com.nightout.ui.activity.LoginActivity
import com.nightout.utils.Utills


import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository



class VenuListViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var venuListModel: LiveData<Resource<VenuListModel>>




    /*fun venulistData(storeType : HashMap<String,String>): LiveData<Resource<VenuListModel>> {
        venuListModel = webServiceRepository.userVenueList(storeType)
        return venuListModel
    }*/


}