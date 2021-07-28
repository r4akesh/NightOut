package com.nightout.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.ChipGroup
import com.nightout.R
import com.nightout.adapter.FillterMainAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.FillterActvityBinding
import com.nightout.model.FoodStoreModel
import com.nightout.model.SubFoodModel
import kotlinx.android.synthetic.main.fillter_actvity.*


class FillterActvity : BaseActivity() {
    lateinit var binding: FillterActvityBinding
    lateinit var fillterMainAdapter : FillterMainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@FillterActvity, R.layout.fillter_actvity)
        setDuumyList()
        binding.filterGroup.setOnCheckedChangeListener(ChipGroup.OnCheckedChangeListener { chipGroup, i ->
            Log.i("TAG", i.toString() + "")
        })
        setToolBar()
    }

    private fun setToolBar() {
         binding.filterActivityToolbar.toolbarBell.visibility = GONE
        binding.filterActivityToolbar.toolbarTitle.text = "Filters"
        binding.filterActivityToolbar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.filterActivityToolbar.toolbar3dot.visibility=GONE
        binding.filterActivityToolbar.toolbarBell.visibility=GONE
        binding.filterActivityToolbar.toolbarClearAll.visibility= VISIBLE
    }

    private fun setDuumyList() {
        var listMain= ArrayList<FoodStoreModel>()

        var listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Recently Added","",0,"",false))
        listSub.add(SubFoodModel("Cheap as Chips($)","",0,"",false))
        listSub.add(SubFoodModel("Happily Affordable($$)","",0,"",true))
        listSub.add(SubFoodModel("Splash the cash($$$)","",0,"",false))
        listSub.add(SubFoodModel("Go Wild($$$)","",0,"",false))
        listMain.add(FoodStoreModel("Price",true,listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("All","",0,"",false))
        listSub.add(SubFoodModel("Chilled","",0,"",false))
        listSub.add(SubFoodModel("Fun","",0,"",true))
        listSub.add(SubFoodModel("Exclusive","",0,"",false))
        listSub.add(SubFoodModel("Up Market","",0,"",false))
        listMain.add(FoodStoreModel("Party",true,listSub))

        listSub = ArrayList<SubFoodModel>()
        listSub.add(SubFoodModel("Polular","",0,"",false))
        listSub.add(SubFoodModel("Recently Added","",0,"",false))
        listSub.add(SubFoodModel("Closest To Me","",0,"",false))
        listSub.add(SubFoodModel("Price","",0,"",true))
        listMain.add(FoodStoreModel("Shot By",true,listSub))

        fillterMainAdapter = FillterMainAdapter(this@FillterActvity,listMain,object:FillterMainAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

            override fun onClickSub(pos: Int, subPos: Int) {

            }

        })

        binding.filterActivityRecyle.also {
            it.layoutManager = LinearLayoutManager(this@FillterActvity,LinearLayoutManager.VERTICAL,false)
            it.adapter = fillterMainAdapter
        }
    }
}