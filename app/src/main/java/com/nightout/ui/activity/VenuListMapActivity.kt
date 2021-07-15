package com.nightout.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.VenumapActivityBinding
import com.nightout.model.VenuModel

class VenuListMapActivity : BaseActivity() {
    lateinit var binding : VenumapActivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   //     setContentView(R.layout.venumap_activity)
        binding = DataBindingUtil.setContentView(this@VenuListMapActivity,R.layout.venumap_activity)
        setTopListTopDummy()
        setToolBar()
    }

    private fun setToolBar() {
        binding.venuMapToolBar.toolbar3dot.setOnClickListener{
            this.showPopMenu( )
        }
        binding.venuMapToolBar.toolbarBack.setOnClickListener{
            finish()
            overridePendingTransition(0,0)
        }
    }

    private fun showPopMenu() {
        val popup = PopupMenu(this@VenuListMapActivity, binding.venuMapToolBar.toolbar3dot)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_list_menu, popup.getMenu())
        popup.setOnMenuItemClickListener { item ->
            finish()
            overridePendingTransition(0,0)
            true
        }

        popup.show() //showing popup menu
    }

    private fun setTopListTopDummy() {
        var list = ArrayList<VenuModel>()
        list.add(VenuModel("Club", true))
        list.add(VenuModel("Bar", false))
        list.add(VenuModel("Pub", false))
        list.add(VenuModel("Food", false))
        list.add(VenuModel("Food2", false))

        venuAdapterAdapter = VenuAdapterAdapter(
            this@VenuListMapActivity,
            list,
            object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })

        binding.venumapToprecycler.also {
            it.layoutManager = LinearLayoutManager(this@VenuListMapActivity, LinearLayoutManager.HORIZONTAL, false)
           it.adapter = venuAdapterAdapter
        }
    }
}