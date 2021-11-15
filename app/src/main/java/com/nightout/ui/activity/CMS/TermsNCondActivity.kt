package com.nightout.ui.activity.CMS

import android.os.Bundle
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.TermsItemAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.TermncondActviityBinding
import com.nightout.model.AboutModelResponse
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

import kotlinx.android.synthetic.main.termncond_actviity.*


class TermsNCondActivity : BaseActivity() {
    lateinit var  binding : TermncondActviityBinding
    private var customProgressDialog = CustomProgressDialog()
    private lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@TermsNCondActivity,R.layout.termncond_actviity)
        commonViewModel = CommonViewModel(this@TermsNCondActivity)
        setToolBar()
        cmsAPICAll()
    }
    private fun cmsAPICAll() {
        customProgressDialog.show(this@TermsNCondActivity, "")
        commonViewModel.aboutCms().observe(this@TermsNCondActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->

                        setUpList(myData.data.term)

                        }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(
                        binding.rootLayoutTerms,
                        it.message!!,
                        this@TermsNCondActivity
                    )
                }
            }
        })
    }
    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = "Terms and Condition"
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility=GONE
        binding.termCondToolBar.toolbarBell.visibility=GONE


    }

    private fun setUpList(termList: ArrayList<AboutModelResponse.Term>) {
        binding.termContentList.layoutManager = LinearLayoutManager(this)
        val termsItemAdapter = TermsItemAdapter(this,termList)
        binding.termContentList.adapter = termsItemAdapter
    }
}