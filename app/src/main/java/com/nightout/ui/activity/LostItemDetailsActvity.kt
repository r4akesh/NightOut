package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.github.drjacky.imagepicker.ImagePicker
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.callbacks.OnSelectOptionListener

import com.nightout.databinding.LostitemdetailsActivityBinding
import com.nightout.ui.fragment.SelectSourceBottomSheetFragment

class LostItemDetailsActvity : BaseActivity(), OnSelectOptionListener {
    lateinit var binding : LostitemdetailsActivityBinding
    private lateinit var selectSourceBottomSheetFragment: SelectSourceBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this@LostItemDetailsActvity,R.layout.lostitemdetails_activity)
        setToolBar()
        initView()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.lostActvityChooseVenues){
            startActivity(Intent(this@LostItemDetailsActvity,ChooseVenuseActivity::class.java))
        }
        else if(v==binding.lostActvityImageConstrent){
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
                ImagePicker.with( this@LostItemDetailsActvity)
                    .cameraOnly().createIntent()
            )
        } else {
            selectSourceBottomSheetFragment.dismiss()
            galleryLauncher.launch(
                ImagePicker.with( this@LostItemDetailsActvity)
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
}