package com.nightout.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.github.drjacky.imagepicker.ImagePicker
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.callbacks.OnSelectOptionListener

import com.nightout.databinding.LostitemdetailsActivityBinding
import com.nightout.model.LostItemDetailCstmModel
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment
import com.nightout.utils.DataManager
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@LostItemDetailsActvity,
            R.layout.lostitemdetails_activity
        )
        setToolBar()
        initView()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.lostActvityChooseVenues) {
            try {
                var mdl = LostItemDetailCstmModel(
                    binding.lostItemName.text.toString(),
                    binding.lostUserName.text.toString(),
                    binding.lostEmailID.text.toString(),
                    binding.lostPhNO.text.toString(),
                    binding.lostProductDetail.text.toString(),
                    resultUri.toString()
                )
                DataManager.instance.lostItemDetailCstmModel = mdl
                startActivityForResult(Intent(this@LostItemDetailsActvity, ChooseVenuseActivity::class.java),requestCodeChooseVenuseActivity)
            } catch (e: Exception) {
                MyApp.popErrorMsg("", "Please add image of item", this@LostItemDetailsActvity)
            }


        } else if (v == binding.lostActvityImageConstrent) {
            onSelectImage()
        }
    }

    fun onSelectImage() {
        selectSourceBottomSheetFragment = SelectSourceBottomSheetFragment(this)
        selectSourceBottomSheetFragment.show(
            this@LostItemDetailsActvity.supportFragmentManager,
            "selectSourceBottomSheetFragment"
        )
    }

    private fun initView() {
        setTouchNClick(binding.lostActvityChooseVenues)
        setTouchNClick(binding.lostActvityImageConstrent)
    }

    private fun setToolBar() {
        setTouchNClick(binding.lostItemToolBar.toolbarBack)
        binding.lostItemToolBar.toolbarBack.setOnClickListener { finish() }
        binding.lostItemToolBar.toolbarTitle.setText("Lost Item Details")

    }

    override fun onOptionSelect(option: String) {
        if (option == "camera") {
            selectSourceBottomSheetFragment.dismiss()
            cameraLauncher.launch(
                ImagePicker.with(this@LostItemDetailsActvity)
                    .cameraOnly().createIntent()
            )
        } else {
            selectSourceBottomSheetFragment.dismiss()
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
            )
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
                val bitmap: Bitmap?
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
                    binding.lostItemImg.setImageBitmap(bitmap)
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
            finish()
            overridePendingTransition(0,0)
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