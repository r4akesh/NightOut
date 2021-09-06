package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrawlSavedmapactivityBinding

class BarCrawlSavedMapActivity : BaseActivity() ,OnMapReadyCallback{
    lateinit var binding : BarcrawlSavedmapactivityBinding
    lateinit var mMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSavedMapActivity,R.layout.barcrawl_savedmapactivity)
        setToolBar()
        initView()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.barcrawlMapSave){
            startActivity(Intent(this@BarCrawlSavedMapActivity,SharedMemeberActvity::class.java))
        }
    }

    private fun initView() {
        setTouchNClick(binding.barcrawlMapSave)
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.barcrawleSaveMapView) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@BarCrawlSavedMapActivity)
    }

    private fun setToolBar() {
         binding.savedBarCrawlMapToolBar.toolbar3dot.visibility = GONE
         binding.savedBarCrawlMapToolBar.toolbarBell.visibility = GONE
        setTouchNClick(binding.savedBarCrawlMapToolBar.toolbarBack)
         binding.savedBarCrawlMapToolBar.toolbarBack.setOnClickListener { finish() }
         binding.savedBarCrawlMapToolBar.toolbarTitle.setText("Map View")
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }
}