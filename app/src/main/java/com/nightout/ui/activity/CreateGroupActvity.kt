package com.nightout.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nightout.R
import com.nightout.adapter.ContactListAdapter
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.chat.model.FSRoomModel
import com.nightout.chat.model.ResponseModel
import com.nightout.chat.model.RoomNewResponseModel
import com.nightout.chat.model.RoomResponseModel
import com.nightout.databinding.CreategrupActivityBinding
import com.nightout.model.AllUserRes
import com.nightout.model.ContactFillterModel
import com.nightout.model.ContactNoModel
import com.nightout.model.FSUsersModel
import com.nightout.utils.*
import com.nightout.vendor.services.APIClient
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap
import com.google.gson.reflect.TypeToken
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.domain.media.MediaType
import com.lassi.presentation.builder.Lassi
import com.nightout.callbacks.OnSelectOptionListener
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment

class CreateGroupActvity : BaseActivity(), WebSocketObserver, OnSelectOptionListener {
    lateinit var binding: CreategrupActivityBinding
    private val REQUEST_CAMERA_PERMISSION = 1
    var imageUri: Uri? = null
    var RequestCodeCamera = 100
    var RequestCodeGallery = 200
    private var imageUrl: Uri? = null
    private var filePath: File? = null
    private lateinit var reqFile: RequestBody
    var body: MultipartBody.Part? = null

