package com.nightout.ui.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.nightout.adapter.ContactListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ContactlistActvityBinding
import com.nightout.model.ContactFillterModel
import com.nightout.model.ContactNoModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import com.nightout.viewmodel.ContactFillterViewModel
import com.nightout.viewmodel.SaveEmngyPhNoViewModel
import org.json.JSONArray
import org.json.JSONObject

class ContactListNewActvity : BaseActivity() {
    lateinit var contactListAdapter: ContactListAdapter
    lateinit var binding: ContactlistActvityBinding
    var savelistContcat = StringBuilder()
    private val progressDialog = CustomProgressDialog()
    lateinit var contactFillterViewModel: CommonViewModel
    lateinit var saveEmngyPhNoViewModel: CommonViewModel
      var listFilter= ArrayList<ContactFillterModel.Data>()
    var chkSelectedCnt = 0

    val REQUEST_READ_CONTACTS = 80
    var contactsInfoList = ArrayList<ContactNoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ContactListNewActvity, R.layout.contactlist_actvity)
        initView()
        setToolBar()

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Handler(Looper.getMainLooper()).post {
                contactsInfoList = getAllContacts()
                if(contactsInfoList.size>0)
                    contact_listAPICAll()
                else
                    MyApp.popErrorMsg("",resources.getString(R.string.Nocontactfounddevice),this@ContactListNewActvity)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
    }

    private fun setToolBar() {
        binding.emerngcyToolBar.toolbarTitle.text = resources.getString(R.string.Contact_List)
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
                    MyApp.popErrorMsg("", "You can add only two contact number", this@ContactListNewActvity)
                } else {
                    if(intent.getBooleanExtra(AppConstant.PrefsName.ISFROM_BarCrwalPathMapActvity,false)){
                        bar_crawl_invitationAPICall()
                    }else {
                        add_emergency_contactAPICALL()
                    }
                }
            } else {
                MyApp.popErrorMsg("", "Please select contact number", this@ContactListNewActvity)
            }
        }
    }

    private fun bar_crawl_invitationAPICall() {
        progressDialog.show(this@ContactListNewActvity, "")
        val map = HashMap<String, String>()
        var stringBuilder = StringBuilder()
        for (i in 0 until listFilter.size){
            if(listFilter[i].isChk){
                stringBuilder.append(listFilter[i].uid)
                stringBuilder.append(",")
            }
        }
        var vID = stringBuilder.substring(0,stringBuilder.length-1)
        map["id"] = intent.getStringExtra(AppConstant.INTENT_EXTRAS.BarcrwalID).toString()
        map["user_ids"] = vID
        try {
            contactFillterViewModel.shareBarCrwal(map).observe(this@ContactListNewActvity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        try {
                            Log.d("ok", "bar_crawl_invitationAPICall: "+it.message)
                        } catch (e: Exception) {
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {

                            // Utills.showSnackBarOnError(binding.constrentEmToolbar, it.message!!, this@ContactListActvity)
                            binding.addContactDoneBtn.visibility=GONE
                        } catch (e: Exception) {
                        }

                    }
                }
            })
        } catch (e: Exception) {
        }


    }

    private fun initView() {
        contactFillterViewModel = CommonViewModel(this@ContactListNewActvity)
        saveEmngyPhNoViewModel = CommonViewModel(this@ContactListNewActvity)
        binding.addContactDoneBtn.setOnClickListener(this)

    }

    private fun contact_listAPICAll() {
        progressDialog.show(this@ContactListNewActvity, "")
        val jsnObjMain = JSONObject()
        val jarr = JSONArray()
        for (i in 0 until contactsInfoList.size) {
            val jsonObjects = JSONObject()
            jsonObjects.put("name", contactsInfoList[i].name)
            jsonObjects.put("phonenumber", contactsInfoList[i].phno)
            jarr.put(jsonObjects)
        }
        jsnObjMain.put("contact_list", jarr)
        try {
            contactFillterViewModel.getContactFilter(jsnObjMain).observe(this@ContactListNewActvity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        try {
                            progressDialog.dialog.dismiss()
                            listFilter.addAll(it.data?.data!!)
                            if (!listFilter.isNullOrEmpty()) {
                                setListContact()

                                binding.addContactDoneBtn.text="Share"
                                binding.addContactDoneBtn.visibility= VISIBLE
                                binding.contactListNoDataConstrent.visibility = GONE
                            }else{
                                binding.contactListNoDataConstrent.visibility = VISIBLE
                                binding.addContactDoneBtn.visibility= VISIBLE
                            }
                        } catch (e: Exception) {
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            binding.contactListNoDataConstrent.visibility = VISIBLE
                           // Utills.showSnackBarOnError(binding.constrentEmToolbar, it.message!!, this@ContactListActvity)
                            binding.addContactDoneBtn.visibility=GONE
                        } catch (e: Exception) {
                        }

                    }
                }
            })
        } catch (e: Exception) {
        }


    }


    private fun add_emergency_contactAPICALL() {
        progressDialog.show(this@ContactListNewActvity, "")
        var hMap = HashMap<String, String>()
        hMap["u_id"] = savelistContcat.substring(0, savelistContcat.toString().length - 1)
        try {
            saveEmngyPhNoViewModel.saveEmngcy(hMap).observe(this@ContactListNewActvity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                        try {
                            Utills.showSnackBarOnError(
                                binding.constrentEmToolbar,
                                it.message!!,
                                this@ContactListNewActvity
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
        contactListAdapter = ContactListAdapter(this@ContactListNewActvity, listFilter!!, object : ContactListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {
                    listFilter!![pos].isChk = !listFilter[pos].isChk
                    contactListAdapter.notifyItemChanged(pos)
                }

            })

        binding.contactListRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@ContactListNewActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = contactListAdapter
        }

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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactsInfoList = getAllContacts()
                    if(contactsInfoList.size>0)
                    contact_listAPICAll()
                    else
                        MyApp.popErrorMsg("",resources.getString(R.string.Nocontactfounddevice),this@ContactListNewActvity)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.disble_permison_contact),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }

    }
}