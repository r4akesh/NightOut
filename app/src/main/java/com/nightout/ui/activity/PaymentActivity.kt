package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.PaymentMethodAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityPaymentBinding

class PaymentActivity : BaseActivity() {
    lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        setToolBar()
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.my_cards)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE

        setUpView()
    }

    private fun setUpView() {
        binding.paymentMethodList.layoutManager = LinearLayoutManager(this)
        val paymentMethodAdapter = PaymentMethodAdapter(this)
        binding.paymentMethodList.adapter = paymentMethodAdapter
    }
}