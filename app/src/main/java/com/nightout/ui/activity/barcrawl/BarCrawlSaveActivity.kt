package com.nightout.ui.activity.barcrawl

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
import com.nightout.databinding.BarcraelsaveActivityBinding

class BarCrawlSaveActivity : BaseActivity() ,OnMapReadyCallback {
    lateinit var  binding: BarcraelsaveActivityBinding
    lateinit var mMap : GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSaveActivity,R.layout.barcraelsave_activity)
        initView()
        setToolBar()

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.barcrawlSaveBtn){
            finish()
        }
    }

    private fun initView() {
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.barcrawleSaveMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@BarCrawlSaveActivity)
        setTouchNClick(binding.barcrawlSaveBtn)
    }

    private fun setToolBar() {
         binding.saveBarCrawlToolBar.toolbarTitle.setText("Bar Crawl")
        setTouchNClick(binding.saveBarCrawlToolBar.toolbarBack)
        binding.saveBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.saveBarCrawlToolBar.toolbar3dot.visibility=GONE
        binding.saveBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.saveBarCrawlToolBar.toolbarCreateGrop.visibility=GONE

//        binding.addBarCrawlToolBar.toolbarBell.visibility= View.GONE
//        binding.addBarCrawlToolBar.toolbar3dot.visibility= View.GONE
//        binding.addBarCrawlToolBar.toolbarCreateGrop.visibility= View.VISIBLE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap!!
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }


}