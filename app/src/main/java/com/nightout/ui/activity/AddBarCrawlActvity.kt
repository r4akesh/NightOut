package com.nightout.ui.activity

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.BarCrawlOptionAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.AddbarcrawlActivityBinding
import com.nightout.model.BarCrwlListModel
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import android.R.attr.country
import android.widget.AdapterView

import android.widget.ArrayAdapter




class AddBarCrawlActvity : BaseActivity() ,AdapterView.OnItemSelectedListener{
    private var customProgressDialog = CustomProgressDialog()
    lateinit var binding: AddbarcrawlActivityBinding
    private lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AddBarCrawlActvity,R.layout.addbarcrawl_activity)
        setToolBar()
        initView()
        barcrawllistAPICall()
    }
    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.createBtn){
            startActivity( Intent (this@AddBarCrawlActvity, BarCrawlSaveActivity::class.java))
            finish()
        }
    }
    private fun barcrawllistAPICall() {
        customProgressDialog.show(this@AddBarCrawlActvity, "")
        commonViewModel.barCrwlList().observe(this@AddBarCrawlActvity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                    var listOption = myData.data.barcrawl_options
                        setlistOption(listOption)
                      //  setSpinBarCrawl(myData.data.barcrawl)
                    }
                }
                Status.LOADING->{ }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(
                        binding.rootLayoutAddBar,
                        it.message!!,
                        this@AddBarCrawlActvity
                    )
                }
            }
        })
    }

    private fun setSpinBarCrawl(barcrawlList: ArrayList<BarCrwlListModel.Barcrawl>) {

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, barcrawlList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.addBarCrawlSpinBarCrawl.adapter = aa



    }

    lateinit var barCrawlOptionAdapter : BarCrawlOptionAdapter
    private fun setlistOption(listOption: ArrayList<BarCrwlListModel.BarcrawlOption>) {
        barCrawlOptionAdapter = BarCrawlOptionAdapter(this@AddBarCrawlActvity,listOption,object:BarCrawlOptionAdapter.ClickListener{
            override fun onClick(pos: Int) {
                listOption[pos].isSelected = !listOption[pos].isSelected

                barCrawlOptionAdapter.notifyItemChanged(pos)

            }

        })

        binding.addBarCrawlRecyle.also {
            it.layoutManager = LinearLayoutManager(this@AddBarCrawlActvity,LinearLayoutManager.VERTICAL,false)
            it.adapter = barCrawlOptionAdapter
        }
    }

    private fun initView() {
        setTouchNClick(R.id.createBtn)
        commonViewModel = CommonViewModel(this@AddBarCrawlActvity)
    }



    private fun setToolBar() {
        setTouchNClick(binding.addBarCrawlToolBar.toolbarBack)
        setTouchNClick(binding.addBarCrawlToolBar.toolbarCreateGrop)

        binding.addBarCrawlToolBar.toolbarTitle.setText("Add Bar Crawl")
        binding.addBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.addBarCrawlToolBar.toolbar3dot.visibility=GONE
        binding.addBarCrawlToolBar.toolbarCreateGrop.visibility= VISIBLE
        binding.addBarCrawlToolBar.toolbarCreateGrop.setText("View List")

        binding.addBarCrawlToolBar.toolbarBack.setOnClickListener{
            finish()
        }

        binding.addBarCrawlToolBar.toolbarCreateGrop.setOnClickListener {
            startActivity(Intent(this@AddBarCrawlActvity,AllBarCrawalNewActivity::class.java))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
         var vv = binding.addBarCrawlSpinBarCrawl.selectedItem.toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}