package com.nightout.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityTaxiListBinding
import com.nightout.model.BarcrwalSavedRes
import com.nightout.model.SharedBarcrwalRes
import com.nightout.utils.AppConstant

class TaxiListActivity : BaseActivity() {
    private lateinit var binding: ActivityTaxiListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_taxi_list)
        setToolBar()
        initView()
        getIntentData()

    }

    private fun getIntentData() {
        try {
            if(intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.IsFROM_BarCrawlSavedMapActivity,false)) {
                var pathList =
                    intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.PATH_POJO) as ArrayList<BarcrwalSavedRes.Venue>
                var pathPos = intent.getIntExtra(AppConstant.INTENT_EXTRAS.PATH_POSITION, 0)
                binding.pickUpLocation.text = pathList[pathPos].store_address
                binding.dropLocation.text = pathList[pathPos + 1].store_address
            }
            else if(intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.IsFROM_BarCrawlShareMapActivity,false)) {
                var pathList = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.PATH_POJO) as ArrayList<SharedBarcrwalRes.Venue>
                var pathPos = intent.getIntExtra(AppConstant.INTENT_EXTRAS.PATH_POSITION, 0)
                binding.pickUpLocation.text = pathList[pathPos].store_address
                binding.dropLocation.text = pathList[pathPos + 1].store_address
            }
        } catch (e: Exception) {
            Log.d("TAG", "getIntentData: "+e)
        }
    }

    private fun initView() {
        binding.transportUberConstrent.setOnClickListener(this)
        binding.transportOlaConstrent.setOnClickListener(this)
        binding.transportLyftConstrent.setOnClickListener(this)
        binding.transportGettConstrent.setOnClickListener(this)
        binding.transportFreeNowConstrent.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.transportUberConstrent) {
            openTaxi("com.ubercab")
        } else if (v == binding.transportOlaConstrent) {
            openTaxi("com.olacabs.customer")
        } else if (v == binding.transportLyftConstrent) {
            openTaxi("me.lyft.android")
        } else if (v == binding.transportFreeNowConstrent) {
            openTaxi("taxi.android.client")
        } else if (v == binding.transportGettConstrent) {
            openTaxi("com.gettaxi.android")
        }
    }

    private fun openTaxi(pkgName:String) {
        val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(pkgName)
        if (launchIntent != null) {
            startActivity(launchIntent) //null pointer check in case package name was not found
        } else {
            val uri = Uri.parse("market://details?id="+pkgName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="+pkgName)
                    )
                )
            }
        }
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.select_taxi)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE

    }


}