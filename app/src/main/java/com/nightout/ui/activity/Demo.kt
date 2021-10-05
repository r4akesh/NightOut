package com.nightout.ui.activity

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuDemoAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.DemoBinding
import com.nightout.model.VenuModel
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast


import android.app.Dialog
import android.graphics.Color
import android.view.View



class Demo : BaseActivity(){
      var listStoreType = ArrayList<VenuModel>()
    lateinit var venuDemoAdapter:VenuDemoAdapter
    lateinit var binding : DemoBinding
    var manager: DownloadManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@Demo, com.nightout.R.layout.demo)



    }

    private fun setListStoreTypeHr() {
        listStoreType = ArrayList()
        listStoreType.add(VenuModel(1, "Bars", false, isApiCall = false))
        listStoreType.add(VenuModel(2, "Pubs", false, isApiCall = false))
        listStoreType.add(VenuModel(3, "Clubs", false, isApiCall = false))
        listStoreType.add(VenuModel(4, "Food", false, isApiCall = false))
        listStoreType.add(VenuModel(5, "Event", false, isApiCall = false))

        listStoreType.add(VenuModel(1, "Bars2", false, isApiCall = false))
        listStoreType.add(VenuModel(2, "Pubs2", false, isApiCall = false))
        listStoreType.add(VenuModel(3, "Clubs2", false, isApiCall = false))
        listStoreType.add(VenuModel(4, "Food2", false, isApiCall = false))
        listStoreType.add(VenuModel(5, "Event2", false, isApiCall = false))

        listStoreType.add(VenuModel(1, "Bars3", false, isApiCall = false))
        listStoreType.add(VenuModel(2, "Pubs3", false, isApiCall = false))
        listStoreType.add(VenuModel(3, "Clubs3", false, isApiCall = false))
        listStoreType.add(VenuModel(4, "Food3", false, isApiCall = false))
        listStoreType.add(VenuModel(5, "Event3", false, isApiCall = false))

        listStoreType.add(VenuModel(1, "Bars4", false, isApiCall = false))
        listStoreType.add(VenuModel(2, "Pubs4", false, isApiCall = false))
        listStoreType.add(VenuModel(3, "Clubs4", false, isApiCall = false))
        listStoreType.add(VenuModel(4, "Food4", false, isApiCall = false))
        listStoreType.add(VenuModel(5, "Event4", false, isApiCall = false))

        listStoreType.add(VenuModel(1, "Bars5", false, isApiCall = false))
        listStoreType.add(VenuModel(2, "Pubs5", false, isApiCall = false))
        listStoreType.add(VenuModel(3, "Clubs5", false, isApiCall = false))
        listStoreType.add(VenuModel(4, "Food5", false, isApiCall = false))
        listStoreType.add(VenuModel(5, "Event5", false, isApiCall = false))



        venuDemoAdapter = VenuDemoAdapter(this, listStoreType, object : VenuDemoAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until listStoreType.size) {
                        if(i==pos){
                            listStoreType[i].isSelected=true
                            listStoreType[i].title = "r"+pos
                        }else{
                            listStoreType[i].isSelected=false
                        }

                    }
                    venuDemoAdapter.notifyDataSetChanged()


                }

            })

        binding.toprecycler.also {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = venuDemoAdapter
            //  venuAdapterAdapter.setData(listStoreType)
        }

    }
}