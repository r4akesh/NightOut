package com.nightout.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
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
import com.nightout.adapter.*
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.model.*
import com.nightout.ui.activity.*
import com.nightout.utils.*
import com.nightout.vendor.services.NetworkHelper
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.HomeViewModel


class HomeFragment() : Fragment(), OnMapReadyCallback, View.OnClickListener {

    lateinit var binding: FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var onMenuOpenListener: OnMenuOpenListener? = null
    lateinit var homeViewModel: HomeViewModel
    lateinit var storyAdapter: StoryAdapter

    lateinit var dashList: DashboardModel.Data
    private val progressDialog = CustomProgressDialog()

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

            dashboardAPICALL()


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

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    private fun dashboardAPICALL() {
        progressDialog.show(requireActivity(), "")
        homeViewModel.dashBoard().observe(requireActivity(), {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    binding.btmShhetInclue.bottomSheetNSrlView.visibility = VISIBLE
                    it.data?.let { users ->
                        setBottomSheet()
                        //save imgPath
                        dashList = users.data
                        PreferenceKeeper.instance.imgPathSave = it.imgPath + "/"
                        if (!(dashList.stories == null ||dashList.stories.size <= 0)) {
                            setListStory(users.data.stories)
                        }
                        if (!(dashList.all_records == null ||dashList.all_records.size <= 0)) {
                           setListAllRecord(dashList.all_records)

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
                    progressDialog.dialog.dismiss()
                    // progressBar.visibility = View.GONE
                    Utills.showSnackBarOnError(
                        binding.fragmentHomeRootLayout,
                        it.message!!,
                        requireActivity()
                    )
                    Log.d("ok", "loginCall:ERROR ")
                }
            }
        })


    }

    lateinit var allRecordAdapter: AllRecordAdapter
    private fun setListAllRecord(allRecordsList: ArrayList<DashboardModel.AllRecord>) {
        allRecordAdapter = AllRecordAdapter(requireActivity(),allRecordsList,object:AllRecordAdapter.ClickListener{
            override fun onClickNext(pos: Int) {
                startActivity(Intent(requireActivity(), VenuListActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,allRecordsList[pos].type ))
            }

            override fun onClickSub(subpos: Int, pos: Int) {
                startActivity(
                    Intent(requireActivity(), StoreDetail::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" +allRecordsList[pos].sub_records[subpos].id )
                )
            }

        })

      binding.btmShhetInclue.bottomSheetRecyclerAll.also {
          it.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
          it.adapter = allRecordAdapter
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


    override fun onClick(v: View?) {
       // store_type => required (1=>Bar, 2=>Pub, 3=>Club, 4=>Food, 5=>Event)
        if (v == binding.headerHome.headerSideMenu) {
            onMenuOpenListener?.onOpenMenu()
        } else if (v == binding.headerHome.headerSetting) {
            startActivity(Intent(requireContext(), FillterActvity::class.java))

        } else if (v == binding.headerHome.headerSearch) {
            startActivity(Intent(requireContext(), SearchLocationActivity::class.java))
            activity?.overridePendingTransition(0, 0)
        }/* else if (v == binding.btmShhetInclue.bottomSheetClubs) {
            if (dashList != null) {
                startActivity(Intent(requireActivity(), VenuListActvity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.StoreType, "3")
                )
            }
        }*/

    }

    private fun initView() {

        homeViewModel = HomeViewModel(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.headerHome.headerSideMenu.setOnClickListener(this)
        binding.headerHome.headerSearch.setOnClickListener(this)
        binding.headerHome.headerSetting.setOnClickListener(this)
        binding.headerHome.headerTitle.text = "Hi, " + PreferenceKeeper.instance.loginResponse?.name
    }


  /*  private fun setListVenuListBootmShhetDuumy(data: Data) {
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
    }*/

   /* private fun setListClub(clubsList: ArrayList<Club>) {
        clubsAdapter =
            ClubsAdapter(requireActivity(), clubsList, object : ClubsAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    startActivity(
                        Intent(requireActivity(), StoreDetail::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID, "" + clubsList[pos].id)
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
*/










    private fun setListStory(listStory: ArrayList<DashboardModel.Story>) {

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