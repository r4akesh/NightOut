package com.nightout.ui.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
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
import com.nightout.utils.*
import com.nightout.vendor.services.NetworkHelper
import com.nightout.vendor.services.Status
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import androidx.recyclerview.widget.RecyclerView

import com.nightout.viewmodel.CommonViewModel


class HomeFragment() : Fragment(), OnMapReadyCallback, View.OnClickListener, ActivtyToFrag {

    lateinit var binding: FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var onMenuOpenListener: OnMenuOpenListener? = null
    lateinit var homeViewModel: CommonViewModel
    lateinit var storyAdapter: StoryAdapter
    lateinit var allRecordAdapter: AllRecordAdapter
    lateinit var dashList: DashboardModel.Data
    private val progressDialog = CustomProgressDialog()
    lateinit var doFavViewModel : CommonViewModel
    var allRecordsList = ArrayList<DashboardModel.AllRecord>()
   // val REQCODE_VENULISTACTIVITY = 1009
   lateinit var  fusedLocationProviderClient : FusedLocationProviderClient
    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null
    private var mMap: GoogleMap? = null
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        //  setBottomSheet()
        initView()

        if (NetworkHelper(requireActivity()).isNetworkConnected()) {
            binding.btmShhetInclue.bottomSheetNSrlView.visibility = GONE
            if(activity!=null && isAdded)
            dashboardAPICALL()
        } else {
            binding.btmShhetInclue.bottomSheetNSrlView.visibility = GONE
            binding.btmShhetInclue.bottomSheet.visibility = INVISIBLE
            bottomSheetBehavior = BottomSheetBehavior.from(binding.btmShhetInclue.bottomSheet)
            bottomSheetBehavior.setPeekHeight(0)//for hide
            MyApp.popErrorMsg("", requireActivity().resources.getString(R.string.No_Internet), requireActivity())
        }
        Log.d("TAG", "onCreateView: ")
        return binding.root
    }
    override fun onClick(v: View?) {
        // store_type => required (1=>Bar, 2=>Pub, 3=>Club, 4=>Food, 5=>Event)
        if (v == binding.headerHome.headerSideMenu) {
            onMenuOpenListener?.onOpenMenu()
        } else if (v == binding.headerHome.headerSetting) {
            startActivity(Intent(requireContext(), FillterActvity::class.java))

        } else if (v == binding.headerHome.headerSearch) {
            startActivity(Intent(requireContext(), SearchLocationActivity::class.java))
            activity?.overridePendingTransition(0, 0)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        try {
            mMap = googleMap
            googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            mMap!!.isMyLocationEnabled = false
        } catch (e: Exception) {
        }
    }

    private fun dashboardAPICALL() {
        progressDialog.show(requireActivity(), "")
        try {
            homeViewModel.dashBoard().observe(requireActivity(), {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        binding.btmShhetInclue.bottomSheetNSrlView.visibility = VISIBLE
                        it.data?.let { users ->
                            try {
                                setBottomSheet()
                                //save imgPath
                                dashList = users.data
                             //   PreferenceKeeper.instance.imgPathSave = it.imgPath + "/"
                                PreferenceKeeper.instance.imgPathSave = "https://nightout.ezxdemo.com/storage/"
                                if (!(dashList.stories == null ||dashList.stories.size <= 0)) {
                                    if(activity!=null)
                                    setListStory(users.data.stories)
                                }
                                if (!(dashList.all_records == null ||dashList.all_records.size <= 0)) {
                                    if(activity!=null) {
                                        allRecordsList.addAll(dashList.all_records)
                                        setListAllRecord()
                                    }
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }
                    Status.LOADING -> { }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        // progressBar.visibility = View.GONE
                        try {
                            Utills.showSnackBarOnError(
                                binding.fragmentHomeRootLayout,
                                it.message!!,
                                requireActivity()
                            )
                        } catch (e: Exception) {
                        }
                        Log.d("ok", "loginCall:ERROR ")
                    }
                }
            })
        } catch (e: Exception) {
        }
    }

    private fun setListAllRecord() {
        allRecordAdapter = AllRecordAdapter(requireActivity(),allRecordsList,object:AllRecordAdapter.ClickListener{
            override fun onClickNext(pos: Int) {
                startActivity(Intent(requireActivity(), VenuListActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,allRecordsList[pos].type ))
            }
            override fun onClickSub(subpos: Int, pos: Int) {
                if (MyApp.isConnectingToInternet(requireContext())) {
                    if(allRecordsList[pos].type=="5"){
                        startActivity(Intent(requireActivity(), EventDetail::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_VENULISTACTIVITY, true)
                            .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + allRecordsList[pos].sub_records[subpos].id))
                    }else{
                        startActivity(Intent(requireActivity(), StoreDetail::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + allRecordsList[pos].sub_records[subpos].id))
                    }
                }
            }

            override fun onClickFav(subPos: Int, mainPos: Int) {
                add_favouriteAPICALL(subPos,mainPos)
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
          it.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
          it.adapter = allRecordAdapter
      }
    }


    private fun add_favouriteAPICALL(pos:Int,mainPos:Int) {
        progressDialog.show(requireActivity(), "")
        var fav = if(allRecordsList[mainPos].sub_records[pos].favrouite.equals("1"))
            "0" //for opp value
        else
            "1"
        var map = HashMap<String, String>()
        map["venue_id"] = allRecordsList[mainPos].sub_records[pos].id
        map["vendor_id"] =allRecordsList[mainPos].sub_records[pos].user_id
        map["status"] = fav


        doFavViewModel.doFavItem(map).observe(requireActivity(), {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            if( detailData.data.status == "1"){
                                allRecordsList[mainPos].sub_records[pos].favrouite = "1"
                                //allRecordAdapter.notifyItemChanged(pos)
                              allRecordAdapter.notifyDataSetChanged()
                            }else{
                                allRecordsList[mainPos].sub_records[pos].favrouite = "0"
                               // allRecordAdapter.notifyItemChanged(pos)
                               // state = mLayoutManager.onSaveInstanceState();
                              allRecordAdapter.notifyDataSetChanged()
                            }

                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(binding.fragmentHomeRootLayout, it.message!!, requireActivity())
                }
            }
        })
    }

    private fun getAddrsFrmLatlang(latitude: Double, longitude: Double) {
        geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {

            addresses = geocoder!!.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val addrs = addresses?.get(0)
                ?.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            Log.d("ok", "addrs: "+addrs)
            binding.headerHome.headerAddrs.setText(addrs)
            PreferenceKeeper.instance.currentAddrs = addrs
            PreferenceKeeper.instance.currentLat = latitude.toString()
            PreferenceKeeper.instance.currentLong= longitude.toString()
        } catch (e: IOException) {
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
        androidx.core.view.ViewCompat.setNestedScrollingEnabled(binding.btmShhetInclue.bottomSheetrecyclerstory, false)
        androidx.core.view.ViewCompat.setNestedScrollingEnabled(binding.btmShhetInclue.bottomSheetRecyclerAll, false)

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
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerHome.headerSideMenu.setOnClickListener(this)
        binding.headerHome.headerSearch.setOnClickListener(this)
        binding.headerHome.headerSetting.setOnClickListener(this)
        binding.headerHome.headerTitle.text = "Hi, " + PreferenceKeeper.instance.loginResponse?.name


    }

    private fun setListStory(listStory: ArrayList<DashboardModel.Story>) {

        storyAdapter = StoryAdapter(requireActivity(), listStory, object : StoryAdapter.ClickListener {
                override fun onClick(pos: Int) {

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
           fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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
            mMap!!.clear()
            for (location in locationResult.locations) {
                //  latTextView.text = location.latitude.toString()
                //  lngTextView.text = location.longitude.toString()
                Log.d("ok", "onLocationResult: "+location.latitude.toString())
                stopLocationUpdate()
                getAddrsFrmLatlang(location.latitude, location.longitude)
                val shopLatlang = LatLng(location.latitude, location.longitude)
                val marker = MarkerOptions().position(shopLatlang)
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.addrs_home))
                mMap!!.addMarker(marker)
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLatlang, 18f))
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
