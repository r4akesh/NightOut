package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.BookticketActviityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.BookEventViewModel
import com.nightout.viewmodel.CommonViewModel

class BookTicketActivity : BaseActivity()  {
    lateinit var binding : BookticketActviityBinding
    lateinit var pojoEvntDetl : VenuDetailModel.Data
    var peopleCount = 1
    var totAmt = 0.0
    private val progressDialog = CustomProgressDialog()
    lateinit var bookEventViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BookTicketActivity,R.layout.bookticket_actviity)
        inItView()
        setToolBar()
        pojoEvntDetl= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.EVENTDETAIL_POJO) as VenuDetailModel.Data
        setData()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.bookticketPay){
            book_event_ticketAPICALL()
        }
        else if(binding.preBookingPlus==v){
            try {
                peopleCount++
                binding.preBookingPeopleValue.text = "$peopleCount"
                totAmt =peopleCount*Commons.strToDouble(pojoEvntDetl.sale_price)
                binding.bookticketPay.text = "Pay "+resources.getString(R.string.currency_sumbol)+""+totAmt
            } catch (e: Exception) {
            }
        }
        else if(binding.preBookingMinus==v){
            if(peopleCount>1) {
                try {
                    peopleCount--
                    binding.preBookingPeopleValue.text = "$peopleCount"
                    totAmt =peopleCount*Commons.strToDouble(pojoEvntDetl.sale_price)
                    binding.bookticketPay.text = "Pay $ $totAmt"
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun book_event_ticketAPICALL() {
        progressDialog.show(this@BookTicketActivity, "")
        var map = HashMap<String, String>()
        //payment_mode :REQUIRED (0=>Online, 1=>Debit Card 2=>Credit Card)
        map["vendor_id"] =pojoEvntDetl.user_id
        map["venue_id"] =pojoEvntDetl.id
        map["qty"] =""+peopleCount
        map["amount"] = pojoEvntDetl.sale_price
        map["payment_mode"] ="0"

        bookEventViewModel.bookEvent(map).observe(this@BookTicketActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { res ->
                        Log.d("ok", "book_event_ticketAPICALL: ")
                        startActivity(Intent (this@BookTicketActivity,TicketConfirmActvity::class.java)
                 .putExtra(AppConstant.INTENT_EXTRAS.EVENTDETAIL_POJO,pojoEvntDetl)
                 .putExtra(AppConstant.INTENT_EXTRAS.TOTAL_AMT,totAmt.toString())
                 .putExtra(AppConstant.INTENT_EXTRAS.TICKET_NO,res.data.ticket_number)
                 .putExtra(AppConstant.INTENT_EXTRAS.TICKET_URL,res.data.ticket_download))

                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                }
            }
        })
    }

    private fun setData() {
        try {


            binding.bookTicketPrice.text = "Price : $${pojoEvntDetl.sale_price}"
            binding.bookTicketURBN.text = pojoEvntDetl.store_name
            binding.bookTicketDate.text = pojoEvntDetl.event_date
            binding.bookTicketTime.text = "Start at :  ${pojoEvntDetl.event_start_time} To ${pojoEvntDetl.event_end_time}"
            binding.bookTicketSAddrs.text = PreferenceKeeper.instance.currentAddrs
            binding.bookTicketDAddrs.text = pojoEvntDetl.store_address
            binding.bookticketPay.text = "Pay "+resources.getString(R.string.currency_sumbol)+pojoEvntDetl.sale_price
            val latitude: Double = Commons.strToDouble(pojoEvntDetl.store_lattitude)
            val longitude: Double = Commons.strToDouble(pojoEvntDetl.store_longitude)
           binding.bookTicketKM.text =  "${MyApp.getDestance(latitude,longitude,PreferenceKeeper.instance.currentLat!!,PreferenceKeeper.instance.currentLong!!)} Km away"
            totAmt =peopleCount*Commons.strToDouble(pojoEvntDetl.sale_price)
            Glide.with(this@BookTicketActivity)
                .load(PreferenceKeeper.instance.imgPathSave + pojoEvntDetl.venue_gallery[0].image)
                .error(R.drawable.no_image)
                .into(binding.bookTicketImageMain)
        } catch (e: Exception) {
            Log.d("TAG", "setData: "+e)
        }

    }

    private fun inItView() {
        bookEventViewModel = CommonViewModel(this@BookTicketActivity)
        setTouchNClick(binding.bookticketPay)
        setTouchNClick(binding.preBookingPlus)
        setTouchNClick(binding.preBookingMinus)
    }

    private fun setToolBar() {
        setTouchNClick(binding.venulistingToolBar.toolbarBack)
         binding.venulistingToolBar.toolbarBack.setOnClickListener{
             finish()
         }
        binding.venulistingToolBar.toolbarTitle.setText("Book Ticket")
        binding.venulistingToolBar.toolbar3dot.visibility=GONE
        binding.venulistingToolBar.toolbarBell.visibility=GONE
    }




}