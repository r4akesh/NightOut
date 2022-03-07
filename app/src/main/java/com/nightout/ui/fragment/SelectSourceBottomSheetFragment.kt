package com.nightout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


import android.widget.Button
import com.nightout.R
import com.nightout.callbacks.OnSelectOptionListener


class SelectSourceBottomSheetFragment(
    private val onSelectOptionListener: OnSelectOptionListener,
    s: String
) :
    BottomSheetDialogFragment() {
    private lateinit var  cameraImageView:ImageView
    private lateinit var galleryImageView:ImageView
    private lateinit var closeDialog: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.image_picker_bottom_sheet, container, false)
        initView(v)
        init()
        return v
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun initView(view: View) {
        cameraImageView = view.findViewById(R.id.cameraImage)
        galleryImageView = view.findViewById(R.id.galleryImage)
        closeDialog = view.findViewById(R.id.closeDialog)

    }

    private fun init() {
        cameraImageView.setOnClickListener {
            onSelectOptionListener.onOptionSelect("camera")
        }

        galleryImageView.setOnClickListener {
            onSelectOptionListener.onOptionSelect("gallery")
        }

        closeDialog.setOnClickListener {
          dismiss()
        }
    }


}