    lateinit var contactFillterViewModel : CommonViewModel
    val REQUEST_READ_CONTACTS = 80
    private val progressDialog = CustomProgressDialog()
    var contactsInfoList = ArrayList<ContactNoModel>()
    var listFilter = ArrayList<ContactFillterModel.Data>()
    lateinit var contactListAdapter: ContactListAdapter
    private lateinit var selectSourceBottomSheetFragment: SelectSourceBottomSheetFragment
    lateinit var commonViewModel : CommonViewModel
    var imagePathUploded = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@CreateGroupActvity, R.layout.creategrup_activity)
        initView()
        setToolbar()



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            progressDialog.show(this@CreateGroupActvity, "")
            GlobalScope.launch (Dispatchers.Main){
                contactsInfoList = getAllContactsFrmDevice()
                if (contactsInfoList.size > 0) {
                    progressDialog.dialog.dismiss()
                    getAllContactsAPICAll()
                }
                else {
                    progressDialog.dialog.dismiss()
                    DialogCustmYesNo.getInstance().createDialogOK(THIS!!,"", resources.getString(R.string.No_oneinyour_contact_share),object:
                        DialogCustmYesNo.Dialogclick{
                        override fun onYES() {
                            finish()
                        }

                        override fun onNO() {

                        }

                    })

                }
            }

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
    }
    override fun onStart() {
        super.onStart()
        WebSocketSingleton.getInstant()!!.register(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i("ok CreateGroupActvity", "onStop: ")
        WebSocketSingleton.getInstant()!!.unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ok CreateGroupActvity", "onDestroy: ")
        WebSocketSingleton.getInstant()!!.unregister(this)
    }

    private fun createGroupCommand() {
        val usersList = JSONArray()
        for (i in 0 until listFilter.size ){
            if(listFilter[i].isChk){
                usersList.put(listFilter[i].uid)
            }
        }
        val jsonObject = JSONObject()
        try {
            usersList.put(PreferenceKeeper.instance.myUserDetail.id)
            jsonObject.put("userList", usersList)
            jsonObject.put("type", "createRoom")
            jsonObject.put("room_type", "group")
            jsonObject.put("createBy", PreferenceKeeper.instance.myUserDetail.id)
            val groupDetails = JSONObject()
            groupDetails.put("group_name", binding.crateGroupName.text.toString())
            groupDetails.put("about_group", "This is a Group")
            groupDetails.put("about_pic", imagePathUploded)
            jsonObject.put("group_details", groupDetails)
            jsonObject.put("create_date", Commons.millsToDateFormat(System.currentTimeMillis()))
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_ROOM)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
       // WebSocketSingleton.getInstant()!!.register(this)
        WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
    }


    private fun joinCommand() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("type", "allUsers")
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_USERS)
            //			jsonObject.put("room", roomId);
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    @SuppressLint("Range")
    private fun getAllContactsFrmDevice(): ArrayList<ContactNoModel> {
        //  progressDialog.show(this@ContactListNewActvity, "")
        var contactId: String? = null
        var displayName: String? = null
        contactsInfoList = ArrayList()
        val cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                if (hasPhoneNumber > 0) {
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
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
                    GlobalScope.launch (Dispatchers.Main){
                        contactsInfoList = getAllContactsFrmDevice()
                        if (contactsInfoList.size > 0){
                            getAllContactsAPICAll()
                        }
                        else{
                            progressDialog.dialog.dismiss()
                            DialogCustmYesNo.getInstance().createDialogOK(THIS!!,"", resources.getString(R.string.No_oneinyour_contact_share),object:DialogCustmYesNo.Dialogclick{
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
            requestPermissionCode -> if (grantResults.isNotEmpty()) {
                val cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val writeStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED
                val locationPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED
                if (cameraPermission && readStoragePermission && writeStoragePermission && locationPermission) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    //Utils.requestMultiplePermission(this,requestPermissionCode)
                }
            }
        }

    }

    private fun getAllContactsAPICAll() {
        Log.e("ok", "getAllContactsAPICAll: ")
          progressDialog.show(this@CreateGroupActvity, "")
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
                .observe(this@CreateGroupActvity, {
                    when (it.status) {
                        Status.SUCCESS -> {
                            try {
                                try {
                                    progressDialog.dialog.dismiss()
                                } catch (e: Exception) {
                                }
                                listFilter.addAll(it.data?.data!!)
                                if (!listFilter.isNullOrEmpty()) {
                                    Log.e("ok", "getAllContactsAPICAll: if")
                                    setListContact()


                                }
                            } catch (e: Exception) {
                                Log.d("ok", "getAllContactsAPICAll: "+e.toString())
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            progressDialog.dialog.dismiss()
                            try {

                             Utills.showDefaultToast(this@CreateGroupActvity, it.message!!, )

                            } catch (e: Exception) {
                            }

                        }
                    }
                })
        } catch (e: Exception) {
            Log.d("ok", "contact_listAPICAll: "+e)
        }


    }

    private fun setListContact() {
        contactListAdapter = ContactListAdapter(this@CreateGroupActvity, listFilter!!,
            object : ContactListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {
                    listFilter!![pos].isChk = !listFilter[pos].isChk
                    contactListAdapter.notifyItemChanged(pos)
                }

            })

        binding.crateGroupRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@CreateGroupActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = contactListAdapter
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when {
            v==binding.createGroupProfileRel -> {
                onSelectImage2()
            }
            v== binding.headerCreateGroup->{
                createGroupCommand()
            }
            binding.crateGroupSelectAll==v -> {
                if( binding.crateGroupSelectAll.isChecked){
                 //   binding.crateGroupSelectAll.text= resources.getString(R.string.Unselect_All)
                    binding.crateGroupSelectAll.isChecked=false
                    binding.crateGroupSelectAll.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.unchk_box,0)
                    for (i in 0 until dataList.size ){
                        dataList[i].isChk=false
                    }
                    groupListAdapter.notifyDataSetChanged()
                }else{
                    binding.crateGroupSelectAll.isChecked=true
                    binding.crateGroupSelectAll.text= resources.getString(R.string.Select_All)
                    binding.crateGroupSelectAll.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.chk_box,0)
                    for (i in 0 until dataList.size ){
                        dataList[i].isChk=true
                    }
                    groupListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun onSelectImage2() {
        if (!Utills.checkingPermissionIsEnabledOrNot(this)) {
            Utills.requestMultiplePermission(this, requestPermissionCode)
        }else{
            selectSourceBottomSheetFragment = SelectSourceBottomSheetFragment(this, "")
            selectSourceBottomSheetFragment.show(
                supportFragmentManager,
                "selectSourceBottomSheetFragment"
            )
        }
    }


    private fun initView() {
         setTouchNClick(binding.createGroupProfileRel)
         setTouchNClick(binding.crateGroupSelectAll)
         setTouchNClick(binding.headerCreateGroup)
        contactFillterViewModel = CommonViewModel(this@CreateGroupActvity)
        commonViewModel = CommonViewModel(this@CreateGroupActvity)
        binding.crateGroupSelectAll.isChecked = false
    }

    private fun setToolbar() {
        binding.toolbarTitle.text = "Create New Group"
        setTouchNClick(binding.toolbarBack)
        binding.toolbarBack.setOnClickListener { finish() }
    }

    lateinit var groupListAdapter: GroupListAdapter
    lateinit var dataList: ArrayList<AllUserRes.Data>
    private fun setUserList() {

        groupListAdapter = GroupListAdapter(this@CreateGroupActvity, dataList,true, object : GroupListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {
                dataList[pos].isChk = !dataList[pos].isChk
                    groupListAdapter.notifyItemChanged(pos)
                }

            })

        binding.crateGroupRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@CreateGroupActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }
    }

    fun onSelectImage() {
        try {
            val dialog = Dialog(this@CreateGroupActvity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.pop_profile)
            dialog.show()
            val txtGallery = dialog.findViewById<View>(R.id.layoutGallery) as LinearLayout
            val txtCamera = dialog.findViewById<View>(R.id.layoutCamera) as LinearLayout
            txtCamera.setOnClickListener { v: View? ->
                val currentAPIVersion = Build.VERSION.SDK_INT
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(
                            this@CreateGroupActvity,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@CreateGroupActvity,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            REQUEST_CAMERA_PERMISSION
                        )
                    } else {
                        selectCameraImage()
                        dialog.dismiss()
                    }
                } else {
                    selectCameraImage()
                    dialog.dismiss()
                }
            }
            txtGallery.setOnClickListener { v: View? ->
                val currentAPIVersion = Build.VERSION.SDK_INT
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    arrayOf(
                        if (ActivityCompat.checkSelfPermission(
                                this@CreateGroupActvity,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@CreateGroupActvity,
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                2
                            )
                        } else {
                            dialog.dismiss()
                            val intent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            intent.type = "image/*"
//                            intent.type = "*/*";
                            intent.action = Intent.ACTION_PICK
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodeCamera)
                        }
                    )

                } else {
                    dialog.dismiss()
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
//                    intent.type = "*/*";
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodeCamera)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectCameraImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, RequestCodeGallery)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodeCamera && resultCode == AppCompatActivity.RESULT_OK && data != null) {//Gallery
            imageUri = data.data
            startCropActivity(imageUri!!)


        }

        else if (requestCode == RequestCodeGallery && resultCode == AppCompatActivity.RESULT_OK && data != null) { //camera
            val extras: Bundle = data.extras!!
            val imageBitmap = extras["data"] as Bitmap?
            imageUri = Utills.getImageUri(this@CreateGroupActvity, imageBitmap!!)
            Log.d("TAG", "iamgedsfas:: $imageUri")
            startCropActivity(imageUri!!)

        }

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val bitmap: Bitmap?
                    binding.createGroupProfile.setImageBitmap(null)
                    imageUrl = result.originalUri
                    val resultUri = result.uri

                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(
                                this@CreateGroupActvity.contentResolver,
                                resultUri
                            )
                    } else {
                        val source = ImageDecoder.createSource(
                            this@CreateGroupActvity.contentResolver,
                            resultUri
                        )
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    binding.createGroupProfile.setImageBitmap(bitmap)
                    setBody(bitmap!!, "file")

                } catch (e: Exception) {
                    e.printStackTrace()
                    // Utills.showSnackBarFromTop(userImgBarcrwal, "catch-> $e", this@CreateGroupActvity)
                }
            }
        }

        else if (requestCode == ImagePicker.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(ImagePicker.photoFile!!.absolutePath)


                binding.createGroupProfile.setImageBitmap(takenImage)

                // venueViewModel.venueLogo = setBody(takenImage!!, "image")


            }
        }
    }

    private fun setBody(bitmap: Bitmap, flag: String): MultipartBody.Part {
        val filePath = Utills.saveImage(this@CreateGroupActvity, bitmap)
        this.filePath = File(filePath)
        reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), this.filePath!!)

