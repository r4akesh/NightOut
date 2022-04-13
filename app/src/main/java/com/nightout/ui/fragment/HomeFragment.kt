package com.nightout.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.*
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.interfaces.ActivtyToFrag
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.*
import com.nightout.ui.activity.*
import com.nightout.ui.activity.Review.RatingListActvity
import com.nightout.ui.activity.barcrawl.BarCrwalPathMap
import com.nightout.utils.*
import com.nightout.vendor.services.NetworkHelper
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class HomeFragment() : Fragment(), OnMapReadyCallback, OnClickListener, ActivtyToFrag {

    lateinit var binding: FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var onMenuOpenListener: OnMenuOpenListener? = null
    lateinit var homeViewModel: CommonViewModel
    lateinit var deviceModel: CommonViewModel
    lateinit var storyAdapter: StoryAdapter
    lateinit var allRecordAdapter: AllRecordAdapter
    lateinit var dashList: DashboardModel.Data
    private val progressDialog = CustomProgressDialog()
    lateinit var doFavViewModel: CommonViewModel
    var allRecordsList = ArrayList<DashboardModel.AllRecord>()

    // val REQCODE_VENULISTACTIVITY = 1009
    val REQCODE_SearchLocationActivity = 1007
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null
    private var mMap: GoogleMap? = null
    var city = ""

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }


    override fun onResume() {
        super.onResume()

        if (PreferenceKeeper.instance.isNotificationOpen) {
            binding.headerHome.headerNotificationText.visibility = GONE
            PreferenceKeeper.instance.isNotificationOpen = false
        } else if (PreferenceKeeper.instance.isFillterApplyByUser) {
            PreferenceKeeper.instance.isFillterApplyByUser = false
            dashboardAPICALL()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        //  setBottomSheet()
        initView()
        Log.e("FCM", "onCreateView: " + PreferenceKeeper.instance.fcmTokenSave)
        if (NetworkHelper(requireActivity()).isNetworkConnected()) {
            binding.btmShhetInclue.bottomSheetNSrlView.visibility = GONE
            if (activity != null && isAdded)
                dashboardAPICALL()

            if (PreferenceKeeper.instance.isUserDeviceAPICall) {
                //  Log.d("TAG", "onCreateView")
            } else {
                userDeviceAPICAll()
                PreferenceKeeper.instance.isUserDeviceAPICall = true
            }

        } else {
            binding.btmShhetInclue.bottomSheetNSrlView.visibility = GONE
            binding.btmShhetInclue.bottomSheet.visibility = INVISIBLE
            bottomSheetBehavior = BottomSheetBehavior.from(binding.btmShhetInclue.bottomSheet)
            bottomSheetBehavior.setPeekHeight(0)//for hide
            MyApp.popErrorMsg(
                "",
                requireActivity().resources.getString(R.string.No_Internet),
                requireActivity()
            )
        }


        return binding.root
    }




    override fun onClick(v: View?) {
        // store_type => required (1=>Bar, 2=>Pub, 3=>Club, 4=>Food, 5=>Event)
        if (v == binding.headerHome.headerSideMenu) {
            onMenuOpenListener?.onOpenMenu()
        } else if (v == binding.headerHome.headerSetting) {
            startActivity(Intent(requireContext(), FillterActvity::class.java))

        } else if (v == binding.headerHome.headerSearch) {
            startActivityForResult(
                Intent(requireContext(), SearchLocationActivity::class.java),
                REQCODE_SearchLocationActivity
            )
            activity?.overridePendingTransition(0, 0)
        } else if (v == binding.headerHome.headerNotificationRel) {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQCODE_SearchLocationActivity) {
            if (resultCode == Activity.RESULT_OK) {
                city = data?.getStringExtra(AppConstant.INTENT_EXTRAS.ADDRS)!!
                var mCity = city.lowercase()
                if ("city" in mCity)
                    city = mCity.replace("city", "")
                dashboardAPICALL()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap
            googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap!!.isMyLocationEnabled = false
            mMap!!.uiSettings.isMapToolbarEnabled = false
        } catch (e: Exception) {
        }
    }

    var mapMarker = HashMap<String, Double>()
    private fun setUpMarker() {
        try {
            val builder = LatLngBounds.Builder()
            var latitudeUpdated: Double
            var longitudeUpdated: Double
            for (i in 0 until dashList.all_records.size) {
                for (j in 0 until dashList.all_records[i].sub_records.size) {
                    var mLat =
                        Commons.strToDouble(dashList.all_records[i].sub_records[j].store_lattitude)
                    var mLang =
                        Commons.strToDouble(dashList.all_records[i].sub_records[j].store_longitude)
                    if (MyApp.isValidLatLng(mLat, mLang)) {
                        var key = "" + mLat + mLang
                        var offset = 0.0
                        if (mapMarker.containsKey(key)) {
                            mapMarker[key]?.plus(0.00005)?.let { mapMarker.put(key, it) };//0.00045
                            offset = mapMarker[key]!!;
                        } else {
                            mapMarker[key] = 0.0;
                        }
                        latitudeUpdated = offset + mLat
                        longitudeUpdated = offset + mLang
                        val positionAddrs = LatLng(latitudeUpdated, longitudeUpdated)
                        var marker: Marker? =
                            mMap!!.addMarker(MarkerOptions().position(positionAddrs))
                        marker?.snippet = "" + i + "," + j
                        // marker?.title =dashList.all_records[i].sub_records[j].store_name
                        //  marker?.snippet = dashList.all_records[i].sub_records[j].store_address
                        var strName = dashList.all_records[i].sub_records[j].store_name
                        var strAddrs = dashList.all_records[i].sub_records[j].store_address
                        when {
                            dashList.all_records[i].sub_records[j].store_type.lowercase()
                                .trim() == AppConstant.PrefsName.BAR -> {
                                //marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bar))

                                marker?.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromView(
                                            R.drawable.marker_bar,
                                            strName,
                                            strAddrs
                                        )!!
                                    )
                                )
                            }
                            dashList.all_records[i].sub_records[j].store_type.lowercase()
                                .trim() == AppConstant.PrefsName.PUB -> {
                                // marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pub))
                                marker?.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromView(
                                            R.drawable.marker_pub,
                                            strName,
                                            strAddrs
                                        )!!
                                    )
                                )
                            }
                            dashList.all_records[i].sub_records[j].store_type.lowercase()
                                .trim() == AppConstant.PrefsName.CLUB -> {
                                // marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_club))
                                marker?.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromView(
                                            R.drawable.marker_club,
                                            strName,
                                            strAddrs
                                        )!!
                                    )
                                )
                            }
                            dashList.all_records[i].sub_records[j].store_type.lowercase()
                                .trim() == AppConstant.PrefsName.FOOD -> {
                                // marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_food))
                                marker?.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromView(
                                            R.drawable.marker_food,
                                            strName,
                                            strAddrs
                                        )!!
                                    )
                                )
                            }
                            dashList.all_records[i].sub_records[j].store_type.lowercase()
                                .trim() == AppConstant.PrefsName.EVENT -> {
                                //   marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_event))
                                marker?.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromView(
                                            R.drawable.marker_event,
                                            strName,
                                            strAddrs
                                        )!!
                                    )
                                )
                            }
                        }
                        marker?.position?.let { builder.include(it) }
                    }
                }
                try {
                    val bounds = builder.build()
                    val padding2 = 110
                    val cu = MyApp.adjustBoundsForMaxZoomLevel(bounds)?.let {
                        CameraUpdateFactory.newLatLngBounds(
                            it, padding2
                        )
                    }
                    //googleMap.moveCamera(cu);
                    if (cu != null) {
                        mMap?.animateCamera(cu)
                    }
                } catch (e: Exception) {
                    Log.e("Exception>>>", "" + e)
                }
                mMap?.setOnMarkerClickListener {
                    var strSnippt: String = it.snippet!!
                   var mSniptPos = strSnippt.split(",")
                    Log.d("TAG", "setUpMarker: " + strSnippt)
                    if (allRecordsList[Integer.parseInt(mSniptPos.get(0))].type == "5") {
                        startActivity(Intent(requireActivity(), EventDetailActivity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                            .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + allRecordsList[Integer.parseInt(mSniptPos.get(0))].sub_records[Integer.parseInt(mSniptPos.get(1))].id))
                    }else{
                        startActivity(Intent(requireActivity(), StoreDetailActvity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + allRecordsList[Integer.parseInt(mSniptPos.get(0))].sub_records[Integer.parseInt(mSniptPos.get(1))].id)
                            .putExtra(AppConstant.INTENT_EXTRAS.StoreType, "" + allRecordsList[Integer.parseInt(mSniptPos.get(0))].type))

                    }
                    false
                }

            }
            /* mMap!!.setInfoWindowAdapter(object : InfoWindowAdapter {
                 override fun getInfoWindow(arg0: Marker): View? {
                     val cw = ContextThemeWrapper(requireActivity(), R.style.Transparent)
                    var  inflater = cw.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                     val layout: View = inflater.inflate(R.layout.custom_infowindow, null)
                     val posss: String = arg0.snippet!!
                     var aa: List<String> =posss.split(",")
                     layout.findViewById<TextView>(R.id.infoStrName).setText(dashList.all_records[Integer.parseInt( aa.get(0))].sub_records[Integer.parseInt( aa.get(1))].store_name)
                   layout.findViewById<TextView>(R.id.infoStrAddrs).setText(dashList.all_records[Integer.parseInt( aa.get(0))].sub_records[Integer.parseInt( aa.get(1))].store_address)
                     var strLogo=dashList.all_records[Integer.parseInt( aa.get(0))].sub_records[Integer.parseInt( aa.get(1))].store_logo
                 //  Utills.setImageNormal(requireActivity(), layout.findViewById<ImageView>(R.id.infoStrImg),strLogo)
                     Glide.with(context!!).load("https://nightout.ezxdemo.com/storage/"+strLogo).centerCrop()
                         .placeholder(R.drawable.no_image)
                         .error(R.drawable.app_icon).into(layout.findViewById<ImageView>(R.id.infoStrImg))
                     return layout
                 }

                 override fun getInfoContents(arg0: Marker): View? {
                         return null
                 }
             })*/


        } catch (e: Exception) {
            Log.d("ok", "setUpMarker: " + e)
        }
    }


    private fun getMarkerBitmapFromView(resId: Int, strName: String, strAddrs: String): Bitmap? {
        val customMarkerView: View = (requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.custom_marker, null)
        val markerImageView = customMarkerView.findViewById(R.id.profile_image) as ImageView
        val tvTitle = customMarkerView.findViewById(R.id.tvTitle) as TextView
        val tvDesc = customMarkerView.findViewById(R.id.tvDesc) as TextView
        tvTitle.text = strName
        tvDesc.text = strAddrs
        markerImageView.setImageResource(resId)
        customMarkerView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
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


    @SuppressLint("HardwareIds")
    private fun userDeviceAPICAll() {
        try {
            var map = HashMap<String, String>()
            map["device_id"] =
                Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
            map["device_type"] = "Android"
            map["device_token"] = "" + PreferenceKeeper.instance.fcmTokenSave
            map["device_info"] = "Android Device"
            deviceModel.userDevice(map).observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->

                        }
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        try {
                            Utills.showErrorToast(
                                requireActivity(),
                                it.message!!,

                                )
                        } catch (e: Exception) {
                        }
                        Log.d("ok", "loginCall:ERROR ")
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun dashboardAPICALL() {
        var str = if (PreferenceKeeper.instance.currentFilterValue?.isNotBlank()!!)
            PreferenceKeeper.instance.currentFilterValue
        else
            ""

        var jarr = JSONArray()
        jarr.put(str)
        var jobj = JSONObject()
        jobj.put("filter", jarr)
        jobj.put("city", city.trim())

        progressDialog.show(requireActivity(), "")
        try {
            homeViewModel.dashBoard(jobj).observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        binding.btmShhetInclue.bottomSheetNSrlView.visibility = VISIBLE
                        it.data?.let { users ->
                            try {
                                setBottomSheet()
                                dashList = DashboardModel.Data(
                                    ArrayList(),
                                    ArrayList(),
                                    ArrayList(),
                                    ArrayList(),
                                    ArrayList(),
                                    "",
                                    ""
                                )
                                dashList.all_records = ArrayList()
                                allRecordsList = ArrayList()
                                //save imgPath
                                dashList = users.data
                                if (dashList.noti_count == "0") {
                                    binding.headerHome.headerNotificationText.visibility = GONE
                                } else {
                                    binding.headerHome.headerNotificationText.visibility = VISIBLE
                                    binding.headerHome.headerNotificationText.text =
                                        dashList.noti_count
                                    if (binding.headerHome.headerNotificationText.text.toString()
                                            .isBlank()
                                    ) {
                                        binding.headerHome.headerNotificationText.visibility = GONE
                                    }
                                }
                                //reviewPopUp
                                try {
                                    if (dashList.venue_review_remaning.toInt() > 0) {
                                        if (!DataManager.instance.isFirstShowPopupReview) {
                                            showPopUpReview()
                                            DataManager.instance.isFirstShowPopupReview = true
                                        }
                                    }
                                } catch (e: Exception) {
                                }
                                //   PreferenceKeeper.instance.imgPathSave = it.imgPath + "/"
                                PreferenceKeeper.instance.imgPathSave =
                                    "https://nightout.ezxdemo.com/storage/"
                                //setStory
                                if (!(dashList.stories == null || dashList.stories.size <= 0)) {
                                    if (activity != null)
                                        setListStory(users.data.stories)
                                } else {
                                    binding.btmShhetInclue.bottomSheetStory.visibility = GONE
                                }
                                //featuredBarCrawl
                                if (dashList.feature_bar_crawl == null || dashList.feature_bar_crawl.size == 0) {
                                    binding.btmShhetInclue.bottomSheetFeature.visibility = GONE
                                } else {
                                    setFeatureList(dashList.feature_bar_crawl)
                                }


                                if (!(dashList.all_records == null || dashList.all_records.size <= 0)) {
                                    if (activity != null) {
                                        allRecordsList.addAll(dashList.all_records)
                                        //save service charge
                                        PreferenceKeeper.instance.SERVICE_CHARGE =
                                            dashList.service_charge[0].charge
                                        setListAllRecord()
                                        var isSetMarkerCall = true
                                        while (mMap != null && isSetMarkerCall) {
                                            if (dashList.all_records.isNotEmpty() && dashList.all_records.size > 0) {
                                                mMap?.clear()
                                                setUpMarker()
                                                isSetMarkerCall = false
                                                Log.e("ok", "setUpMarker: call")


                                            }
                                        }
                                    }
                                } else {
                                    mMap?.clear()
                                    allRecordsList = ArrayList()
                                    if (requireActivity() != null) {
                                        setListAllRecord()
                                        MyApp.ShowTost(
                                            requireActivity(),
                                            "Venues not available in $city"
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                mMap?.clear()
                                allRecordsList = ArrayList()
                                if (activity != null) {
                                    setListAllRecord()
                                }
                            }
                        }
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        // progressBar.visibility = View.GONE
                        try {
                            Utills.showErrorToast(
                                requireActivity(),
                                it.message!!,

                                )
                        } catch (e: Exception) {
                        }
                        Log.d("ok", "loginCall:ERROR ")
                    }
                }
            }
        } catch (e: Exception) {
        }
    }


    private fun showPopUpReview() {
        val adDialog = Dialog(requireActivity(), R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        adDialog.setContentView(R.layout.dialog_review)
        adDialog.setCancelable(false)
        var dgCloseBtnTop: ImageView = adDialog.findViewById(R.id.dgCloseBtnTop)
        var dgOkBtn: TextView = adDialog.findViewById(R.id.dgOkBtn)

        dgCloseBtnTop.setOnClickListener {
            adDialog.dismiss()
        }
        dgOkBtn.setOnClickListener {
            adDialog.dismiss()
            if (requireActivity() != null)
                startActivity(Intent(requireActivity(), RatingListActvity::class.java))
        }

        adDialog.show()
    }

    private fun setListAllRecord() {
        allRecordAdapter = AllRecordAdapter(
            requireActivity(),
            allRecordsList,
            object : AllRecordAdapter.ClickListener {
                override fun onClickNext(pos: Int) {
                    startActivity(
                        Intent(requireActivity(), VenuListActvity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.StoreType, allRecordsList[pos].type)
                    )
                }

                override fun onClickSub(subpos: Int, pos: Int) {
                    if (MyApp.isConnectingToInternet(requireContext())) {
                        if (allRecordsList[pos].type == "5") {
                            startActivity(Intent(requireActivity(), EventDetailActivity::class.java)
                                    .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                                    .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + allRecordsList[pos].sub_records[subpos].id))
                        } else {
                            startActivity(Intent(requireActivity(), StoreDetailActvity::class.java)
                                    .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + allRecordsList[pos].sub_records[subpos].id)
                                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType, "" + allRecordsList[pos].type)
                            )
                        }
                    }
                }

                override fun onClickFav(subPos: Int, mainPos: Int) {
                    add_favouriteAPICALL(subPos, mainPos)
                }
            })
        binding.btmShhetInclue.bottomSheetRecyclerAll.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                //save dx
            }
        })
        binding.btmShhetInclue.bottomSheetRecyclerAll.also {
            it.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            it.adapter = allRecordAdapter
        }
    }


    private fun add_favouriteAPICALL(pos: Int, mainPos: Int) {
        //  progressDialog.show(requireActivity(), "")
        var fav = if (allRecordsList[mainPos].sub_records[pos].favrouite.equals("1"))
            "0" //for opp value
        else
            "1"
        var map = HashMap<String, String>()
        map["venue_id"] = allRecordsList[mainPos].sub_records[pos].id
        map["vendor_id"] = allRecordsList[mainPos].sub_records[pos].user_id
        map["status"] = fav


        doFavViewModel.doFavItem(map).observe(requireActivity(), {
            when (it.status) {
                Status.SUCCESS -> {
                    //  progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            /* if( detailData.data.status == "1"){
                                 allRecordsList[mainPos].sub_records[pos].favrouite = "1"
                                 //allRecordAdapter.notifyItemChanged(pos)
                               allRecordAdapter.notifyDataSetChanged()
                             }else{
                                 allRecordsList[mainPos].sub_records[pos].favrouite = "0"
                                // allRecordAdapter.notifyItemChanged(pos)
                                // state = mLayoutManager.onSaveInstanceState();
                               allRecordAdapter.notifyDataSetChanged()
                             }*/

                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    // progressDialog.dialog.dismiss()
                    Utills.showErrorToast(requireActivity(), it.message!!)
                }
            }
        })
    }

    private fun getAddrsFrmLatlang(latitude: Double, longitude: Double) {
        try {

            if (requireActivity() != null) {
                geocoder = Geocoder(requireActivity(), Locale.getDefault())
                addresses = geocoder!!.getFromLocation(
                    latitude,
                    longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                val addrs = addresses?.get(0)
                    ?.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city =
                    addresses?.get(0)?.locality // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                Log.d("ok", "addrs: " + addresses?.get(0)?.locality)

                binding.headerHome.headerAddrs.text = addrs
                PreferenceKeeper.instance.currentAddrs = addrs
                PreferenceKeeper.instance.currentCity = city
                PreferenceKeeper.instance.currentLat = latitude.toString()
                PreferenceKeeper.instance.currentLong = longitude.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onActivityResultMy(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                //startLocationUpdate()
                // getLastLocation();
            }
        }
    }

    private fun setBottomSheet() {
        //for solve issue scrolling
        androidx.core.view.ViewCompat.setNestedScrollingEnabled(
            binding.btmShhetInclue.bottomSheetrecyclerstory,
            false
        )
        androidx.core.view.ViewCompat.setNestedScrollingEnabled(
            binding.btmShhetInclue.bottomSheetRecyclerAll,
            false
        )

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
        doFavViewModel = CommonViewModel(requireActivity())
        homeViewModel = CommonViewModel(requireActivity())
        deviceModel = CommonViewModel(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerHome.headerSideMenu.setOnClickListener(this)
        binding.headerHome.headerSearch.setOnClickListener(this)
        binding.headerHome.headerSetting.setOnClickListener(this)
        binding.headerHome.headerNotificationRel.setOnClickListener(this)
        binding.headerHome.headerTitle.text = "Hi, " + PreferenceKeeper.instance.loginResponse?.name


    }

    lateinit var featureBarcrwlAdapter: FeatureBarcrwlAdapter

    private fun setFeatureList(featureBarCrawlList: ArrayList<DashboardModel.FeatureBarCrawl>) {
        featureBarcrwlAdapter = FeatureBarcrwlAdapter(
            requireActivity(),
            featureBarCrawlList,
            object : FeatureBarcrwlAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    startActivity(
                        Intent(requireActivity(), BarCrwalPathMap::class.java)
                            //  .putExtra(AppConstant.PrefsName.SelectedBarcrwalList, listHr)
                            .putExtra(
                                AppConstant.INTENT_EXTRAS.BarcrwalID,
                                featureBarCrawlList[pos].id
                            )
                            .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_FEATURED_BARCRWAL, true)
                            .putExtra(
                                AppConstant.INTENT_EXTRAS.CITYNAME,
                                featureBarCrawlList[pos].city
                            )
                            .putExtra(
                                AppConstant.INTENT_EXTRAS.FEATURE_MODEL,
                                featureBarCrawlList[pos]
                            )
                            .putExtra(
                                AppConstant.INTENT_EXTRAS.FEATURE_LIST,
                                featureBarCrawlList[pos].venue_list
                            )
                    )
                }

            })
        binding.btmShhetInclue.bottomSheetrecyclerFeature.also {
            it.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = featureBarcrwlAdapter
        }
    }

    private fun setListStory(listStory: ArrayList<DashboardModel.Story>) {
        storyAdapter =
            StoryAdapter(requireActivity(), listStory, object : StoryAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    startActivity(
                        Intent(requireActivity(), StoryPreviewActivity::class.java)
                            .putExtra(
                                AppConstant.INTENT_EXTRAS.STORY_LIST,
                                listStory[pos].storydetail
                            )
                    )
                }
            })

        binding.btmShhetInclue.bottomSheetrecyclerstory.also {
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = storyAdapter
        }

    }


    private fun setUpLocationListener() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        // startLocationUpdate
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            //  mMap!!.clear()
            for (location in locationResult.locations) {
                Log.d("ok", "onLocationResult: " + location.latitude.toString())
                stopLocationUpdate()
                if (PreferenceKeeper.instance.currentAddrs!!.isBlank()) {
                    getAddrsFrmLatlang(location.latitude, location.longitude)
                } else {
                    binding.headerHome.headerAddrs.text = PreferenceKeeper.instance.currentAddrs

                }
                /*      val shopLatlang = LatLng(location.latitude, location.longitude)
                    val marker = MarkerOptions().position(shopLatlang)
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_yello_ic))
                    mMap!!.addMarker(marker)
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLatlang, 18f))*/
            }

        }
    }

    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(requireActivity()) -> {
                when {
                    PermissionUtils.isLocationEnabled(requireActivity()) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(requireActivity())
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    requireActivity(),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(requireActivity()) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(requireActivity())
                        }
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun methodName() {

    }


}
