package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.adapter.ImageViewPagerAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.EventdetailActvityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.model.VenueGallery
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.VenuDetailViewModel

class EventDetail : BaseActivity() {

    lateinit var binding :EventdetailActvityBinding
    var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    var venuID = ""
    private val progressDialog = CustomProgressDialog()
    lateinit var dt: VenuDetailModel.Data
    lateinit var userVenueDetailViewModel: VenuDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //setContentView(R.layout.eventdetail_actvity)
        binding = DataBindingUtil.setContentView(this@EventDetail,R.layout.eventdetail_actvity)
        inItView()
        setTopImgSlider()
        venuID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_ID)!!
        if (!venuID.isNullOrBlank()) {
            Log.d("venuID", "onCreate: "+venuID)
           user_venue_detailAPICALL()
        }

    }


    private fun user_venue_detailAPICALL() {
        progressDialog.show(this@EventDetail, "")
        var map = HashMap<String, Any>()
        map["id"] = venuID!!

        userVenueDetailViewModel.userVenueDetail(map).observe(this@EventDetail, {
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

    private fun setData() {
        binding.eventDetailTitle.text = dt.store_name
     //   binding.storeDeatilRating.text = dt.rating.avg_rating
        binding.eventDetailOpenTime.text = "Open at : " + dt.open_time + " To " + dt.close_time
     //   binding.storeDeatilSubTitle.text = "Free Entry " + dt.free_start_time + " To " + dt.free_end_time
        binding.eventDetailPhno.text = dt.store_number
        binding.eventDetailEmail.text = dt.store_email
        binding.eventDetailDate.text = "date"
        binding.eventDetailRating.text = "$"+dt.price
        binding.storeDeatilAddrs.text = dt.store_address
        if (dt.favrouite == "1") {
            favStatus = "0"
            binding.storeDeatilFav.setImageResource(R.drawable.fav_selected)
        } else {
            favStatus = "1"
            binding.storeDeatilFav.setImageResource(R.drawable.fav_unselected)
        }
    }


    private fun setTopImgSlider() {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST)) {
            try {
                var venueGalleryList = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.GALLERY_LIST) as ArrayList<VenueGallery>
                imageViewPagerAdapter = ImageViewPagerAdapter(this@EventDetail, venueGalleryList)
                binding.viewPager.adapter = imageViewPagerAdapter
                binding.dotsIndicator.setViewPager(binding.viewPager)
            } catch (e: Exception) {
            }
        }

    }
    private fun inItView() {
        setTouchNClick(binding.eventDetailPlaceOrder)
        setTouchNClick(binding.eventDetailBakBtn)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.eventDetailPlaceOrder){
            startActivity(Intent (this@EventDetail,BookTicketActivity::class.java))
        }
        else if(v==binding.eventDetailBakBtn){
           finish()
        }
    }
}