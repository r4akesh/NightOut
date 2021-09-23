package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityContactUsBinding
import android.widget.ArrayAdapter




class ContactUsActivity : BaseActivity(), AdapterView.OnItemSelectedListener  {
    lateinit var binding: ActivityContactUsBinding
    var country = arrayOf("Select", "Complain", "Enquiry")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        setSpinner()

        setToolBar()
    }

    private fun setSpinner() {
        binding.spinQuery.onItemSelectedListener = this
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

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