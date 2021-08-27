package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuSubAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenulistingActivityBinding
import com.nightout.model.*
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.VenuListViewModel


class VenuListActvity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: VenulistingActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
    var isListShow = true
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var venuListModel: VenuListViewModel
    private var customProgressDialog = CustomProgressDialog()
    var storeType = ""
    var listStoreType = ArrayList<VenuModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@VenuListActvity, R.layout.venulisting_activity)
        setListStoreType()
        initView()
        setToolBar()
        venue_type_listAPICALL(storeType)


    }

    private fun initView() {
        venuListModel = VenuListViewModel(this@VenuListActvity)
        supportMapFragment = (supportFragmentManager.findFragmentById(R.id.venulistingMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@VenuListActvity)
        supportMapFragment.view?.visibility = GONE
        storeType = intent.getStringExtra(AppConstant.INTENT_EXTRAS.StoreType)!!
        //store_type => required (1=>Bar, 2=>Pub, 3=>Club, 4=>Food, 5=>Event)
        when(storeType){
            "1"->{
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 1 == i
                }
              // venuAdapterAdapter.notifyDataSetChanged()
               venuAdapterAdapter.setData(listStoreType)
            }
            "2"->{
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 2 == i
                }
                // venuAdapterAdapter.notifyDataSetChanged()
                venuAdapterAdapter.setData(listStoreType)
            }
            "3"->{
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 0 == i
                }
                // venuAdapterAdapter.notifyDataSetChanged()
                venuAdapterAdapter.setData(listStoreType)
            }
            "4"->{
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 3 == i
                }
                // venuAdapterAdapter.notifyDataSetChanged()
                venuAdapterAdapter.setData(listStoreType)
            }
            "5"->{
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 4 == i
                }
                // venuAdapterAdapter.notifyDataSetChanged()
                venuAdapterAdapter.setData(listStoreType)
            }
        }

    }

    private fun venue_type_listAPICALL(storeType: String) {
        var map = HashMap<String,String>()
        map["store_type"] = storeType

        customProgressDialog.show(this@VenuListActvity)
        venuListModel.venulistData(map).observe(this@VenuListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.hide()
                    it.data?.let {
                       setListVenu(it.data)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    customProgressDialog.dialog.hide()
                }
            }
        })
    }

    private fun setListVenu(dataList: ArrayList<VenuListModel.Data>) {
        venuSubAdapter =
            VenuSubAdapter(this@VenuListActvity, dataList, object : VenuSubAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })

        binding.venulistingRecyclersub.also {
            it.layoutManager = LinearLayoutManager(
                this@VenuListActvity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter = venuSubAdapter
        }
    }

    private fun setToolBar() {
        setTouchNClick(binding.venulistingToolBar.toolbarBack)
        setTouchNClick(binding.venulistingToolBar.toolbar3dot)


        binding.venulistingToolBar.toolbarBack.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }

        binding.venulistingToolBar.toolbar3dot.setOnClickListener {
            if (isListShow) {
                isListShow = false
                supportMapFragment.view?.visibility = VISIBLE
                binding.venulistingToolBar.toolbar3dot.setImageResource(R.drawable.listtop_ic)

                binding.venulistingRecyclersub.visibility = GONE
            } else {
                isListShow = true
                binding.venulistingRecyclersub.visibility = VISIBLE
                supportMapFragment.view?.visibility = GONE
                binding.venulistingToolBar.toolbar3dot.setImageResource(R.drawable.maptop_ic)
            }

        }
    }


    lateinit var venuSubAdapter: VenuSubAdapter
    private fun setTopListSubDummy() {
/*        var list = ArrayList<VenuListModel>()
        list.add(VenuListModel("Vanity Night Club", "", R.drawable.venusub_img1))
        list.add(VenuListModel("Feel the Beat", "", R.drawable.venusub_img2))
        list.add(VenuListModel("Vanity Night Club", "", R.drawable.venusub_img3))

        venuSubAdapter =
            VenuSubAdapter(this@VenuListActvity, list, object : VenuSubAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    startActivity(Intent(this@VenuListActvity, StoreDetail::class.java))
                    overridePendingTransition(0, 0)
                }

            })

        binding.venulistingRecyclersub.also {
            it.layoutManager = LinearLayoutManager(
                this@VenuListActvity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter = venuSubAdapter
        }*/
    }


    private fun setListStoreType() {
          listStoreType = ArrayList<VenuModel>()
        listStoreType.add(VenuModel(3,"Club", false))
        listStoreType.add(VenuModel(1,"Bar", false))
        listStoreType.add(VenuModel(2,"Pub", false))
        listStoreType.add(VenuModel(4,"Food", false))
        listStoreType.add(VenuModel(5,"Event", false))
        // venuAdapterAdapter = PromotionAdapter(list)

        venuAdapterAdapter = VenuAdapterAdapter(this@VenuListActvity, listStoreType,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until listStoreType.size) {
                        listStoreType[i].isSelected = pos == i
                    }
                    venuAdapterAdapter.notifyDataSetChanged()
                }

            })
        binding.venulistingToprecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@VenuListActvity, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = venuAdapterAdapter
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val success =
            googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}