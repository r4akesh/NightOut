package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.MyOrderAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.MyorderActvityBinding
import com.nightout.model.MyOrderRes
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.nodata_found.view.*

class MyOrderActvitiy : BaseActivity() {
    lateinit var customProgressDialog: CustomProgressDialog
    lateinit var myOrderViewModel: CommonViewModel
    lateinit var binding: MyorderActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MyOrderActvitiy,R.layout.myorder_actvity)
        setToolBar()
        customProgressDialog = CustomProgressDialog()
        myOrderViewModel = CommonViewModel(this@MyOrderActvitiy)
        myOrderAPICall()
    }

    private fun setToolBar() {
        setTouchNClick(binding.myOrdersToolBar.toolbarBack)
        binding.myOrdersToolBar.toolbarBack.setOnClickListener { finish() }
        binding.myOrdersToolBar.toolbarTitle.text = "My Orders"
        binding.myOrdersToolBar.toolbar3dot.visibility = View.GONE
        binding.myOrdersToolBar.toolbarBell.visibility = View.GONE
    }


    private fun myOrderAPICall() {
        customProgressDialog.show(this@MyOrderActvitiy,"")

        myOrderViewModel.orderList().observe(this@MyOrderActvitiy) {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    it.data?.let { myData ->
                        setList(myData.data)
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    customProgressDialog.dialog.dismiss()
                    binding.myOrderNoDataConstrent.visibility= VISIBLE
                    binding.myOrderNoData.nodataFoundMsg.text=resources.getString(R.string.No_data_found)
                    it.message?.let { it1 -> Utills.showDefaultToast(THIS!!, it1) }

                }
            }
        }
    }

    lateinit var myOrderAdapter: MyOrderAdapter
    private fun setList(dataList: ArrayList<MyOrderRes.Data>) {
        myOrderAdapter = MyOrderAdapter(this@MyOrderActvitiy,dataList,object:MyOrderAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.myOrderRecycleView.also {
        it.layoutManager= LinearLayoutManager(this@MyOrderActvitiy,LinearLayoutManager.VERTICAL,false)
            it.adapter = myOrderAdapter
        }


    }
}