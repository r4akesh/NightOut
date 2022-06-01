package com.nightout.viewmodel

import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.BaseModel
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository

class SaveEmngyPhNoViewModel (activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var dashboardModel: LiveData<Resource<BaseModel>>







}