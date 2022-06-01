package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.AllbarActvityBinding

class AllBarCrawalActivity : BaseActivity() {
    lateinit var binding : AllbarActvityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AllBarCrawalActivity,R.layout.allbar_actvity)
        setToolBar()
        setDummyList() //now plz call API
    }

    private fun setDummyList() {

       /* var list = ArrayList<VenuesModel>()
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
        venuesAdapter = VenuesAdapter (this@AllBarCrawalActivity,list,object: VenuesAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.allBarRecycle.also {
            it.layoutManager = LinearLayoutManager (this@AllBarCrawalActivity,
                LinearLayoutManager.VERTICAL,false)
            it.adapter = venuesAdapter
        }*/
    }

    private fun setToolBar() {
        binding.allBarToolBar.toolbarTitle.text = "All Bar Crawls"
        binding.allBarToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.allBarToolBar.toolbar3dot.visibility= View.GONE
        binding.allBarToolBar.toolbarBell.visibility= View.GONE
    }
}