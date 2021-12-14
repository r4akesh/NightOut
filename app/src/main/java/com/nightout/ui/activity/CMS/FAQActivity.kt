package com.nightout.ui.activity.CMS

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.FAQItemAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityFaqactivityBinding
import com.nightout.model.AboutModelResponse
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class FAQActivity : BaseActivity() {
    lateinit var binding: ActivityFaqactivityBinding
    private var customProgressDialog = CustomProgressDialog()
    private lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faqactivity)
        commonViewModel = CommonViewModel(this@FAQActivity)
        setToolBar()
        cmsAPICAll()
    }

    private fun cmsAPICAll() {
        customProgressDialog.show(this@FAQActivity, "")
        commonViewModel.aboutCms().observe(this@FAQActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->

                            setUpList(myData.data.faq)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@FAQActivity,
                        it.message!!,

                    )
                }
            }
        })
    }

    private fun setUpList(faqList: ArrayList<AboutModelResponse.Faq>) {
        binding.faqItemList.layoutManager = LinearLayoutManager(this)
        val faqItemAdapter = FAQItemAdapter(this,faqList)
        binding.faqItemList.adapter = faqItemAdapter
    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.faq)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE


    }


}