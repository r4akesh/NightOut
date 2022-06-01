package com.nightout.ui.activity



import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.utils.HttpConnection
import com.nightout.utils.MyApp
import com.nightout.utils.PathJSONParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class DemoMap : BaseActivity(), OnMapReadyCallback {
    var googleMap: GoogleMap? = null
    val TAG = "PathGoogleMapActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barcrwalmappath_actvity)
        val fm = supportFragmentManager
            .findFragmentById(R.id.barCrawlPathMap) as SupportMapFragment?
        fm!!.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
//        val options = MarkerOptions()
//        options.position(LOWER_MANHATTAN)
//        options.position(BROOKLYN_BRIDGE)
//        options.position(WALL_STREET)
//        googleMap!!.addMarker(options)
        addMarkers()
        val url = mapsApiDirectionsUrl()
        if(!url.isNullOrBlank()) {
            readTask(url)
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE, 13f))

        }
        else{
            MyApp.popErrorMsg("","URL blank !!",THIS!!)
        }
    }



    private  fun mapsApiDirectionsUrl(): String   {
        val origin = "origin=" + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
            val dest = "destination=" + BROOKLYN_BRIDGE.latitude + "," + BROOKLYN_BRIDGE.longitude
            val sensor = "sensor=false"
            val key = "key=" + getString(R.string.google_maps_key)
            val parameters = "$origin&$dest&$key"
            return "https://maps.googleapis.com/maps/api/directions/json?$parameters"
        }

    private fun addMarkers() {
        if (googleMap != null) {
            googleMap!!.addMarker(MarkerOptions().position(BROOKLYN_BRIDGE).title("First Point"))
            googleMap!!.addMarker(MarkerOptions().position(LOWER_MANHATTAN).title("Second Point"))
            googleMap!!.addMarker(MarkerOptions().position(WALL_STREET).title("Third Point"))
        }
    }

    fun readTask(url:String){
        GlobalScope.launch {
            val http = HttpConnection()
           var  data = http.readUrl(url)
            GlobalScope.launch {
                val jObject: JSONObject
                var routes: List<List<HashMap<String, String>>>? = null
                try {
                    jObject = JSONObject(data)
                    val parser = PathJSONParser()
                    routes = parser.parse(jObject)
                    Log.d(TAG, "readTask: "+routes)
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
                        polyLineOptions.color(Color.BLUE)
                    }
                    runOnUiThread{
                        googleMap!!.addPolyline(polyLineOptions!!)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }






    companion object {
        private val LOWER_MANHATTAN = LatLng(
            40.722543,
            -73.998585
        )
        private val BROOKLYN_BRIDGE = LatLng(40.7057, -73.9964)
        private val WALL_STREET = LatLng(40.7064, -74.0094)
    }
}