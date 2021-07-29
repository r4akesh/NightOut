package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.VenuesAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ChossesvenuesActvityBinding
import com.nightout.model.VenuesModel

class ChooseVenuseActivity : BaseActivity () {
    lateinit var binding : ChossesvenuesActvityBinding
    lateinit var venuesAdapter: VenuesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ChooseVenuseActivity,R.layout.chossesvenues_actvity)
        setDummyList()
        setToolBar()
    }

    private fun setToolBar() {
        setTouchNClick(binding.chooseVenuesToolbar.toolbarBack)
        binding.chooseVenuesToolbar.toolbarBack.setOnClickListener { finish() }
        binding.chooseVenuesToolbar.toolbarTitle.setText("Choose Venues")
        binding.chooseVenuesToolbar.toolbar3dot.visibility= View.GONE
        binding.chooseVenuesToolbar.toolbarBell.visibility= View.GONE
    }

    private fun setDummyList() {

        var list = ArrayList<VenuesModel>()
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues1,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues2,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues3,true))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues4,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues1,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues2,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues3,true))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues4,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues1,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues2,false))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues3,true))
        list.add(VenuesModel("Vanity Night Club","2.3 miles away",R.drawable.venues4,false))
        venuesAdapter = VenuesAdapter (this@ChooseVenuseActivity,list,object:VenuesAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.chooseVenuesRecyle.also {
            it.layoutManager = LinearLayoutManager (this@ChooseVenuseActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = venuesAdapter
        }
    }
}