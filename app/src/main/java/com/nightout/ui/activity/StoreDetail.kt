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
import com.nightout.viewmodel.SendQueryViewModel
import kotlinx.android.synthetic.main.discount_desc.view.*
import android.text.Editable

import android.text.TextWatcher
import com.nightout.viewmodel.CommonViewModel


class StoreDetail : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: StoredetailActivityBinding
    lateinit var userVenueDetailViewModel: CommonViewModel
    lateinit var doFavViewModel: CommonViewModel
    lateinit var doAddBarCrawlModel: CommonViewModel
    lateinit var sendQueryViewModel: CommonViewModel
    lateinit var mMap: GoogleMap
    var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    private val progressDialog = CustomProgressDialog()
    var facilityList = ArrayList<VenuDetailModel.Facility>()
    var venuID = ""
    var favStatus = "0"
    var addBarCrawlStatus = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@StoreDetail, R.layout.storedetail_activity)
        initView()

        venuID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_ID)!!
        if (!venuID.isNullOrBlank()) {
            user_venue_detailAPICALL()
        }
        setListHorizntalFood()
        setListDrinksDummy()//first time set
        // add_Remove_bar_crawlAPICAll()
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        if(v==binding.storeDeatilAddVenuRel){
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
            add_favouriteAPICALL()
        } else if (v == binding.storeDeatilPreBookingBtn) {
            startActivity(Intent(this@StoreDetail, PreBookingActivity::class.java))

        } else if (v == binding.storeDeatilPlaceOrder) {
            startActivity(Intent(this@StoreDetail, OrderDetailActivity::class.java))

        } else if (v == binding.storeDeatilFacilityBtn) {
            if (facilityList != null && facilityList.size > 0)
                showPopUpFacilities() else {
                MyApp.popErrorMsg(
                    "",
                    resources.getString(R.string.facilitynot_avail),
                    this@StoreDetail
                )
            }
        } else if (v == binding.storeDeatilBakBtn) {
            var myIntent = Intent()
            myIntent.putExtra("result",favStatus)
            setResult(Activity.RESULT_OK,myIntent)
            finish()

        } else if (v == binding.storeDeatilMenu) {
            binding.storeDeatilMenu.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.black))
            binding.storeDeatilDiscount.setBackgroundResource(0)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))


            binding.storeDeatilMenuDesc.visibility = VISIBLE
            binding.storeDeatilDisDesc.visibility = GONE
            binding.storeDeatilMoreDesc.visibility = GONE


        } else if (v == binding.storeDeatilDiscount) {
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
        } else if (v == binding.storeDeatilMore) {
            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilDiscount.setBackgroundResource(0)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.black))

            binding.storeDeatilMoreDesc.visibility = VISIBLE
            binding.storeDeatilDisDesc.visibility = GONE
            binding.storeDeatilMenuDesc.visibility = GONE
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
        try {//setSlider
            imageViewPagerAdapter = ImageViewPagerAdapter(this@StoreDetail,  dt.venue_gallery)
            binding.viewPager.adapter = imageViewPagerAdapter
            binding.dotsIndicator.setViewPager(binding.viewPager)

            binding.storeDeatilTitle.text = dt.store_name
            binding.storeDeatilRating.text = dt.rating.avg_rating
            binding.storeDeatilOpenTime.text = "Open at : " + dt.open_time + " To " + dt.close_time
            binding.storeDeatilSubTitle.text =
                "Free Entry " + dt.free_start_time + " To " + dt.free_end_time
            binding.storeDeatilPhno.text = dt.store_number
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
                addBarCrawlStatus = "0"
                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)
            }else{
                addBarCrawlStatus = "1"
                binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)
            }
            //topImg
            Glide.with(this@StoreDetail)
                .load(PreferenceKeeper.instance.imgPathSave + dt.store_logo)
                .error(R.drawable.no_image)
                .into(binding.storeDeatilLogo)

            //faciltyList
            facilityList = dt.facilities
            showMapLoc(dt.store_lattitude, dt.store_longitude)
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
            googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }

    lateinit var storDetailFoodHorizontalAdapter: StorDetailFoodHorizontalAdapter
    private fun setListHorizntalFood() {
        var listFood = ArrayList<StorDetailFoodModel>()
        listFood.add(StorDetailFoodModel("Drinks", true))
        listFood.add(StorDetailFoodModel("Food", false))
        listFood.add(StorDetailFoodModel("Snacks", false))
        listFood.add(StorDetailFoodModel("Packages", false))

        storDetailFoodHorizontalAdapter = StorDetailFoodHorizontalAdapter(
            this@StoreDetail,
            listFood,
            object : StorDetailFoodHorizontalAdapter.ClickListener {
                override fun onClick(pos: Int) {

                    for (i in 0 until listFood.size) {
                        listFood[i].isSelected = pos == i
                    }
                    storDetailFoodHorizontalAdapter.notifyDataSetChanged()
                    if (pos == 0)
                        setListDrinksDummy()
                    else if (pos == 1)
                        setListFoodDummy()
                    else if (pos == 2)
                        setListSnakesDummy()
                    else if (pos == 3)
                        setListPackgesDummy()
                }

            })


        binding.storeDeatilHorizintalRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = storDetailFoodHorizontalAdapter
        }

    }

    lateinit var subAdapter: DrinksSubAdapter
    private fun setListPackgesDummy() {
        var listDrinks = ArrayList<SubFoodModel>()
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )

        subAdapter =
            DrinksSubAdapter(this@StoreDetail, listDrinks, object : DrinksSubAdapter.ClickListener {
                override fun onClickChk(subPos: Int) {
                    listDrinks[subPos].isChekd = !listDrinks[subPos].isChekd

                    subAdapter.notifyDataSetChanged()
                }

            })
        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = subAdapter
        }
    }


    private fun setListFoodDummy() {
        var listDrinks = ArrayList<StoreDetailDrinksModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Quick Noodles",
                "1 Plate",
                R.drawable.chiness_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.chiness_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.chiness_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Italian", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Quick Noodles",
                "1 Plate",
                R.drawable.chiness_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.chiness_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.chiness_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Chinese", false, listSub))

        drinksAdapter =
            DrinksAdapter(this@StoreDetail, listDrinks, object : DrinksAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listDrinks[pos].isSelected = !listDrinks[pos].isSelected
                    drinksAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {

                    listDrinks[pos].list[subPos].isChekd = !listDrinks[pos].list[subPos].isChekd

                    drinksAdapter.notifyDataSetChanged()
                }


            })

        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = drinksAdapter
        }

    }

    private fun setListSnakesDummy() {
        var listDrinks = ArrayList<StoreDetailDrinksModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Sprouts", "1 Plate", R.drawable.snaks_img1, "Price : $10", false))
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.chiness_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.snaks_img1,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Non-Veg", false, listSub))


        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Quick Noodles",
                "1 Plate",
                R.drawable.snaks_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.snaks_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.chiness_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Veg", false, listSub))



        drinksAdapter =
            DrinksAdapter(this@StoreDetail, listDrinks, object : DrinksAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listDrinks[pos].isSelected = !listDrinks[pos].isSelected
                    drinksAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {

                    listDrinks[pos].list[subPos].isChekd = !listDrinks[pos].list[subPos].isChekd

                    drinksAdapter.notifyDataSetChanged()
                }


            })

        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = drinksAdapter
        }
    }


    lateinit var drinksAdapter: DrinksAdapter
    private fun setListDrinksDummy() {
        var listDrinks = ArrayList<StoreDetailDrinksModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Grey Goose",
                "1 Glass",
                R.drawable.drink_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(SubFoodModel("Ciroc", "3 Glass", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(
            SubFoodModel(
                "Belvedere",
                "2 Glass",
                R.drawable.drink_img2,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Ketel One",
                "5 Glass",
                R.drawable.drink_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Finlandia One",
                "2 Glass",
                R.drawable.drink_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Scotch", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Grey Goose",
                "1 Glass",
                R.drawable.drink_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(SubFoodModel("Ciroc", "3 Glass", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(
            SubFoodModel(
                "Belvedere",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Ketel One",
                "5 Glass",
                R.drawable.drink_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Finlandia One",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Vodka", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Grey Goose",
                "1 Glass",
                R.drawable.drink_img2,
                "Price : $10",
                false
            )
        )
        listSub.add(SubFoodModel("Ciroc", "3 Glass", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(
            SubFoodModel(
                "Belvedere",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Ketel One",
                "5 Glass",
                R.drawable.drink_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Finlandia One",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Rum", false, listSub))


        drinksAdapter =
            DrinksAdapter(this@StoreDetail, listDrinks, object : DrinksAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listDrinks[pos].isSelected = !listDrinks[pos].isSelected
                    drinksAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {

                    listDrinks[pos].list[subPos].isChekd = !listDrinks[pos].list[subPos].isChekd

                    drinksAdapter.notifyDataSetChanged()
                }


            })

        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = drinksAdapter
        }
    }

    private fun initView() {
        val supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.storeDeatillocMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@StoreDetail)

        userVenueDetailViewModel = CommonViewModel(this)
        sendQueryViewModel = CommonViewModel(this)
        doFavViewModel = CommonViewModel(this)
        doAddBarCrawlModel = CommonViewModel(this)
        setTouchNClick(binding.storeDeatilMenu)
        setTouchNClick(binding.storeDeatilAddVenuRel)
        setTouchNClick(binding.storeDeatilDiscount)
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

    }


    lateinit var dt: VenuDetailModel.Data
    private fun user_venue_detailAPICALL() {
        progressDialog.show(this@StoreDetail, "")
        var map = HashMap<String, String>()
        map["id"] = venuID!!

        userVenueDetailViewModel.userVenueDetail(map).observe(this@StoreDetail, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        dt = detailData.data
                        setData()
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
        progressDialog.show(this@StoreDetail, "")
        var map = HashMap<String, String>()
        map["venue_id"] = venuID
        map["vendor_id"] = dt.vendor_detail.id
        map["status"] =addBarCrawlStatus
        map["store_type"] =dt.store_type

        doAddBarCrawlModel.doAddBarCrawl(map).observe(this@StoreDetail, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        it.data?.let { detailData ->
                            try {
                                Log.d("ok", "add_favouriteAPICALL: " + detailData.data.status)
                                if (detailData.data.status == "1") {
                                    addBarCrawlStatus = "0"
                                    binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.save_fav)

                                } else {
                                    addBarCrawlStatus = "1"
                                    binding.storeDeatilAddRemBarCrl.setImageResource(R.drawable.ic_unseleted_barcrwl)
                                }
                                MyApp.ShowTost(this@StoreDetail,detailData.message)
                            } catch (e: Exception) {
                                MyApp.popErrorMsg("StoreDetail",""+e.toString(),this@StoreDetail)
                            }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        Utills.showSnackBarOnError(binding.rootLayoutStorDetail, it.message!!, this@StoreDetail)
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


        doFavViewModel.doFavItem(map).observe(this@StoreDetail, {
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
                    Utills.showSnackBarOnError(binding.rootLayoutStorDetail, it.message!!, this@StoreDetail)
                }
            }
        })
    }

    private fun showPopUpFacilities() {
        val adDialog = Dialog(this@StoreDetail, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        adDialog.setContentView(R.layout.facility_dialog)
        adDialog.setCancelable(false)

        val listRecyclerview: RecyclerView = adDialog.findViewById(R.id.dialog_recycler)
        val dialog_close: ImageView = adDialog.findViewById(R.id.dialog_close)
        val dialog_QueryBtn: Button = adDialog.findViewById(R.id.dialog_QueryBtn)
        setTouchNClick(dialog_close)
        //  var list = setListFacility()
        var facilityAdapter =
            FacilityAdapter(this@StoreDetail, facilityList, object : FacilityAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })
        dialog_QueryBtn.setOnClickListener {
            adDialog.dismiss()
            showQueryPopup()

        }

        listRecyclerview.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = facilityAdapter

        }

        dialog_close.setOnClickListener {
            adDialog.dismiss()
        }
        adDialog.show()

    }

    private fun showQueryPopup() {
        val adDialog = Dialog(this@StoreDetail, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
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
                textCount.setText(""+s.toString().length+"/100")
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
        progressDialog.show(this@StoreDetail, "")
        // type (0=>Enquiry, 1=>Complaints)
        var map = HashMap<String, String>()
        map["type"] = "0"
        map["email"] = PreferenceKeeper.instance.loginResponse!!.email
        map["name"] = PreferenceKeeper.instance.loginResponse!!.name
        map["phonenumber"] = PreferenceKeeper.instance.loginResponse!!.phonenumber
        map["query"] = "my query"

        sendQueryViewModel.sendQuery(map).observe(this@StoreDetail, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            Utills.showSnackBarOnError(
                                binding.rootLayoutStorDetail,
                                detailData.message!!,
                                this@StoreDetail
                            )
                        } catch (e: Exception) {
                            Utills.showSnackBarOnError(
                                binding.rootLayoutStorDetail,
                                e.toString(),
                                this@StoreDetail
                            )
                        }
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(
                        binding.rootLayoutStorDetail,
                        it.message!!,
                        this@StoreDetail
                    )
                }
            }
        })
    }


    override fun onBackPressed() {
        var myIntent = Intent()
        myIntent.putExtra("result",favStatus)
        setResult(Activity.RESULT_OK,myIntent)
        super.onBackPressed()
    }


}