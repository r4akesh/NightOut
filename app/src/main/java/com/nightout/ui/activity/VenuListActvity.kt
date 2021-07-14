package com.nightout.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuSubAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenulistingActivityBinding
import com.nightout.model.VenuListModel
import com.nightout.model.VenuModel

class VenuListActvity : BaseActivity() {
    lateinit var binding: VenulistingActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
   // lateinit var venuAdapterAdapter: VenuAdapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@VenuListActvity, R.layout.venulisting_activity)

        setTopListTopDummy()
        setTopListSubDummy()
    }

    lateinit var venuSubAdapter: VenuSubAdapter
    private fun setTopListSubDummy() {
        var list = ArrayList<VenuListModel>()
        list.add(VenuListModel("Vanity Night Club","",R.drawable.venusub_img1))
        list.add(VenuListModel("Feel the Beat","",R.drawable.venusub_img2))
        list.add(VenuListModel("Vanity Night Club","",R.drawable.venusub_img3))

        venuSubAdapter = VenuSubAdapter(this@VenuListActvity,list,object:VenuSubAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.venulistingRecyclersub.also {
            it.layoutManager = LinearLayoutManager(this@VenuListActvity,LinearLayoutManager.VERTICAL,false)
            it.adapter = venuSubAdapter
        }
    }


    private fun setTopListTopDummy() {
         var list = ArrayList<VenuModel>()
        list.add(VenuModel("Club",true))
        list.add(VenuModel("Bar",false))
        list.add(VenuModel("Pub",false))
        list.add(VenuModel("Food",false))
        list.add(VenuModel("Food2",false))

        venuAdapterAdapter = VenuAdapterAdapter(this@VenuListActvity,list,object : VenuAdapterAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })
        binding.venulistingToprecycler.also {
            it.layoutManager = LinearLayoutManager(this@VenuListActvity,LinearLayoutManager.HORIZONTAL,false)
            it.adapter = venuAdapterAdapter
        }

    }
}