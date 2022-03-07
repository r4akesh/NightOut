package com.nightout.ui.activity.LostItem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.github.drjacky.imagepicker.ImagePicker
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.callbacks.OnSelectOptionListener

import com.nightout.databinding.LostitemdetailsActivityBinding
import com.nightout.model.GetLostItemListModel
import com.nightout.model.LostItemDetailCstmModel
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment
import com.nightout.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class LostItemDetailsActvity : BaseActivity(), OnSelectOptionListener {
    lateinit var binding: LostitemdetailsActivityBinding
    private lateinit var selectSourceBottomSheetFragment: SelectSourceBottomSheetFragment
    lateinit var resultUri: Uri
    private var filePath: File? = null
    private lateinit var reqFile: RequestBody
    var body: MultipartBody.Part? = null
    val requestCodeChooseVenuseActivity = 10001
    var itemID="0"
    var RequestCodeCamera = 100
    var RequestCodeGallery = 400
    var imageUriNew : Uri ? = null
    private val REQUEST_CAMERA_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LostItemDetailsActvity, R.layout.lostitemdetails_activity)
        if(intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.ISFROM_EDITITEM,false)){
            var lostModel = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.LOSTITEM_POJO) as GetLostItemListModel.Data
            itemID=lostModel.id
            binding.lostItemName.setText(lostModel.product_name)
            binding.lostUserName.setText(lostModel.customer_name)
            binding.lostEmailID.setText(lostModel.email)
            binding.lostPhNO.setText(PhoneNumberUtils.formatNumber(lostModel.phonenumber,"US"))
            binding.lostProductDetail.setText(lostModel.product_detail)
            Utills.setImageNormal(this@LostItemDetailsActvity,binding.lostItemImg,lostModel.image)
        }else{
            itemID="0"//means newItem
            binding.lostUserName.setText(PreferenceKeeper.instance.loginResponse?.name)
            binding.lostEmailID.setText(PreferenceKeeper.instance.loginResponse?.email)
            binding.lostPhNO.setText(PhoneNumberUtils.formatNumber(PreferenceKeeper.instance.loginResponse?.phonenumber, "US"))

        }
        setToolBar()
        initView()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.lostActvityChooseVenues) {
            if(isValidetedData()) {
                try {
                    var mdl = LostItemDetailCstmModel(
                        itemID,
                        binding.lostItemName.text.toString(),
                        binding.lostUserName.text.toString(),
                        binding.lostEmailID.text.toString(),
                        binding.lostPhNO.text.toString(),
                        binding.lostProductDetail.text.toString(),
                        resultUri.toString()
                    )
                    DataManager.instance.lostItemDetailCstmModel = mdl
                    startActivityForResult(
                        Intent(
                            this@LostItemDetailsActvity,
                            ChooseVenuseActivity::class.java
                        ), requestCodeChooseVenuseActivity
                    )
                } catch (e: Exception) {
                    var mdl = LostItemDetailCstmModel(
                        itemID,
                        binding.lostItemName.text.toString(),
                        binding.lostUserName.text.toString(),
                        binding.lostEmailID.text.toString(),
                        binding.lostPhNO.text.toString(),
                        binding.lostProductDetail.text.toString(),
                        ""
                    )
                    DataManager.instance.lostItemDetailCstmModel = mdl
                    startActivityForResult(
                        Intent(
                            this@LostItemDetailsActvity,
                            ChooseVenuseActivity::class.java
                        ), requestCodeChooseVenuseActivity
                    )
                    //MyApp.popErrorMsg("", "Please add image of item", this@LostItemDetailsActvity)
                }
            }

        } else if (v == binding.lostActvityImageConstrent) {
            onSelectImage()
        }
    }

    private fun isValidetedData(): Boolean {
        if(binding.lostItemName.text.isNullOrBlank()){
            MyApp.popErrorMsg("","Please enter Item Name",THIS!!)
            return false
        }
        else if(binding.lostUserName.text.isNullOrBlank()){
            MyApp.popErrorMsg("","Please enter your Name",THIS!!)
            return false
        } else if(binding.lostEmailID.text.isNullOrBlank()){
            MyApp.popErrorMsg("","Please enter your Email",THIS!!)
            return false
        }else if(binding.lostPhNO.text.isNullOrBlank()){
            MyApp.popErrorMsg("","Please enter your Mobile Number",THIS!!)
            return false
        }else if(binding.lostProductDetail.text.isNullOrBlank()){
            MyApp.popErrorMsg("","Please enter your product detail",THIS!!)
            return false
        }
        return true

    }

    fun onSelectImage() {
        selectSourceBottomSheetFragment = SelectSourceBottomSheetFragment(this, "")
        selectSourceBottomSheetFragment.show(
            this@LostItemDetailsActvity.supportFragmentManager,
            "selectSourceBottomSheetFragment"
        )
    }

    private fun initView() {
        setTouchNClick(binding.lostActvityChooseVenues)
        setTouchNClick(binding.lostActvityImageConstrent)

        //setPhoneFormat
        binding.lostPhNO.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val text: String = binding.lostPhNO.getText().toString()
                val textLength: Int = binding.lostPhNO.getText().toString().length
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    if (!text.contains("(")) {
                        binding.lostPhNO.setText(
                            StringBuilder(text).insert(text.length - 1, "(").toString()
                        )
                        binding.lostPhNO.setSelection(binding.lostPhNO.getText().length)
                    }
                } else if (textLength == 5) {
                    if (!text.contains(")")) {
                        binding.lostPhNO.setText(
                            StringBuilder(text).insert(text.length - 1, ")").toString()
                        )
                        binding.lostPhNO.setSelection(binding.lostPhNO.getText().length)
                    }
                } else if (textLength == 6) {
                    binding.lostPhNO.setText(
                        StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    binding.lostPhNO.setSelection(binding.lostPhNO.getText().length)
                } else if (textLength == 10) {
                    if (!text.contains("-")) {
                        binding.lostPhNO.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.lostPhNO.setSelection(binding.lostPhNO.getText().length)
                    }


                } else if (textLength == 15) {
                    if (text.contains("-")) {
                        binding.lostPhNO.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.lostPhNO.setSelection(binding.lostPhNO.getText().length)
                    }
                } else if (textLength == 18) {
                    if (text.contains("-")) {
                        binding.lostPhNO.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                        binding.lostPhNO.setSelection(binding.lostPhNO.getText().length)
                    }
                }

            }
        })
    }

    private fun setToolBar() {
        setTouchNClick(binding.lostItemToolBar.toolbarBack)
        binding.lostItemToolBar.toolbarBack.setOnClickListener { finish() }
        binding.lostItemToolBar.toolbarTitle.setText("Lost Item Details")

    }

    override fun onOptionSelect(option: String) {
        if (option == "camera") {
          /*  selectSourceBottomSheetFragment.dismiss()
            cameraLauncher.launch(
                ImagePicker.with(this@LostItemDetailsActvity)
                    .cameraOnly().createIntent()
            )*/
            selectSourceBottomSheetFragment.dismiss()
            val currentAPIVersion = Build.VERSION.SDK_INT
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        THIS!!,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        THIS!!,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    selectCameraImage()

                }
            } else {
                selectCameraImage()

            }
        } else {
         /*   selectSourceBottomSheetFragment.dismiss()
            galleryLauncher.launch(
                ImagePicker.with(this@LostItemDetailsActvity)
                    .galleryOnly()
                    .galleryMimeTypes( // no gif images at all
                        mimeTypes = arrayOf(
                            "image/png",
                            "image/jpg",
                            "image/jpeg"
                        )
                    )
                    .createIntent()
            )*/
            selectSourceBottomSheetFragment.dismiss()
            val currentAPIVersion = Build.VERSION.SDK_INT
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                arrayOf(
                    if (ActivityCompat.checkSelfPermission(
                            THIS!!,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            THIS!!,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            2
                        )
                    } else {
                     //   dialog.dismiss()
                        val intent =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        intent.type = "image/*"
//                            intent.type = "*/*";
                        intent.action = Intent.ACTION_PICK
                        THIS!!.startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodeCamera)
                    }
                )

            } else {
               // dialog.dismiss()
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
//                    intent.type = "*/*";
                intent.action = Intent.ACTION_PICK
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodeCamera)
            }
        }
    }
    private fun selectCameraImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, RequestCodeGallery)
        }

    }
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                startCropActivity(uri)
            } else parseError(it)
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                startCropActivity(uri)
            } else parseError(it)
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(
                this@LostItemDetailsActvity,
                ImagePicker.getError(activityResult.data),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Utills.showSnackBarFromTop(
                binding.lostItemdeailRoot,
                "You have cancelled the image upload process.",
                this@LostItemDetailsActvity
            )
        }
    }

    private fun startCropActivity(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setOutputCompressQuality(100)
            .setAspectRatio(1, 1)
            .start(this@LostItemDetailsActvity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this@LostItemDetailsActvity, data)
            if (CropImage.isReadExternalStoragePermissionsRequired(
                    this@LostItemDetailsActvity,
                    imageUri
                )
            ) {
                // mCropImageUri = imageUri
                requestPermissions(
                    listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
                    0
                )
            } else {
                startCropActivity(imageUri)
            }

        }

       else  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                var bitmap: Bitmap?
                binding.lostItemImg.setImageBitmap(null)
                var imageUrl = result.originalUri
                resultUri = result.uri
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            this@LostItemDetailsActvity.contentResolver,
                            resultUri
                        )
                    } else {
                        val source = ImageDecoder.createSource(
                            this@LostItemDetailsActvity.contentResolver,
                            resultUri
                        )
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    try {
                        binding.lostItemImg.setImageBitmap(null)
                        binding.lostItemImg.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.d("crashImage", "onActivityResult: "+e)
                    }

                    setBody(bitmap!!, "profile")


                } catch (e: Exception) {
                    e.printStackTrace()
                    Utills.showSnackBarFromTop(
                        binding.lostItemdeailRoot,
                        "catch-> $e",
                        this@LostItemDetailsActvity
                    )
                }
            }
        }
        else if(requestCode == requestCodeChooseVenuseActivity && resultCode==Activity.RESULT_OK){
            setResult(Activity.RESULT_OK)
            finish()
            overridePendingTransition(0,0)
        }
       else if (requestCode == RequestCodeCamera && resultCode == RESULT_OK && data != null) {//Gallery
            imageUriNew = data.data
            startCropActivity(imageUriNew!!)

            /* Glide.with(this)
                 .asBitmap()
                 .load(imageUri)
                 .centerCrop()
                 .into(binding.profilePic)
             addProfile()*/
        }
        else if (requestCode == RequestCodeGallery && resultCode == RESULT_OK && data != null) { //camera
            val extras: Bundle = data.extras!!
            val imageBitmap = extras["data"] as Bitmap?
            imageUriNew = Utills.getImageUri(THIS!!, imageBitmap!!)
            Log.d("TAG", "iamgedsfas:: $imageUriNew")
            startCropActivity(imageUriNew!!)
            /* val image = imageUri
             Glide.with(this)
                 .asBitmap()
                 .load(image)
                 .centerCrop()
                 .into(binding.profilePic)
             addProfile()*/
        }

    }

    private fun setBody(bitmap: Bitmap, flag: String): MultipartBody.Part {
        val filePath = Utills.saveImage(this@LostItemDetailsActvity, bitmap)
        this.filePath = File(filePath)
        reqFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            this.filePath!!
        )


        body = MultipartBody.Part.createFormData(
            flag,
            this.filePath!!.name,
            reqFile
        )

        return body!!
    }


}