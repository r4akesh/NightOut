package com.nightout.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.ContactListAdapter
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.CreategrupActivityBinding
import com.nightout.model.AllUserRes
import com.nightout.model.ContactFillterModel
import com.nightout.model.ContactNoModel
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.DialogCustmYesNo
import com.nightout.utils.Utills
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
import org.json.JSONObject
import java.io.File

class CreateGroupActvity : BaseActivity() {
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
                onSelectImage()
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

    private fun initView() {
         setTouchNClick(binding.createGroupProfileRel)
         setTouchNClick(binding.crateGroupSelectAll)
        contactFillterViewModel = CommonViewModel(this@CreateGroupActvity)
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


        }else if (requestCode == RequestCodeGallery && resultCode == AppCompatActivity.RESULT_OK && data != null) { //camera
            val extras: Bundle = data.extras!!
            val imageBitmap = extras["data"] as Bitmap?
            imageUri = Utills.getImageUri(this@CreateGroupActvity, imageBitmap!!)
            Log.d("TAG", "iamgedsfas:: $imageUri")
            startCropActivity(imageUri!!)

        }
        else  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                try {  val bitmap: Bitmap?
                    binding.createGroupProfile.setImageBitmap(null)
                    imageUrl = result.originalUri
                    val resultUri = result.uri

                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(this@CreateGroupActvity.contentResolver, resultUri)
                    } else {
                        val source = ImageDecoder.createSource(this@CreateGroupActvity.contentResolver, resultUri)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    binding.createGroupProfile.setImageBitmap(bitmap)
                    setBody(bitmap!!, "image")

                } catch (e: Exception) {
                    e.printStackTrace()
                    // Utills.showSnackBarFromTop(userImgBarcrwal, "catch-> $e", this@CreateGroupActvity)
                }
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

        body = MultipartBody.Part.createFormData(
            flag,
            this.filePath!!.name,
            reqFile
        )

        return body!!
    }

    private fun startCropActivity(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setOutputCompressQuality(100)
            .setAspectRatio(1, 1)
            .start(this@CreateGroupActvity)
    }
}