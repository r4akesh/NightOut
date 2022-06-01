package com.nightout.ui.activity.barcrawl

import android.content.Intent
import android.net.Uri
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
import com.google.gson.Gson
import com.nightout.R
import com.nightout.adapter.BarcrwalRootPathShareAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrawlSavedmapactivityBinding
import com.nightout.model.PathParseModel
import com.nightout.model.SharedBarcrwalRes
import com.nightout.ui.activity.TaxiListActivity
import com.nightout.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class BarCrawlShareMapActivity : BaseActivity() ,OnMapReadyCallback{
    lateinit var binding : BarcrawlSavedmapactivityBinding
    lateinit var googleMap : GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    lateinit var barCrwalListShare: SharedBarcrwalRes.Data
    var barCrwalId=""
    var selectedMode="driving"
    private var builder: LatLngBounds.Builder? = null
    private var bounds: LatLngBounds? = null
    var indexOfList = 0
    var sizeOfList = 0


//    case Driving = "driving" //driving
//    case Walking = "walking"
//    case Bicycling = "transit"//"bicycling"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlShareMapActivity,R.layout.barcrawl_savedmapactivity)
    //    binding.btmShhetInclue.bottomSheetShareBtn.setOnClickListener(this)
        binding.btmShhetInclue.bottomSheetShareBtn.visibility = GONE

            barCrwalListShare = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.BarcrwalList) as SharedBarcrwalRes.Data


            if(barCrwalListShare!=null){
                barCrwalId = barCrwalListShare.id
                if(barCrwalListShare.venue_list.isNotEmpty()){
                    // setList(barCrwalList.venue_list)
                }
            }



        setToolBar()
        initView()
        setBottomSheet()
       // share and  show path
    }





    override fun onClick(v: View?) {
        super.onClick(v)
       /* if(v==binding.btmShhetInclue.bottomSheetShareBtn){
            startActivity(Intent(this@BarCrawlShareMapActivity, ContactListNewActvity::class.java)
                .putExtra(AppConstant.PrefsName.ISFROM_BarCrwalPathMapActvity,true)
                .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalID,barCrwalId))
                finish()
        }*/
          if(binding.btmShhetInclue.drivingText==v){
            googleMap.clear()
            selectedMode="driving"
            indexOfList = 0
            addMarkers()
            drawPath()
            binding.btmShhetInclue.drivingText.setTextColor(THIS!!.resources.getColor(R.color.text_yello))
            binding.btmShhetInclue.bicyclingText.setTextColor(THIS!!.resources.getColor(R.color.view_line_clr))
            binding.btmShhetInclue.walkingText.setTextColor(THIS!!.resources.getColor(R.color.view_line_clr))
              startActivity(Intent(THIS!!, TaxiListActivity::class.java))
        }
        else if(binding.btmShhetInclue.bicyclingText==v){
            googleMap.clear()
            selectedMode="transit"
            indexOfList = 0
            addMarkers()
            drawPath()
            binding.btmShhetInclue.bicyclingText.setTextColor(THIS!!.resources.getColor(R.color.text_yello))
            binding.btmShhetInclue.drivingText.setTextColor(THIS!!.resources.getColor(R.color.view_line_clr))
            binding.btmShhetInclue.walkingText.setTextColor(THIS!!.resources.getColor(R.color.view_line_clr))
              openTrainWebsite()
        }
        else if(binding.btmShhetInclue.walkingText==v){
            googleMap.clear()
            selectedMode="walking"
            indexOfList = 0
            addMarkers()
            drawPath()
            binding.btmShhetInclue.walkingText.setTextColor(THIS!!.resources.getColor(R.color.text_yello))
            binding.btmShhetInclue.bicyclingText.setTextColor(THIS!!.resources.getColor(R.color.view_line_clr))
            binding.btmShhetInclue.drivingText.setTextColor(THIS!!.resources.getColor(R.color.view_line_clr))

        }
    }

    lateinit var barcrwalRootPathAdapter: BarcrwalRootPathShareAdapter
    private fun setList(venueList: ArrayList<SharedBarcrwalRes.Venue>) {
        barcrwalRootPathAdapter = BarcrwalRootPathShareAdapter(this@BarCrawlShareMapActivity,venueList,object:BarcrwalRootPathShareAdapter.ClickListener{
            override fun onClick(pos: Int) {
                if(selectedMode == "transit"){
                    //train
                    openTrainWebsite()
                }else {
                    Log.d("TAG", "onClick: " + venueList[pos].store_name)
                    startActivity(Intent(THIS!!, TaxiListActivity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.PATH_POJO, venueList)
                            .putExtra(AppConstant.INTENT_EXTRAS.PATH_POSITION, pos)
                            .putExtra(AppConstant.INTENT_EXTRAS.IsFROM_BarCrawlShareMapActivity, true)
                    )
                }
            }

        })
        binding.btmShhetInclue.bottomSheetRecyclerRoot.also {
            it.layoutManager = LinearLayoutManager(this@BarCrawlShareMapActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = barcrwalRootPathAdapter
        }
    }

    private fun openTrainWebsite() {
        try {
            val url = "https://ojp.nationalrail.co.uk/service/planjourney/search"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: Exception) {
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
         setTouchNClick(binding.btmShhetInclue.drivingText)
         setTouchNClick(binding.btmShhetInclue.walkingText)
         setTouchNClick(binding.btmShhetInclue.bicyclingText)
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.barcrawleSaveMapView) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@BarCrawlShareMapActivity)
    }

    private fun setToolBar() {
         binding.savedBarCrawlMapToolBar.toolbar3dot.visibility = GONE
         binding.savedBarCrawlMapToolBar.toolbarBell.visibility = GONE
        setTouchNClick(binding.savedBarCrawlMapToolBar.toolbarBack)
         binding.savedBarCrawlMapToolBar.toolbarBack.setOnClickListener { finish() }
         binding.savedBarCrawlMapToolBar.toolbarTitle.setText("Map View")
    }


    override fun onMapReady(po: GoogleMap) {
        googleMap = po!!
      googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        if(barCrwalListShare!=null) {
            if (barCrwalListShare.venue_list.isNotEmpty()) {
                addMarkers()
                drawPath()
            }
        }


    }

    private fun addMarkers() {
        if (googleMap != null) {
            builder = LatLngBounds.Builder()
            for (i in 0 until barCrwalListShare.venue_list.size) {
                var lat = Commons.strToDouble(barCrwalListShare.venue_list[i].store_lattitude)
                var lang = Commons.strToDouble(barCrwalListShare.venue_list[i].store_longitude)
                drawMarker(LatLng(lat, lang), barCrwalListShare.venue_list[i].store_name,barCrwalListShare.venue_list[i].store_address)
                // googleMap!!.addMarker(MarkerOptions().position(LatLng(lat, lang)).title("" + i + "st Point"))
            }
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.30).toInt() // offset from edges of the map 10% of screen
            bounds = builder!!.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds!!,width,height, padding)
            googleMap.animateCamera(cu)
        }

    }
    private fun drawMarker(point: LatLng, strName: String, storeAddress: String) {
        val markerOptions = MarkerOptions()
        var marker: Marker? = googleMap!!.addMarker(MarkerOptions().position(point))
        marker?.setIcon(
            BitmapDescriptorFactory.fromBitmap(
                MyApp.getMarkerBitmapFromView(THIS!!, R.drawable.marker_bar, strName, storeAddress)!!
            )
        )
      //  markerOptions.position(point).title("$text").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ic))
      //  googleMap.addMarker(markerOptions)
        builder?.include(marker!!.position)
    }

    private fun drawPath() {
        val url = mapsApiDirectionsUrl()
        if (!url.isNullOrBlank()) {
            readTask(url)
            // googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lang), 13f))
        } else {
            if (! this@BarCrawlShareMapActivity.isFinishing) {
                MyApp.popErrorMsg("", "URL blank !!", THIS!!)
            }
        }
    }

    private fun mapsApiDirectionsUrl(): String {
        val origin = "origin=" + barCrwalListShare.venue_list[indexOfList].store_lattitude + "," +  barCrwalListShare.venue_list[indexOfList].store_longitude
        val dest = "destination=" +  barCrwalListShare.venue_list[indexOfList + 1].store_lattitude + "," +  barCrwalListShare.venue_list[indexOfList + 1].store_longitude
        val sensor = "sensor=false"
        val key = "key=" + getString(R.string.google_maps_key)
        var mode = "mode="+selectedMode
        val parameters = "$origin&$dest&$mode&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$parameters"
    }

    fun readTask(url: String) {
        GlobalScope.launch {
            val http = HttpConnection()
            var data = http.readUrl(url)
            var durtion =""
            var dist=""
            GlobalScope.launch {
                var jObject: JSONObject
                var routes: List<List<HashMap<String, String>>>? = null

                try {
                    jObject = JSONObject(data)
                    try {
                         var pathParseModel =  Gson().fromJson(data, PathParseModel::class.java)
                        Log.d("TAG", "readTask: "+pathParseModel)
                        dist=   pathParseModel.routes[0].legs[0].distance.text
                        durtion=   pathParseModel.routes[0].legs[0].duration.text


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
                            barCrwalListShare.venue_list[indexOfList].durration=durtion
                            barCrwalListShare.venue_list[indexOfList].distance=dist
                            if (indexOfList < barCrwalListShare.venue_list.size - 2) {
                                indexOfList++
                                drawPath()
                            }
                            else{
                                if(barCrwalListShare.venue_list.isNotEmpty()){
                                    setList(barCrwalListShare.venue_list)
                                }
                            }
                        } else{
                            if (! this@BarCrawlShareMapActivity.isFinishing) {
                                MyApp.popErrorMsg("", "Lat-Long not found!!", THIS!!)
                            }
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}