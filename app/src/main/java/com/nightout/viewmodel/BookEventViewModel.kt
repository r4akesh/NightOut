package com.nightout.vendor.viewmodel


import android.app.Activity
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.nightout.model.BookEventMdlResponse
import com.nightout.vendor.services.Resource
import com.nightout.vendor.services.WebServiceRepository


class BookEventViewModel(activity: Activity) : BaseObservable() {
    private val webServiceRepository: WebServiceRepository = WebServiceRepository(activity)
    lateinit var venuListModel: LiveData<Resource<BookEventMdlResponse>>







}