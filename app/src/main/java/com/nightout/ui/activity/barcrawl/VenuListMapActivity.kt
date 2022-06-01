package com.nightout.ui.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenumapActivityBinding
import com.nightout.model.AllBarCrwalListResponse
import com.nightout.utils.AppConstant
import com.nightout.utils.Commons
import com.nightout.utils.MyApp

class VenuListMapActivity : BaseActivity(),OnMapReadyCallback {
    lateinit var binding : VenumapActivityBinding
    lateinit var googleMap: GoogleMap
    lateinit var venuDataList : ArrayList<AllBarCrwalListResponse.Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   //     setContentView(R.layout.venumap_activity)
        binding = DataBindingUtil.setContentView(this@VenuListMapActivity,R.layout.venumap_activity)
        //setTopListTopDummy()
        setToolBar()
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.venumap_Map) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@VenuListMapActivity)
        getDataFrmIntent()
    }

    private fun getDataFrmIntent() {
        venuDataList = intent.getSerializableExtra(AppConstant.PrefsName.SelectedBarcrwalList) as ArrayList<AllBarCrwalListResponse.Data>
    }

    private fun setToolBar() {
        binding.venuMapToolBar.toolbar3dot.visibility= GONE
        binding.venuMapToolBar.toolbarBell.visibility= GONE
        binding.venuMapToolBar.toolbarTitle.text = "Venues"
//        binding.venuMapToolBar.toolbar3dot.setOnClickListener{
//            this.showPopMenu( )
//        }
        binding.venuMapToolBar.toolbarBack.setOnClickListener{
            finish()

        }
    }






    override fun onMapReady(googleMap: GoogleMap) {
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
        this.googleMap = googleMap
        setMarker()
    }


    var mapMarker = HashMap<String, Double>()
    private fun setMarker() {
        googleMap.clear()
        val builder = LatLngBounds.Builder()
        var latitudeUpdated: Double
        var longitudeUpdated: Double
        for (i in 0 until venuDataList.size){
            var mLat= Commons.strToDouble(venuDataList[i].store_lattitude)
            var mLang=Commons.strToDouble(venuDataList[i].store_longitude)
            if(MyApp.isValidLatLng(mLat,mLang)){
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
                val marker: Marker = googleMap!!.addMarker(MarkerOptions().position(positionAddrs))!!
                marker.snippet = ""+i
                var strName = venuDataList[i].store_name
                var strAddrs = venuDataList[i].store_address
                when {
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.BAR -> {
                        //  marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bar))
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_bar, strName, strAddrs)!!))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.PUB -> {
                        //  marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pub))
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_pub, strName, strAddrs)!!))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.CLUB -> {
                        // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_club))
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_club, strName, strAddrs)!!))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.FOOD -> {
                        //   marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_food))
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_food, strName, strAddrs)!!))
                    }
                    venuDataList[i].store_type.lowercase().trim()==AppConstant.PrefsName.EVENT -> {
                        // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_event))
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_event, strName, strAddrs)!!))
                    }
                }
                builder.include(marker.position)
            }
            try {
                runOnUiThread(Runnable {
                    Handler().postDelayed({
                        val bounds = builder.build()
                        val padding2 = 150
                        val cu = CameraUpdateFactory.newLatLngBounds(MyApp.adjustBoundsForMaxZoomLevel(bounds)!!, padding2)
                        //googleMap.moveCamera(cu);
                        if (cu != null) {
                            googleMap?.animateCamera(cu)
                        }
                    },1000
                    )


                })


            } catch (e: java.lang.Exception) {
                Log.e("Exception>>>", "" + e)
            }

        }
    }
    private fun getMarkerBitmapFromView(resId: Int, strName: String, strAddrs: String): Bitmap? {
        val customMarkerView: View = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.custom_marker, null)
        val markerImageView = customMarkerView.findViewById(R.id.profile_image) as ImageView
        val tvTitle = customMarkerView.findViewById(R.id.tvTitle) as TextView
        val tvDesc = customMarkerView.findViewById(R.id.tvDesc) as TextView
        tvTitle.text = strName
        tvDesc.text = strAddrs
        markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable: Drawable? = customMarkerView.background
        if (drawable != null) drawable.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }
}