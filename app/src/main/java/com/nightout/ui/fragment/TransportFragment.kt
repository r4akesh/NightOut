package com.nightout.ui.fragment

import android.content.Intent
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


class TransportFragment() : Fragment(), OnMapReadyCallback {
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

        init()
        return binding.root
    }

    private fun init(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerSideMenu.setOnClickListener { onMenuOpenListener?.onOpenMenu() }
        setUpTaxiList()

    }


    override fun onMapReady(googleMap: GoogleMap?) {
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