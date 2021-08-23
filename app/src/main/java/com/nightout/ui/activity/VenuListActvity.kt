package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.PromotionAdapter
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuSubAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenulistingActivityBinding
import com.nightout.model.*
import com.nightout.utils.AppConstant


class VenuListActvity : BaseActivity() {
    lateinit var binding: VenulistingActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@VenuListActvity, R.layout.venulisting_activity)
        setToolBar()
        var listVenu = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.VENU_LIST_POS) as Data
        setTopListTopDummy()
        setTopListSubDummy()
    }

    private fun setToolBar() {
        setTouchNClick(binding.venulistingToolBar.toolbarBack)
        setTouchNClick(binding.venulistingToolBar.toolbar3dot)

        binding.venulistingToolBar.toolbarBack.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        binding.venulistingToolBar.toolbar3dot.setOnClickListener {
            this.showPopMenu()
        }
    }

    private fun showPopMenu() {
        val popup = PopupMenu(this@VenuListActvity, binding.venulistingToolBar.toolbar3dot)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_map_menu, popup.getMenu())
        popup.setOnMenuItemClickListener { item ->
            startActivity(Intent(this@VenuListActvity, VenuListMapActivity::class.java))
            overridePendingTransition(0, 0)
            true
        }

        popup.show() //showing popup menu

    }

    lateinit var venuSubAdapter: VenuSubAdapter
    private fun setTopListSubDummy() {
        var list = ArrayList<VenuListModel>()
        list.add(VenuListModel("Vanity Night Club", "", R.drawable.venusub_img1))
        list.add(VenuListModel("Feel the Beat", "", R.drawable.venusub_img2))
        list.add(VenuListModel("Vanity Night Club", "", R.drawable.venusub_img3))

        venuSubAdapter = VenuSubAdapter(this@VenuListActvity, list, object : VenuSubAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    startActivity(Intent(this@VenuListActvity,StoreDetail::class.java))
                    overridePendingTransition(0,0)
                }

            })

        binding.venulistingRecyclersub.also {
            it.layoutManager = LinearLayoutManager(
                this@VenuListActvity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter = venuSubAdapter
        }
    }


    private fun setTopListTopDummy() {
        var list = ArrayList<VenuModel>()
        list.add(VenuModel("Club", true))
        list.add(VenuModel("Bar", false))
        list.add(VenuModel("Pub", false))
        list.add(VenuModel("Food", false))
        list.add(VenuModel("Event", false))
       // venuAdapterAdapter = PromotionAdapter(list)

       venuAdapterAdapter = VenuAdapterAdapter(
            this@VenuListActvity,
            list,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until list.size) {
                        list[i].isSelected = pos == i
                    }
                    venuAdapterAdapter.notifyDataSetChanged()
                }

            })
        binding.venulistingToprecycler.also {
            it.layoutManager = LinearLayoutManager(
                this@VenuListActvity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = venuAdapterAdapter
        }

    }
}