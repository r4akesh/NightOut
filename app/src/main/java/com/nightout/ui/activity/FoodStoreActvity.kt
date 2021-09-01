package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.AllRecordAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.FoodstoreActviityBinding
import com.nightout.model.FoodStoreModel
import com.nightout.model.SubFoodModel
import kotlinx.android.synthetic.main.foodstore_actviity.*
import kotlinx.android.synthetic.main.storedetail_activity.*

class FoodStoreActvity : BaseActivity() {
    lateinit var binding : FoodstoreActviityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.foodstore_actviity)
        binding = DataBindingUtil.setContentView(this@FoodStoreActvity,R.layout.foodstore_actviity)
        iniiView()
        setDuumyList()
    }

    lateinit var foodAdapter: AllRecordAdapter
    private fun setDuumyList() {
        var listFood = ArrayList<FoodStoreModel>()
        var listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Quick Noodles", "", R.drawable.drink_img1, "Price : $10", false))
        listSub.add(SubFoodModel("Dim Sums", "", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(SubFoodModel("Quick Noodles", "", R.drawable.drink_img1, "Price : $10", false))
        listSub.add(SubFoodModel("Dim Sums", "", R.drawable.drink_img1, "Price : $20", false))
        listFood.add(FoodStoreModel("Fast Food", false, listSub))

          listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Zuppe e salse", "", R.drawable.drink_img1, "Price : $10", false))
        listSub.add(SubFoodModel("Pane (bread)", "", R.drawable.drink_img1, "Price : $20", false))
        listSub.add(SubFoodModel("Common Pizzas", "", R.drawable.drink_img1, "Price : $30", false))
        listSub.add(SubFoodModel("Pasta varieties", "", R.drawable.drink_img1, "Price : $20", false))
        listFood.add(FoodStoreModel("Italian Food", false, listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Zuppe e salse", "", R.drawable.drink_img1, "Price : $10", false))
        listSub.add(SubFoodModel("Pane (bread)", "", R.drawable.drink_img1, "Price : $40", false))
        listSub.add(SubFoodModel("Common Pizzas", "", R.drawable.drink_img1, "Price : $10", false))
        listSub.add(SubFoodModel("Pasta varieties", "", R.drawable.drink_img1, "Price : $50", false))
        listFood.add(FoodStoreModel("Chinese Food", false, listSub))

       /* foodAdapter = AllRecordAdapter(this@FoodStoreActvity,listFood, object : AllRecordAdapter.ClickListener{
            override fun onClick(pos: Int) {
                listFood[pos].isSelected = !listFood[pos].isSelected
                foodAdapter.notifyDataSetChanged()
            }

            override fun onClickSub(pos: Int, subPos: Int) {
                listFood[pos].list[subPos].isChekd = !listFood[pos].list[subPos].isChekd
                foodAdapter.notifyDataSetChanged()
            }

        })
        binding.foodstoreRecyle.also {
            it.layoutManager =
                LinearLayoutManager(this@FoodStoreActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = foodAdapter
        }*/

    }

    private fun iniiView() {
        setTouchNClick(binding.foodstoreMenu)
        setTouchNClick(binding.foodstoreDiscount)
        setTouchNClick(binding.foodstoreBakBtn)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.foodstoreMenu) {
            binding.foodstoreMenu.setBackgroundResource(R.drawable.box_yelo)
            binding.foodstoreMenu.setTextColor(resources.getColor(R.color.black))
            binding.foodstoreDiscount.setBackgroundResource(0)
            binding.foodstoreDiscount.setTextColor(resources.getColor(R.color.white_second))

        }
        else if(v==binding.foodstoreDiscount){
            binding.foodstoreMenu.setBackgroundResource(0)
            binding.foodstoreMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.foodstoreDiscount.setBackgroundResource(R.drawable.box_yelo)
            binding.foodstoreDiscount.setTextColor(resources.getColor(R.color.black))
        }
        else if(v==binding.foodstoreBakBtn){
            finish()
        }
    }
}