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

//calendra link-https://github.com/Mulham-Raee/Horizontal-Calendar
class PreBookingActivity : BaseActivity() {
    var intialValuePeople: Int = 4
    lateinit var binding: PrebookingActivityBinding
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
        startDate.add(Calendar.MONTH, -1)

/* ends after 1 month from now */
/* ends after 1 month from now */
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 2)
        var horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .configure()
            //.formatTopText("MMM")
            .formatTopText("EEE")
            .formatMiddleText("dd")
            // .formatBottomText("EEE")
            .showTopText(true)
            .showBottomText(false)
            .textColor(Color.LTGRAY, Color.WHITE)
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
    }
}