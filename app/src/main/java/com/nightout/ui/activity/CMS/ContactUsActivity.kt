package com.nightout.ui.activity.CMS

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityContactUsBinding
import android.widget.ArrayAdapter
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel


class ContactUsActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityContactUsBinding
    var country = arrayOf("Select", "Complain", "Enquiry")
    private var progressDialog = CustomProgressDialog()
    lateinit var sendQueryViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        setToolBar()
        initView()
        setSpinner()

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        MyApp.preventDoubleClick(v!!)
        if (v == binding.contactUsSubmitBtn) {
            if (isValidInput()) {
                senQueryAPICall()
            }
        }
    }

    private fun isValidInput(): Boolean {
        if (binding.spinQuery.selectedItem.toString()
                .equals(resources.getString(R.string.Select))
        ) {
            Utills.showSnackBarOnError(
                binding.contactUsName,
                resources.getString(R.string.query_spin_plz),
                this@ContactUsActivity
            )
            return false
        } else if (binding.contactUsName.text.toString().isNullOrBlank()) {
            Utills.showSnackBarOnError(
                binding.contactUsName,
                resources.getString(R.string.name_plz),
                this@ContactUsActivity
            )
            return false
        } else if (binding.contactUsPhNo.text.toString().isNullOrBlank()) {
            Utills.showSnackBarOnError(
                binding.contactUsName,
                resources.getString(R.string.phone_no_plz),
                this@ContactUsActivity
            )
            return false
        } else if (binding.contactUsDetail.text.toString().isNullOrBlank()) {
            Utills.showSnackBarOnError(
                binding.contactUsName,
                resources.getString(R.string.detail_plz),
                this@ContactUsActivity
            )
            return false
        }

        return true
    }

    private fun senQueryAPICall() {
        progressDialog.show(this@ContactUsActivity, "")
        // type (0=>Enquiry, 1=>Complaints)
        var map = HashMap<String, String>()
        map["type"] = "0"
        map["email"] = PreferenceKeeper.instance.loginResponse!!.email
        map["name"] = PreferenceKeeper.instance.loginResponse!!.name
        map["phonenumber"] = PreferenceKeeper.instance.loginResponse!!.phonenumber
        map["query"] = binding.contactUsDetail.text.toString()

        sendQueryViewModel.sendQuery(map).observe(this@ContactUsActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let { detailData ->
                        try {
                            binding.contactUsDetail.setText("")
                            Utills.showSnackBarOnError(binding.rootLayoutContactUs, detailData.message!!, this@ContactUsActivity)
                        } catch (e: Exception) {
                            Utills.showSnackBarOnError(binding.rootLayoutContactUs, e.toString(), this@ContactUsActivity)
                        }
                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(binding.rootLayoutContactUs, it.message!!, this@ContactUsActivity)
                }
            }
        })
    }



    private fun initView() {
        try {
            sendQueryViewModel = CommonViewModel(this@ContactUsActivity)
            binding.contactUsSubmitBtn.setOnClickListener(this)
            var loginDetail = PreferenceKeeper.instance.loginResponse
            binding.contactUsPhNo.setText(Utills.phoneNoUKFormat(loginDetail!!.phonenumber))
            binding.contactUsName.setText(loginDetail!!.name)
            binding.callNumber.setText("+44 "+Utills.phoneNoUKFormat(loginDetail.admin_detail.phonenumber))
            binding.email.setText(loginDetail.admin_detail.email)
            binding.contactAddress.setText(loginDetail.admin_detail.address)

        } catch (e: Exception) {
            Log.d("ok", "Exception: "+e.toString())
        }
    }

    private fun setSpinner() {
        binding.spinQuery.onItemSelectedListener = this
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.row_spin_item, country)
        aa.setDropDownViewResource(R.layout.simple_spin_dropdownitem_contactus)

        binding.spinQuery.setAdapter(aa)

    }

    private fun setToolBar() {
        binding.termCondToolBar.toolbarTitle.text = resources.getString(R.string.Contact_Us)
        binding.termCondToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.termCondToolBar.toolbar3dot.visibility = View.GONE
        binding.termCondToolBar.toolbarBell.visibility = View.GONE
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}