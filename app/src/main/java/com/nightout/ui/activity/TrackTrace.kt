package com.nightout.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.TracktraceActvityBinding
import com.nightout.utils.AppConstant
import com.nightout.utils.DialogCustmYesNo
import com.nightout.utils.MyApp
import java.io.File
import java.util.*


class TrackTrace : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: TracktraceActvityBinding
    val CAMERA_REQUEST_CODE_VEDIO = 1005
    private val REQUEST_CAMERA_PERMISSION = 101
    var LAUNCH_GOOGLE_ADDRESS = 102
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@TrackTrace, R.layout.tracktrace_actvity)
        initView()
        setToolBar()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.tractraceaSetEndLoc) {
            Places.initialize(
                this@TrackTrace,
                resources.getString(R.string.google_place_picker_key)
            )
            val fieldList =
                Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
                .build(this@TrackTrace)
            startActivityForResult(intent, LAUNCH_GOOGLE_ADDRESS)
        } else if (v == binding.tractraceaLostitem) {
            startActivity(Intent(this@TrackTrace, LostitemActivity::class.java))
        } else if (v == binding.tractraceaPanic) {
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
                    DialogCustmYesNo.getInstance().createDialog(
                        this@TrackTrace,
                        "",
                        "Are you sure you want to record the video ?",
                        object : DialogCustmYesNo.Dialogclick {
                            override fun onYES() {
                                openCameraVideo()
                            }

                            override fun onNO() {

                            }

                        })


                }
            } else {
                DialogCustmYesNo.getInstance().createDialog(
                    this@TrackTrace,
                    "",
                    "Are you sure you want to record the video ?",
                    object : DialogCustmYesNo.Dialogclick {
                        override fun onYES() {
                            openCameraVideo()
                        }

                        override fun onNO() {

                        }

                    })

            }

        }
    }


    private fun openCameraVideo() {
        try {
            val f = File(Environment.getExternalStorageDirectory(), AppConstant.PrefsName.VideoName)
            val takePictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60) // only work for 60 sec
            takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE_VEDIO)
            }
        } catch (e: Exception) {
            MyApp.popErrorMsg("", "Error occurred ", this@TrackTrace)
        }
    }

    private fun setToolBar() {
        setTouchNClick(binding.toolbarBack)
        binding.toolbarBack.setOnClickListener { finish() }
        binding.toolbarTitle.setText("Track and Trace")

    }

    private fun initView() {
        binding.tractraceaLostitem.setOnClickListener(this)
        binding.tractraceaPanic.setOnClickListener(this)
        binding.tractraceaSetEndLoc.setOnClickListener(this)
        val supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.treactraceMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@TrackTrace)
    }

    override fun onMapReady(googleMapp: GoogleMap?) {
        googleMap = googleMapp
      googleMap!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentt: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentt)
        if (requestCode == CAMERA_REQUEST_CODE_VEDIO && resultCode == Activity.RESULT_OK) {
            var file = File(Environment.getExternalStorageDirectory().toString())
            for (tempp in file.listFiles()) {
                if (tempp.name == AppConstant.PrefsName.VideoName) {
                    file = tempp
                    break
                }
            }
            var videoPath = file.getAbsolutePath()
            Log.d("ok", "videoPath: " + videoPath)
        } else if (requestCode == LAUNCH_GOOGLE_ADDRESS && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(intentt!!)
            Log.d("location", "location: " + place.address)

            googleMap!!.clear()
            val latLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
            val yourBitmap = getDrawable(R.drawable.ic_crnt_loc)!!.toBitmap(50, 55)//svg img
            googleMap!!.addMarker(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(yourBitmap))
            )
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15f).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            Handler(Looper.getMainLooper()).postDelayed({
                DialogCustmYesNo.getInstance().createDialogOK(this@TrackTrace,""+place.address,"You have set your End location successfully.",object:DialogCustmYesNo.Dialogclick{
                    override fun onYES() {}
                    override fun onNO() {}
                })
            },1500)




        }
    }
}