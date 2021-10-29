package com.nightout.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.TracktraceActvityBinding
import com.nightout.utils.AppConstant
import com.nightout.utils.FileUtils
import androidx.core.app.ActivityCompat.startActivityForResult

import android.os.Environment
import androidx.core.app.ActivityCompat
import java.io.File


class TrackTrace : BaseActivity(),OnMapReadyCallback{
    lateinit var binding: TracktraceActvityBinding
    val CAMERA_REQUEST_CODE_VEDIO =1005
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@TrackTrace,R.layout.tracktrace_actvity)
       initView()
        setToolBar()
    }
    private val REQUEST_CAMERA_PERMISSION = 101
    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.tractraceaLostitem){
            startActivity(Intent(this@TrackTrace,LostitemActivity::class.java))
        }

        if(v==binding.tractraceaPanic){
            val currentAPIVersion = Build.VERSION.SDK_INT
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        this@TrackTrace,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@TrackTrace,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    openCameraVideo()

                }
            } else {
                openCameraVideo()

            }

        }
    }

    private fun openCameraVideo() {
        try {
            val f = File(Environment.getExternalStorageDirectory(), "temp.mp4")
            val takePictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60) //second
            takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE_VEDIO)
            }
        } catch (e: Exception) {
            Log.d("TAG", "onClick: "+e.toString())
        }
    }

    private fun setToolBar() {
        setTouchNClick(  binding.toolbarBack)
         binding.toolbarBack.setOnClickListener { finish() }
        binding.toolbarTitle.setText("Track and Trace")

    }

    private fun initView() {
        binding.tractraceaLostitem.setOnClickListener(this)
        binding.tractraceaPanic.setOnClickListener(this)
        val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.treactraceMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@TrackTrace)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val success = googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(55.3781, 3.4360)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentt: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentt)
        if(requestCode==CAMERA_REQUEST_CODE_VEDIO && resultCode == Activity.RESULT_OK){
            var file = File(Environment.getExternalStorageDirectory().toString())
            for ( tempp in file.listFiles()) {
                if (tempp.name == "temp.mp4") {
                    file = tempp
                    break
                }
            }
           var  videoPath = file.getAbsolutePath()
            Log.d("ok", "videoPath: "+videoPath)
        }
    }
}