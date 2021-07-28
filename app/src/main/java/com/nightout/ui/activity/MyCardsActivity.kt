package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.MyCardAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityMyCardsBinding

class MyCardsActivity : BaseActivity() {
    lateinit var binding: ActivityMyCardsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cards)
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
        binding.savedCardList.layoutManager = LinearLayoutManager(this)
        val myCardAdapter = MyCardAdapter(this)
        binding.savedCardList.adapter = myCardAdapter
    }
}