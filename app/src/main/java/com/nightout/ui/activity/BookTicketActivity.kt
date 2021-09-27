package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.BookticketActviityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import com.nightout.utils.PreferenceKeeper

class BookTicketActivity : BaseActivity()  {
    lateinit var binding : BookticketActviityBinding
    lateinit var pojoEvntDetl : VenuDetailModel.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BookTicketActivity,R.layout.bookticket_actviity)
        inItView()
        setToolBar()
          pojoEvntDetl= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.EVENTDETAIL_POJO) as VenuDetailModel.Data
        setData()
    }

    private fun setData() {
        try {
            Glide.with(this@BookTicketActivity)
                .load(PreferenceKeeper.instance.imgPathSave + pojoEvntDetl.venue_gallery[0])
                .error(R.drawable.no_image)
                .into(binding.bookTicketImageMain)

            binding.bookTicketPrice.text = "Price : $${pojoEvntDetl.price}"
            binding.bookTicketURBN.text = pojoEvntDetl.store_name
            binding.bookTicketDate.text = pojoEvntDetl.event_date
            binding.bookTicketTime.text = "Start at :  ${pojoEvntDetl.open_time} To ${pojoEvntDetl.close_time}"
            binding.bookTicketSAddrs.setText(PreferenceKeeper.instance.currentAddrs)
            fdfdf
        } catch (e: Exception) {
        }

    }

    private fun inItView() {
        setTouchNClick(binding.bookticketPay)
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

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.bookticketPay){
            startActivity(Intent (this@BookTicketActivity,TicketConfirmActvity::class.java))
        }
    }


}