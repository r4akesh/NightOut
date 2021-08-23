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
        setTopListTopDummy()
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

    private fun setTopListTopDummy() {
        var list = ArrayList<VenuModel>()
        list.add(VenuModel("Club", true))
        list.add(VenuModel("Bar", false))
        list.add(VenuModel("Pub", false))
        list.add(VenuModel("Food", false))
        list.add(VenuModel("Event", false))

        venuAdapterAdapter = VenuAdapterAdapter(
            this@VenuListMapActivity,
            list,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until list.size ){
                        if(pos==i){
                            list[i].isSelected=true
                        }else{
                            list[i].isSelected=false
                        }
                    }
                    venuAdapterAdapter.notifyDataSetChanged()
                }

            })

        binding.venumapToprecycler.also {
            it.layoutManager = LinearLayoutManager(this@VenuListMapActivity, LinearLayoutManager.HORIZONTAL, false)
           it.adapter = venuAdapterAdapter
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}