package com.nightout.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.ContactListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ContactlistActvityBinding
import com.nightout.model.ContactFillterModel
import com.nightout.model.ContactNoModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.ContactFillterViewModel
import com.nightout.viewmodel.SaveEmngyPhNoViewModel
import org.json.JSONArray
import org.json.JSONObject

class ContactListActvity : BaseActivity() {
    lateinit var contactListAdapter: ContactListAdapter
    lateinit var binding: ContactlistActvityBinding
    var savelistContcat = StringBuilder()
    private val progressDialog = CustomProgressDialog()
    lateinit var contactFillterViewModel: ContactFillterViewModel
    lateinit var saveEmngyPhNoViewModel: SaveEmngyPhNoViewModel
      var listFilter= ArrayList<ContactFillterModel.Data>()
    var chkSelectedCnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ContactListActvity, R.layout.contactlist_actvity)
        initView()
        setToolBar()


        contact_listAPICAll()
    }

    private fun setToolBar() {
        binding.emerngcyToolBar.toolbarTitle.text = resources.getString(R.string.Emergency_Contact)
        binding.emerngcyToolBar.toolbarBack.setOnClickListener { finish() }
        binding.emerngcyToolBar.toolbar3dot.visibility = View.GONE
        binding.emerngcyToolBar.toolbarBell.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.addContactDoneBtn) {
            savelistContcat = StringBuilder()
            chkSelectedCnt = 0
            for (i in 0 until listFilter.size) {
                if (listFilter[i].isChk) {
                    savelistContcat.append(listFilter[i].uid + ",")
                    chkSelectedCnt += 1
                }
            }
            var existingCount = intent.getIntExtra(AppConstant.INTENT_EXTRAS.EMERNGCY_COUNT, 0)
            var totCnt = existingCount + chkSelectedCnt
            if (chkSelectedCnt > 0) {
                if (totCnt > 2) {
                    MyApp.popErrorMsg("", "You can add only two contact number", this@ContactListActvity)
                } else {
                    add_emergency_contactAPICALL()
                }
            } else {
                MyApp.popErrorMsg("", "Please select contact number", this@ContactListActvity)
            }
        }
    }

    private fun initView() {
        contactFillterViewModel = ContactFillterViewModel(this@ContactListActvity)
        saveEmngyPhNoViewModel = SaveEmngyPhNoViewModel(this@ContactListActvity)
        binding.addContactDoneBtn.setOnClickListener(this)

    }

    private fun contact_listAPICAll() {
        progressDialog.show(this@ContactListActvity, "")
        var listContcatAll =
            intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.CONTACT_LIST) as ArrayList<ContactNoModel>
        val jsnObjMain = JSONObject()
        val jarr = JSONArray()
        for (i in 0 until listContcatAll.size) {
            val jsonObjects = JSONObject()
            jsonObjects.put("name", listContcatAll[i].name)
            jsonObjects.put("phonenumber", listContcatAll[i].phno)
            jarr.put(jsonObjects)
        }
        jsnObjMain.put("contact_list", jarr)
        try {
            contactFillterViewModel.getContactFilter(jsnObjMain).observe(this@ContactListActvity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        listFilter.addAll(it.data?.data!!)
                        if (!listFilter.isNullOrEmpty()) {
                            setListContact()
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            Utills.showSnackBarOnError(
                                binding.constrentEmToolbar,
                                it.message!!,
                                this@ContactListActvity
                            )
                        } catch (e: Exception) {
                        }
                        Log.d("ok", "loginCall:ERROR ")
                    }
                }
            })
        } catch (e: Exception) {
        }


    }


    private fun add_emergency_contactAPICALL() {
        progressDialog.show(this@ContactListActvity, "")
        var hMap = HashMap<String, String>()
        hMap["u_id"] = savelistContcat.substring(0, savelistContcat.toString().length - 1)
        try {
            saveEmngyPhNoViewModel.saveEmngcy(hMap).observe(this@ContactListActvity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        MyApp.popErrorMsg("hii", "" + it.message, THIS!!)
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            Utills.showSnackBarOnError(
                                binding.constrentEmToolbar,
                                it.message!!,
                                this@ContactListActvity
                            )
                        } catch (e: Exception) {
                        }
                        Log.d("ok", "loginCall:ERROR ")
                    }
                }
            })
        } catch (e: Exception) {
        }


    }

    private fun setListContact() {
        contactListAdapter = ContactListAdapter(this@ContactListActvity, listFilter!!, object : ContactListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {
                    listFilter!![pos].isChk = !listFilter[pos].isChk
                    contactListAdapter.notifyItemChanged(pos)
                }

            })

        binding.contactListRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@ContactListActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = contactListAdapter
        }

    }
}