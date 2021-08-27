package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nightout.R
import com.nightout.databinding.ActivityProfileBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.ui.activity.EditProfileActivity
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper


class ProfileFragment() : Fragment() {
    lateinit var binding: ActivityProfileBinding
    private var onMenuOpenListener: OnMenuOpenListener? = null

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_profile, container, false)
        var loginDta = PreferenceKeeper.instance.loginResponse
        binding.loginModel=loginDta
        init()
        return binding.root
    }

    private fun init() {
        binding.menuOpenBtn.setOnClickListener { onMenuOpenListener?.onOpenMenu() }
        binding.editProfileBtn.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }


}