//        if (flag == "store_logo") {
//            activity.binding.iconName.text = this.filePath!!.name
//        }

        body = MultipartBody.Part.createFormData(flag, this.filePath!!.name, reqFile)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("room_id", "image_name_ios"+System.currentTimeMillis())
        if (body != null) {
            //  builder.addPart(editProfileViewModel.profilePic!!)
            builder.addPart(body!!)
        } else {
            builder.addFormDataPart("file", "")
        }
        uploadChatImage(builder.build())


        return body!!
    }


    private fun uploadChatImage(requestBody: MultipartBody) {
        progressDialog.show(this@CreateGroupActvity)
        commonViewModel.uploadChatImg(requestBody).observe(this@CreateGroupActvity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {
                      //  Utills.showDefaultToast( this@CreateGroupActvity,  it.message!!, )
                            imagePathUploded=   it.image_path+"/"+it.data.file
                        Log.d("ok", "imagePathUploded: "+imagePathUploded)
                    }

                }
                Status.LOADING -> {
                    Log.d("ok", "LOADING: ")
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Log.d("ok", "ERROR: ")
                    Utills.showErrorToast( this@CreateGroupActvity,  it.message!!, )
                }
            }
        })



    }

    private fun startCropActivity(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setOutputCompressQuality(100)
            .setAspectRatio(1, 1)
            .start(this@CreateGroupActvity)
    }
    var fsUsersList: java.util.ArrayList<FSUsersModel> = java.util.ArrayList<FSUsersModel>()

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) =
        try {
            runOnUiThread {
                Log.d("ok", "received message createGroup: $response")
                val gson = Gson()
                if (ResponseType.RESPONSE_TYPE_USERS.equalsTo(type)) {
                   /*if (statusCode == 200) {
                        val typeUserList =
                            object : TypeToken<ResponseModel<java.util.ArrayList<FSUsersModel?>?>?>() {}.type
                        val arrayListResponseModel: ResponseModel<java.util.ArrayList<FSUsersModel>> =
                            gson.fromJson<ResponseModel<java.util.ArrayList<FSUsersModel>>>(
                                response,
                                typeUserList
                            )
                        fsUsersList = java.util.ArrayList<FSUsersModel>()
                        for (element in arrayListResponseModel.getData()) {
                            if (element.id != UserDetails.myDetail.id) {
                                fsUsersList.add(element)
                            }
                        }
                        adapter.addAll(fsUsersList)
                    } else {
                        Toast.makeText(this@AllUsersListActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }*/
                } else if (ResponseType.RESPONSE_TYPE_CREATE_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomNewResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomNewResponseModel> = gson.fromJson<ResponseModel<RoomNewResponseModel>>(response, type1)
                        val tmpUserList: HashMap<String, FSUsersModel> = roomResponseModelResponseModel.getData().userListMap
                        for (key in tmpUserList.keys) {
                           // UserDetails.chatUsers[key] = tmpUserList[key]!!
                          //  MyApp.fetchUserDetailChatUsers()[key]=tmpUserList[key]!!
                            var chatArr = MyApp.fetchUserDetailChatUsers()
                            chatArr[key]= tmpUserList[key]!!
                            MyApp.saveUserDetailChatUsers(chatArr)
                            var vv= MyApp.fetchUserDetailChatUsers()
                        }
                        val element: FSRoomModel = roomResponseModelResponseModel.getData().newRoom!!
                      //  if (element.createBy == UserDetails.myDetail.id) {
                       /* if (element.createBy == PreferenceKeeper.instance.myUserDetail.id) {
                            for (userId in element.userList) {
                                if (userId != PreferenceKeeper.instance.myUserDetail.id) {
                                    element.senderUserDetail =  MyApp.fetchUserDetailChatUsers()[userId]
                                    break
                                }
                            }

                            // TODO: in Group details need to add the users details


                            *//*  if(element.isGroup){
                                  val intent = Intent(this, ChatPersonalActvity::class.java)
                                  intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_IS_GROUP,element.isGroup)
                                  intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_ROOM_ID, element.roomId)
                                  intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_GROUP_DETAILS,element.groupDetails)
                                  intent.putExtra(ChatPersonalActvity.INTENT_EXTRAS_KEY_PARTICIPENT_SIZE, element.userList.size.toString())
                                  startActivity(intent)
                                  finish()
                              }else{
                                  val intent = Intent(this, ChatActivity::class.java)
                                  intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_IS_GROUP,element.isGroup)
                                  intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_ROOM_ID, element.roomId)
                                  intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_SENDER_DETAILS,element.senderUserDetail)
                                  startActivity(intent)
                                  finish()
                              }*//*
                        }*/
                        finish()

                        //                    startActivity(new Intent(RoomListActivity.this, RoomListActivity.class));
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_CHECK_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        val type1 =
                            object : TypeToken<ResponseModel<RoomNewResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> =
                            gson.fromJson<ResponseModel<RoomResponseModel>>(response, type1)
                    } else if (statusCode == 404) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("ok", "onWebSocketResponse: $type")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    override val activityName: String = CreateGroupActvity::class.java.name


    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_USERS,
            ResponseType.RESPONSE_TYPE_CREATE_ROOM,
            ResponseType.RESPONSE_TYPE_CHECK_ROOM
        )
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onOptionSelect(option: String) {
        if (option == "camera") {
            selectSourceBottomSheetFragment.dismiss()
            ImagePicker.onCaptureImage(this)
        } else {
            selectSourceBottomSheetFragment.dismiss()
            val intent = Lassi(this)
                .with(LassiOption.GALLERY)
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.IMAGE)
                .setCompressionRation(10)
                .build()
            receiveData.launch(intent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private val receiveData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedMedia = it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
            if (!selectedMedia.isNullOrEmpty()) {
                val bitmap: Bitmap?

                try {
                    binding.createGroupProfile.setImageBitmap(null)
                } catch (e: Exception) {
                    Log.d("ok", ""+e)
                }


                imageUrl = Uri.parse(selectedMedia[0].path)

                println("results>>>>>>>" + Uri.parse(selectedMedia[0].path))
                val resultUri = Uri.parse(selectedMedia[0].path)
                try {
                    bitmap = BitmapFactory.decodeFile(selectedMedia[0].path)
                    binding.createGroupProfile.setImageBitmap(bitmap)
                    setBody(bitmap!!, "file")

                    /* bitmap = if (Build.VERSION.SDK_INT < 28) {
                         MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                     } else {
                         val source = ImageDecoder.createSource(this.contentResolver, resultUri)
                         ImageDecoder.decodeBitmap(source)
                     }*/
/*
                    if (isPremisesImageSelection) {
                        binding.premisesImageAddBtn.visibility = View.GONE
                        binding.premisesLicenseImage.setImageBitmap(bitmap)
                        isPremisesImageSelection = false
                        venueViewModel.premisesImage =
                            setBody(bitmap!!, "premises_license_image")

                    }*/


                    /*  val builder = MultipartBody.Builder()
                 builder.setType(MultipartBody.FORM)
                 builder.addPart(body!!)*/


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }

    }


    companion object {
        const val requestPermissionCode = 101
    }

}