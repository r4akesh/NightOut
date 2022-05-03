package com.nightout.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.nightout.R
import com.nightout.adapter.SomeTaxiListAdapter
import com.nightout.databinding.FragmentTransportLayoutBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.ui.activity.TaxiListActivity
import java.util.*


class TransportFragment() : Fragment(), View.OnClickListener, OnMapReadyCallback {
    lateinit var binding: FragmentTransportLayoutBinding
    private var onMenuOpenListener:OnMenuOpenListener? = null

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transport_layout, container, false)
        binding.arrowBtn.setOnClickListener {
            startActivity(Intent(requireContext(),TaxiListActivity::class.java))
        }
        binding.transportUberConstrent.setOnClickListener {

        }

        init()
        return binding.root
    }

    override fun onClick(v: View?) {
            if(v== binding.transportUberConstrent){
                openUber()
            }
        else if(v==binding.transportOlaConstrent){
            openOla()
            }

            else if(v==binding.transportLyftConstrent){
            openLyft()
            }

            else if(v==binding.transportFreeNowConstrent){
                openFreeNow()
            }
            else if(v==binding.transportGettConstrent){
                openGett()
            }

    }

    private fun openGett() {
        val launchIntent: Intent? = requireContext().getPackageManager().getLaunchIntentForPackage("com.gettaxi.android")
        if (launchIntent != null) {
            startActivity(launchIntent) //null pointer check in case package name was not found
        } else {
            val uri = Uri.parse("market://details?id=com.gettaxi.android")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e:  Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.gettaxi.android")
                    )
                )
            }
        }
    }

    private fun openFreeNow() {
        val launchIntent: Intent? = requireContext().getPackageManager().getLaunchIntentForPackage("taxi.android.client")
        if (launchIntent != null) {
            startActivity(launchIntent) //null pointer check in case package name was not found
        } else {
            val uri = Uri.parse("market://details?id=taxi.android.client")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e:  Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=taxi.android.client")
                    )
                )
            }
        }
    }

    private fun openLyft() {
        val launchIntent: Intent? = requireContext().getPackageManager().getLaunchIntentForPackage("me.lyft.android")
        if (launchIntent != null) {
            startActivity(launchIntent) //null pointer check in case package name was not found
        } else {
            val uri = Uri.parse("market://details?id=me.lyft.android")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e:  Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=me.lyft.android")
                    )
                )
            }
        }
    }

    private fun openOla() {
        val launchIntent: Intent? = requireContext().getPackageManager().getLaunchIntentForPackage("com.olacabs.customer")
        if (launchIntent != null) {
            startActivity(launchIntent) //null pointer check in case package name was not found
        } else {
            val uri = Uri.parse("market://details?id=com.olacabs.customer")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e:  Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.olacabs.customer")
                    )
                )
            }
        }
    }

    private fun openUber() {
        val pm: PackageManager = requireContext().getPackageManager()
        try {
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES)
           // val uri = "uber://?action=setPickup&pickup=my_location" // show locaton page
            val uri = "uber://"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")))
            } catch (anfe:  Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")))
            }
        }
//            val intent = Intent(ReserveIntents.ACTION_RESERVE_TAXI_RESERVATION)
//            if (intent.resolveActivity(pm) != null) {
//                startActivity(intent)
//            }

    }

    private fun init(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerSideMenu.setOnClickListener { onMenuOpenListener?.onOpenMenu() }
        setUpTaxiList()
        binding.transportUberConstrent.setOnClickListener(this)
        binding.transportOlaConstrent.setOnClickListener(this)
        binding.transportLyftConstrent.setOnClickListener(this)
        binding.transportGettConstrent.setOnClickListener(this)
        binding.transportFreeNowConstrent.setOnClickListener(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        try {
            val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
        } catch (e: Exception) {
        }
    }

    private fun setUpTaxiList(){
        binding.nearTaxiList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val someTaxiListAdapter = SomeTaxiListAdapter(requireContext())
        binding.nearTaxiList.adapter = someTaxiListAdapter
    }




}