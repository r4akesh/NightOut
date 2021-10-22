package com.nightout.ui.activity

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

class AddBarCrawlActvity : BaseActivity() {
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

    private fun barcrawllistAPICall() {
        customProgressDialog.show(this@AddBarCrawlActvity, "")
        commonViewModel.barCrwlList().observe(this@AddBarCrawlActvity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                    var listOption = myData.data.barcrawl_options
                        setlistOption(listOption)
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

    lateinit var barCrawlOptionAdapter : BarCrawlOptionAdapter
    private fun setlistOption(listOption: ArrayList<BarCrwlListModel.BarcrawlOption>) {
        barCrawlOptionAdapter = BarCrawlOptionAdapter(this@AddBarCrawlActvity,listOption,object:BarCrawlOptionAdapter.ClickListener{
            override fun onClick(pos: Int) {

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

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.createBtn){
            startActivity( Intent (this@AddBarCrawlActvity, BarCrawlSaveActivity::class.java))
            finish()
        }
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

}