package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nightout.BuildConfig
import com.nightout.R
import com.nightout.databinding.ActivityProfileBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.ui.activity.EditProfileActivity
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills


class ProfileFragment() : Fragment() {
    lateinit var binding: ActivityProfileBinding
    private var onMenuOpenListener: OnMenuOpenListener? = null
    var TAG="TAG"

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_profile, container, false)
        Log.d(TAG, "onCreateView: ")
        return binding.root
    }



    override fun onResume() {
        super.onResume()
        var loginDta = PreferenceKeeper.instance.loginResponse
        binding.loginModel=loginDta
        var pthh =PreferenceKeeper.instance.imgPathSave+binding.loginModel?.profile
        Log.d(TAG, "onResume: "+pthh)
        Utills.setImage(requireActivity(), binding.userProfile, binding.loginModel?.profile)
        init()
        Log.d(TAG, "onResume: ")
    }
    private fun init() {
        binding.menuOpenBtn.setOnClickListener { onMenuOpenListener?.onOpenMenu() }
        binding.editProfileBtn.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }

}