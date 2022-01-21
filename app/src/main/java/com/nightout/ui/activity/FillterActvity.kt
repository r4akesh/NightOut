package com.nightout.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.FillterMainAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.FillterActvityBinding
import com.nightout.model.FillterRes
import com.nightout.model.FoodStoreModel
import com.nightout.model.SubFoodModel
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.fillter_actvity.*
import java.lang.StringBuilder


class FillterActvity : BaseActivity() {
    lateinit var binding: FillterActvityBinding
    lateinit var fillterMainAdapter : FillterMainAdapter
    private var customProgressDialog = CustomProgressDialog()
    lateinit var filterViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@FillterActvity, R.layout.fillter_actvity)
        setTouchNClick(binding.filterActivityFilter)
        setTouchNClick( binding.filterActivityToolbar.toolbarClearAll)
//        binding.filterGroup.setOnCheckedChangeListener(ChipGroup.OnCheckedChangeListener { chipGroup, i ->
//            Log.i("TAG", i.toString() + "")
//        })
        setToolBar()
        filterViewModel =  CommonViewModel(this@FillterActvity)
        filter_listAPICAll()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.filterActivityFilter){
            var stringBuilder = StringBuilder()
            if(dataList!=null && dataList.isNotEmpty()){
                for (i in 0 until dataList.size){
                    for (j in 0 until dataList[i].filter_options.size){
                       if( dataList[i].filter_options[j].isChekd){
                           stringBuilder.append(dataList[i].filter_options[j].id+",")
                       }
                    }
                }
                 PreferenceKeeper.instance.currentFilterValue = stringBuilder.substring(0,stringBuilder.length-1)
                var vv=PreferenceKeeper.instance.currentFilterValue
                var vv2=PreferenceKeeper.instance.currentFilterValue
            }
        }
        else if(v== binding.filterActivityToolbar.toolbarClearAll){
            try {
                if(dataList!=null && dataList.isNotEmpty()){
                    for (i in 0 until dataList.size){
                        for (j in 0 until dataList[i].filter_options.size){
                            dataList[i].filter_options[j].isChekd=false
                        }
                    }
                    fillterMainAdapter.notifyDataSetChanged()
                    PreferenceKeeper.instance.currentFilterValue = ""//data clear
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun filter_listAPICAll() {
        customProgressDialog.show(this@FillterActvity, "")
        filterViewModel.getFiterList().observe(this@FillterActvity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        dataList = ArrayList()
                        dataList = myData.data.filter_name
                        seList()
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    // Utills.showSnackBarOnError(binding.lostConstrentToolbar, it.message!!, this@LostitemActivity)

                }
            }
        })
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
  lateinit var  dataList: ArrayList<FillterRes.FilterName>
    private fun seList( ) {
        fillterMainAdapter = FillterMainAdapter(this@FillterActvity,dataList,object:FillterMainAdapter.ClickListener{
            override fun onClick(pos: Int) {
                dataList[pos].isSelected = !dataList[pos].isSelected
                fillterMainAdapter.notifyItemChanged(pos)

              //  MyApp.ShowTost(this@FillterActvity,"hi")
            }

            override fun onClickSub(pos: Int, subPos: Int) {
                dataList[pos].filter_options[subPos].isChekd =   !dataList[pos].filter_options[subPos].isChekd
                fillterMainAdapter.notifyDataSetChanged()
            }

        })
        binding.filterActivityRecyle.also {
            it.layoutManager = LinearLayoutManager(this@FillterActvity)
            it.adapter=fillterMainAdapter
        }

    }
}