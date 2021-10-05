package com.nightout.handlers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.nightout.R
import com.nightout.callbacks.OnSelectOptionListener
import com.nightout.model.LoginModel
import com.nightout.ui.activity.EditProfileActivity
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.EditProfileViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.*


open class EditProfileHandler(val activity: EditProfileActivity) : OnSelectOptionListener {
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var selectSourceBottomSheetFragment: SelectSourceBottomSheetFragment
    private var imageUrl: Uri? = null
    private var filePath: File? = null
    private lateinit var reqFile: RequestBody
    var body: MultipartBody.Part? = null
    private val progressDialog = CustomProgressDialog()
    var LAUNCH_GOOGLE_ADDRESS = 1005
    var LAUNCH_GOOGLE_ADDRESS2 = 1006

//    init {
//        editProfileViewModel = EditProfileViewModel(activity)
//    }

    fun onFinishScreen() {
        activity.finish()
    }


    fun onSelectImage() {
        selectSourceBottomSheetFragment = SelectSourceBottomSheetFragment(this)
        selectSourceBottomSheetFragment.show(
            activity.supportFragmentManager,
            "selectSourceBottomSheetFragment"
        )
    }

    fun saveProfile(editProfileViewModel: EditProfileViewModel) {
        this.editProfileViewModel = editProfileViewModel
        MyApp.hideSoftKeyboard(activity)
        if (editProfileViewModel.isValidate(activity)) {
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("first_name", editProfileViewModel.fName)
            builder.addFormDataPart("last_name", editProfileViewModel.lName)
            builder.addFormDataPart("address1", editProfileViewModel.addrs1)
            builder.addFormDataPart("address2", editProfileViewModel.addrs2)
            builder.addFormDataPart("about_me", editProfileViewModel.aboutMe)
            builder.addFormDataPart("city", city)
            // if (editProfileViewModel.profilePic != null) {
            if (body != null) {
                //  builder.addPart(editProfileViewModel.profilePic!!)
                builder.addPart(body!!)
            } else {
                builder.addFormDataPart("profile", "")
            }

            saveProfileAPICall(builder.build())
        }
    }

    fun openLoactionActvity(editProfileViewModel: EditProfileViewModel) {
        Places.initialize(activity, activity.resources.getString(R.string.google_place_picker_key))
        val fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
            .build(activity)
        activity.startActivityForResult(intent, LAUNCH_GOOGLE_ADDRESS)

    }

    fun openLoactionActvity2(editProfileViewModel: EditProfileViewModel) {
        Places.initialize(activity, activity.resources.getString(R.string.google_place_picker_key))
        val fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
            .build(activity)
        activity.startActivityForResult(intent, LAUNCH_GOOGLE_ADDRESS2)

    }


    override fun onOptionSelect(option: String) {
        if (option == "camera") {
            selectSourceBottomSheetFragment.dismiss()
            cameraLauncher.launch(
                ImagePicker.with(activity)
                    .cameraOnly().createIntent()
            )
        } else {
            selectSourceBottomSheetFragment.dismiss()
            galleryLauncher.launch(
                ImagePicker.with(activity)
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
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                startCropActivity(uri)
            } else parseError(it)
        }

    private val galleryLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                startCropActivity(uri)
            } else parseError(it)
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Utills.showSnackBarFromTop(
                activity.binding.etFName,
                "You have cancelled the image upload process.",
                activity
            )
        }
    }

    private fun startCropActivity(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setAspectRatio(1, 1)
            .start(activity)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(activity, data)
            if (CropImage.isReadExternalStoragePermissionsRequired(activity, imageUri)) {
                // mCropImageUri = imageUri
                activity.requestPermissions(
                    listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
                    0
                )
            } else {
                startCropActivity(imageUri)
            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val bitmap: Bitmap?
                activity.binding.userProfile.setImageBitmap(null)
                imageUrl = result.originalUri
                val resultUri = result.uri
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(activity.contentResolver, resultUri)
                    } else {
                        val source = ImageDecoder.createSource(activity.contentResolver, resultUri)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    activity.binding.userProfile.setImageBitmap(bitmap)
                    setBody(bitmap!!, "profile")
                    //  editProfileViewModel.profilePic = setBody(bitmap!!, "profile")
                    //  Log.d("TAG", "onActivityResult: "+editProfileViewModel.profilePic)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Utills.showSnackBarFromTop(activity.binding.etFName, "catch-> $e", activity)
                }
            }
        }

        if (requestCode == LAUNCH_GOOGLE_ADDRESS && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            activity.binding.editProfileLocation.text = place.address
         //   activity.binding.editProfileLocation2.setText(place.address)
            getAddrsFrmLatlang(place.latLng!!.latitude,place.latLng!!.longitude)
        }
        if (requestCode == LAUNCH_GOOGLE_ADDRESS2 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            activity.binding.editProfileLocation2.text = place.address
           // getAddrsFrmLatlang(place.latLng!!.latitude,place.latLng!!.longitude)
        }
    }

    var geocoder: Geocoder? = null
    var city = ""
    var addresses: List<Address>? = null
    private fun getAddrsFrmLatlang(latitude: Double, longitude: Double) {
        geocoder = Geocoder(activity, Locale.getDefault())
        try {
          //  latitudeGlobal = "" + latitude
          //  longitudeGlobal = "" + longitude
            addresses = geocoder!!.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
          //  val addrs = addresses?.get(0)?.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
              city = addresses?.get(0)?.getLocality()!!

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setBody(bitmap: Bitmap, flag: String): MultipartBody.Part {
        val filePath = Utills.saveImage(activity, bitmap)
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

    private fun saveProfileAPICall(requestBody: MultipartBody) {
        progressDialog.show(activity)
        editProfileViewModel.updateProfile(requestBody).observe(activity, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {
                        var logModel: LoginModel.Data = it.data
                        PreferenceKeeper.instance.loginResponse = logModel
                        Log.d("ok", "SUCCESS: ")
                        activity.finish()
                    }

                }
                Status.LOADING -> {
                    Log.d("ok", "LOADING: ")
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Log.d("ok", "ERROR: ")
                    Utills.showSnackBarOnError(activity.binding.etFName, it.message!!, activity)
                }
            }
        })
    }
}