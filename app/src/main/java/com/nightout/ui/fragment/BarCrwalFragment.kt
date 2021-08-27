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
import com.nightout.adapter.PromotionAdapter
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.databinding.FragmentBarcrawlBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.VenuModel
import com.nightout.ui.activity.ListParticipteActvity

class BarCrwalFragment() : Fragment() , View.OnClickListener, OnMapReadyCallback {

    lateinit var binding : FragmentBarcrawlBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
    private var onMenuOpenListener: OnMenuOpenListener? = null

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcrawl, container, false)

        initView()
        setlistTop()

        return binding.root
    }

    override fun onClick(v: View?) {
        if(v==binding.headerBarCrawl.headerSideMenu){
            onMenuOpenListener?.onOpenMenu()
        }

        else if(v==binding.barcrawlSharBarCrawal){
            startActivity(Intent(requireActivity(), ListParticipteActvity::class.java))
        }

    }


    private fun setlistTop() {
        var listStoreType = ArrayList<VenuModel>()
        listStoreType.add(VenuModel(3,"Club", false))
        listStoreType.add(VenuModel(1,"Bar", false))
        listStoreType.add(VenuModel(2,"Pub", false))
        listStoreType.add(VenuModel(4,"Food", false))
        listStoreType.add(VenuModel(5,"Event", false))

       // venuAdapterAdapter = PromotionAdapter(list)
         venuAdapterAdapter = VenuAdapterAdapter(
            requireContext(),
             listStoreType,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until listStoreType.size) {
                        listStoreType[i].isSelected = pos == i
                    }
                    venuAdapterAdapter.notifyDataSetChanged()
                }

            })

        binding.fragmentBatRecyclerTop.also {
            it.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = venuAdapterAdapter
        }
    }

    private fun initView() {
        val supportMapFragment = (childFragmentManager.findFragmentById(R.id.barcrawleMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this)
        binding.headerBarCrawl.headerSideMenu.setOnClickListener(this)
        binding.barcrawlSharBarCrawal.setOnClickListener(this)
    }





    override fun onMapReady(googleMap: GoogleMap?) {
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}