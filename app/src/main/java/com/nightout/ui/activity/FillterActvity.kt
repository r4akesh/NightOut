package com.nightout.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.ChipGroup
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.FillterActvityBinding
import kotlinx.android.synthetic.main.fillter_actvity.*


class FillterActvity : BaseActivity() {
    lateinit var binding: FillterActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@FillterActvity, R.layout.fillter_actvity)
        binding.filterGroup.setOnCheckedChangeListener(ChipGroup.OnCheckedChangeListener { chipGroup, i ->
            Log.i("TAG", i.toString() + "")
        })
    }
}