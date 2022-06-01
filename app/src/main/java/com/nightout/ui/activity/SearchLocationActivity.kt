package com.nightout.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.nightout.R
import com.nightout.adapter.SearchCityAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.SerchlocationActivityBinding
import com.nightout.model.SearchCityResponse
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import java.util.*

class SearchLocationActivity : BaseActivity() {
    lateinit var binding : SerchlocationActivityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var cityViewModel: CommonViewModel
    var LAUNCH_GOOGLE_ADDRESS = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SearchLocationActivity,R.layout.serchlocation_activity)
        cityViewModel = CommonViewModel(THIS!!)
        binding.searchLocationEditText.setOnClickListener (this)
        setToolBar()
        city_listAPICAll()
    }

    private fun city_listAPICAll() {
        customProgressDialog.show(this@SearchLocationActivity, "")
        cityViewModel.searchCity().observe(this@SearchLocationActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    it.data?.let { users ->
                        try {
                            setList(it.data.data)

                        } catch (e: Exception) {
                            MyApp.popErrorMsg("",resources.getString(R.string.No_data_found),THIS!!)
                        }
                    }
                }
                Status.LOADING -> {
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    customProgressDialog.dialog.dismiss()
                    try {
                        Utills.showErrorToast(
                            this@SearchLocationActivity,
                            it.message!!,

                            )
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    lateinit var searchCityAdapter: SearchCityAdapter
    private fun setList(dataList: MutableList<SearchCityResponse.Data>) {
        searchCityAdapter=SearchCityAdapter(this@SearchLocationActivity,dataList,object:SearchCityAdapter.ClickListener{
            override fun onClick(pos: Int) {
                var intentt= Intent()
                intentt.putExtra(AppConstant.INTENT_EXTRAS.isFromSelectPredefineCity,true)
                intentt.putExtra(AppConstant.INTENT_EXTRAS.ADDRS,dataList[pos].title)//city
                intentt.putExtra(AppConstant.INTENT_EXTRAS.LATITUDE,dataList[pos].city_lattitude)
                intentt.putExtra(AppConstant.INTENT_EXTRAS.LONGITUDE,dataList[pos].city_longitude)
                setResult(Activity.RESULT_OK,intentt)
                finish()
            }

        })

        binding.recycleCity.also {
            it.layoutManager = LinearLayoutManager(THIS!!,LinearLayoutManager.VERTICAL,false)
            it.adapter = searchCityAdapter
        }


    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.toolbarBack){
            finish()
            overridePendingTransition(0,0)

        }
        else if(v==binding.searchLocationEditText){
                        Places.initialize(this@SearchLocationActivity, resources.getString(R.string.google_place_picker_key))
            val fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(this@SearchLocationActivity)
            startActivityForResult(intent, LAUNCH_GOOGLE_ADDRESS)
            overridePendingTransition(0,0)
        }
    }
    private fun setToolBar() {
         setTouchNClick(binding.toolbarBack)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentt: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentt)
        try {
            val place = Autocomplete.getPlaceFromIntent(intentt!!)
            Log.d("location", "location: " + place.address)
         //   binding.barcralCity.text = place.address

          //  val latLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)


            var intentt= Intent()
            intentt.putExtra(AppConstant.INTENT_EXTRAS.isFromSelectPredefineCity,false)//city
            intentt.putExtra(AppConstant.INTENT_EXTRAS.ADDRS,place.address)
            intentt.putExtra(AppConstant.INTENT_EXTRAS.LATITUDE,place.latLng?.latitude.toString())
            intentt.putExtra(AppConstant.INTENT_EXTRAS.LONGITUDE,place.latLng?.longitude.toString())
            setResult(Activity.RESULT_OK,intentt)
            finish()
        } catch (e: Exception) {
        }
    }
}