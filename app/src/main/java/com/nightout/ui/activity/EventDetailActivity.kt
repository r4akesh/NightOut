package com.nightout.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Html
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.nightout.R
import com.nightout.adapter.ImageViewPagerAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.EventdetailActvityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import java.text.DecimalFormat

class EventDetailActivity : BaseActivity(), OnMapReadyCallback {

    lateinit var binding :EventdetailActvityBinding
    var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    var venuID = ""
    private val progressDialog = CustomProgressDialog()
    lateinit var dt: VenuDetailModel.Data
    lateinit var userVenueDetailViewModel: CommonViewModel
    lateinit var mMap: GoogleMap
    lateinit var doFavViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@EventDetailActivity,R.layout.eventdetail_actvity)
        inItView()

        venuID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_ID)!!
        if (!venuID.isNullOrBlank()) {
            Log.d("venuID", "onCreate: $venuID")
           user_venue_detailAPICALL()
        }
        if(intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.iSFROMESelectBarCrwlActivity,false)){
            binding.eventDetailBuyTkt.visibility= View.GONE
        }
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.eventDetailBuyTkt){
            startActivity(Intent (this@EventDetailActivity,BookTicketActivity::class.java)
                .putExtra(AppConstant.INTENT_EXTRAS.EVENTDETAIL_POJO,dt))
        }
        else if(v==binding.eventDetailBakBtn){
            val myIntent = Intent()
            myIntent.putExtra("resultFav",favStatus)
            setResult(Activity.RESULT_OK,myIntent)
            finish()

        }
        else if(v==binding.eventDeatilFav){
            if (favStatus == "1") {
                favStatus = "0"
                binding.eventDeatilFav.setImageResource(R.drawable.fav_unselected)
            } else {
                favStatus = "1"
                binding.eventDeatilFav.setImageResource(R.drawable.fav_selected)
            }
            binding.eventDeatilFav.startAnimation(AnimationUtils.loadAnimation(THIS!!, R.anim.bounce))
            add_favouriteAPICALL()
        }
        else if(v==binding.eventDetailShareLoc){
            try {
                val latitude: Double = Commons.strToDouble(dt.store_lattitude)
                val longitude: Double = Commons.strToDouble(dt.store_longitude)
               /// val uri = ("geo:" + latitude.toString() + "," + longitude.toString() + "?q=" + latitude.toString() + "," + longitude)
              // startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                // val uri = "http://maps.google.com/maps?saddr=$latitude,$longitude"
                val uri = "https://www.google.com/maps/?q=$latitude,$longitude"
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val ShareSub = "Here is my location"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, ShareSub)
                sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
                startActivity(Intent.createChooser(sharingIntent, "Share via"))
            } catch (e: Exception) {
            }
        }

        else if(v==binding.eventDetailAddCalndra){
            addEvent()
        }

        else if(v==binding.eventDetaiDirection){
            try {
                var crntLat = PreferenceKeeper.instance.currentLat
                var crntLong = PreferenceKeeper.instance.currentLong
                var eventLat = dt.store_lattitude
                var eventLong = dt.store_longitude
                if(eventLat.isNotBlank() && eventLong.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=$crntLat,$crntLong&daddr=$eventLat,$eventLong"))
                    startActivity(intent)
                }
            } catch (e: Exception) {
            }
        }
    }




    private fun user_venue_detailAPICALL() {
        progressDialog.show(this@EventDetailActivity, "")
        val map = HashMap<String, String>()
        map["id"] = venuID

        userVenueDetailViewModel.userVenueDetail(map).observe(this@EventDetailActivity) {
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
                    Utills.showErrorToast(
                        this@EventDetailActivity ,
                        it.message!!

                    )
                }
            }
        }
    }
    private fun addEvent() {


        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
          //  .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 1632990051544)
          //  .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, 1633026600000)
            .putExtra(CalendarContract.Events.TITLE, dt.store_name)
           // .putExtra(CalendarContract.Events.DESCRIPTION, dt.store_description)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, dt.store_address)
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
          //  .putExtra(Intent.EXTRA_EMAIL, "abc@example.com,codeplayon@example.com");

        startActivity(intent)
        Log.d("TAG", "addEvent: ")
    }
    var favStatus = "0"
    private fun setData() {
        try {
            binding.eventDetailScrollLayout.visibility=VISIBLE
            binding.eventDeatilFav.visibility=VISIBLE
            if(dt.ticket_qty == ""){
                binding.eventDetailBuyTkt.text = "Tickets are not available"
                 binding.eventDetailBuyTkt.setTextColor(resources.getColor(R.color.red_clr))
                binding.eventDetailBuyTkt.isClickable = false
                binding.eventDetailBuyTkt.isEnabled = false
            }else{
                binding.eventDetailBuyTkt.text = resources.getString(R.string.Buy_Ticket)
            }
            //setSlider
            try {
                imageViewPagerAdapter = ImageViewPagerAdapter(this@EventDetailActivity, dt.venue_gallery)
                binding.viewPager.adapter = imageViewPagerAdapter
                binding.dotsIndicator.setViewPager(binding.viewPager)
            } catch (e: Exception) {
            }
            binding.eventDetailTitle.text = dt.store_name
            //   binding.storeDeatilRating.text = dt.rating.avg_rating
            binding.eventDetailOpenTime.text = "Start at : " + dt.event_start_time + " To " + dt.event_end_time
            //   binding.storeDeatilSubTitle.text = "Free Entry " + dt.free_start_time + " To " + dt.free_end_time
            binding.eventDetailPhno.text = "+44 "+dt.store_number
            binding.eventDetailEmail.text = dt.store_email
            binding.eventDetailDate.text = dt.event_date
            binding.eventDetailRating.text = resources.getString(R.string.currency_sumbol)+dt.sale_price
           // binding.eventDetailAge.text = dt.age_limit+" age limit"
            binding.eventDetailAge.text = dt.age_limit
            binding.eventDetailMusic.text = dt.party_theme
            binding.eventDetailCocktail.text = dt.dress_code
            binding.eventDetaiAddrs.text = "Address :  ${dt.store_address}"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.eventDetailDescription.text = Html.fromHtml(dt.store_description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.eventDetailDescription.text = Html.fromHtml(dt.store_description)
            }
            val crntLat = Commons.strToDouble(PreferenceKeeper.instance.currentLat!!)
            val crntLong = Commons.strToDouble(PreferenceKeeper.instance.currentLong!!)
            val eventLat = (dt.store_lattitude)
            val eventLong = (dt.store_longitude)
            val vv =MyApp.getDestance(crntLat,crntLong,eventLat,eventLong)*0.621371
            binding.eventDetailDistence.text =""+ DecimalFormat("##.##").format(vv)+" miles"

            //  binding.storeDeatilAddrs.text = dt.store_address
            if (dt.favrouite == "1") {
                favStatus = "1"
                binding.eventDeatilFav.setImageResource(R.drawable.fav_selected)
            } else {
                favStatus = "0"
                binding.eventDeatilFav.setImageResource(R.drawable.fav_unselected)
            }
            //topImg
            Glide.with(this@EventDetailActivity)
                .load(PreferenceKeeper.instance.imgPathSave + dt.store_logo)
                .error(R.drawable.no_image)
                .into(binding.eventDetailLogo)

            showMapLoc(dt.store_lattitude, dt.store_longitude)
        } catch (e: Exception) {
            MyApp.popErrorMsg("EventDetail",""+e.toString(),THIS!!)
        }
    }

    private fun showMapLoc(storeLattitude: String, storeLongitude: String) {
        val latLng =
            LatLng(Commons.strToDouble(storeLattitude), Commons.strToDouble(storeLongitude))
        mMap.addMarker(MarkerOptions().position(latLng))
        val yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 8.0f)
        mMap.animateCamera(yourLocation)
        binding.eventDetailMap.setImageBitmap(null)
        mMap.setOnMapLoadedCallback {
            mMap.snapshot { bitmap ->
                binding.eventDetailMap.setImageBitmap(bitmap)
                // If map won't be used afterwards, remove it's views
                //              ((FrameLayout)findViewById(R.id.map)).removeAllViews();
            }
        }
    }

  /*  private fun setTopImgSlider() {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST)) {
            try {
                var venueGalleryList = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST) as ArrayList<VenueGallery>
                imageViewPagerAdapter = ImageViewPagerAdapter(this@EventDetail, venueGalleryList)
                binding.viewPager.adapter = imageViewPagerAdapter
                binding.dotsIndicator.setViewPager(binding.viewPager)
            } catch (e: Exception) {
            }
        }

    }*/
    private fun inItView() {
        setTouchNClick(binding.eventDetailAddCalndra)
        setTouchNClick(binding.eventDetailBuyTkt)
        setTouchNClick(binding.eventDetailBakBtn)
        setTouchNClick(binding.eventDetaiDirection)
        setTouchNClick(binding.eventDetailShareLoc)
        setTouchNClick(binding.eventDeatilFav)
        userVenueDetailViewModel = CommonViewModel(this@EventDetailActivity)
        doFavViewModel = CommonViewModel(this@EventDetailActivity)
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.eventDeatillocMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@EventDetailActivity)
    }

    private fun add_favouriteAPICALL() {
        //progressDialog.show(this@EventDetail, "")
        var map = HashMap<String, String>()
        map["venue_id"] = venuID
        map["vendor_id"] = dt.vendor_detail.id
        map["status"] = favStatus


        doFavViewModel.doFavItem(map).observe(this@EventDetailActivity) {
            when (it.status) {
                Status.SUCCESS -> {
                 //   progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
//                            Log.d("ok", "add_favouriteAPICALL: " + detailData.data.status)
//                            if (detailData.data.status == "1") {
//                                favStatus = "0"
//                                binding.eventDeatilFav.setImageResource(R.drawable.fav_selected)
//                            } else {
//                                favStatus = "1"
//                                binding.eventDeatilFav.setImageResource(R.drawable.fav_unselected)
//                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                  //  progressDialog.dialog.dismiss()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap!!
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }

    override fun onBackPressed() {
        var myIntent = Intent()
        myIntent.putExtra("resultFav",favStatus)
        setResult(Activity.RESULT_OK,myIntent)
        finish()
        super.onBackPressed()
    }




}