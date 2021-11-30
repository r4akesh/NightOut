package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.BarcrwalRootPathAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrawlSavedmapactivityBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.utils.AppConstant

class BarCrawlSavedMapActivity : BaseActivity() ,OnMapReadyCallback{
    lateinit var binding : BarcrawlSavedmapactivityBinding
    lateinit var mMap : GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSavedMapActivity,R.layout.barcrawl_savedmapactivity)
        var barCrwalList: BarcrwalSavedRes.Data
        barCrwalList = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.BarcrwalList) as BarcrwalSavedRes.Data
        if(barCrwalList!=null){
            if(barCrwalList.venue_list.isNotEmpty()){
                setList(barCrwalList.venue_list)
            }
        }
        setToolBar()
        initView()
        setBottomSheet()
        share and  show path
    }

    lateinit var barcrwalRootPathAdapter: BarcrwalRootPathAdapter
    private fun setList(venueList: ArrayList<BarcrwalSavedRes.Venue>) {
        barcrwalRootPathAdapter = BarcrwalRootPathAdapter(this@BarCrawlSavedMapActivity,venueList,object:BarcrwalRootPathAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })
        binding.btmShhetInclue.bottomSheetRecyclerRoot.also {
            it.layoutManager = LinearLayoutManager(this@BarCrawlSavedMapActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = barcrwalRootPathAdapter
        }
    }

    private fun setBottomSheet() {

        bottomSheetBehavior = BottomSheetBehavior.from(binding.btmShhetInclue.bottomSheet)
        //   bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
         // bottomSheetBehavior.peekHeight = 150
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // offset == 0f when bottom sheet is collapsed
                // offset == 1f when bottom sheet is expanded
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    /* STATE_COLLAPSED -> TODO()
                     STATE_ANCHORED -> TODO()
                     STATE_EXPANDED -> TODO()*/
                }
            }
        })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
       /* if(v==binding.barcrawlMapSave){
            startActivity(Intent(this@BarCrawlSavedMapActivity,SharedMemeberActvity::class.java))
        }*/
    }

    private fun initView() {
        //setTouchNClick(binding.barcrawlMapSave)
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