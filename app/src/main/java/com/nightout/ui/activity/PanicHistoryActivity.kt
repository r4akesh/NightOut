package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.PanicHistryAdpter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityEmergencyContactListBinding
import com.nightout.model.PanicHistoryRes
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class PanicHistoryActivity : BaseActivity() {
    lateinit var binding: ActivityEmergencyContactListBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var panicHistoryViewMode: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency_contact_list)
        panicHistoryViewMode = CommonViewModel(this@PanicHistoryActivity)
        setToolBar()
        panic_historyAPICall()
    }

    private fun panic_historyAPICall() {
        customProgressDialog.show(this@PanicHistoryActivity, "")
        panicHistoryViewMode.panicHistory().observe(this@PanicHistoryActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {
                        setList(it.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@PanicHistoryActivity,
                        it.message!!

                    )
                }
            }
        })
    }

    lateinit var panicHistryAdpter: PanicHistryAdpter
    private fun setList(dataList: ArrayList<PanicHistoryRes.Data>) {
        panicHistryAdpter = PanicHistryAdpter(this@PanicHistoryActivity,dataList,object:PanicHistryAdpter.ClickListener{
            override fun onClickChk(pos: Int) {

            }

        })
            binding.previousEmergencyList.also {
                it.layoutManager = LinearLayoutManager(this@PanicHistoryActivity,LinearLayoutManager.VERTICAL,false)
                it.adapter=panicHistryAdpter
            }
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.Emergency_History)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE
        init()
    }

    private fun init(){
     //   row_emergency_item
//        binding.previousEmergencyList.layoutManager = LinearLayoutManager(this)
//        val previousEmergencyInfoAdatper = PreviousEmergencyInfoAdatper(this)
//        binding.previousEmergencyList.adapter = previousEmergencyInfoAdatper
    }
}