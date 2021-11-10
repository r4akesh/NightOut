package com.nightout.ui.activity.barcrawl

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.BarcrwalSelectedAdapter
import com.nightout.adapter.VenuListBarCrawaAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrwallistActivityBinding
import com.nightout.model.BarCrwalVenuesModel

class BarcrawlListActivity : BaseActivity() {
    lateinit var binding: BarcrwallistActivityBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@BarcrawlListActivity,
            R.layout.barcrwallist_activity
        )

        list set
        setToolBar()
        setTopHrList()
        setDummyList()
        //   setBottomSheet()
    }

    lateinit var barcrwalSelectedAdapter: BarcrwalSelectedAdapter
    lateinit var listHr: ArrayList<BarCrwalVenuesModel>
    private fun setTopHrList() {
        listHr = ArrayList<BarCrwalVenuesModel>()

        barcrwalSelectedAdapter = BarcrwalSelectedAdapter(
            this@BarcrawlListActivity,
            listHr,
            object : BarcrwalSelectedAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })

        binding.barCrwlReyleBotm.also {
            it.layoutManager =
                LinearLayoutManager(this@BarcrawlListActivity, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = barcrwalSelectedAdapter
        }
    }

    /*   private fun setBottomSheet() {
           //for solve issue scrolling
          // androidx.core.view.ViewCompat.setNestedScrollingEnabled(binding.btmShhetInclue.bottomSheetrecyclerstory, false)
         //  androidx.core.view.ViewCompat.setNestedScrollingEnabled(binding.btmShhetInclue.bottomSheetRecyclerAll, false)

           bottomSheetBehavior = BottomSheetBehavior.from(binding.btmShhetList.bottomSheet)
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
                       *//* STATE_COLLAPSED -> TODO()
                     STATE_ANCHORED -> TODO()
                     STATE_EXPANDED -> TODO()*//*
                }
            }
        })
    }*/

    lateinit var venuListBarCrawaAdapter: VenuListBarCrawaAdapter
    private fun setDummyList() {
        var listDummy = ArrayList<BarCrwalVenuesModel>()
        listDummy.add(BarCrwalVenuesModel(1, "Vanity Night Club", "4.5", R.drawable.allbar1, false))
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club2",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club3",
                "4.5",
                R.drawable.venusub_img2,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                1,
                "Vanity Night Club4",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                1,
                "Vanity Night Club5",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club6",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club6",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                1,
                "Vanity Night Club4",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                1,
                "Vanity Night Club5",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club6",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club6",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                1,
                "Vanity Night Club4",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                1,
                "Vanity Night Club5",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club6",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        listDummy.add(
            BarCrwalVenuesModel(
                2,
                "Vanity Night Club6",
                "4.5",
                R.drawable.allbar1,
                false
            )
        )
        for (i in 0 until listDummy.size) {
            if (i > 0) {
                Log.d("pos", "setDummyList: ")
                for (i in 0 until 2) {

                }
            }
        }
        venuListBarCrawaAdapter = VenuListBarCrawaAdapter(this@BarcrawlListActivity, listDummy, object : VenuListBarCrawaAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    // binding.barCrwalBtmDetail.visibility = VISIBLE
                    listHr.add(listDummy[pos])
                    barcrwalSelectedAdapter.notifyDataSetChanged()
                    binding.barCrwlReyleBotm.scrollToPosition(listHr.size-1)
                    Log.d("TAG", "onClick: " + pos)
                }
        })

        binding.brcrlRecyle.also {
            // it.layoutManager =  LinearLayoutManager(this@BarcrawlListActivity,LinearLayoutManager.VERTICAL,true)
            // it.layoutManager =  GridLayoutManager(this@BarcrawlListActivity,2)
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            it.adapter = venuListBarCrawaAdapter
        }

    }

    private fun setToolBar() {
        binding.BarCrawlListToolBar.toolbarTitle.setText("Bar Crawl")
        setTouchNClick(binding.BarCrawlListToolBar.toolbarBack)
        binding.BarCrawlListToolBar.toolbarBack.setOnClickListener { finish() }
        binding.BarCrawlListToolBar.toolbar3dot.visibility = View.GONE
        binding.BarCrawlListToolBar.toolbarBell.visibility = View.GONE
        binding.BarCrawlListToolBar.toolbarCreateGrop.visibility = View.GONE
    }


}