package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.OrderdetailAvctivityBinding
import kotlinx.android.synthetic.main.orderdetail_avctivity.view.*
import kotlinx.android.synthetic.main.toolbar_common.view.*

class OrderDetailActivity : BaseActivity() {

    lateinit var binding: OrderdetailAvctivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OrderDetailActivity,R.layout.orderdetail_avctivity)
       setToolBar()

    }

    private fun setToolBar() {
        binding.constrentToolbar.toolbar_title.setText("Order Detail")
        binding.constrentToolbar.toolbar_3dot.visibility=GONE
        binding.constrentToolbar.toolbar_bell.visibility=GONE

        setTouchNClick(  binding.constrentToolbar.oredrDetailToolBar)
         binding.constrentToolbar.oredrDetailToolBar.setOnClickListener {
             finish()
             overridePendingTransition(0,0)
         }
    }


    override fun onClick(v: View?) {
        super.onClick(v)
    }
}