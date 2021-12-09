package com.nightout.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.prebooking_activity.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


//calendra link-https://github.com/Mulham-Raee/Horizontal-Calendar
class PreBookingActivity : BaseActivity() {
    var intialValuePeople: Int = 4
    lateinit var binding: PrebookingActivityBinding
    lateinit var horizontalCalendar: HorizontalCalendar
    lateinit var pakgAdapter: PackageAdapter
    private val progressDialog = CustomProgressDialog()
    var venuID = ""
      var vendorId = ""
    lateinit var userVenueDetailViewModel: CommonViewModel
    lateinit var venuePkgList: ArrayList<VenuDetailModel.PkgModel>
    lateinit var drinksList: ArrayList<VenuDetailModel.CategoryDrinksMdl>
    lateinit var foodsList: ArrayList<VenuDetailModel.CategoryFoodMdl>
    lateinit var snacksList: ArrayList<VenuDetailModel.SnacksModl>
    lateinit var barMenuAdapter: DrinksMenuAdapter
    lateinit var foodsMenuAdapter: FoodsMenuAdapter
    lateinit var snacksMenuAdapter: SnacksMenuAdapter
    var selectedDateFinal=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@PreBookingActivity, R.layout.prebooking_activity)
        initView()
        binding.toolbarSubTitle.text= intent.getStringExtra(AppConstant.INTENT_EXTRAS.VENU_NAME)
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
        else if(v==binding.preBookingBokNow){
            if(isValidateInput())
            pre_bookingAPICall()
        }
        else if(v==binding.preBookingbookWholeVenus){
            if(binding.preBookingbookWholeVenus.isChecked){
                binding.preBookingPeopleValue.setFocusable(false)
                binding.preBookingPeopleValue.setFocusableInTouchMode(false)
                binding.preBookingPeopleValue.setClickable(false)

                binding.preBookingbookWholeVenus.isChecked=false
                binding.preBookingbookWholeVenus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.unchk_box,0,0,0)
            }else{

                binding.preBookingPeopleValue.setFocusable(true)
                binding.preBookingPeopleValue.setFocusableInTouchMode(true)
                binding.preBookingPeopleValue.setClickable(true)

                binding.preBookingbookWholeVenus.isChecked=true
                binding.preBookingbookWholeVenus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.chk_box,0,0,0)
            }
        }
    }

    private fun isValidateInput(): Boolean {
        if(binding.preBookingTimePicker.text.toString().isNullOrBlank()){
            MyApp.popErrorMsg("","Please choose booking time",THIS!!)
            return false
        }

        return true

    }

    private fun pre_bookingAPICall() {
        try {
            var jarr = JSONArray()
            val jsnObjMain = JSONObject()
            var flag =false

            for (i in 0 until venuePkgList.size){
                if(venuePkgList[i].quantityLocal>0){
                    val jsonObjects = JSONObject()
                    jsonObjects.put("id", venuePkgList[i].id)
                    jsonObjects.put("qty", "" + venuePkgList[i].quantityLocal)
                    jarr.put(jsonObjects)
                    flag=true
                }
            }

            for (i in 0 until drinksList.size) {
                for (j in 0 until drinksList[i].products.size) {
                    if(drinksList[i].products[j].quantityLocal>0) {
                        val jsonObjects = JSONObject()
                        jsonObjects.put("id", drinksList[i].products[j].id)
                        jsonObjects.put("qty", "" + drinksList[i].products[j].quantityLocal)
                        jarr.put(jsonObjects)
                        flag=true
                    }
                }
            }
            for (i in 0 until foodsList.size) {
                for (j in 0 until foodsList[i].products.size) {
                    if(foodsList[i].products[j].quantityLocal>0) {
                        val jsonObjects = JSONObject()
                        jsonObjects.put("id", foodsList[i].products[j].id)
                        jsonObjects.put("qty", "" + foodsList[i].products[j].quantityLocal)
                        jarr.put(jsonObjects)
                        flag=true
                    }
                }
            }
            for (i in 0 until snacksList.size) {
                for (j in 0 until snacksList[i].products.size) {
                    if(snacksList[i].products[j].quantityLocal>0) {
                        val jsonObjects = JSONObject()
                        jsonObjects.put("id", snacksList[i].products[j].id)
                        jsonObjects.put("qty", "" + snacksList[i].products[j].quantityLocal)
                        jarr.put(jsonObjects)
                        flag=true
                    }
                }
            }
            if(flag==false){
                MyApp.popErrorMsg("","Please select any special package OR Bar Menu",THIS!!)
                return
            }
            // jsnObjMain.put("venue_id",venuID)
           jsnObjMain.put("venue_id","217")
            jsnObjMain.put("vendor_id",vendorId)
            jsnObjMain.put("date",selectedDateFinal)
            jsnObjMain.put("time",binding.preBookingTimePicker.text.toString())
            jsnObjMain.put("people",binding.preBookingPeopleValue.text.toString())
            var isWholeValue = if(binding.preBookingbookWholeVenus.isChecked) "1" else "0"
            jsnObjMain.put("whole_venue",isWholeValue)
            jsnObjMain.put("description",binding.preBookingSpclReqEdit.text.toString())
            var amt=binding.preBookingTotAmtValue.text.toString()
            jsnObjMain.put("amount",amt.substring(1,amt.length))
            jsnObjMain.put("pro_id_qty",jarr)
            Log.d("TAG", "pre_bookingAPICall: "+jsnObjMain)

            progressDialog.show(this@PreBookingActivity, "")

            userVenueDetailViewModel.preBook(jsnObjMain).observe(this@PreBookingActivity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        it.data?.let { detailData ->
                        startActivity(Intent(THIS!!,CongrtulationActvity::class.java)
                            .putExtra(AppConstant.INTENT_EXTRAS.CONGRETS_MSG,"Pre Booking is successfully completed"))
                        finish()
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                    }
                }
            })
        } catch (e: Exception) {
            MyApp.popErrorMsg("API call erroe",""+e,THIS!!)
        }

    }


    private fun user_venue_detailAPICALL() {
        progressDialog.show(this@PreBookingActivity, "")
        var map = HashMap<String, String>()
      //  map["id"] = venuID!!
        map["id"] = "217"
        userVenueDetailViewModel.userVenueDetail(map).observe(this@PreBookingActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                          vendorId=  detailData.data.vendor_detail.id
                        venuePkgList = ArrayList()
                        drinksList = ArrayList()
                        foodsList = ArrayList()
                        snacksList = ArrayList()
                        venuePkgList = detailData.data.packageProducts.products
                        if (venuePkgList?.size>0) {
                            setListPkg()
                        }else{
                            Utills.showErrorToast(THIS!!,"Packages are not available now")
                            finish()
                        }
                         if (detailData.data?.drinkProducts?.categories?.size > 0) {
                            drinksList = detailData.data?.drinkProducts?.categories
                        }
                       else if (detailData.data?.foodProducts?.categories?.size > 0) {
                            foodsList = detailData.data?.foodProducts?.categories
                        }
                       else if (detailData.data?.snackProducts?.categories?.size > 0) {
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

    //var totFoodPrice = 0.0
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
                    try {
                        var qty = drinksList[pos].products[subPos].quantityLocal + 1
                        var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                        var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                        var per = bb/100
                        var disValue= aa-per
                        drinksList[pos].products[subPos].quantityLocal = qty
                        drinksList[pos].products[subPos].totPriceLocal = disValue
                        barMenuAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for(i in 0 until drinksList[pos].products.size){
                            totCost= totCost+drinksList[pos].products[i].totPriceLocal
                        }
                        binding.preBookingDrinksValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                        doGrandTot()
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                    }
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    try {
                        if (drinksList[pos].products[subPos].quantityLocal > 0) {
                            var qty = drinksList[pos].products[subPos].quantityLocal - 1
                            var aa = qty * Commons.strToDouble(drinksList[pos].products[subPos].price)
                            var bb = aa*Commons.strToDouble(drinksList[pos].products[subPos].discount)
                            var per = bb/100
                            var disValue= aa-per
                            drinksList[pos].products[subPos].quantityLocal = qty
                            drinksList[pos].products[subPos].totPriceLocal = disValue
                            barMenuAdapter.notifyDataSetChanged()
                            var totCost = 0.0
                            for(i in 0 until drinksList[pos].products.size){
                                totCost= totCost+drinksList[pos].products[i].totPriceLocal
                            }
                            binding.preBookingDrinksValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                            doGrandTot()
                        }
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
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
                    try {
                        var qty = foodsList[pos].products[subPos].quantityLocal + 1
                        var aa = qty * Commons.strToDouble(foodsList[pos].products[subPos].price)
                        var bb = aa*Commons.strToDouble(foodsList[pos].products[subPos].discount)
                        var per = bb/100
                        var disValue= aa-per
                        foodsList[pos].products[subPos].quantityLocal = qty
                        foodsList[pos].products[subPos].totPriceLocal = disValue
                        foodsMenuAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for(i in 0 until foodsList[pos].products.size){
                            totCost= totCost+foodsList[pos].products[i].totPriceLocal
                        }
                        binding.preBookingFoodPriceValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                        doGrandTot()
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                    }
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    try {
                        if (foodsList[pos].products[subPos].quantityLocal > 0) {
                            var qty = foodsList[pos].products[subPos].quantityLocal - 1
                            var aa = qty * Commons.strToDouble(foodsList[pos].products[subPos].price)
                            var bb = aa*Commons.strToDouble(foodsList[pos].products[subPos].discount)
                            var per = bb/100
                            var disValue= aa-per
                            foodsList[pos].products[subPos].quantityLocal = qty
                            foodsList[pos].products[subPos].totPriceLocal = disValue
                            foodsMenuAdapter.notifyDataSetChanged()
                            var totCost = 0.0
                            for(i in 0 until foodsList[pos].products.size){
                                totCost= totCost+foodsList[pos].products[i].totPriceLocal
                            }
                            binding.preBookingFoodPriceValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                            doGrandTot()
                        }
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
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
                    try {
                        var qty = snacksList[pos].products[subPos].quantityLocal + 1
                        var aa = qty * Commons.strToDouble(snacksList[pos].products[subPos].price)
                        var bb = aa*Commons.strToDouble(snacksList[pos].products[subPos].discount)
                        var per = bb/100
                        var disValue= aa-per
                        snacksList[pos].products[subPos].quantityLocal = qty
                        snacksList[pos].products[subPos].totPriceLocal = disValue
                        snacksMenuAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for(i in 0 until snacksList[pos].products.size){
                            totCost= totCost+snacksList[pos].products[i].totPriceLocal
                        }
                        binding.preBookingSnakesValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                        doGrandTot()
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                    }
                }

                override fun onClickMinus(pos: Int, subPos: Int) {
                    try {
                        if (snacksList[pos].products[subPos].quantityLocal > 0) {
                            var qty = snacksList[pos].products[subPos].quantityLocal - 1
                            var aa = qty * Commons.strToDouble(snacksList[pos].products[subPos].price)
                            var bb = aa*Commons.strToDouble(snacksList[pos].products[subPos].discount)
                            var per = bb/100
                            var disValue= aa-per
                            snacksList[pos].products[subPos].quantityLocal = qty
                            snacksList[pos].products[subPos].totPriceLocal = disValue
                            snacksMenuAdapter.notifyDataSetChanged()
                            var totCost = 0.0
                            for(i in 0 until snacksList[pos].products.size){
                                totCost= totCost+snacksList[pos].products[i].totPriceLocal
                            }
                            binding.preBookingSnakesValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                            doGrandTot()
                        }
                    } catch (e: Exception) {
                        MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
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
        pakgAdapter = PackageAdapter(this@PreBookingActivity, venuePkgList, object : PackageAdapter.ClickListener {
                override fun onClickChk(subPos: Int) {
                    venuePkgList[subPos].isChekd = !venuePkgList[subPos].isChekd
                    pakgAdapter.notifyDataSetChanged()
                }

            override fun onClickPlus(pos: Int) {
                try {
                    var qty = venuePkgList[pos].quantityLocal + 1
                    var aa = qty * Commons.strToDouble(venuePkgList[pos].price)
                    var bb = aa*Commons.strToDouble(venuePkgList[pos].discount)
                    var per = bb/100
                    var disValue= aa-per
                    venuePkgList[pos].quantityLocal = qty
                    venuePkgList[pos].totPriceLocal = disValue
                    pakgAdapter.notifyDataSetChanged()
                    var totCost = 0.0
                    for(i in 0 until venuePkgList.size){
                        totCost= totCost+venuePkgList[i].totPriceLocal
                    }
                    binding.preBookingTablePriceValue.text = resources.getString(R.string.currency_sumbol)+totCost.toString()
                    //GrandTotal
                    doGrandTot()
                } catch (e: Exception) {
                    MyApp.popErrorMsg("Error in increase the quantity",""+e,THIS!!)
                }

            }

            override fun onClickMinus(pos: Int) {
                try {
                    if (venuePkgList[pos].quantityLocal > 0) {
                        var qty = venuePkgList[pos].quantityLocal - 1
                        var aa = qty * Commons.strToDouble(venuePkgList[pos].price)
                        var bb = aa * Commons.strToDouble(venuePkgList[pos].discount)
                        var per = bb / 100
                        var disValue = aa - per
                        venuePkgList[pos].quantityLocal = qty
                        venuePkgList[pos].totPriceLocal = disValue
                        pakgAdapter.notifyDataSetChanged()
                        var totCost = 0.0
                        for (i in 0 until venuePkgList.size) {
                            totCost = totCost + venuePkgList[i].totPriceLocal
                        }
                        binding.preBookingTablePriceValue.text =
                            resources.getString(R.string.currency_sumbol) + totCost.toString()
                        doGrandTot()
                    }
                } catch (e: Exception) {
                    MyApp.popErrorMsg("Error in decrease the quantity",""+e,THIS!!)
                }
            }
        })
        binding.preBookingSpclPkgRecyle.isNestedScrollingEnabled = true
        binding.preBookingSpclPkgRecyle.also {
            it.layoutManager =
                LinearLayoutManager(this@PreBookingActivity, LinearLayoutManager.VERTICAL, false)
            it.adapter = pakgAdapter
        }
    }

    private fun doGrandTot() {
        try {
            var pkgP=binding.preBookingTablePriceValue.text.toString()
            var foodP=binding.preBookingFoodPriceValue.text.toString()
            var snksP=binding.preBookingSnakesValue.text.toString()
            var drinksP=binding.preBookingDrinksValue.text.toString()
            var grndTot=Commons.strToDouble(pkgP.substring(1,pkgP.length-1))+Commons.strToDouble(foodP.substring(1,foodP.length-1))+
                    Commons.strToDouble(snksP.substring(1,snksP.length-1))+Commons.strToDouble(drinksP.substring(1,drinksP.length-1))
            binding.preBookingTotAmtValue.text=resources.getString(R.string.currency_sumbol)+grndTot
        } catch (e: Exception) {
            MyApp.popErrorMsg("Error to fetch grand total",""+e,THIS!!)
        }
    }

    private fun setCalendra() {
        // Default Date set to Today.
        var defaultSelectedDate = Calendar.getInstance();
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, 0)//from current
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 6)
        horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .configure()
            .formatTopText("EEE")
            .formatMiddleText("dd")
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

        val yy =defaultSelectedDate?.get(Calendar.YEAR)
        val mm =defaultSelectedDate?.get(Calendar.MONTH)
        val dd =defaultSelectedDate?.get(Calendar.DATE)

        selectedDateFinal= ""+Commons.strToTimemills("$dd-$mm-$yy")
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                Log.d("DATE", "onDateSelected: "+date)
                val yy =date?.get(Calendar.YEAR)
                val mm =date?.get(Calendar.MONTH)
                val dd =date?.get(Calendar.DATE)
                  selectedDateFinal=""+Commons.strToTimemills("$dd-$mm-$yy")
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
        setTouchNClick(binding.preBookingBokNow)
        setTouchNClick(binding.preBookingbookWholeVenus)
        binding.preBookingbookWholeVenus.isChecked = false
        userVenueDetailViewModel = CommonViewModel(this)

        binding.preBookingPeopleValue.setFocusable(false)
        binding.preBookingPeopleValue.setFocusableInTouchMode(false)
        binding.preBookingPeopleValue.setClickable(false)

    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]
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
            }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        val calendr = Calendar.getInstance()
        calendr.add(Calendar.MONTH, 6)
        datePickerDialog.datePicker.maxDate = calendr.timeInMillis
        datePickerDialog.show()
    }
}