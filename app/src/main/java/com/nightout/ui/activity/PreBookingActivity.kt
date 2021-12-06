package com.nightout.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.DrinksSubAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.PrebookingActivityBinding
import com.nightout.model.SubFoodModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*

import android.app.DatePickerDialog

import android.app.TimePickerDialog
import com.nightout.adapter.PackageAdapter
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlin.collections.ArrayList


//calendra link-https://github.com/Mulham-Raee/Horizontal-Calendar
class PreBookingActivity : BaseActivity() {
    var intialValuePeople: Int = 4
    lateinit var binding: PrebookingActivityBinding
    lateinit var horizontalCalendar : HorizontalCalendar
    lateinit var subAdapter: PackageAdapter
    private val progressDialog = CustomProgressDialog()
    var venuID = ""
    lateinit var userVenueDetailViewModel: CommonViewModel
    lateinit var venuePkgList: ArrayList<VenuDetailModel.PkgModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@PreBookingActivity, R.layout.prebooking_activity)
        initView()

        setCalendra()

        user_venue_detailAPICALL()
    }
    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.toolbarBack){
            finish()
        }
        else if (v == binding.preBookingPlus) {
            intialValuePeople = intialValuePeople + 1
            binding.preBookingPeopleValue.setText("" + intialValuePeople)
        }

        else if (v == binding.preBookingMinus) {

            if (intialValuePeople > 0) {
                intialValuePeople = intialValuePeople - 1
                binding.preBookingPeopleValue.setText("" + intialValuePeople)
            }
        }
        else if(v==binding.toolbarCelendra){
            showDatePicker()
        }
        else if(v==binding.preBookingTimePicker){
            showTimePicker()
        }
        else if(v==binding.preBookingSpclPkg){
            binding.preBookingSpclPkg.setBackgroundResource(R.drawable.box_bgyello_left)
            binding.preBookingBarMenu.setBackgroundResource(0)
            binding.preBookingSpclPkg.setTextColor(resources.getColor(R.color.black))
            binding.preBookingBarMenu.setTextColor(resources.getColor(R.color.text_gray3))
        }
        else if(v==binding.preBookingBarMenu){
            binding.preBookingBarMenu.setBackgroundResource(R.drawable.box_bgyello_right)
            binding.preBookingSpclPkg.setBackgroundResource(0)
            binding.preBookingBarMenu.setTextColor(resources.getColor(R.color.black))
            binding.preBookingSpclPkg.setTextColor(resources.getColor(R.color.text_gray3))
        }
    }


    private fun user_venue_detailAPICALL() {
        progressDialog.show(this@PreBookingActivity, "")
        var map = HashMap<String, String>()
       // map["id"] = venuID!!
        map["id"] = "217"

        userVenueDetailViewModel.userVenueDetail(map).observe(this@PreBookingActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        venuePkgList= ArrayList()
                          venuePkgList = detailData.data.packageProducts.products
                        setListPkg()
                       // setData()
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                }
            }
        })
    }

    private fun setCalendra() {
        // Default Date set to Today.
        var defaultSelectedDate = Calendar.getInstance();
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, 0)//from current

/* ends after 1 month from now */
/* ends after 1 month from now */
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 6)
          horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .configure()
              .formatTopText("EEE")
              .formatMiddleText("dd")
                //  jhkjhk

//             .formatTopText("MMM")
//          .formatMiddleText("dd")
//              .formatBottomText("EEE")
            .showTopText(true)
            .showBottomText(false)
            .textColor(Color.LTGRAY, resources.getColor(R.color.text_yello))
            .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
            .end()
            .defaultSelectedDate(defaultSelectedDate)

            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                //do something
            }
        }


    }


    private fun setListPkg() {
       /* var listDrinks = ArrayList<SubFoodModel>()
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )*/


        subAdapter =
            PackageAdapter(
                this@PreBookingActivity,
                venuePkgList,
                object : PackageAdapter.ClickListener {
                    override fun onClickChk(subPos: Int) {
                        venuePkgList[subPos].isChekd = !venuePkgList[subPos].isChekd

                        subAdapter.notifyDataSetChanged()
                    }

                })
        binding.preBookingSpclPkgRecyle.isNestedScrollingEnabled = true
        binding.preBookingSpclPkgRecyle.also {
            it.layoutManager =
                LinearLayoutManager(this@PreBookingActivity, LinearLayoutManager.VERTICAL, false)
            it.adapter = subAdapter
        }
    }

    private fun initView() {
        venuID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_ID)!!
        setTouchNClick(binding.preBookingPlus)
        setTouchNClick(binding.preBookingMinus)
        setTouchNClick(binding.toolbarBack)
        setTouchNClick(binding.toolbarCelendra)
        setTouchNClick(binding.preBookingTimePicker)
        setTouchNClick(binding.preBookingBarMenu)
        setTouchNClick(binding.preBookingSpclPkg)
        userVenueDetailViewModel = CommonViewModel(this)
    }



    private fun showTimePicker() {
        // Get Current Time

        val c = Calendar.getInstance()
       var  mHour = c[Calendar.HOUR_OF_DAY]
      var  mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog


        val timePickerDialog = TimePickerDialog(this,
            { view, hourOfDay, minute ->
                var min = minute.toString()
                if(min.length==1){
                    min = "0$min"
                }
                binding.preBookingTimePicker.text = "$hourOfDay:$min" },
            mHour,
            mMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        var mYear = c[Calendar.YEAR]
        var mMonth = c[Calendar.MONTH]
        var mDay = c[Calendar.DAY_OF_MONTH]


        val datePickerDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
            //binding.preBookingTimePicker.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
            c[Calendar.YEAR] = year
            c[Calendar.MONTH] = monthOfYear
            c[Calendar.DAY_OF_MONTH] = dayOfMonth
            horizontalCalendar.selectDate(c,false)//set date to horizontal cal
                                                      },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        val calendr = Calendar.getInstance()
        calendr.add(Calendar.MONTH, 6)
        datePickerDialog.datePicker.maxDate= calendr.timeInMillis
        datePickerDialog.show()
    }
}