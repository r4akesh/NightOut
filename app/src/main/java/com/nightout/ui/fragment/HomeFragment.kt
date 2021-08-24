package com.nightout.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.*
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.*

import com.nightout.ui.activity.*
import com.nightout.utils.AppConstant
import com.nightout.utils.PreferenceKeeper
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.HomeViewModel
import java.io.Serializable


class HomeFragment() : Fragment(), OnMapReadyCallback, View.OnClickListener {

    lateinit var binding: FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var onMenuOpenListener: OnMenuOpenListener? = null
    lateinit var homeViewModel: HomeViewModel
    lateinit var storyAdapter: StoryAdapter
    lateinit var clubsAdapter: ClubsAdapter
    lateinit var barsAdapter: BarsAdapter
    lateinit var foodsAdapter: FoodsAdapter
    lateinit var pubsAdapter: PubsAdapter
    lateinit var dashList: Data

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
        dashboardAPICALL()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    private fun dashboardAPICALL() {
        homeViewModel.dashBoard().observe(requireActivity(), {
            when (it.status) {
                Status.SUCCESS -> {
                    //progressBar.visibility = View.GONE
                    it.data?.let { users ->
                        setBottomSheet()
                        //save imgPath
                        dashList = users.data
                        PreferenceKeeper.instance.imgPathSave = it.imgPath + "/"
                        if (!(users.data.stories == null || users.data.stories.size <= 0)) {
                            setListStory(users.data.stories)
                        }
                        if (users.data.clubs != null && users.data.clubs.size > 0) {
                            binding.btmShhetInclue.bottomSheetClubs.visibility= VISIBLE
                            setListClub(users.data.clubs)
                        }
                        if (users.data.bars != null && users.data.bars.size > 0) {
                            binding.btmShhetInclue.bottomSheetBars.visibility= VISIBLE
                            setListBars(users.data.bars)
                        }
                        if (users.data.pubs != null && users.data.pubs.size > 0) {
                            binding.btmShhetInclue.bottomSheetPubs.visibility= VISIBLE
                            setListPubs(users.data.pubs)
                        }
                        if (users.data.foods != null && users.data.foods.size > 0) {
                            binding.btmShhetInclue.bottomSheetFoods.visibility= VISIBLE
                            setListFoods(users.data.foods)
                        }
                        if (users.data.events != null && users.data.events.size > 0) {
                            binding.btmShhetInclue.bottomSheetEvents.visibility= VISIBLE
                            setListEvents(users.data.events)
                        }
                        //setListVenuListBootmShhetDuumy(users.data)

                        // renderList(users.data)
                    }
                    Log.d("ok", "loginCall:SUCCESS " + it)
                }
                Status.LOADING -> {
                    //progressBar.visibility = View.VISIBLE
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    // progressBar.visibility = View.GONE
                    Log.d("ok", "loginCall:ERROR ")
                }
            }
        })


    }


    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.btmShhetInclue.bottomSheet)
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
        if (v == binding.headerHome.headerSideMenu) {
            onMenuOpenListener?.onOpenMenu()
        } else if (v == binding.headerHome.headerSetting) {
            startActivity(Intent(requireContext(), FillterActvity::class.java))

        } else if (v == binding.headerHome.headerSearch) {
            startActivity(Intent(requireContext(), SearchLocationActivity::class.java))
            activity?.overridePendingTransition(0, 0)
        }
        else if (v == binding.btmShhetInclue.bottomSheetClubs) {
            if(dashList!=null) {
                startActivity(Intent(requireActivity(), VenuListActvity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.VENU_LIST_POS,dashList)
                )
            }
        }
    }

    private fun initView() {
        binding.btmShhetInclue.bottomSheetClubs.setOnClickListener(this)
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerHome.headerSideMenu.setOnClickListener(this)
        binding.headerHome.headerSearch.setOnClickListener(this)
        binding.headerHome.headerSetting.setOnClickListener(this)
        binding.headerHome.headerTitle.text = "Hi, " + PreferenceKeeper.instance.loginResponse?.name
    }

    lateinit var venuTitleBotmSheetAdapter: VenuTitleBotmSheetAdapter
    private fun setListVenuListBootmShhetDuumy(data: Data) {
        var vv = data.bars
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
            it.adapter = venuTitleBotmSheetAdapter
        }

        binding.btmShhetInclue.bottomSheetRecycleVenuList.setOnTouchListener(OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)

            true
        })
    }

    private fun setListClub(clubsList: ArrayList<Club>) {
        clubsAdapter =
            ClubsAdapter(requireActivity(), clubsList, object : ClubsAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    var vv = clubsList[pos]
                    startActivity(Intent(requireActivity(), StoreDetail::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.VENU_POS, clubsList[pos])
                        .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, ""+clubsList[pos].id)
                    )
                }



            })

        binding.btmShhetInclue.bottomSheetrecyclerClubs.also {
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = clubsAdapter
        }
    }

    private fun setListEvents(events: ArrayList<Any>) {

    }

    private fun setListFoods(foodsList: ArrayList<Food>) {
        foodsAdapter =
            FoodsAdapter(requireActivity(), foodsList, object : FoodsAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })

        binding.btmShhetInclue.bottomSheetrecyclerFoods.also {
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = foodsAdapter
        }
    }

    private fun setListPubs(pubsList: ArrayList<Pub>) {
        pubsAdapter = PubsAdapter(requireActivity(), pubsList, object : PubsAdapter.ClickListener {
            override fun onClick(pos: Int) {

            }

        })

        binding.btmShhetInclue.bottomSheetrecyclerPubs.also {
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = pubsAdapter
        }
    }


    private fun setListBars(barsList: ArrayList<Bar>) {
        barsAdapter = BarsAdapter(requireActivity(), barsList, object : BarsAdapter.ClickListener {
            override fun onClick(pos: Int) {

            }

        })

        binding.btmShhetInclue.bottomSheetrecyclerBars.also {
            it.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = barsAdapter
        }
    }


    private fun setListStory(listStory: ArrayList<Story>) {

        storyAdapter =
            StoryAdapter(requireActivity(), listStory, object : StoryAdapter.ClickListener {
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

    override fun onMapReady(googleMap: GoogleMap?) {
        try {
            val success =
                googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
        } catch (e: Exception) {
        }
    }


}