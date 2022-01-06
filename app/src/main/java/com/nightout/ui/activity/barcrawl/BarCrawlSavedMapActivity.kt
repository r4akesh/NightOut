package com.nightout.ui.activity.barcrawl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.BarcrwalRootPathAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrawlSavedmapactivityBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.ui.activity.ContactListNewActvity
import com.nightout.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.HashMap
import com.google.gson.Gson
import com.nightout.model.PathParseModel


class BarCrawlSavedMapActivity : BaseActivity() ,OnMapReadyCallback{
    lateinit var binding : BarcrawlSavedmapactivityBinding
    lateinit var googleMap : GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var barCrwalList: BarcrwalSavedRes.Data
    var barCrwalId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSavedMapActivity,R.layout.barcrawl_savedmapactivity)
        binding.btmShhetInclue.bottomSheetShareBtn.setOnClickListener(this)
        barCrwalList = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.BarcrwalList) as BarcrwalSavedRes.Data
        if(barCrwalList!=null){
            barCrwalId = barCrwalList.id
            if(barCrwalList.venue_list.isNotEmpty()){
                setList(barCrwalList.venue_list)
            }
        }
        setToolBar()
        initView()
        setBottomSheet()
       // share and  show path
    }





    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.btmShhetInclue.bottomSheetShareBtn){
            startActivity(Intent(this@BarCrawlSavedMapActivity, ContactListNewActvity::class.java)
                .putExtra(AppConstant.PrefsName.ISFROM_BarCrwalPathMapActvity,true)
                .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalID,barCrwalId))
                finish()
        }
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
    private var builder: LatLngBounds.Builder? = null
    private var bounds: LatLngBounds? = null
    var indexOfList = 0
    var sizeOfList = 0

    override fun onMapReady(po: GoogleMap?) {
        googleMap = po!!
      googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        if(barCrwalList!=null) {
            if (barCrwalList.venue_list.isNotEmpty()) {
                addMarkers()
                drawPath()
            }
        }


    }

    private fun addMarkers() {
        if (googleMap != null) {
            builder = LatLngBounds.Builder()
            for (i in 0 until barCrwalList.venue_list.size) {
                var lat = Commons.strToDouble(barCrwalList.venue_list[i].store_lattitude)
                var lang = Commons.strToDouble(barCrwalList.venue_list[i].store_longitude)
                drawMarker(LatLng(lat, lang), "$i")
                // googleMap!!.addMarker(MarkerOptions().position(LatLng(lat, lang)).title("" + i + "st Point"))
            }
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.30).toInt() // offset from edges of the map 10% of screen
            bounds = builder!!.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height, padding)
            googleMap.animateCamera(cu)
        }

    }
    private fun drawMarker(point: LatLng, text: String) {
        var value: Int = text.toInt()+65
        val c = value.toChar()
        val markerOptions = MarkerOptions()
        markerOptions.position(point).title("Point $c").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ic))
        googleMap.addMarker(markerOptions)
        builder?.include(markerOptions.position)
    }

    private fun drawPath() {
        val url = mapsApiDirectionsUrl()
        if (!url.isNullOrBlank()) {
            readTask(url)
            // googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lang), 13f))
        } else {
            MyApp.popErrorMsg("", "URL blank !!", THIS!!)
        }
    }

    private fun mapsApiDirectionsUrl(): String {
        val origin =
            "origin=" + barCrwalList.venue_list[indexOfList].store_lattitude + "," +  barCrwalList.venue_list[indexOfList].store_longitude
        val dest =
            "destination=" +  barCrwalList.venue_list[indexOfList + 1].store_lattitude + "," +  barCrwalList.venue_list[indexOfList + 1].store_longitude
        val sensor = "sensor=false"
        val key = "key=" + getString(R.string.google_maps_key)
        val parameters = "$origin&$dest&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$parameters"
    }

    fun readTask(url: String) {
        GlobalScope.launch {
            val http = HttpConnection()
            var data = http.readUrl(url)
            GlobalScope.launch {
                var jObject: JSONObject
                var routes: List<List<HashMap<String, String>>>? = null
                try {
                    jObject = JSONObject(data)
                    try {
                        var vvbb =  Gson().fromJson(data, PathParseModel::class.java)     hyfghgfh
                        Log.d("TAG", "readTask: "+vvbb)

                    } catch (e: Exception) {
                        Log.d("TAG", "readTask: ")
                    }
                    val parser = PathJSONParser()
                    routes = parser.parse(jObject)




                    //onPostExecute
                    var points: ArrayList<LatLng?>? = null
                    var polyLineOptions: PolylineOptions? = null
                    // traversing through routes
                    for (i in routes!!.indices) {
                        points = ArrayList()
                        polyLineOptions = PolylineOptions()
                        val path = routes[i]
                        for (j in path.indices) {

                            val point = path[j]
                            val lat = point["lat"]!!.toDouble()
                            val lng = point["lng"]!!.toDouble()
                            val position = LatLng(lat, lng)
                            points.add(position)
                        }
                        polyLineOptions.addAll(points)
                        polyLineOptions.width(5f)
                        polyLineOptions.color(resources.getColor(R.color.text_yello))

                    }
                    runOnUiThread {
                        if (polyLineOptions != null) {
                            googleMap!!.addPolyline(polyLineOptions)
                            if (indexOfList < barCrwalList.venue_list.size - 2) {
                                indexOfList++
                                drawPath()
                            }
                        } else{
                            MyApp.popErrorMsg("","Lat-Long not found!!",THIS!!)
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}