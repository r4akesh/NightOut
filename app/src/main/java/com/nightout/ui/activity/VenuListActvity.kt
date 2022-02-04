package com.nightout.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuSubAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenulistingActivityBinding
import com.nightout.model.*
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.CameraPosition

import com.google.android.gms.maps.model.PolylineOptions





class VenuListActvity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: VenulistingActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
    var isListShow = true
   lateinit var googleMap: GoogleMap
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var venuListModel: CommonViewModel
    private var customProgressDialog = CustomProgressDialog()
    lateinit var doAddBarCrawlModel : CommonViewModel
   // var storeType = ""
    var listStoreType = ArrayList<VenuModel>()
    lateinit var venuSubAdapter: VenuSubAdapter
    private val progressDialog = CustomProgressDialog()
    lateinit var doFavViewModel : CommonViewModel
    var selectedStrType="1"
    val REQCODE_STOREDETAILACTIVITY = 1002
    var addBarCrawlStatus="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@VenuListActvity, R.layout.venulisting_activity)
        setListStoreTypeHr()
        initView()
        setToolBar()

        binding.venulistingToolBar.searchLocationEditText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterData(p0.toString());
            }

        })
    }

    private fun filterData(searchText: String) {
        if(searchText.isBlank()){
            setListVenu()
        }else {
            val venuDataListTemp: ArrayList<VenuListModel.Data> = ArrayList()
            for (item in venuDataList) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.store_address.toLowerCase().contains(searchText.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    venuDataListTemp.add(item)
                }
            }
            if (venuDataListTemp.isEmpty()) {
                //Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                venuSubAdapter.filterList(venuDataListTemp)
            }
        }
    }

    private fun initView() {
        doFavViewModel = CommonViewModel(this@VenuListActvity)
        venuListModel = CommonViewModel(this@VenuListActvity)
        doAddBarCrawlModel = CommonViewModel(this@VenuListActvity)
        supportMapFragment = (supportFragmentManager.findFragmentById(R.id.venulistingMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@VenuListActvity)
        supportMapFragment.view?.visibility = GONE
        selectedStrType = intent.getStringExtra(AppConstant.INTENT_EXTRAS.StoreType)!!
        setSelectedStore(selectedStrType)
    }

    private fun setSelectedStore(strType: String) {
        if(!selectedStrType.equals("1")){
            binding.venulistingToprecycler.smoothScrollToPosition(venuAdapterAdapter.itemCount)
        }
        //store_type => required (1=>Bar, 2=>Pub, 3=>Club, 4=>Food, 5=>Event)
        when (strType) {
            "1" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 0 == i
                }
                binding.venulistingToolBar.toolbarTitle.text = "Bars"
                venue_type_listAPICALL()
                binding.venulistingToolBar.toolbarBell.visibility= GONE

            }
            "2" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 1 == i
                }
                binding.venulistingToolBar.toolbarTitle.text = "Pubs"
                venue_type_listAPICALL()
                binding.venulistingToolBar.toolbarBell.visibility= GONE

            }
            "3" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 2 == i
                }
                binding.venulistingToolBar.toolbarTitle.text = "Clubs"
                venue_type_listAPICALL()
                binding.venulistingToolBar.toolbarBell.visibility= GONE

            }
            "4" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 3 == i
                }
                binding.venulistingToolBar.toolbarTitle.text = "Food"
                venue_type_listAPICALL()
                binding.venulistingToolBar.toolbarBell.visibility= GONE

            }
            "5" -> {
                for (i in 0 until listStoreType.size) {
                    listStoreType[i].isSelected = 4 == i
                }
                binding.venulistingToolBar.toolbarTitle.text = "Events"
                venue_type_listAPICALL()

            }
        }

    }

   lateinit var venuDataList: ArrayList<VenuListModel.Data>
    private fun venue_type_listAPICALL() {
        var map = HashMap<String, String>()
        map["store_type"] = selectedStrType

        customProgressDialog.show(this@VenuListActvity)
        venuListModel.venulistData(map).observe(this@VenuListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.hide()
                    venuDataList = ArrayList()
                    setListVenu()//for empty the list
                    it.data?.let {
                        venuDataList=it.data
                        setListVenu()
                        if(!isListShow)
                        setMarker()
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    venuDataList = ArrayList()
                    setListVenu()
                    googleMap.clear()
                    customProgressDialog.dialog.hide()
                    Utills.showErrorToast(
                        this@VenuListActvity,
                        it.message!!

                    )
                }
            }
        })
    }
    var mapMarker = HashMap<String, Double>()
    private fun setMarker() {

            googleMap.clear()



        val builder = LatLngBounds.Builder()
        var latitudeUpdated: Double
        var longitudeUpdated: Double
        for (i in 0 until venuDataList.size){
            var mLat=Commons.strToDouble(venuDataList[i].store_lattitude)
            var mLang=Commons.strToDouble(venuDataList[i].store_longitude)
            if(MyApp.isValidLatLng(mLat,mLang)&& mLat>0.0 && mLang>0.0){
                var key= ""+mLat+mLang
                var offset = 0.0
                if (mapMarker.containsKey(key)) {
                    mapMarker[key]?.plus(0.00005)?.let { mapMarker.put(key, it) };//0.00045
                    offset = mapMarker[key]!!;
                } else {
                    mapMarker[key] = 0.0;
                }
                latitudeUpdated = offset+mLat
                longitudeUpdated = offset+mLang
                val positionAddrs = LatLng(latitudeUpdated, longitudeUpdated)
                val marker: Marker = googleMap!!.addMarker(MarkerOptions().position(positionAddrs))
                marker.title =venuDataList[i].store_name
                marker.snippet = venuDataList[i].store_address
                when {
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.BAR -> {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bar))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.PUB -> {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pub))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.CLUB -> {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_club))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.FOOD -> {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_food))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.EVENT -> {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_event))
                    }
                }
                builder.include(marker.position)
            }
            try {
                runOnUiThread(Runnable {
                    val bounds = builder.build()
                    val padding2 = 110
                    val cu = CameraUpdateFactory.newLatLngBounds(MyApp.adjustBoundsForMaxZoomLevel(bounds), padding2)
                    //googleMap.moveCamera(cu);
                    googleMap?.animateCamera(cu)
                })


            } catch (e: java.lang.Exception) {
                Log.e("Exception>>>", "" + e)
            }
            googleMap?.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener { marker ->
                var v = marker.id
                var v2 = marker.id
//                    System.out.println("snipptet>>" + arList.get(marker.snippet.toInt()))
//                    startActivity(Intent(THIS, MakeReservationStep3::class.java).putExtra(StaticData.SportCenter, arList.get(marker.snippet.toInt())))
            })
        }
    }

    var posSaveForUpdate=0
    private fun setListVenu() {
        venuSubAdapter = VenuSubAdapter(this@VenuListActvity, venuDataList, object : VenuSubAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    posSaveForUpdate = pos
                    var vv=venuDataList[pos].venue_gallery
                    if(selectedStrType == "5"){
                        startActivityForResult(Intent(this@VenuListActvity, EventDetailActivity::class.java)
                                .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                                .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + venuDataList[pos].id)
                                .putExtra(AppConstant.INTENT_EXTRAS.FAVROUITE_VALUE, venuDataList[pos].favrouite)
                            ,REQCODE_STOREDETAILACTIVITY)
                    }else {
                        startActivityForResult(
                            Intent(this@VenuListActvity, StoreDetailActvity::class.java)
                                .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                                .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + venuDataList[pos].id)
                                .putExtra(AppConstant.INTENT_EXTRAS.FAVROUITE_VALUE, venuDataList[pos].favrouite)
                                .putExtra(AppConstant.INTENT_EXTRAS.StoreType, selectedStrType)
                            ,REQCODE_STOREDETAILACTIVITY)
                    }
                }

                override fun onClickFav(pos: Int) {
                    add_favouriteAPICALL(pos)
                }

            override fun onClikSaveToBarcrewal(pos: Int) {
                addRemoveBarCrawlAPICall(pos)
            }

            override fun onClikAddrs(pos: Int) {
                try {
                    val lat= Commons.strToDouble(venuDataList[pos].store_lattitude)
                    val lang= Commons.strToDouble(venuDataList[pos].store_longitude)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lang"))
                    startActivity(intent)
                } catch (e: Exception) {
                    MyApp.popErrorMsg("","Address not valid",THIS!!)
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQCODE_STOREDETAILACTIVITY && resultCode==Activity.RESULT_OK){
            if(data?.getStringExtra("resultFav")!=null)
            venuDataList[posSaveForUpdate].favrouite = data?.getStringExtra("resultFav")!!
            var vv=data?.getStringExtra("resultBarcrwal")
             if (data?.getStringExtra("resultBarcrwal")!=null)
            venuDataList[posSaveForUpdate].barcrawl = data?.getStringExtra("resultBarcrwal")!!
            venuSubAdapter.notifyItemChanged(posSaveForUpdate)
        }
    }

    private fun addRemoveBarCrawlAPICall(pos: Int) {
        addBarCrawlStatus = if(venuDataList[pos].barcrawl == "0") "1" else "0"
       // progressDialog.show(this@VenuListActvity, "")
        var map = HashMap<String, String>()
        map["venue_id"] = venuDataList[pos].id
        map["vendor_id"] = venuDataList[pos].vendor_detail.id
        map["status"] =venuDataList[pos].barcrawl
        map["store_type"] =venuDataList[pos].store_type

        doAddBarCrawlModel.doAddBarCrawl(map).observe(this@VenuListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                 //   progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                           /* Log.d("ok", "add_favouriteAPICALL: " + detailData.data.status)
                            if (detailData.data.status == "1") {
                                addBarCrawlStatus = "0"
                                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)

                            } else {
                                addBarCrawlStatus = "1"
                                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)
                            }*/
                           // MyApp.ShowTost(this@VenuListActvity,detailData.message)
                        } catch (e: Exception) {
                           // MyApp.popErrorMsg("StoreDetail",""+e.toString(),this@VenuListActvity)
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                   // progressDialog.dialog.dismiss()
                   // Utills.showErrorToast(THIS!!,it.message!!)
                  //  Utills.showSnackBarOnError(binding.rootLayoutStorDetail, it.message!!, this@VenuListActvity)
                }
            }
        })

    }
    
    
    private fun add_favouriteAPICALL(pos:Int) {
       // progressDialog.show(this@VenuListActvity, "")

        var fav = if(venuDataList[pos].favrouite == "1")
            "1" //for opp value
        else
            "0"
        var map = HashMap<String, String>()
        map["venue_id"] = venuDataList[pos].id
        map["vendor_id"] =venuDataList[pos].vendor_detail.id
        map["status"] = fav


        doFavViewModel.doFavItem(map).observe(this@VenuListActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                  //  progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                       /* try {
                            Log.d("ok", "add_favouriteAPICALL: "+detailData.data.status)
                            if( detailData.data.status == "1"){
                                venuDataList[pos].favrouite = "1"
                                venuSubAdapter.notifyItemChanged(pos)
                            }else{
                                venuDataList[pos].favrouite = "0"
                                venuSubAdapter.notifyItemChanged(pos)
                            }
                        } catch (e: Exception) {
                        }*/
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                   // progressDialog.dialog.dismiss()
                    Utills.showErrorToast(this@VenuListActvity, it.message!!)
                }
            }
        })
    }

    private fun setToolBar() {
        setTouchNClick(binding.venulistingToolBar.toolbarBack)
        setTouchNClick(binding.venulistingToolBar.toolbar3dot)

         binding.venulistingToolBar.toolbarBell.visibility= VISIBLE
       //   binding.venulistingToolBar.toolbarBell.setImageResource(R.drawable.search_ic)
        binding.venulistingToolBar.toolbarBell.setImageResource(R.drawable.ic_search)
        binding.venulistingToolBar.toolbarBack.setOnClickListener {
            finish()
          //  overridePendingTransition(0, 0)
        }

        binding.venulistingToolBar.toolbarClose.setOnClickListener {
                binding.venulistingToolBar.toolbarSerchConstrent.visibility=GONE
                binding.venulistingToolBar.toolbarBell.visibility= VISIBLE
                binding.venulistingToolBar.toolbar3dot.visibility= VISIBLE

        }

        binding.venulistingToolBar.toolbarBell.setOnClickListener {
            if(binding.venulistingToolBar.toolbarSerchConstrent.visibility== VISIBLE){
                binding.venulistingToolBar.toolbarSerchConstrent.visibility = GONE
                binding.venulistingToolBar.toolbar3dot.visibility = VISIBLE
                binding.venulistingToolBar.toolbarBell.visibility = VISIBLE
            }else{
                binding.venulistingToolBar.toolbarSerchConstrent.visibility= VISIBLE
                binding.venulistingToolBar.toolbar3dot.visibility = GONE
                binding.venulistingToolBar.toolbarBell.visibility = GONE
            }
        }


        binding.venulistingToolBar.toolbar3dot.setOnClickListener {
            if (isListShow) {
                isListShow = false
                supportMapFragment.view?.visibility = VISIBLE
                binding.venulistingToolBar.toolbar3dot.setImageResource(R.drawable.listtop_ic)
                binding.venulistingRecyclersub.visibility = GONE


                Handler(getMainLooper()).postDelayed(Runnable { // set market after completely visible map
                    setMarker()
                }, 1000)




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
        listStoreType.add(VenuModel(1, "Bars", false, isApiCall = false))
        listStoreType.add(VenuModel(2, "Pubs", false, isApiCall = false))
        listStoreType.add(VenuModel(3, "Clubs", false, isApiCall = false))
        listStoreType.add(VenuModel(4, "Food", false, isApiCall = false))
        listStoreType.add(VenuModel(5, "Events", false, isApiCall = false))


        venuAdapterAdapter = VenuAdapterAdapter(this@VenuListActvity, listStoreType,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until listStoreType.size) {
                        listStoreType[i].isSelected = pos == i
                    }
                    binding.venulistingToolBar.toolbarTitle.setText(listStoreType[pos].title)
                    venuAdapterAdapter.notifyDataSetChanged()
                    selectedStrType=listStoreType[pos].id.toString()
                    venue_type_listAPICALL()
                  /*  if(pos==4){
                        binding.searchLocationEditText.visibility= VISIBLE
                     //   binding.venulistingToolBar.toolbarBell.visibility= VISIBLE
                    }else{
                        binding.searchLocationEditText.visibility= GONE
                     //   binding.venulistingToolBar.toolbarBell.visibility=GONE
                    }*/

                }

            })

        binding.venulistingToprecycler.also {
            it.layoutManager = LinearLayoutManager(this@VenuListActvity, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = venuAdapterAdapter
            //  venuAdapterAdapter.setData(listStoreType)
        }

    }

    override fun onMapReady(gMap: GoogleMap) {
        gMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap= gMap
        googleMap.uiSettings.isMapToolbarEnabled=false
    }
}