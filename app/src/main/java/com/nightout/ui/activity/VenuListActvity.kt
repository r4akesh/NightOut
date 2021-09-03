package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.VenuListViewModel
import com.nightout.viewmodel.DoFavViewModel


class VenuListActvity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: VenulistingActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
    var isListShow = true
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var venuListModel: VenuListViewModel
    private var customProgressDialog = CustomProgressDialog()
    var storeType = ""
    var listStoreType = ArrayList<VenuModel>()
    lateinit var venuSubAdapter: VenuSubAdapter
    private val progressDialog = CustomProgressDialog()
    lateinit var doFavViewModel : DoFavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@VenuListActvity, R.layout.venulisting_activity)
        setListStoreTypeHr()
        initView()
        setToolBar()


    }

    private fun initView() {
        doFavViewModel = DoFavViewModel(this@VenuListActvity)
        venuListModel = VenuListViewModel(this@VenuListActvity)
        supportMapFragment = (supportFragmentManager.findFragmentById(R.id.venulistingMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@VenuListActvity)
        supportMapFragment.view?.visibility = GONE
        storeType = intent.getStringExtra(AppConstant.INTENT_EXTRAS.StoreType)!!
        setSelectedStore(storeType)
    }

    private fun setSelectedStore(strType: String) {
        if(!storeType.equals("1")){
            binding.venulistingToprecycler.smoothScrollToPosition(venuAdapterAdapter.itemCount)
        }
        //store_type => required (1=>Bar, 2=>Pub, 3=>Club, 4=>Food, 5=>Event)
        when (strType) {
            "1" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 0 == i
                }

                venue_type_listAPICALL("1")
            }
            "2" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 1 == i
                }

                venue_type_listAPICALL("2")
            }
            "3" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 2 == i
                }

                venue_type_listAPICALL("3")
            }
            "4" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 3 == i
                }
                venue_type_listAPICALL("4")
            }
            "5" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 4 == i
                }
                venue_type_listAPICALL("5")
            }
        }

    }

   lateinit var venuDataList: ArrayList<VenuListModel.Data>
    private fun venue_type_listAPICALL(storeType: String) {
        var map = HashMap<String, String>()
        map["store_type"] = storeType

        customProgressDialog.show(this@VenuListActvity)
        venuListModel.venulistData(map).observe(this@VenuListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.hide()
                    it.data?.let {
                        venuDataList=it.data
                        setListVenu()
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    venuDataList = ArrayList<VenuListModel.Data>()
                    setListVenu()
                    customProgressDialog.dialog.hide()
                    Utills.showSnackBarOnError(
                        binding.constrentToolbar,
                        it.message!!,
                        this@VenuListActvity
                    )
                }
            }
        })
    }

    private fun setListVenu() {
        venuSubAdapter =
            VenuSubAdapter(this@VenuListActvity, venuDataList, object : VenuSubAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    var vv=venuDataList[pos].venue_gallery
                    startActivity(
                        Intent(this@VenuListActvity, StoreDetail::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY,true)
                        .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" +venuDataList[pos].id )
                        .putExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST,venuDataList[pos].venue_gallery )
                    )
                }

                override fun onClickFav(pos: Int) {
                    add_favouriteAPICALL(pos)
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

    private fun add_favouriteAPICALL(pos:Int) {
        progressDialog.show(this@VenuListActvity, "")

        var fav = if(venuDataList[pos].favrouite.equals("1"))
            "0" //for opp value
        else
            "1"
        var map = HashMap<String, Any>()
        map["venue_id"] = venuDataList[pos].id
        map["vendor_id"] =venuDataList[pos].vendor_detail.id
        map["status"] = fav


        doFavViewModel.doFavItem(map).observe(this@VenuListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            Log.d("ok", "add_favouriteAPICALL: "+detailData.data.status)
                            if( detailData.data.status == "1"){
                                venuDataList[pos].favrouite = "1"
                                venuSubAdapter.notifyItemChanged(pos)
//                                favStatus="0"
//                                binding.storeDeatilFav.setImageResource(R.drawable.fav_selected)
                            }else{
                                venuDataList[pos].favrouite = "0"
                                venuSubAdapter.notifyItemChanged(pos)
//                                favStatus="1"
//                                binding.storeDeatilFav.setImageResource(R.drawable.fav_unselected)
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                }
            }
        })
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


    private fun setListStoreTypeHr() {
        listStoreType = ArrayList()
        listStoreType.add(VenuModel(1, "Bars", false))
        listStoreType.add(VenuModel(2, "Pubs", false))
        listStoreType.add(VenuModel(3, "Clubs", false))
        listStoreType.add(VenuModel(4, "Food", false))
        listStoreType.add(VenuModel(5, "Event", false))


        venuAdapterAdapter = VenuAdapterAdapter(this@VenuListActvity, listStoreType,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until listStoreType.size) {
                        listStoreType[i].isSelected = pos == i
                    }
                    venuAdapterAdapter.notifyDataSetChanged()
                    venue_type_listAPICALL(listStoreType[pos].id.toString())

                }

            })

        binding.venulistingToprecycler.also {
            it.layoutManager = LinearLayoutManager(this@VenuListActvity, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = venuAdapterAdapter
            //  venuAdapterAdapter.setData(listStoreType)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val success =
            googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}