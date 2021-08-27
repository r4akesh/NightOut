package com.nightout.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.ActivityEditProfileBinding
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.vendor.viewmodel.EditProfileViewModel
import com.nightout.vendor.viewmodel.LoginViewModel

class EditProfileActivity : BaseActivity() {
      lateinit var binding: ActivityEditProfileBinding
      lateinit var editProfileViewModel: EditProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.loginModel = PreferenceKeeper.instance.loginResponse
        init()
    }

    private fun init(){
        editProfileViewModel = EditProfileViewModel(this)
        editProfileViewModel.fName = binding.loginModel?.first_name!!
        editProfileViewModel.lName = binding.loginModel?.last_name!!

        binding.editPrfleViewModl = editProfileViewModel
        binding.editProfileHandler = MyApp.getEditProfile(this)

    }
}