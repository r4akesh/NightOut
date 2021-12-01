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
import devs.mulham.horizontalcalendar.model.CalendarEvent
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener


//calendra link-https://github.com/Mulham-Raee/Horizontal-Calendar
class PreBookingActivity : BaseActivity() {
    var intialValuePeople: Int = 4
    lateinit var binding: PrebookingActivityBinding
    lateinit var horizontalCalendar : HorizontalCalendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@PreBookingActivity, R.layout.prebooking_activity)
        initView()
        setListPkg()
        setCalendra()
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

    lateinit var subAdapter: DrinksSubAdapter
    private fun setListPkg() {
        var listDrinks = ArrayList<SubFoodModel>()
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )
        listDrinks.add(
            SubFoodModel(
                "Drinks Package 8",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )

        subAdapter =
            DrinksSubAdapter(
                this@PreBookingActivity,
                listDrinks,
                object : DrinksSubAdapter.ClickListener {
                    override fun onClickChk(subPos: Int) {
                        listDrinks[subPos].isChekd = !listDrinks[subPos].isChekd

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
        setTouchNClick(binding.preBookingPlus)
        setTouchNClick(binding.preBookingMinus)
        setTouchNClick(binding.toolbarBack)
        setTouchNClick(binding.toolbarCelendra)
        setTouchNClick(binding.preBookingTimePicker)
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