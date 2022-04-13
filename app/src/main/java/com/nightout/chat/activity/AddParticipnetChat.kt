package com.nightout.chat.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.ContactListAdapter
import com.nightout.base.BaseActivity
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.databinding.AddparticientchatActivityBinding
import com.nightout.model.ContactFillterModel
import com.nightout.model.ContactNoModel
import com.nightout.model.FSUsersModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AddParticipnetChat : BaseActivity(), WebSocketObserver {
    val REQUEST_READ_CONTACTS = 81
    private val progressDialog = CustomProgressDialog()
    lateinit var contactFillterViewModel: CommonViewModel
    var contactsInfoList = ArrayList<ContactNoModel>()
    var listFilter = ArrayList<ContactFillterModel.Data>()
    var listFilterCopy = ArrayList<ContactFillterModel.Data>()

    lateinit var binding : AddparticientchatActivityBinding
    lateinit var contactListAdapter: ContactListAdapter
    lateinit var existingFrend : ArrayList<FSUsersModel>
    var roomID=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AddParticipnetChat, R.layout.addparticientchat_activity)
        setToolBar()
          existingFrend= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.GROUP_LIST) as ArrayList<FSUsersModel>
          roomID= intent.getStringExtra(AppConstant.INTENT_EXTRAS.ROOM_ID).toString()
        contactFillterViewModel = CommonViewModel(this@AddParticipnetChat)
        setTouchNClick(binding.addPricpentChatToolBar.toolbarCreateGrop)
        WebSocketSingleton.getInstant()!!.register(this)



        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            progressDialog.show(this@AddParticipnetChat, "")
            GlobalScope.launch(Dispatchers.Main) {
                contactsInfoList = getAllContactsFrmDevice()
                if (contactsInfoList.size > 0) {
                    progressDialog.dialog.dismiss()
                    getAllContactsAPICAll()
                } else {
                    progressDialog.dialog.dismiss()
                    DialogCustmYesNo.getInstance().createDialogOK(
                        THIS!!,
                        "",
                        resources.getString(R.string.No_oneinyour_contact_share),
                        object :
                            DialogCustmYesNo.Dialogclick {
                            override fun onYES() {
                                finish()
                            }

                            override fun onNO() {}
                        })
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        }

    }
    lateinit var usersList: JSONArray
    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.addPricpentChatToolBar.toolbarCreateGrop){
             usersList = JSONArray()
            for (i in 0 until listFilter.size) {
                if (listFilter[i].isChk) {
                    usersList.put(listFilter[i].uid)
                }
            }

            if(usersList.length()>0) {
                val jsonObject = JSONObject()
                jsonObject.put("type", "addUser")
                jsonObject.put("userList", usersList)
                jsonObject.put("roomId", roomID)
                jsonObject.put("request", "room")
                jsonObject.put("room_type", "group")
                WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
            }else{
                MyApp.popErrorMsg("","Please add participant",THIS!!)
            }

        }
    }

    private fun setToolBar() {
         binding.addPricpentChatToolBar.toolbarTitle.setText(resources.getString(R.string.Add_Participant))
        setTouchNClick( binding.addPricpentChatToolBar.toolbarBack)
         binding.addPricpentChatToolBar.toolbarBack.setOnClickListener { finish() }
        binding.addPricpentChatToolBar.toolbar3dot.visibility= View.GONE
        binding.addPricpentChatToolBar.toolbarBell.visibility= View.GONE
        binding.addPricpentChatToolBar.toolbarCreateGrop.visibility= View.VISIBLE
        binding.addPricpentChatToolBar.toolbarCreateGrop.setText(resources.getString(R.string.DONE))
    }

    @SuppressLint("Range")
    private fun getAllContactsFrmDevice(): ArrayList<ContactNoModel> {
        //  progressDialog.show(this@ContactListNewActvity, "")
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
                    GlobalScope.launch(Dispatchers.Main) {
                        contactsInfoList = getAllContactsFrmDevice()
                        if (contactsInfoList.size > 0) {
                            getAllContactsAPICAll()
                        } else {
                            progressDialog.dialog.dismiss()
                            DialogCustmYesNo.getInstance().createDialogOK(
                                THIS!!,
                                "",
                                resources.getString(R.string.No_oneinyour_contact_share),
                                object : DialogCustmYesNo.Dialogclick {
                                    override fun onYES() {
                                        finish()
                                    }

                                    override fun onNO() {

                                    }

                                })
                        }

                    }

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

    private fun getAllContactsAPICAll() {
        Log.e("ok", "getAllContactsAPICAll: ")
        progressDialog.show(this@AddParticipnetChat, "")
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
            contactFillterViewModel.getContactAll(jsnObjMain)
                .observe(this@AddParticipnetChat) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            try {
                                try {
                                    progressDialog.dialog.dismiss()
                                } catch (e: Exception) {
                                }
                                listFilter.addAll(it.data?.data!!)
                                listFilterCopy.addAll(it.data?.data!!)

                                if (!listFilter.isNullOrEmpty()) {
                                    Log.e("ok", "getAllContactsAPICAll: if")

                                    if(!existingFrend.isNullOrEmpty()) {
                                        //remove the existing frend
                                        listFilterCopy.forEach {
                                                existingFrend.forEach { fsuserModel->
                                                    if(it.uid ==fsuserModel.id){
                                                        listFilter.remove(it)
                                                    }
                                                }
                                            }

                                    }

                                    setListContact()


                                }
                            } catch (e: Exception) {
                                Log.d("ok", "getAllContactsAPICAll: " + e.toString())
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            progressDialog.dialog.dismiss()
                            try {

                                Utills.showDefaultToast(this@AddParticipnetChat, it.message!!)

                            } catch (e: Exception) {
                            }

                        }
                    }
                }
        } catch (e: Exception) {
            Log.d("ok", "contact_listAPICAll: " + e)
        }


    }

    private fun setListContact() {
        contactListAdapter = ContactListAdapter(this@AddParticipnetChat, listFilter!!,
            object : ContactListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {
                    listFilter!![pos].isChk = !listFilter[pos].isChk
                    contactListAdapter.notifyItemChanged(pos)
                }

            })

        binding.addPersonRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@AddParticipnetChat, LinearLayoutManager.VERTICAL, false)
            it.adapter = contactListAdapter
        }

    }

    override fun onWebSocketResponse(
        response: String,
        type: String,
        statusCode: Int,
        message: String?
    ) {
        try {
            runOnUiThread {
                Log.d("ok", "received message GroupInfo2: $response")
                if (ResponseType.RESPONSE_TYPE_ADD_USER.equalsTo(type)) {
                    finish()
                }
            }

        }catch (e:Exception){

        }

    }

    override val activityName: String = AddParticipnetChat::class.java.name


    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_ADD_USER ,

        )
    }
}