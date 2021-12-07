package com.nightout.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.DrinksMenuAdapter
import com.nightout.adapter.FoodsMenuAdapter
import com.nightout.adapter.PackageAdapter
import com.nightout.adapter.SnacksMenuAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.PrebookingActivityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import com.nightout.utils.Commons
import com.nightout.utils.CustomProgressDialog
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*
import kotlin.collections.ArrayList


//calendra link-https://github.com/Mulham-Raee/Horizontal-Calendar
class PreBookingActivity : BaseActivity() {
    var intialValuePeople: Int = 4
    lateinit var binding: PrebookingActivityBinding
    lateinit var horizontalCalendar: HorizontalCalendar
    lateinit var subAdapter: PackageAdapter
    private val progressDialog = CustomProgressDialog()
    var venuID = ""
    lateinit var userVenueDetailViewModel: CommonViewModel
    lateinit var venuePkgList: ArrayList<VenuDetailModel.PkgModel>
    lateinit var drinksList: ArrayList<VenuDetailModel.CategoryDrinksMdl>
    lateinit var foodsList: ArrayList<VenuDetailModel.CategoryFoodMdl>
    lateinit var snacksList: ArrayList<VenuDetailModel.SnacksModl>
    lateinit var barMenuAdapter: DrinksMenuAdapter
    lateinit var foodsMenuAdapter: FoodsMenuAdapter
    lateinit var snacksMenuAdapter: SnacksMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@PreBookingActivity, R.layout.prebooking_activity)
        initView()

        setCalendra()

        user_venue_detailAPICALL()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.toolbarBack) {
            finish()
        } else if (v == binding.preBookingPlus) {
            intialValuePeople = intialValuePeople + 1
            binding.preBookingPeopleValue.setText("" + intialValuePeople)
        } else if (v == binding.preBookingMinus) {

            if (intialValuePeople > 0) {
                intialValuePeople = intialValuePeople - 1
                binding.preBookingPeopleValue.setText("" + intialValuePeople)
            }
        } else if (v == binding.toolbarCelendra) {
            showDatePicker()
        } else if (v == binding.preBookingTimePicker) {
            showTimePicker()
        } else if (v == binding.preBookingSpclPkg) {
            binding.preBookingSpclPkg.setBackgroundResource(R.drawable.box_bgyello_left)
            binding.preBookingBarMenu.setBackgroundResource(0)
            binding.preBookingSpclPkg.setTextColor(resources.getColor(R.color.black))
            binding.preBookingBarMenu.setTextColor(resources.getColor(R.color.text_gray3))
            binding.preBookingLnrMenu.visibility = GONE
            setListPkg()
        } else if (v == binding.preBookingBarMenu) {
            binding.preBookingBarMenu.setBackgroundResource(R.drawable.box_bgyello_right)
            binding.preBookingSpclPkg.setBackgroundResource(0)
            binding.preBookingBarMenu.setTextColor(resources.getColor(R.color.black))
            binding.preBookingSpclPkg.setTextColor(resources.getColor(R.color.text_gray3))
            binding.preBookingLnrMenu.visibility = VISIBLE
            setListDrinks()
        } else if (v == binding.preBookingDrinksBtn) {
            binding.preBookingDrinksBtn.setTextColor(resources.getColor(R.color.text_yello))
            binding.preBookingFoodsBtn.setTextColor(resources.getColor(R.color.text_gray3))
            binding.preBookingSnacksBtn.setTextColor(resources.getColor(R.color.text_gray3))

            setListDrinks()
        } else if (v == binding.preBookingFoodsBtn) {
            binding.preBookingFoodsBtn.setTextColor(resources.getColor(R.color.text_yello))
            binding.preBookingDrinksBtn.setTextColor(resources.getColor(R.color.text_gray3))
            binding.preBookingSnacksBtn.setTextColor(resources.getColor(R.color.text_gray3))
            setListFoods()
        } else if (v == binding.preBookingSnacksBtn) {
            binding.preBookingSnacksBtn.setTextColor(resources.getColor(R.color.text_yello))
            binding.preBookingFoodsBtn.setTextColor(resources.getColor(R.color.text_gray3))
            binding.preBookingDrinksBtn.setTextColor(resources.getColor(R.color.text_gray3))
            setListSnacks()
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
                        venuePkgList = ArrayList()
                        venuePkgList = detailData.data.packageProducts.products
                        if (!venuePkgList.isNullOrEmpty()) {
                            setListPkg()
                        }
                        drinksList = ArrayList()
                        foodsList = ArrayList()
                        snacksList = ArrayList()
                        if (detailData.data?.drinkProducts?.categories?.size > 0) {
                            drinksList = detailData.data?.drinkProducts?.categories
                        }
                        if (detailData.data?.foodProducts?.categories?.size > 0) {
                            foodsList = detailData.data?.foodProducts?.categories
                        }
                        if (detailData.data?.snackProducts?.categories?.size > 0) {
                            snacksList = detailData.data?.snackProducts?.categories
                        }

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

    var totFoodPrice = 0.0
    private fun setListDrinks() {
        barMenuAdapter = DrinksMenuAdapter(
            this@PreBookingActivity,
            drinksList,
            object : DrinksMenuAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    drinksList[pos].isSelected = !drinksList[pos].isSelected
                    barMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {
                    drinksList[pos].products[subPos].isChekd =
                        !drinksList[pos].products[subPos].isChekd
                    barMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickPluse(pos: Int, subPos: Int) {

                    var qty = drinksList[pos].products[subPos].quantityLocal + 1
                    var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                    if (totFoodPrice>0)
                        totFoodPrice =  totFoodPrice + aa
                    else sdfsdfsd
                        totFoodPrice = aa
                    binding.preBookingFoodPriceValue.text = totFoodPrice.toString()
                    drinksList[pos].products[subPos].quantityLocal = qty
                    barMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    if (drinksList[pos].products[subPos].quantityLocal > 0) {
                        var qty = drinksList[pos].products[subPos].quantityLocal - 1
                       // totFoodPrice =  Commons.strToDouble(totFoodPrice) + (qty * Commons.strToDouble(drinksList[pos].products[subPos].price))
                      //  binding.preBookingFoodPriceValue.text = totFoodPrice
                        drinksList[pos].products[subPos].quantityLocal = qty
                        barMenuAdapter.notifyDataSetChanged()
                    }
                }

            })
        binding.preBookingSpclPkgRecyle.isNestedScrollingEnabled = true
        binding.preBookingSpclPkgRecyle.also {
            it.layoutManager =
                LinearLayoutManager(this@PreBookingActivity, LinearLayoutManager.VERTICAL, false)
            it.adapter = barMenuAdapter
        }
    }

    private fun setListFoods() {
        foodsMenuAdapter = FoodsMenuAdapter(
            this@PreBookingActivity,
            foodsList,
            object : FoodsMenuAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    foodsList[pos].isSelected = !foodsList[pos].isSelected
                    foodsMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {
                    foodsList[pos].products[subPos].isChekd =
                        !foodsList[pos].products[subPos].isChekd
                    foodsMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickPluse(pos: Int, subPos: Int) {
                    var qty = foodsList[pos].products[subPos].quantityLocal + 1
                    foodsList[pos].products[subPos].quantityLocal = qty
                    foodsMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    if (foodsList[pos].products[subPos].quantityLocal > 1) {
                        var qty = foodsList[pos].products[subPos].quantityLocal - 1
                        foodsList[pos].products[subPos].quantityLocal = qty
                        foodsMenuAdapter.notifyDataSetChanged()
                    }
                }

            })
        binding.preBookingSpclPkgRecyle.isNestedScrollingEnabled = true
        binding.preBookingSpclPkgRecyle.also {
            it.layoutManager =
                LinearLayoutManager(this@PreBookingActivity, LinearLayoutManager.VERTICAL, false)
            it.adapter = foodsMenuAdapter
        }
    }

    private fun setListSnacks() {
        snacksMenuAdapter = SnacksMenuAdapter(
            this@PreBookingActivity,
            snacksList,
            object : SnacksMenuAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    snacksList[pos].isSelected = !snacksList[pos].isSelected
                    snacksMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {
                    snacksList[pos].products[subPos].isChekd =
                        !snacksList[pos].products[subPos].isChekd
                    snacksMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickPluse(pos: Int, subPos: Int) {
                    var qty = snacksList[pos].products[subPos].quantityLocal + 1
                    snacksList[pos].products[subPos].quantityLocal = qty
                    snacksMenuAdapter.notifyDataSetChanged()
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    if (snacksList[pos].products[subPos].quantityLocal > 1) {
                        var qty = snacksList[pos].products[subPos].quantityLocal - 1
                        snacksList[pos].products[subPos].quantityLocal = qty
                        snacksMenuAdapter.notifyDataSetChanged()
                    }
                }

            })
        binding.preBookingSpclPkgRecyle.isNestedScrollingEnabled = true
        binding.preBookingSpclPkgRecyle.also {
            it.layoutManager =
                LinearLayoutManager(this@PreBookingActivity, LinearLayoutManager.VERTICAL, false)
            it.adapter = snacksMenuAdapter
        }
    }


    private fun setListPkg() {
        subAdapter = PackageAdapter(
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

    private fun initView() {
        venuID = intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_ID)!!
        setTouchNClick(binding.preBookingPlus)
        setTouchNClick(binding.preBookingMinus)
        setTouchNClick(binding.toolbarBack)
        setTouchNClick(binding.toolbarCelendra)
        setTouchNClick(binding.preBookingTimePicker)
        setTouchNClick(binding.preBookingBarMenu)
        setTouchNClick(binding.preBookingSpclPkg)
        setTouchNClick(binding.preBookingDrinksBtn)
        setTouchNClick(binding.preBookingFoodsBtn)
        setTouchNClick(binding.preBookingSnacksBtn)
        userVenueDetailViewModel = CommonViewModel(this)
    }

    private fun showTimePicker() {
        // Get Current Time

        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog


        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                var min = minute.toString()
                if (min.length == 1) {
                    min = "0$min"
                }
                binding.preBookingTimePicker.text = "$hourOfDay:$min"
            },
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


        val datePickerDialog = DatePickerDialog(
            this, { view, year, monthOfYear, dayOfMonth ->
                //binding.preBookingTimePicker.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                c[Calendar.YEAR] = year
                c[Calendar.MONTH] = monthOfYear
                c[Calendar.DAY_OF_MONTH] = dayOfMonth
                horizontalCalendar.selectDate(c, false)//set date to horizontal cal
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        val calendr = Calendar.getInstance()
        calendr.add(Calendar.MONTH, 6)
        datePickerDialog.datePicker.maxDate = calendr.timeInMillis
        datePickerDialog.show()
    }
}