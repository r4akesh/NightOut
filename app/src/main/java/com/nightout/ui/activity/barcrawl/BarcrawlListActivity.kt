package com.nightout.ui.activity.barcrawl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.adapter.BarcrwalSelectedAdapter
import com.nightout.adapter.VenuListBarCrawaAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrwallistActivityBinding
import com.nightout.model.AllBarCrwalListResponse
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class BarcrawlListActivity : BaseActivity() {
    lateinit var binding: BarcrwallistActivityBinding

    //  private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var barcrwalSelectedAdapter: BarcrwalSelectedAdapter
    lateinit var listHr: ArrayList<AllBarCrwalListResponse.Barcrawl>
    lateinit var venuListBarCrawaAdapter: VenuListBarCrawaAdapter
    private val progressDialog = CustomProgressDialog()
    lateinit var getBarCrwalVieModel: CommonViewModel
    var listAllVenue = ArrayList<AllBarCrwalListResponse.Barcrawl>()
    var listClickPosSave = 0
      var barcrwalId: String = ""
    var isFromShareListActivity=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarcrawlListActivity, R.layout.barcrwallist_activity)
        initView()
        setToolBar()
        setTopHrList()
        barcrwalId = try {
            intent.getStringExtra(AppConstant.INTENT_EXTRAS.BarcrwalID)!!
        } catch (e: Exception) {
            ""
        }
        if(barcrwalId.isNullOrBlank())
            Log.d("TAG", "onCreate: blnk")
        else
            Log.d("TAG", "onCreate: not blnk")

        bar_crawl_listAPICAll()

    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.barcrwalAddBtn) {
            binding.barCrwalBtmDetail.visibility = GONE
            if (listAllVenue[listClickPosSave].isSelected) {
                //  binding.barcrwalAddBtn.visibility=GONE
            } else {
                if (listAllVenue[listClickPosSave].isSelected) {
                    listAllVenue[listClickPosSave].isSelected = false
                } else {
                    listAllVenue[listClickPosSave].isSelected = true
                }
                venuListBarCrawaAdapter.notifyItemChanged(listClickPosSave)

                //  binding.barcrwalAddBtn.visibility= VISIBLE
                //  listAllVenue[listClickPosSave].isSelected=true
                listHr.add(listAllVenue[listClickPosSave])
                if (listHr.size > 0) {
                    binding.barCrwlSelectedConstrant.visibility = VISIBLE
                    barcrwalSelectedAdapter.notifyDataSetChanged()
                    binding.barCrwlReyleBotm.smoothScrollToPosition(listHr.size - 1)
                } else {
                    binding.barCrwlSelectedConstrant.visibility = GONE
                }
            }

        } else if (v == binding.barcrwalCloseBtn) {
            binding.barCrwalBtmDetail.visibility = GONE
        } else if (v == binding.barCrwalNextBtn) {
            if (listHr.size < 2) {
                MyApp.popErrorMsg("", "Please select at least two venues.", THIS!!)
            } else {
                startActivity(
                    Intent(this@BarcrawlListActivity, BarCrwalPathMap::class.java)
                        .putExtra(AppConstant.PrefsName.SelectedBarcrwalList, listHr)
                        .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalID, barcrwalId)
                        .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_ShareListActivity, isFromShareListActivity)
                )
                finish()
            }
        }
    }

    private fun bar_crawl_listAPICAll() {
        progressDialog.show(this@BarcrawlListActivity, "")
        try {
            getBarCrwalVieModel.venuListBarCrwl().observe(this@BarcrawlListActivity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        listAllVenue = ArrayList()
                        try {
                            listAllVenue.addAll(it.data?.data?.barcrawl!!)
                            if (listAllVenue.isNotEmpty()) {
                                setAllVenuList()

                            }else{
                                binding.barcrwalVenueNoData.visibility= VISIBLE
                            }
                        } catch (e: Exception) {
                            binding.barcrwalVenueNoData.visibility= VISIBLE
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            Utills.showSnackBarOnError(
                                binding.barcrwalListMainLayout,
                                it.message!!,
                                this@BarcrawlListActivity
                            )
                        } catch (e: Exception) {
                        }
                        binding.barcrwalVenueNoData.visibility= VISIBLE
                        Log.d("ok", "loginCall:ERROR ")
                    }
                }
            })
        } catch (e: Exception) {
        }


    }

    private fun initView() {
        binding.barCrwalNextBtn.setOnClickListener(this)
        binding.barcrwalAddBtn.setOnClickListener(this)
        binding.barcrwalCloseBtn.setOnClickListener(this)
        getBarCrwalVieModel = CommonViewModel(this)
        isFromShareListActivity = intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.ISFROM_ShareListActivity,false)

    }


    private fun setTopHrList() {
        listHr = ArrayList()
        barcrwalSelectedAdapter = BarcrwalSelectedAdapter(
            this@BarcrawlListActivity,
            listHr,
            object : BarcrwalSelectedAdapter.ClickListener {
                override fun onClickClose(pos: Int) {
                    //notify list(unselect)
                    for (i in 0 until listAllVenue.size) {
                        //for (j in 0 until listHr.size){
                        if (listAllVenue[i].id == listHr[pos].id) {
                            listAllVenue[i].isSelected = false
                            venuListBarCrawaAdapter.notifyItemChanged(i)
                            break
                        }
                        //}
                    }
                    //notify remove data
                    var listSize = listHr.size
                    listHr.removeAt(pos)
                    barcrwalSelectedAdapter.notifyItemRemoved(pos)
                    barcrwalSelectedAdapter.notifyItemRangeChanged(pos, listSize)
                    if (listHr.size == 0) {
                        binding.barCrwlSelectedConstrant.visibility = GONE
                    }


                }

            })

        binding.barCrwlReyleBotm.also {
            it.layoutManager =
                LinearLayoutManager(
                    this@BarcrawlListActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
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


    private fun setAllVenuList() {
        var mdl = listAllVenue[0]
        mdl.viewType = 1
        listAllVenue[0] = mdl
        var totSize = listAllVenue.size - 1
        var iForIndx = 1
        try {
            var viewType = 2
            while (totSize > 0) {
                for (i in 0 until 2) {
                    totSize -= 1
                    var mdl = listAllVenue[iForIndx]
                    mdl.viewType = viewType
                    listAllVenue[iForIndx] = mdl
                    iForIndx += 1
                    if (i == 1) {
                        if (viewType == 1)
                            viewType = 2
                        else
                            viewType = 1
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("ok", "setAllVenuList: " + e.toString())
        }
        venuListBarCrawaAdapter = VenuListBarCrawaAdapter(
            this@BarcrawlListActivity,
            listAllVenue,
            object : VenuListBarCrawaAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listClickPosSave = pos
                    setDetailVenue(listAllVenue[pos])
                    binding.barCrwalBtmDetail.visibility = VISIBLE


                }
            })

        binding.brcrlRecyle.also {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            it.adapter = venuListBarCrawaAdapter
        }

    }

    private fun setDetailVenue(barcrawlData: AllBarCrwalListResponse.Barcrawl) {
        if (barcrawlData.isSelected) {
            binding.barcrwalAddBtn.visibility = GONE
        } else {
            binding.barcrwalAddBtn.visibility = VISIBLE
        }
        binding.barCrwalBtmDetail.visibility = VISIBLE
        Glide.with(this@BarcrawlListActivity)
            .load(PreferenceKeeper.instance.imgPathSave + barcrawlData.venue_detail.store_logo)
            .error(R.drawable.no_image)
            .into(binding.barcrwalLogo)
        binding.barcrwalTitle.text = barcrawlData.venue_detail.store_name
        binding.barcrwalSubTitle.text = barcrawlData.venue_detail.store_address
        binding.barcralListClostTime.text = "Close: " + barcrawlData.venue_detail.close_time


    }

    private fun setToolBar() {
        binding.BarCrawlListToolBar.toolbarTitle.setText(resources.getString(R.string.Select_Venues))
        setTouchNClick(binding.BarCrawlListToolBar.toolbarBack)
        binding.BarCrawlListToolBar.toolbarBack.setOnClickListener { finish() }
        binding.BarCrawlListToolBar.toolbar3dot.visibility = View.GONE
        binding.BarCrawlListToolBar.toolbarBell.visibility = View.GONE
        binding.BarCrawlListToolBar.toolbarCreateGrop.visibility = View.GONE
    }


}