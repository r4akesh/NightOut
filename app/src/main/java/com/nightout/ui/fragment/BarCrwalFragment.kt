package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
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
import com.nightout.adapter.ChatAdapter
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.databinding.FragmentBarcrawlBinding
import com.nightout.databinding.FragmentChatBinding
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.model.ChatModel
import com.nightout.model.VenuModel
import com.nightout.ui.activity.ChatPersonalActvity
import com.nightout.ui.activity.HomeActivity
import com.nightout.ui.activity.ListParticipteActvity

class BarCrwalFragment : Fragment() , View.OnClickListener, OnMapReadyCallback {

    lateinit var binding : FragmentBarcrawlBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcrawl, container, false)

        initView()
        setlistTop()

        return binding.root
    }

    private fun setlistTop() {
        var list = ArrayList<VenuModel>()
        list.add(VenuModel("Club", true))
        list.add(VenuModel("Bar", false))
        list.add(VenuModel("Pub", false))
        list.add(VenuModel("Food", false))
        list.add(VenuModel("Event", false))
        venuAdapterAdapter = VenuAdapterAdapter(
            requireContext(),
            list,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until list.size) {
                        list[i].isSelected = pos == i
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




    override fun onClick(v: View?) {
        if(v==binding.headerBarCrawl.headerSideMenu){
            (activity as HomeActivity?)?.sideMenuBtnClick()
        }

        else if(v==binding.barcrawlSharBarCrawal){
            startActivity(Intent(requireActivity(), ListParticipteActvity::class.java))
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }
}