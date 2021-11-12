package com.nightout.ui.activity.barcrawl

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrwalmappathActvityBinding
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import com.nightout.ui.activity.SharedMemeberActvity
import java.util.*


class BarCrwalPathMap : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: BarcrwalmappathActvityBinding
    lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrwalPathMap, R.layout.barcrwalmappath_actvity)
        initView()
        setToolBar()
    }

    private fun setToolBar() {
        binding.BarCrawlMapPathToolBar.toolbarTitle.setText("Create Bar Crawl")
        setTouchNClick(binding.BarCrawlMapPathToolBar.toolbarBack)
        binding.BarCrawlMapPathToolBar.toolbarBack.setOnClickListener { finish() }
        binding.BarCrawlMapPathToolBar.toolbar3dot.visibility = View.GONE
        binding.BarCrawlMapPathToolBar.toolbarBell.visibility = View.GONE
        binding.BarCrawlMapPathToolBar.toolbarCreateGrop.visibility = View.GONE
    }

    private fun initView() {
        binding.barcrwalMapPathBtn.setOnClickListener(this@BarCrwalPathMap)
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.barCrawlPathMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@BarCrwalPathMap)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.barcrwalMapPathBtn) {
            showDialog()
        }
    }
    lateinit var dgDateBtn :TextView
    private fun showDialog() {
        val adDialog = Dialog(this@BarCrwalPathMap, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        adDialog.setContentView(R.layout.dialog_createbarcrwal)
        adDialog.setCancelable(false)

          dgDateBtn = adDialog.findViewById(R.id.dgDateBtn)
        val dgCloseBtn: ImageView = adDialog.findViewById(R.id.dgCloseBtn)
        val dgDoneBtn: Button = adDialog.findViewById(R.id.dgDoneBtn)

        setTouchNClick(dgCloseBtn)
        setTouchNClick(dgDateBtn)
        setTouchNClick(dgDoneBtn)
        dgDateBtn.setOnClickListener {
            showDatePicker()
        }
        dgDoneBtn.setOnClickListener{
            startActivity(Intent(this@BarCrwalPathMap, SharedMemeberActvity::class.java))
        }

        dgCloseBtn.setOnClickListener {
            adDialog.dismiss()
        }
        adDialog.show()
    }
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private  var mHour:Int = 0
    private  var mMinute:Int = 0
    private fun showDatePicker() {
        // Get Current Date
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this,
            { view, year, monthOfYear, dayOfMonth -> dgDateBtn.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
        datePickerDialog.show()
    }

    override fun onMapReady(p0: GoogleMap?) {
        gMap = p0!!
        p0!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
    }
}