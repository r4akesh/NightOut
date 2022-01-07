package com.nightout.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.nightout.R
import com.nightout.adapter.*
import com.nightout.base.BaseActivity
import com.nightout.databinding.StoredetailActivityBinding
import com.nightout.model.*
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import kotlinx.android.synthetic.main.discount_desc.view.*
import android.text.Editable

import android.text.TextWatcher
import com.nightout.viewmodel.CommonViewModel
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.AnimationUtils


class StoreDetailActvity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: StoredetailActivityBinding
    lateinit var userVenueDetailViewModel: CommonViewModel
    lateinit var doFavViewModel: CommonViewModel
    lateinit var doAddBarCrawlModel: CommonViewModel
    lateinit var sendQueryViewModel: CommonViewModel
    lateinit var mMap: GoogleMap
    var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    private val progressDialog = CustomProgressDialog()
    var facilityList = ArrayList<VenuDetailModel.VenueFacility>()
    var venuID = ""
    var favStatus = "0"
    var addBarCrawlStatus = "0"
    var storeType=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@StoreDetailActvity, R.layout.storedetail_activity)
        initView()

        venuID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_ID)!!
        if (venuID.isNotBlank()) {
            user_venue_detailAPICALL()
        }
        if(intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.iSFROMESelectBarCrwlActivity,false)){
            binding.storeDeatilPlaceOrder.visibility= GONE
        }

       // setListDrinksDummy()//first time set
        // add_Remove_bar_crawlAPICAll()
        //duumy
        setTouchNClick(binding.storeDeatilLogo)

         binding.storeDeatilLogo.setOnClickListener {

            val imagedialog = Dialog(this@StoreDetailActvity)
             val window: Window = imagedialog.window!!
             window.setGravity(Gravity.TOP)
             val layoutParams: WindowManager.LayoutParams = imagedialog.window!!.attributes
             layoutParams.y = 170//margin top
             imagedialog.window!!.attributes = layoutParams

             imagedialog.setContentView( R.layout.imagedialog)
             imagedialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val photo    = imagedialog.findViewById(R.id.photoenlarge) as ImageView
            val viewTransTop    = imagedialog.findViewById(R.id.viewTransTop) as View



             Glide.with(this@StoreDetailActvity)
                 .load(PreferenceKeeper.instance.imgPathSave + dt.store_logo)
                 .error(R.drawable.no_image)
                 .into(photo)
             val fade_in = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
             fade_in.duration = 500
             fade_in.fillAfter = true
             photo.startAnimation(fade_in)
               Handler(Looper.getMainLooper()).postDelayed({
                   viewTransTop.visibility= VISIBLE },490)
             imagedialog.show()

        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        if(v==binding.storeDeatilAddVenuRel){
            if (addBarCrawlStatus == "1") {
                addBarCrawlStatus = "0"
                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)

            } else {
                addBarCrawlStatus = "1"
                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)
            }
            addRemoveBarCrawlAPICall()
        }
        else if (v == binding.storeDeatilFav) {

            if (favStatus == "1") {
                favStatus = "0"
                binding.storeDeatilFav.setImageResource(R.drawable.fav_unselected)
            } else {
                favStatus = "1"
                binding.storeDeatilFav.setImageResource(R.drawable.fav_selected)
            }
            binding.storeDeatilFav.startAnimation(AnimationUtils.loadAnimation(THIS!!, R.anim.bounce))
            add_favouriteAPICALL()
        } else if (v == binding.storeDeatilPreBookingBtn) {
            if (!venuID.isNullOrBlank()) {
                startActivity(Intent(this@StoreDetailActvity, PreBookingActivity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.VENU_ID,venuID)
                    .putExtra(AppConstant.INTENT_EXTRAS.VENU_NAME,dt.store_name)
                    .putExtra(AppConstant.INTENT_EXTRAS.StoreDetailPoJO,dt)

                )
            }


        } else if (v == binding.storeDeatilPlaceOrder) {
            var totPrice : Double = 0.0
            if(dt.all_products.size>0) {
                for (i in 0 until 3) {//coz packege no include
                    for (j in 0 until dt.all_products[i].records.size) {
                        for (k in 0 until dt.all_products[i].records[j].products.size) {
                            if (dt.all_products[i].records[j].products[k].quantityLocal > 0) {
                                totPrice+=dt.all_products[i].records[j].products[k].totPriceLocal
                            }
                        }
                    }
                }
            }
            if(storeType!="4"){
            //for package
            for (j in 0 until dt.all_products[3].records.size) {
                if (dt.all_products[3].records[j].quantityLocal > 0) {
                    totPrice+=dt.all_products[3].records[j].totPriceLocal
                }
            }}

            if(totPrice>0){
            startActivity(Intent(this@StoreDetailActvity, OrderDetailActivity::class.java)
                .putExtra(AppConstant.INTENT_EXTRAS.StoreDetailPoJO,dt)
                .putExtra(AppConstant.INTENT_EXTRAS.StoreType,storeType)
            )}else{
                MyApp.popErrorMsg("","Please select menu",THIS!!)
            }

        } else if (v == binding.storeDeatilFacilityBtn) {
            if (facilityList != null && facilityList.size > 0)
                showPopUpFacilities() else {
                MyApp.popErrorMsg(
                    "",
                    resources.getString(R.string.facilitynot_avail),
                    this@StoreDetailActvity
                )
            }
        } else if (v == binding.storeDeatilBakBtn) {
            var myIntent = Intent()
            myIntent.putExtra("resultFav",favStatus)
            myIntent.putExtra("resultBarcrwal",addBarCrawlStatus)
            setResult(Activity.RESULT_OK,myIntent)
            finish()

        } else if (v == binding.storeDeatilMenu) {
            binding.storeDeatilConstrentMore.visibility= GONE
                    binding.storeDeatilConstrentDrinks.visibility= VISIBLE


            binding.storeDeatilMenu.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.black))
         //   binding.storeDeatilDiscount.setBackgroundResource(0)
         //   binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))


            binding.storeDeatilMenuDesc.visibility = VISIBLE
         //   binding.storeDeatilDisDesc.visibility = GONE
            binding.storeDeatilMoreDesc.visibility = GONE


        }
      /*  else if (v == binding.storeDeatilDiscount) {
            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilDiscount.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.black))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))

            val str1 = resources.getString(R.string.discount10)
            var str2 = resources.getString(R.string.firsLine)
            var settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.firstLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )

            str2 = resources.getString(R.string.secondLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.secondLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )


            str2 = resources.getString(R.string.thridLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.thrdLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )

            str2 = resources.getString(R.string.firsLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.fourthLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )

            str2 = resources.getString(R.string.secondLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.fifthLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )

            binding.storeDeatilMenuDesc.visibility = GONE
            binding.storeDeatilDisDesc.visibility = VISIBLE
            binding.storeDeatilMoreDesc.visibility = GONE
        } */

        else if (v == binding.storeDeatilMore) {
            binding.storeDeatilConstrentMore.visibility= VISIBLE
            binding.storeDeatilConstrentDrinks.visibility= GONE

            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
         //   binding.storeDeatilDiscount.setBackgroundResource(0)
        //    binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.black))

            binding.storeDeatilMoreDesc.visibility = VISIBLE
            //binding.storeDeatilDisDesc.visibility = GONE
        //    binding.storeDeatilMenuDesc.visibility = GONE
        } else if (v == binding.storeDeatilSharLoc) {
            val latitude: Double = 26.906473
            val longitude: Double = 75.772804
            val uri = "http://maps.google.com/maps?saddr=$latitude,$longitude"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val ShareSub = "Here is my location"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, ShareSub)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        } else if (v == binding.storeDeatilDirection) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=26.906473,26.862470&daddr=75.772804,75.762410")
            )
            startActivity(intent)
        }
    }



    /*  private fun setTopImgSlider() {
          if (intent != null && intent.hasExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST)) {
              try {
                  var venueGalleryList = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST) as ArrayList<DashboardModel.VenueGallery>
                  imageViewPagerAdapter = ImageViewPagerAdapter(this@StoreDetail, venueGalleryList)
                  binding.viewPager.adapter = imageViewPagerAdapter
                  binding.dotsIndicator.setViewPager(binding.viewPager)
              } catch (e: Exception) {
              }
          }

      }*/




    private fun setData() {
        try {
            binding.constrentCardLayout.visibility= VISIBLE

            //setSlider
            imageViewPagerAdapter = ImageViewPagerAdapter(this@StoreDetailActvity,  dt.venue_gallery)
            binding.viewPager.adapter = imageViewPagerAdapter
            binding.dotsIndicator.setViewPager(binding.viewPager)

            binding.storeDeatilTitle.text = dt.store_name
            binding.storeDeatilRating.text = dt.rating.avg_rating
            binding.storeDeatilOpenTime.text = "Open at : " + dt.open_time + " To " + dt.close_time
            binding.storeDeatilSubTitle.text = "Free Entry " + dt.free_start_time + " To " + dt.free_end_time
            binding.storeDeatilPhno.text = "+44 "+dt.store_number
            binding.storeDeatilEmail.text = dt.store_email
            binding.storeDeatilAddrs.text = dt.store_address

             // fav = intent.getStringExtra(AppConstant.INTENT_EXTRAS.FAVROUITE_VALUE)!!
            binding.storeDeatilFav.visibility= VISIBLE
           if (dt.favrouite == "1") {
           // if (fav == "1") {
                favStatus = "1"
                binding.storeDeatilFav.setImageResource(R.drawable.fav_selected)
            } else {
                favStatus = "0"
                binding.storeDeatilFav.setImageResource(R.drawable.fav_unselected)
            }
            binding.storeDeatilAddRemBarCrl.visibility = VISIBLE
            if(dt.barcrawl=="1"){
                addBarCrawlStatus = "1"
                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)
            }else{
                addBarCrawlStatus = "0"
                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)
            }
            //topImg
            Glide.with(this@StoreDetailActvity)
                .load(PreferenceKeeper.instance.imgPathSave + dt.store_logo)
                .error(R.drawable.no_image)
                .into(binding.storeDeatilLogo)

            //faciltyList
            facilityList = dt.venue_facility as ArrayList
            showMapLoc(dt.store_lattitude, dt.store_longitude)
            if(dt.store_description.isNotBlank()) {
                val htmlData = dt.store_description
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    binding.storeDeatilMenuDesc.text = Html.fromHtml(htmlData,Html.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.storeDeatilMenuDesc.text = Html.fromHtml(htmlData)
                }

            }else{
                binding.storeDeatilLinerMenuDesc.visibility=GONE
            }
        } catch (e: Exception) {
            MyApp.popErrorMsg("StoreDetail",""+e.toString(),THIS!!)
        }

    }


    private fun showMapLoc(storeLattitude: String, storeLongitude: String) {
        val latLng =
            LatLng(Commons.strToDouble(storeLattitude), Commons.strToDouble(storeLongitude))
        mMap.addMarker(MarkerOptions().position(latLng))
        val yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 4.0f)
        mMap.animateCamera(yourLocation)
        binding.storeDeatilMap.setImageBitmap(null)
        mMap.setOnMapLoadedCallback {
            mMap.snapshot { bitmap ->
                binding.storeDeatilMap.setImageBitmap(bitmap)
                // If map won't be used afterwards, remove it's views
                //              ((FrameLayout)findViewById(R.id.map)).removeAllViews();
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        val success =
            googleMap.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }

    lateinit var storDetailFoodHorizontalAdapter: StorDetailFoodHorizontalAdapter
    private fun setListHorizntalFood(allProducts: MutableList<VenuDetailModel.AllProduct>) {
        storDetailFoodHorizontalAdapter = StorDetailFoodHorizontalAdapter(
            this@StoreDetailActvity,
            allProducts,
            object : StorDetailFoodHorizontalAdapter.ClickListener {
                override fun onClick(pos: Int) {

                    for (i in 0 until allProducts.size) {
                        allProducts[i].isSelected = pos == i
                    }
                    with(storDetailFoodHorizontalAdapter) { notifyDataSetChanged() }
                    when (pos) {
                        0 -> {
                            drinksList = dt.all_products[0].records
                            setListDrinks()
                        }
                        1 -> {
                            drinksList = dt.all_products[1].records
                            setListFoods()
                        }
                        2 -> {
                            drinksList = dt.all_products[2].records
                            setListSnacks()
                        }
                        3 -> {
                            drinksList = dt.all_products[3].records
                            setListPkg()
                        }
                    }
                }

            })


        binding.storeDeatilHorizintalRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetailActvity, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = storDetailFoodHorizontalAdapter
        }

    }

    private fun initView() {
        val supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.storeDeatillocMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@StoreDetailActvity)

        userVenueDetailViewModel = CommonViewModel(this)
        sendQueryViewModel = CommonViewModel(this)
        doFavViewModel = CommonViewModel(this)
        doAddBarCrawlModel = CommonViewModel(this)
        setTouchNClick(binding.storeDeatilMenu)
        setTouchNClick(binding.storeDeatilAddVenuRel)
      //  setTouchNClick(binding.storeDeatilDiscount)
        setTouchNClick(binding.storeDeatilMore)
        setTouchNClick(binding.storeDeatilBakBtn)
        setTouchNClick(binding.storeDeatilFacilityBtn)
        setTouchNClick(binding.storeDeatilPlaceOrder)
        setTouchNClick(binding.storeDeatilPreBookingBtn)
        setTouchNClick(binding.storeDeatilSharLoc)
        setTouchNClick(binding.storeDeatilDirection)
        setTouchNClick(binding.storeDeatilFav)

        val str1 = resources.getString(R.string.Direction)
        var settext = "<u>$str1 </u>"
        binding.storeDeatilDirection.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)
        storeType = intent.getStringExtra(AppConstant.INTENT_EXTRAS.StoreType)!!
        if(storeType=="4"){
            binding.storeDeatilFacilityBtn.visibility = GONE
            binding.storeDeatilPreBookingBtn.visibility = GONE
            binding.storeDeatilAddVenuRel.visibility = GONE
        }

    }


    lateinit var dt: VenuDetailModel.Data

    lateinit var barMenuAdapter: DrinksMenuAdapter
  //  lateinit var foodsMenuAdapter: FoodsMenuAdapter
   // lateinit var snacksMenuAdapter: SnacksMenuAdapter
    lateinit var pakgAdapter: PackageAdapter
    lateinit var drinksList : MutableList<VenuDetailModel.Record>
   // lateinit var foodsList : ArrayList<VenuDetailModel.Record>

    private fun user_venue_detailAPICALL() {
        progressDialog.show(this@StoreDetailActvity, "")
        val map = HashMap<String, String>()
      // map["id"] = venuID
        map["id"] = "217"

        userVenueDetailViewModel.userVenueDetail(map).observe(this@StoreDetailActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        dt = detailData.data
                        setData()
                        if(storeType=="4") {
                            //here packeg part not include
                            var vv=dt.all_products
                            vv.removeLast()
                            vv[0].isSelected=true//first time waana to select
                            setListHorizntalFood(vv)
                        }
                        else {
                            dt.all_products[0].isSelected=true
                            setListHorizntalFood(dt.all_products)
                        }
                        drinksList = ArrayList()
                        drinksList = dt.all_products[0].records
                       // foodsList = dt.all_products[1].records
                        setListDrinks()

                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                }
            }
        })
    }




    private fun addRemoveBarCrawlAPICall() {
      //  progressDialog.show(this@StoreDetailActvity, "")
        val map = HashMap<String, String>()
        map["venue_id"] = venuID
        map["vendor_id"] = dt.vendor_detail.id
       // map["status"] =if(addBarCrawlStatus.equals("1")) "0" else "1"
        map["status"] = addBarCrawlStatus
        map["store_type"] =dt.store_type

        doAddBarCrawlModel.doAddBarCrawl(map).observe(this@StoreDetailActvity, {
                when (it.status) {
                    Status.SUCCESS -> {
                       // progressDialog.dialog.dismiss()
                        it.data?.let {
                            try {
//                                Log.d("ok", "add_favouriteAPICALL: " + detailData.data.status)
//                                if (detailData.data.status == "1") {
//                                    addBarCrawlStatus = "0"
//                                    binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)
//
//                                } else {
//                                    addBarCrawlStatus = "1"
//                                    binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)
//                                }
                               // MyApp.ShowTost(this@StoreDetailActvity,detailData.message)
                            } catch (e: Exception) {
                               // MyApp.popErrorMsg("StoreDetail",""+e.toString(),this@StoreDetailActvity)
                            }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                       // progressDialog.dialog.dismiss()
                       // Utills.showSnackBarOnError(binding.rootLayoutStorDetail, it.message!!, this@StoreDetailActvity)
                    }
                }
            })

    }


    private fun add_favouriteAPICALL() {
       // progressDialog.show(this@StoreDetail, "")
        var map = HashMap<String, String>()
        map["venue_id"] = venuID
        map["vendor_id"] = dt.vendor_detail.id
        map["status"] = favStatus


        doFavViewModel.doFavItem(map).observe(this@StoreDetailActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                 //   progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            Log.d("ok", "add_favouriteAPICALL: " + detailData.data.status)

                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                  //  progressDialog.dialog.dismiss()
                    Utills.showErrorToast(this@StoreDetailActvity, it.message!! )
                }
            }
        })
    }

    private fun showPopUpFacilities() {
        val adDialog = Dialog(this@StoreDetailActvity, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        adDialog.setContentView(R.layout.facility_dialog)
        adDialog.setCancelable(false)

        val listRecyclerview: RecyclerView = adDialog.findViewById(R.id.dialog_recycler)
        val dialog_close: ImageView = adDialog.findViewById(R.id.dialog_close)
        val dialog_QueryBtn: Button = adDialog.findViewById(R.id.dialog_QueryBtn)
        setTouchNClick(dialog_close)
        //  var list = setListFacility()
        var facilityAdapter =
            FacilityAdapter(this@StoreDetailActvity, facilityList, object : FacilityAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })
        dialog_QueryBtn.setOnClickListener {
            adDialog.dismiss()
            showQueryPopup()

        }

        listRecyclerview.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetailActvity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = facilityAdapter

        }

        dialog_close.setOnClickListener {
            adDialog.dismiss()
        }
        adDialog.show()

    }

    private fun showQueryPopup() {
        val adDialog = Dialog(this@StoreDetailActvity, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        adDialog.setContentView(R.layout.query_dialog)
        adDialog.setCancelable(false)


        val dialog_close: ImageView = adDialog.findViewById(R.id.dialog_close)
        val dialog_QueryBtn: Button = adDialog.findViewById(R.id.dialog_QueryBtn)
        val dialog_editMsg: EditText = adDialog.findViewById(R.id.dialog_editMsg)
        val textCount: TextView = adDialog.findViewById(R.id.textCount)
        setTouchNClick(dialog_close)

        dialog_editMsg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                textCount.text = ""+s.toString().length+"/100"
            }
        })

        dialog_QueryBtn.setOnClickListener {
            if (dialog_editMsg.text.toString().isNullOrBlank()) {
                MyApp.popErrorMsg("", "Please type your query", THIS!!)
            } else {
                adDialog.dismiss()
                userFacilityAPICALL()
            }
        }


        dialog_close.setOnClickListener {
            adDialog.dismiss()
        }
        adDialog.show()

    }

    private fun userFacilityAPICALL() {
        progressDialog.show(this@StoreDetailActvity, "")
        // type (0=>Enquiry, 1=>Complaints)
        var map = HashMap<String, String>()
        map["type"] = "0"
        map["email"] = PreferenceKeeper.instance.loginResponse!!.email
        map["name"] = PreferenceKeeper.instance.loginResponse!!.name
        map["phonenumber"] = PreferenceKeeper.instance.loginResponse!!.phonenumber
        map["query"] = "my query"

        sendQueryViewModel.sendQuery(map).observe(this@StoreDetailActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            Utills.showSuccessToast(
                                this@StoreDetailActvity,
                                detailData.message

                            )
                        } catch (e: Exception) {
                            Utills.showErrorToast(
                                this@StoreDetailActvity,
                                e.toString()

                            )
                        }
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@StoreDetailActvity,
                        it.message!!

                    )
                }
            }
        })
    }


    override fun onBackPressed() {
        var myIntent = Intent()
        myIntent.putExtra("resultFav",favStatus)
       // myIntent.putExtra("resultBarcrwal",if(addBarCrawlStatus.equals("1")) "0" else "1")
        myIntent.putExtra("resultBarcrwal", addBarCrawlStatus)
        setResult(Activity.RESULT_OK,myIntent)
        super.onBackPressed()
    }
    private fun setListDrinks() {
        barMenuAdapter = DrinksMenuAdapter(
            this@StoreDetailActvity,
            drinksList,
            object : DrinksMenuAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    drinksList[pos].isSelected = !drinksList[pos].isSelected
                    barMenuAdapter.notifyDataSetChanged()
                }

//                override fun onClickSub(pos: Int, subPos: Int) {
//                    drinksList[pos].products[subPos].isChekd = !drinksList[pos].products[subPos].isChekd
//                    barMenuAdapter.notifyDataSetChanged()
//                }

                override fun onClickPluse(pos: Int, subPos: Int) {
                    try {
                        val qty = drinksList[pos].products[subPos].quantityLocal + 1
                        val aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                        val bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                        var per = bb/100
                        var disValue= aa-per
                        drinksList[pos].products[subPos].quantityLocal = qty
                        drinksList[pos].products[subPos].totPriceLocal = disValue
                        barMenuAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for(i in 0 until drinksList[pos].products.size){
                            totCost= totCost+drinksList[pos].products[i].totPriceLocal
                        }

                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                    }
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    try {
                        if (drinksList[pos].products[subPos].quantityLocal > 0) {
                            var qty = drinksList[pos].products[subPos].quantityLocal - 1
                            var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                            var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                            var per = bb/100
                            var disValue= aa-per
                            drinksList[pos].products[subPos].quantityLocal = qty
                            drinksList[pos].products[subPos].totPriceLocal = disValue
                            barMenuAdapter.notifyDataSetChanged()
                            var totCost = 0.0
                            for(i in 0 until drinksList[pos].products.size){
                                totCost= totCost+drinksList[pos].products[i].totPriceLocal
                            }
//                            binding.preBookingDrinksValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
 //                            doGrandTot()
                        }
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
                    }
                }

            })
        binding.storeDeatilDrinksRecycler.isNestedScrollingEnabled = true
        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetailActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = barMenuAdapter
        }
    }

    private fun setListFoods() {
        barMenuAdapter = DrinksMenuAdapter(
            this@StoreDetailActvity,
            drinksList,
            object : DrinksMenuAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    drinksList[pos].isSelected = !drinksList[pos].isSelected
                    barMenuAdapter.notifyDataSetChanged()
                }

//                override fun onClickSub(pos: Int, subPos: Int) {
//                    drinksList[pos].products[subPos].isChekd =
//                        !drinksList[pos].products[subPos].isChekd
//                    barMenuAdapter.notifyDataSetChanged()
//                }

                override fun onClickPluse(pos: Int, subPos: Int) {
                    try {
                        var qty = drinksList[pos].products[subPos].quantityLocal + 1
                        var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                        var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                        var per = bb/100
                        var disValue= aa-per
                        drinksList[pos].products[subPos].quantityLocal = qty
                        drinksList[pos].products[subPos].totPriceLocal = disValue
                        barMenuAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for(i in 0 until drinksList[pos].products.size){
                            totCost= totCost+drinksList[pos].products[i].totPriceLocal
                        }
//                        binding.preBookingFoodPriceValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
//                        doGrandTot()
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                    }
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    try {
                        if (drinksList[pos].products[subPos].quantityLocal > 0) {
                            var qty = drinksList[pos].products[subPos].quantityLocal - 1
                            var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                            var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                            var per = bb/100
                            var disValue= aa-per
                            drinksList[pos].products[subPos].quantityLocal = qty
                            drinksList[pos].products[subPos].totPriceLocal = disValue
                            barMenuAdapter.notifyDataSetChanged()
                            var totCost = 0.0
                            for(i in 0 until drinksList[pos].products.size){
                                totCost= totCost+drinksList[pos].products[i].totPriceLocal
                            }
//                            binding.preBookingFoodPriceValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
//                            doGrandTot()
                        }
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
                    }
                }

            })
        binding.storeDeatilDrinksRecycler.isNestedScrollingEnabled = true
        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetailActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = barMenuAdapter
        }
    }

    private fun setListSnacks() {
        barMenuAdapter = DrinksMenuAdapter(
            this@StoreDetailActvity,
            drinksList,
            object : DrinksMenuAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    drinksList[pos].isSelected = !drinksList[pos].isSelected
                    barMenuAdapter.notifyDataSetChanged()
                }

//                override fun onClickSub(pos: Int, subPos: Int) {
//                    drinksList[pos].products[subPos].isChekd =
//                        !drinksList[pos].products[subPos].isChekd
//                    barMenuAdapter.notifyDataSetChanged()
//                }

                override fun onClickPluse(pos: Int, subPos: Int) {
                    try {
                        var qty = drinksList[pos].products[subPos].quantityLocal + 1
                        var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                        var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                        var per = bb/100
                        var disValue= aa-per
                        drinksList[pos].products[subPos].quantityLocal = qty
                        drinksList[pos].products[subPos].totPriceLocal = disValue
                        barMenuAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for(i in 0 until drinksList[pos].products.size){
                            totCost= totCost+drinksList[pos].products[i].totPriceLocal
                        }
//                        binding.preBookingSnakesValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
//                        doGrandTot()
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                    }
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    try {
                        if (drinksList[pos].products[subPos].quantityLocal > 0) {
                            var qty = drinksList[pos].products[subPos].quantityLocal - 1
                            var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                            var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                            var per = bb/100
                            var disValue= aa-per
                            drinksList[pos].products[subPos].quantityLocal = qty
                            drinksList[pos].products[subPos].totPriceLocal = disValue
                            barMenuAdapter.notifyDataSetChanged()
                            var totCost = 0.0
                            for(i in 0 until drinksList[pos].products.size){
                                totCost= totCost+drinksList[pos].products[i].totPriceLocal
                            }
//                            binding.preBookingSnakesValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
//                            doGrandTot()
                        }
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
                    }
                }

            })
        binding.storeDeatilDrinksRecycler.isNestedScrollingEnabled = true
        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetailActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = barMenuAdapter
        }
    }

    private fun setListPkg() {
        pakgAdapter = PackageAdapter(this@StoreDetailActvity, drinksList, object : PackageAdapter.ClickListener {
            override fun onClickChk(subPos: Int) {
                drinksList[subPos].isSelected = !drinksList[subPos].isSelected
                pakgAdapter.notifyDataSetChanged()
            }

            override fun onClickPlus(pos: Int) {
                try {
                    var qty = drinksList[pos].quantityLocal + 1
                    var aa = qty * Commons.strToDouble(drinksList[pos].price)
                    var bb = aa*Commons.strToDouble(drinksList[pos].discount)
                    var per = bb/100
                    var disValue= aa-per
                    drinksList[pos].quantityLocal = qty
                    drinksList[pos].totPriceLocal = disValue
                    pakgAdapter.notifyDataSetChanged()
                    var totCost = 0.0
                    for(i in 0 until drinksList.size){
                        totCost= totCost+drinksList[i].totPriceLocal
                    }
                    //binding.preBookingTablePriceValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                    //GrandTotal
                   // doGrandTot()
                } catch (e: Exception) {
                    MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                }

            }

            override fun onClickMinus(pos: Int) {
                try {
                    if (drinksList[pos].quantityLocal > 0) {
                        var qty = drinksList[pos].quantityLocal - 1
                        var aa = qty * Commons.strToDouble(drinksList[pos].price)
                        var bb = aa * Commons.strToDouble(drinksList[pos].discount)
                        var per = bb / 100
                        var disValue = aa - per
                        drinksList[pos].quantityLocal = qty
                        drinksList[pos].totPriceLocal = disValue
                        pakgAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for (i in 0 until drinksList.size) {
                            totCost = totCost + drinksList[i].totPriceLocal
                        }
                       // binding.preBookingTablePriceValue.text =
                            resources.getString(R.string.currency_sumbol) + totCost.toString()
                        //doGrandTot()
                    }
                } catch (e: Exception) {
                    MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
                }
            }
        })
        binding.storeDeatilDrinksRecycler.isNestedScrollingEnabled = true
        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetailActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = pakgAdapter
        }
    }
}