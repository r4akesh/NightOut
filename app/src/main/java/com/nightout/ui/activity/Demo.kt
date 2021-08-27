package com.nightout.ui.activity

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.shape.CornerFamily
import com.nightout.R
import com.nightout.base.BaseActivity

class Demo : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demoactvituy)
       // var img   = findViewById<ImageView>(R.id.img)
       // val radius = resources.getDimension(70)
//        val shapeAppearanceModel = shapeableImageView.shapeAppearanceModel.toBuilder()
//            .setTopRightCorner(CornerFamily.ROUNDED,radius)
//            .build()
//        shapeableImageView.shapeAppearanceModel = shapeAppearanceModel
    }
}