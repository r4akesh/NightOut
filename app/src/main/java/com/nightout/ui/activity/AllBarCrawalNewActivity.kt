package com.nightout.ui.activity

import android.os.Bundle
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.AllBarCrawaAdapter
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.AllbarcrawalNewactivityBinding
import com.nightout.model.VenuModel
import com.nightout.model.VenuesModel

class AllBarCrawalNewActivity : BaseActivity() {

    lateinit var binding : AllbarcrawalNewactivityBinding
    lateinit var venuAdapterAdapter: VenuAdapterAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AllBarCrawalNewActivity,R.layout.allbarcrawal_newactivity)
        setToolBar()
        setListTop()
        setListGrid()
    }

    lateinit var venuesAdapter : AllBarCrawaAdapter
    private fun setListGrid() {
        var list = ArrayList<VenuesModel>()
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar1,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar2,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar3,true))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar4,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar1,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar2,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar3,true))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.allbar4,false))

        venuesAdapter = AllBarCrawaAdapter (this@AllBarCrawalNewActivity,list,object: AllBarCrawaAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.allCrawlRecyleGrid.also {
            it.layoutManager = GridLayoutManager (this@AllBarCrawalNewActivity, 2)
            it.adapter = venuesAdapter
        }
    }

    private fun setListTop() {
     var listStoreType = ArrayList<VenuModel>()
     listStoreType.add(VenuModel(3,"Club", false,false))
     listStoreType.add(VenuModel(1,"Bar", false,false))
     listStoreType.add(VenuModel(2,"Pub", false,false))
     listStoreType.add(VenuModel(4,"Food", false,false))
     listStoreType.add(VenuModel(5,"Event", false,false))


      venuAdapterAdapter = VenuAdapterAdapter(this@AllBarCrawalNewActivity,listStoreType,  object : VenuAdapterAdapter.ClickListener {
             override fun onClick(pos: Int) {
                 for (i in 0 until listStoreType.size) {
                     listStoreType[i].isSelected = pos == i
                 }
                 venuAdapterAdapter.notifyDataSetChanged()
             }

         })

     binding.allCrawlRecyleTop.also {
         it.layoutManager = LinearLayoutManager(this@AllBarCrawalNewActivity, LinearLayoutManager.HORIZONTAL, false)
         it.adapter = venuAdapterAdapter
     }
 }

    private fun setToolBar() {
         binding.BarCrawlAllNewToolBar.toolbarTitle.setText("All Bar Crawls")
        binding.BarCrawlAllNewToolBar.toolbar3dot.visibility = GONE
        binding.BarCrawlAllNewToolBar.toolbarBell.visibility = GONE
        setTouchNClick( binding.BarCrawlAllNewToolBar.toolbarBack)
        binding.BarCrawlAllNewToolBar.toolbarBack.setOnClickListener { finish() }
    }
}