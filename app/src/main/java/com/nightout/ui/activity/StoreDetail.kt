package com.nightout.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nightout.R
import com.nightout.adapter.DrinksAdapter
import com.nightout.adapter.DrinksSubAdapter
import com.nightout.adapter.FacilityAdapter
import com.nightout.adapter.StorDetailFoodHorizontalAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.StoredetailActivityBinding
import com.nightout.model.FacilityModel
import com.nightout.model.StorDetailFoodModel
import com.nightout.model.StoreDetailDrinksModel
import com.nightout.model.SubFoodModel
import kotlinx.android.synthetic.main.discount_desc.view.*


class StoreDetail : BaseActivity() {
    lateinit var binding: StoredetailActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.storedetail_activity)
        binding = DataBindingUtil.setContentView(this@StoreDetail, R.layout.storedetail_activity)

        initView()
        setListHorizntalFood()
        setListDrinksDummy()//first time set
    }

    lateinit var storDetailFoodHorizontalAdapter: StorDetailFoodHorizontalAdapter
    private fun setListHorizntalFood() {
        var listFood = ArrayList<StorDetailFoodModel>()
        listFood.add(StorDetailFoodModel("Drinks", true))
        listFood.add(StorDetailFoodModel("Food", false))
        listFood.add(StorDetailFoodModel("Snacks", false))
        listFood.add(StorDetailFoodModel("Packages", false))

        storDetailFoodHorizontalAdapter = StorDetailFoodHorizontalAdapter(
            this@StoreDetail,
            listFood,
            object : StorDetailFoodHorizontalAdapter.ClickListener {
                override fun onClick(pos: Int) {

                    for (i in 0 until listFood.size) {
                        listFood[i].isSelected = pos == i
                    }
                    storDetailFoodHorizontalAdapter.notifyDataSetChanged()
                    if (pos == 0)
                        setListDrinksDummy()
                    else if (pos == 1)
                        setListFoodDummy()
                    else if (pos == 2)
                        setListSnakesDummy()
                    else if (pos == 3)
                        setListPackgesDummy()
                }

            })


        binding.storeDeatilHorizintalRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = storDetailFoodHorizontalAdapter
        }

    }

    lateinit var subAdapter: DrinksSubAdapter
    private fun setListPackgesDummy() {
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
                "Drinks Package 1",
                "1 Bottle of Prosecco & x 4 Glasses",
                0,
                "Free : 4 Tequilas Shots",
                false
            )
        )

        subAdapter =
            DrinksSubAdapter(this@StoreDetail, listDrinks, object : DrinksSubAdapter.ClickListener {
                override fun onClickChk(subPos: Int) {
                    listDrinks[subPos].isChekd = !listDrinks[subPos].isChekd

                    subAdapter.notifyDataSetChanged()
                }

            })
        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = subAdapter
        }
    }


    private fun setListFoodDummy() {
        var listDrinks = ArrayList<StoreDetailDrinksModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Quick Noodles",
                "1 Plate",
                R.drawable.chiness_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.chiness_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.chiness_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Italian", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Quick Noodles",
                "1 Plate",
                R.drawable.chiness_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.chiness_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.chiness_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Chinese", false, listSub))

        drinksAdapter =
            DrinksAdapter(this@StoreDetail, listDrinks, object : DrinksAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listDrinks[pos].isSelected = !listDrinks[pos].isSelected
                    drinksAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {

                    listDrinks[pos].list[subPos].isChekd = !listDrinks[pos].list[subPos].isChekd

                    drinksAdapter.notifyDataSetChanged()
                }


            })

        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = drinksAdapter
        }

    }

    private fun setListSnakesDummy() {
        var listDrinks = ArrayList<StoreDetailDrinksModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Sprouts", "1 Plate", R.drawable.snaks_img1, "Price : $10", false))
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.chiness_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.snaks_img1,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Non-Veg", false, listSub))


        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Quick Noodles",
                "1 Plate",
                R.drawable.snaks_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Dim Sums",
                "3 Plate",
                R.drawable.chiness_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Hot and Sour Soup",
                "2 Plate",
                R.drawable.chiness_img3,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Szechwan Chilli Chicken",
                "5 Plate",
                R.drawable.snaks_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Spring Rolls",
                "2 Plate",
                R.drawable.chiness_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Veg", false, listSub))



        drinksAdapter =
            DrinksAdapter(this@StoreDetail, listDrinks, object : DrinksAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listDrinks[pos].isSelected = !listDrinks[pos].isSelected
                    drinksAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {

                    listDrinks[pos].list[subPos].isChekd = !listDrinks[pos].list[subPos].isChekd

                    drinksAdapter.notifyDataSetChanged()
                }


            })

        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = drinksAdapter
        }
    }


    lateinit var drinksAdapter: DrinksAdapter
    private fun setListDrinksDummy() {
        var listDrinks = ArrayList<StoreDetailDrinksModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Grey Goose",
                "1 Glass",
                R.drawable.drink_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(SubFoodModel("Ciroc", "3 Glass", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(
            SubFoodModel(
                "Belvedere",
                "2 Glass",
                R.drawable.drink_img2,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Ketel One",
                "5 Glass",
                R.drawable.drink_img1,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Finlandia One",
                "2 Glass",
                R.drawable.drink_img2,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Scotch", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Grey Goose",
                "1 Glass",
                R.drawable.drink_img1,
                "Price : $10",
                false
            )
        )
        listSub.add(SubFoodModel("Ciroc", "3 Glass", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(
            SubFoodModel(
                "Belvedere",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Ketel One",
                "5 Glass",
                R.drawable.drink_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Finlandia One",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Vodka", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(
            SubFoodModel(
                "Grey Goose",
                "1 Glass",
                R.drawable.drink_img2,
                "Price : $10",
                false
            )
        )
        listSub.add(SubFoodModel("Ciroc", "3 Glass", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(
            SubFoodModel(
                "Belvedere",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $40",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Ketel One",
                "5 Glass",
                R.drawable.drink_img2,
                "Price : $20",
                false
            )
        )
        listSub.add(
            SubFoodModel(
                "Finlandia One",
                "2 Glass",
                R.drawable.drink_img1,
                "Price : $60",
                false
            )
        )
        listDrinks.add(StoreDetailDrinksModel("Rum", false, listSub))


        drinksAdapter =
            DrinksAdapter(this@StoreDetail, listDrinks, object : DrinksAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    listDrinks[pos].isSelected = !listDrinks[pos].isSelected
                    drinksAdapter.notifyDataSetChanged()
                }

                override fun onClickSub(pos: Int, subPos: Int) {

                    listDrinks[pos].list[subPos].isChekd = !listDrinks[pos].list[subPos].isChekd

                    drinksAdapter.notifyDataSetChanged()
                }


            })

        binding.storeDeatilDrinksRecycler.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = drinksAdapter
        }
    }

    private fun initView() {
        setTouchNClick(binding.storeDeatilMenu)
        setTouchNClick(binding.storeDeatilDiscount)
        setTouchNClick(binding.storeDeatilMore)
        setTouchNClick(binding.storeDeatilBakBtn)
        setTouchNClick(binding.storeDeatilFacilityBtn)
        setTouchNClick(binding.storeDeatilPlaceOrder)
        setTouchNClick(binding.storeDeatilPreBookingBtn)
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        if(v== binding.storeDeatilPreBookingBtn){
            startActivity(Intent(this@StoreDetail, PreBookingActivity::class.java))
            overridePendingTransition(0, 0)
        }
        else if (v == binding.storeDeatilPlaceOrder) {
            startActivity(Intent(this@StoreDetail, OrderDetailActivity::class.java))
            overridePendingTransition(0, 0)
        } else if (v == binding.storeDeatilFacilityBtn) {
            showPopUpFacilities()
        } else if (v == binding.storeDeatilBakBtn) {
            finish()
            overridePendingTransition(0, 0)
        } else if (v == binding.storeDeatilMenu) {
            binding.storeDeatilMenu.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.black))
            binding.storeDeatilDiscount.setBackgroundResource(0)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))


            binding.storeDeatilMenuDesc.visibility = VISIBLE
            binding.storeDeatilDisDesc.visibility = GONE
            binding.storeDeatilMoreDesc.visibility = GONE


        } else if (v == binding.storeDeatilDiscount) {
            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilDiscount.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.black))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))

            val str1 = resources.getString(R.string.discount10)
            var str2 = resources.getString(R.string.firsLine)
            var settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.firstLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

            str2 = resources.getString(R.string.secondLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.secondLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )


            str2 = resources.getString(R.string.thridLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.thrdLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )

            str2 = resources.getString(R.string.firsLine)
            settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.fourthLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

            str2 = resources.getString(R.string.secondLine)
            settext =
                "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.fifthLine.setText(
                Html.fromHtml(settext),
                TextView.BufferType.SPANNABLE
            )

            binding.storeDeatilMenuDesc.visibility = GONE
            binding.storeDeatilDisDesc.visibility = VISIBLE
            binding.storeDeatilMoreDesc.visibility = GONE
        } else if (v == binding.storeDeatilMore) {
            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilDiscount.setBackgroundResource(0)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.black))

            binding.storeDeatilMoreDesc.visibility = VISIBLE
            binding.storeDeatilDisDesc.visibility = GONE
            binding.storeDeatilMenuDesc.visibility = GONE
        }
    }

    private fun showPopUpFacilities() {

        val adDialog = Dialog(this@StoreDetail, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        adDialog.setContentView(R.layout.facility_dialog)
        adDialog.setCancelable(false)

        val listRecyclerview: RecyclerView = adDialog.findViewById(R.id.dialog_recycler)
        val dialog_close: ImageView = adDialog.findViewById(R.id.dialog_close)
        setTouchNClick(dialog_close)
        var list = setListFacility()
        var facilityAdapter =
            FacilityAdapter(this@StoreDetail, list, object : FacilityAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })

        listRecyclerview.also {
            it.layoutManager =
                LinearLayoutManager(this@StoreDetail, LinearLayoutManager.VERTICAL, false)
            it.adapter = facilityAdapter
        }

        dialog_close.setOnClickListener {
            adDialog.dismiss()
        }
        adDialog.show()

    }

    private fun setListFacility(): ArrayList<FacilityModel> {
        var listFacility = ArrayList<FacilityModel>()
        listFacility.add(FacilityModel("Smoking", true))
        listFacility.add(FacilityModel("Cloakroom", true))
        listFacility.add(FacilityModel("Dance", false))
        listFacility.add(FacilityModel("Sky Sports", true))
        listFacility.add(FacilityModel("BT Sport", false))
        listFacility.add(FacilityModel("Beer Garden", true))
        listFacility.add(FacilityModel("Rooftop Terrace", true))
        listFacility.add(FacilityModel("Snooker", false))
        listFacility.add(FacilityModel("Table Tennis", true))

        return listFacility;
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }
}