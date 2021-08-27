package com.nightout.handlers

import com.nightout.ui.activity.EditProfileActivity
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import com.nightout.vendor.viewmodel.EditProfileViewModel


open class EditProfileHandler(val activity: EditProfileActivity) {
    private lateinit var editProfileViewModel: EditProfileViewModel


    fun onFinishScreen() {
       activity.finish()
    }

    fun openCamera(){
        if (!Utills.checkingPermissionIsEnabledOrNot(activity)) {
          //  Utills.requestMultiplePermission(activity,requestPermissionCode)
        }
    }

    fun saveProfile(editProfileViewModel: EditProfileViewModel){
        this.editProfileViewModel = editProfileViewModel
        MyApp.hideSoftKeyboard(activity)
        if(editProfileViewModel.isValidate(activity)){
            val map = HashMap<String,Any>()
            map["first_name"]=editProfileViewModel.fName
            map["last_name"]=editProfileViewModel.lName
            map["address1"]=editProfileViewModel.addrs1
            map["address2"]=editProfileViewModel.addrs2
            map["profile"]=""
            map["about_me"]=editProfileViewModel.aboutMe
            map["location"]=""//no need
            saveProfileAPICall()
        }
    }

    private fun saveProfileAPICall() {

    }


}