package com.nightout.ui.activity.barcrawl

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.RelativeLayout.ALIGN_PARENT_TOP
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.SrchCityBinding
import com.nightout.ui.activity.SearchLocationActivity
import com.nightout.utils.*
import java.util.*
import com.nightout.ui.activity.EditProfileActivity

import android.location.Geocoder




class SearchCityActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: SrchCityBinding
    lateinit var gMap: GoogleMap
    var LAUNCH_GOOGLE_ADDRESS = 102
    var REQCODE_SearchLocationActivity = 888
    var REQCODE_CreateBarCrwlSuccess = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SearchCityActivity, R.layout.srch_city)
        setToolBar()
        initView()
        Handler(Looper.getMainLooper()).postDelayed({
            Utills.slideUp(binding.barcrawlBtmConstrent)
        }, 100)


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.barcralCity) {
            startActivityForResult(Intent(THIS!!, SearchLocationActivity::class.java
                ), REQCODE_SearchLocationActivity)

//            Places.initialize(
//                this@SearchCityActivity,
//                resources.getString(R.string.google_place_picker_key)
//            )
//            val fieldList =
//                Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
//            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
//                .build(this@SearchCityActivity)
//            startActivityForResult(intent, LAUNCH_GOOGLE_ADDRESS)
        }
        else if (v == binding.barcrawlNextBtn) {
            if (binding.barcralCity.text.toString().isNullOrBlank()) {
                MyApp.popErrorMsg("", "Please select city", THIS!!)
            } else {
                startActivityForResult(Intent(this@SearchCityActivity, BarcrawlListActivity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.CITYNAME,binding.barcralCity.text.toString()),REQCODE_CreateBarCrwlSuccess)

            }
        }
        else if(v==binding.barcrawlBtmCrntLocImg){
            val shopLatlang = LatLng( Commons.strToDouble(PreferenceKeeper.instance.currentLat!!),Commons.strToDouble( PreferenceKeeper.instance.currentLong!!))
            val marker = MarkerOptions().position(shopLatlang)
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_yello_ic))
            gMap!!.addMarker(marker)
            gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLatlang, 18f))
            binding.barcralCity.text = PreferenceKeeper.instance.currentCity
        }
    }

    private fun initView() {
        val supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.barcrawleSerchMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@SearchCityActivity)
        binding.barcralCity.setOnClickListener(this@SearchCityActivity)
        binding.barcrawlNextBtn.setOnClickListener(this@SearchCityActivity)
        binding.barcrawlBtmCrntLocImg.setOnClickListener(this@SearchCityActivity)
    }

    private fun setToolBar() {
        binding.srchBarCrawlToolBar.toolbarTitle.setText(resources.getString(R.string.SearchCity))
        setTouchNClick(binding.srchBarCrawlToolBar.toolbarBack)
        binding.srchBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.srchBarCrawlToolBar.toolbar3dot.visibility = View.GONE
        binding.srchBarCrawlToolBar.toolbarBell.visibility = View.GONE
        binding.srchBarCrawlToolBar.toolbarCreateGrop.visibility = View.GONE
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0!!
        p0!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentt: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentt)
        if (requestCode == REQCODE_SearchLocationActivity && resultCode == Activity.RESULT_OK) {
            try {
                var mIsFromSelectPredefineCity = intentt?.getBooleanExtra(AppConstant.INTENT_EXTRAS.isFromSelectPredefineCity,false)//city
                var addrs = intentt?.getStringExtra(AppConstant.INTENT_EXTRAS.ADDRS)
                var lat = intentt?.getStringExtra(AppConstant.INTENT_EXTRAS.LATITUDE)
                var lang = intentt?.getStringExtra(AppConstant.INTENT_EXTRAS.LONGITUDE)
                if(mIsFromSelectPredefineCity == true){
                    binding.barcralCity.text = addrs
                }else{
                    //getCity from lat-lang
                    val geocoder = Geocoder(this@SearchCityActivity, Locale.getDefault())
                    try {
                        val addresses: List<Address> = geocoder.getFromLocation(lat!!.toDouble(), lang!!.toDouble(), 1)
                        // val stateName: String = addresses[0].getAdminArea()
                        val cityName: String = addresses[0].locality
                        binding.barcralCity.text = cityName
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                gMap!!.clear()
                val latLng = LatLng(Commons.strToDouble(lat!!), Commons.strToDouble(lang!!))
                val yourBitmap = getDrawable(R.drawable.ic_crnt_loc)!!.toBitmap(50, 55)//svg img
                gMap!!.addMarker(
                    MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(yourBitmap))
                )
                val cameraPosition = CameraPosition.Builder().target(latLng).zoom(17f).build()
                gMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            } catch (e: Exception) {
            }
        }
        else if(requestCode == REQCODE_CreateBarCrwlSuccess && resultCode == Activity.RESULT_OK){

            finish()

        }
//        try {
//            val place = Autocomplete.getPlaceFromIntent(intentt!!)
//            Log.d("location", "location: " + place.address)
//            binding.barcralCity.text = place.address
//            gMap!!.clear()
//            val latLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
//            val yourBitmap = getDrawable(R.drawable.ic_crnt_loc)!!.toBitmap(50, 55)//svg img
//            gMap!!.addMarker(
//                MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(yourBitmap))
//            )
//            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(17f).build()
//            gMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        } catch (e: Exception) {
//        }
    }


}