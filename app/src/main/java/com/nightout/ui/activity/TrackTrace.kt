package com.nightout.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.FilterSubitemBinding
import com.nightout.databinding.TracktraceActvityBinding

class TrackTrace : BaseActivity(),OnMapReadyCallback{
    lateinit var binding: TracktraceActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@TrackTrace,R.layout.tracktrace_actvity)
       initView()
        setToolBar()
    }

    private fun setToolBar() {
        setTouchNClick(  binding.toolbarBack)
         binding.toolbarBack.setOnClickListener { finish() }
        binding.toolbarTitle.setText("Track and Trace")

    }

    private fun initView() {
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.treactraceMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@TrackTrace)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}