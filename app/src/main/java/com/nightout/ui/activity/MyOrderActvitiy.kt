package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import com.nightout.R
import com.nightout.adapter.MyOrderAdapter
import com.nightout.base.BaseActivity
import com.nightout.model.MyOrderRes
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class MyOrderActvitiy : BaseActivity() {
    lateinit var customProgressDialog: CustomProgressDialog
    lateinit var myOrderViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myorder_actvity)
        myOrderAPICall()
    }

    private fun myOrderAPICall() {
        customProgressDialog.show(this@MyOrderActvitiy,"")

        myOrderViewModel.orderList().observe(this@MyOrderActvitiy,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        setList(myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showDefaultToast(THIS!!,it.data!!.message)

                }
            }
        })
    }

    lateinit var myOrderAdapter: MyOrderAdapter
    private fun setList(dataList: ArrayList<MyOrderRes.Data>) {
        myOrderAdapter = MyOrderAdapter()

    }
}