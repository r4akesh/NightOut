package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.BookticketActviityBinding

class BookTicketActivity : BaseActivity()  {
    lateinit var binding : BookticketActviityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BookTicketActivity,R.layout.bookticket_actviity)
        setTouchNClick(binding.bookticketPay)
        setToolBar()
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