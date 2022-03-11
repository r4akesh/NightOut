package com.nightout.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
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
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenumapActivityBinding
import com.nightout.model.VenuModel

class VenuListMapActivity : BaseActivity(),OnMapReadyCallback {
    lateinit var binding : VenumapActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   //     setContentView(R.layout.venumap_activity)
        binding = DataBindingUtil.setContentView(this@VenuListMapActivity,R.layout.venumap_activity)
        //setTopListTopDummy()
        setToolBar()
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.venumap_Map) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@VenuListMapActivity)
    }

    private fun setToolBar() {
        binding.venuMapToolBar.toolbar3dot.setOnClickListener{
            this.showPopMenu( )
        }
        binding.venuMapToolBar.toolbarBack.setOnClickListener{
            finish()
            overridePendingTransition(0,0)
        }
    }

    private fun showPopMenu() {
        val popup = PopupMenu(this@VenuListMapActivity, binding.venuMapToolBar.toolbar3dot)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_list_menu, popup.getMenu())
        popup.setOnMenuItemClickListener { item ->
            finish()
            overridePendingTransition(0,0)
            true
        }

        popup.show() //showing popup menu
    }



    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}