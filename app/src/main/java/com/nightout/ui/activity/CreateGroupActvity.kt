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
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.CreategrupActivityBinding
import com.nightout.model.AllUserRes
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    private var customProgressDialog = CustomProgressDialog()
    lateinit var getALLUserViewModel : CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.creategrup_activity)
        binding = DataBindingUtil.setContentView(this@CreateGroupActvity, R.layout.creategrup_activity)
        initView()
        setToolbar()
        getAllNightOutUser()

    }

    private fun getAllNightOutUser() {
        customProgressDialog.show(this@CreateGroupActvity, "")
        getALLUserViewModel.getAllUserList().observe(this@CreateGroupActvity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        dataList = ArrayList()
                        dataList = myData.data
                       setUserList()
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    // Utills.showSnackBarOnError(binding.lostConstrentToolbar, it.message!!, this@LostitemActivity)
                    it.message?.let { it1 -> Utills.showDefaultToast(this@CreateGroupActvity, it1) }
                }
            }
        })
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
        getALLUserViewModel = CommonViewModel(this@CreateGroupActvity)
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