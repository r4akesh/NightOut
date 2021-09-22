package com.nightout.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.EmergencyContactAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityEmergencyContactBinding
import com.nightout.model.ContactNoModel
import com.nightout.model.GetEmergencyModel
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.DelEmngyPhNoViewModel
import com.nightout.viewmodel.GetEmngyPhNoViewModel


class EmergencyContactActivity : BaseActivity() {
    var contactsInfoList = ArrayList<ContactNoModel>()
    lateinit var binding: ActivityEmergencyContactBinding
    val REQUEST_READ_CONTACTS = 80
    val REQCODE_ContactListActvity = 115

    private val progressDialog = CustomProgressDialog()
    lateinit var getEmngyPhNoViewModel: GetEmngyPhNoViewModel
    lateinit var delEmngyPhNoViewModel: DelEmngyPhNoViewModel
    var listEmergency = ArrayList<GetEmergencyModel.Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency_contact)
        setToolBar()
        initView()
        /// setUpList()
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            contactsInfoList = getAllContacts()
            Log.d("TAG", "onCreate: " + contactsInfoList)
            Log.d("TAG", "Size: " + contactsInfoList.size)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        emergency_contact_listAPICALL()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.addContactBtn) {
            startActivityForResult(Intent(this@EmergencyContactActivity, ContactListActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.CONTACT_LIST, contactsInfoList)
                    .putExtra(AppConstant.INTENT_EXTRAS.EMERNGCY_COUNT, 0)
            ,REQCODE_ContactListActvity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==REQCODE_ContactListActvity && resultCode== Activity.RESULT_OK ){
            emergency_contact_listAPICALL()//for again get update list
        }
    }


    private fun emergency_contact_listAPICALL() {
        progressDialog.show(this@EmergencyContactActivity, "")
        try {
            getEmngyPhNoViewModel.getEmngcy().observe(this@EmergencyContactActivity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        try {
                            listEmergency = ArrayList()
                            listEmergency.addAll(it.data?.data!!)
                            setListEmerngcy()
                            setTextAddBtn()
                        } catch (e: Exception) {
                            binding.addContactBtn.visibility= VISIBLE
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            Utills.showSnackBarOnError(
                                binding.emRootLayout,
                                it.message!!,
                                this@EmergencyContactActivity
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

    lateinit var emergencyContactAdapter: EmergencyContactAdapter
    private fun setListEmerngcy() {
        emergencyContactAdapter = EmergencyContactAdapter(this, listEmergency, object : EmergencyContactAdapter.ClickListenerr {
                override fun onClick(pos: Int) {
                    delAPICAll(listEmergency[pos].id, pos)
                }

            })

        binding.emergencyList.also {
            it.layoutManager = LinearLayoutManager(
                this@EmergencyContactActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter = emergencyContactAdapter
        }
    }

    private fun delAPICAll(uId: String, listPos: Int) {
        progressDialog.show(this@EmergencyContactActivity, "")
        var map = HashMap<String, String>()
        map["id"] = uId
        try {
            delEmngyPhNoViewModel.delEmngcy(map).observe(this@EmergencyContactActivity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        val listSize = listEmergency.size
                        listEmergency.removeAt(listPos)
                        emergencyContactAdapter.notifyItemRemoved(listPos)
                        emergencyContactAdapter.notifyItemRangeChanged(listPos, listSize)
                        setTextAddBtn()
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            Utills.showSnackBarOnError(
                                binding.emRootLayout,
                                it.message!!,
                                this@EmergencyContactActivity
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

    private fun setTextAddBtn() {
        binding.addContactBtn.visibility = VISIBLE
        if (listEmergency.size == 0) {
            binding.addContactBtn.setText("Add Contact")
        } else if (listEmergency.size == 1) {
            binding.addContactBtn.setText("Add One More Contact")
        } else {
            binding.addContactBtn.visibility = GONE
        }
    }

    private fun initView() {
        binding.addContactBtn.setOnClickListener(this)
        getEmngyPhNoViewModel = GetEmngyPhNoViewModel(this@EmergencyContactActivity)
        delEmngyPhNoViewModel = DelEmngyPhNoViewModel(this@EmergencyContactActivity)
    }


    private fun getAllContacts(): ArrayList<ContactNoModel> {
        var contactId: String? = null
        var displayName: String? = null
        contactsInfoList = ArrayList()
        val cursor = getContentResolver().query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val hasPhoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt()
                if (hasPhoneNumber > 0) {

                    contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    displayName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    // contactsInfo.setContactId(contactId)
                    // contactsInfo.setDisplayName(displayName)
                    val phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactId),
                        null
                    )
                    if (phoneCursor!!.moveToNext()) {
                        val phoneNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        //contactsInfo.setPhoneNumber(phoneNumber)
                        contactsInfoList.add(
                            ContactNoModel(
                                displayName,
                                phoneNumber.replace(" ", "").trim(),
                                false
                            )
                        )
                    }
                    phoneCursor.close()
                    // contactsInfoList.add(contactsInfo)
                }
            }
        }
        cursor.close()
        return contactsInfoList
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactsInfoList = getAllContacts()
                } else {
                    Toast.makeText(
                        this,
                        "You have disabled a contacts permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }

    }

    private fun setToolBar() {
        binding.emergCondToolBar.toolbarTitle.text = resources.getString(R.string.Emergency_Contact)
        binding.emergCondToolBar.toolbarBack.setOnClickListener { finish() }
        binding.emergCondToolBar.toolbar3dot.visibility = View.GONE
        binding.emergCondToolBar.toolbarBell.visibility = View.GONE

    }

}