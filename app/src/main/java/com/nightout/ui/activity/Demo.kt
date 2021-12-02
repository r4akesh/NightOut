package com.nightout.ui.activity

import android.media.Image
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.adapter.VenuDemoAdapter
import com.nightout.base.BaseActivity

import com.nightout.model.VenuModel
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log


import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.view.animation.Animation

import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.nightout.databinding.DemoBinding


class Demo : BaseActivity(){
      var listStoreType = ArrayList<VenuModel>()
    lateinit var venuDemoAdapter:VenuDemoAdapter
    lateinit var binding : DemoBinding
    var manager: DownloadManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@Demo, com.nightout.R.layout.demo)
           // setContentView(R.layout.demo)
        var my_button  : Button = findViewById(R.id.my_button)
       // var my_view  : ConstraintLayout = findViewById(R.id.my_view)
        my_button.setOnClickListener {
            //slideUp(binding.myView)
            val snackbar: Snackbar
            snackbar = Snackbar.make(binding.myView, "Message", Snackbar.LENGTH_SHORT)
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(resources.getColor(R.color.white_second))
            val textView: TextView = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(resources.getColor(R.color.primary_clr))
            snackbar.show()

        }


    }



    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(0f,   0f,   view.height.toFloat(),  0f)
        animate.duration = 1000
        animate.fillAfter = true
        view.startAnimation(animate)
    }
}