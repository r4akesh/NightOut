package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.StoryAdapter
import com.nightout.adapter.VenuTitleBotmSheetAdapter
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.StoryModel
import com.nightout.model.VenuBotmSheetModel
import com.nightout.model.VenuBotmSheetTitleModel
import com.nightout.ui.activity.*
import com.nightout.utils.PreferenceKeeper


class HomeFragment(): Fragment() , OnMapReadyCallback, View.OnClickListener {

    lateinit var binding : FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var onMenuOpenListener: OnMenuOpenListener? = null

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setBottomSheet()
        initView()
        setListStoryDummy()
        setListVenuListBootmShhetDuumy()

        return binding.root
    }

    private fun setBottomSheet() {
        bottomSheetBehavior =BottomSheetBehavior.from(binding.btmShhetInclue.bottomSheet)
       // bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // bottomSheetBehavior.isHideable = false
        //  bottomSheetBehavior.peekHeight = resources.getDimension(R.dimen._100sdp).toInt()
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
    }


    override fun onClick(v: View?) {
        if(v==binding.headerHome.headerSideMenu){
            onMenuOpenListener?.onOpenMenu()
        }

        else if(v==binding.headerHome.headerSetting){
            startActivity(Intent(requireContext(), FillterActvity::class.java))

        }
        else if(v==binding.headerHome.headerSearch){
            startActivity(Intent(requireContext(), SearchLocationActivity::class.java))
            activity?.overridePendingTransition(0, 0)
        }
    }

    private fun initView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerHome.headerSideMenu.setOnClickListener(this)
        binding.headerHome.headerSearch.setOnClickListener(this)
        binding.headerHome.headerSetting.setOnClickListener(this)
        binding.headerHome.headerTitle.text = "Hi, "+PreferenceKeeper.instance.loginResponse?.name
    }

    lateinit var venuTitleBotmSheetAdapter: VenuTitleBotmSheetAdapter
    private fun setListVenuListBootmShhetDuumy() {
        var listTile = ArrayList<VenuBotmSheetTitleModel>()
        var listSub = ArrayList<VenuBotmSheetModel>()
        listSub.add(
            VenuBotmSheetModel(
                "Vanity Night Clubs",
                "1 Fairclough St, Liverpool",
                R.drawable.venusub_img1
            )
        )
        listTile.add(VenuBotmSheetTitleModel("Clubs", listSub))
        listSub.add(
            VenuBotmSheetModel(
                "Raise a Glass",
                "Liverpool 1 Fairclough St",
                R.drawable.venusub_img2
            )
        )
        listTile.add(VenuBotmSheetTitleModel("Bars", listSub))
        listSub.add(
            VenuBotmSheetModel(
                "Neon Nights",
                "25 Fairclough St, Lverol",
                R.drawable.venusub_img3
            )
        )
        listTile.add(VenuBotmSheetTitleModel("Pubs", listSub))
        listSub.add(
            VenuBotmSheetModel(
                "Neon Nights",
                "25 Fairclough St, Lverol",
                R.drawable.venusub_img3
            )
        )
        listTile.add(VenuBotmSheetTitleModel("Food", listSub))
        listSub.add(
            VenuBotmSheetModel(
                "Neon Nights",
                "25 Fairclough St, Lverol",
                R.drawable.venusub_img3
            )
        )
        listTile.add(VenuBotmSheetTitleModel("Event", listSub))
        venuTitleBotmSheetAdapter = VenuTitleBotmSheetAdapter(
            requireContext(),
            listTile,
            object : VenuTitleBotmSheetAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    if (pos == 0 || pos == 1) {
                        startActivity(Intent(requireActivity(), VenuListActvity::class.java))
                    } else if (pos == 2) {
                        startActivity(Intent(requireActivity(), VenuListActvity::class.java))
                    } else if (pos == 3) {
                        startActivity(Intent(requireActivity(), FoodStoreActvity::class.java))
                    } else if (pos == 4) {
                        startActivity(Intent(requireActivity(), EventDetail::class.java))
                    }

                }

                override fun onWholeClickdd(subPos: Int, mainPos: Int) {
                    if (mainPos == 0 || mainPos == 1) {
                        startActivity(Intent(requireActivity(), VenuListActvity::class.java))
                    } else if (mainPos == 2) {
                        startActivity(Intent(requireActivity(), VenuListActvity::class.java))
                    } else if (mainPos == 3) {
                        startActivity(Intent(requireActivity(), FoodStoreActvity::class.java))
                    } else if (mainPos == 4) {
                        startActivity(Intent(requireActivity(), EventDetail::class.java))
                    }

                }

            })

        binding.btmShhetInclue.bottomSheetRecycleVenuList.also {
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter=venuTitleBotmSheetAdapter
        }

        binding.btmShhetInclue.bottomSheetRecycleVenuList.setOnTouchListener(OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)

            true
        })
    }

    lateinit var storyAdapter: StoryAdapter
    private fun setListStoryDummy() {
        var listStory = ArrayList<StoryModel>()
        listStory.add(StoryModel("Raise Glass", R.drawable.stry_img1))
        listStory.add(StoryModel("Vanity Night", R.drawable.stry_img2))
        listStory.add(StoryModel("Selbys Store", R.drawable.stry_img3))
        listStory.add(StoryModel("Feel the", R.drawable.stry_img4))
        listStory.add(StoryModel("Raise Glass", R.drawable.stry_img1))
        listStory.add(StoryModel("Vanity Night", R.drawable.stry_img2))
        listStory.add(StoryModel("Selbys Store", R.drawable.stry_img3))
        listStory.add(StoryModel("Feel the", R.drawable.stry_img4))
        storyAdapter= StoryAdapter(
            requireActivity(),
            listStory,
            object : StoryAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })

        binding.btmShhetInclue.bottomSheetRecyclerstory.also{
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = storyAdapter
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        try {
            val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
        } catch (e: Exception) {
        }
    }




}