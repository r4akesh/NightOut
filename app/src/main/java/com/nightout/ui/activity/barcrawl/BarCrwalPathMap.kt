package com.nightout.ui.activity.barcrawl

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrwalmappathActvityBinding
import com.nightout.model.AllBarCrwalListResponse
import com.nightout.ui.activity.SharedMemeberActvity
import com.nightout.utils.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*
import com.google.android.gms.maps.model.LatLng


import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdate
import com.nightout.model.BarCrwlListModel
import com.nightout.model.BarcrwalSavedRes
import com.nightout.model.LoginModel
import com.nightout.ui.activity.ContactListNewActvity
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.StringBuilder
import kotlin.collections.ArrayList


class BarCrwalPathMap : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: BarcrwalmappathActvityBinding
    lateinit var googleMap: GoogleMap
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private val progressDialog = CustomProgressDialog()
    var listBarcrwal = ArrayList<AllBarCrwalListResponse.Data>()
    private lateinit var commonViewModel: CommonViewModel

    var mMarkerPoints: ArrayList<LatLng>? = null
    var indexOfList = 0
    var sizeOfList = 0
    private var bounds: LatLngBounds? = null
    private var builder: LatLngBounds.Builder? = null
    var barcrwalId : String= ""
    var isFromShareListActivity=false
    var isFromSaveListActivity=false
    var publicPrivetValue="1"
    var REQCODE_CreateBarCrwlSuccess = 999
    lateinit  var venusSelectedOld : BarcrwalSavedRes.Data


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrwalPathMap, R.layout.barcrwalmappath_actvity)
        barcrwalId = try {
            intent.getStringExtra(AppConstant.INTENT_EXTRAS.BarcrwalID)!!
        } catch (e: Exception) {
            ""
        }
        if(barcrwalId.isNotBlank()) {
            Log.d("TAG", "NotBlank() ")
        }else{
            Log.d("TAG", "YesBlank()  ")
        }


        listBarcrwal = ArrayList ()
        listBarcrwal = intent.getSerializableExtra(AppConstant.PrefsName.SelectedBarcrwalList) as ArrayList<AllBarCrwalListResponse.Data>
        sizeOfList = listBarcrwal.size
        initView()
        setToolBar()
    }

    private fun setToolBar() {
        binding.BarCrawlMapPathToolBar.toolbarTitle.setText("Create Bar Crawl")
        setTouchNClick(binding.BarCrawlMapPathToolBar.toolbarBack)
        binding.BarCrawlMapPathToolBar.toolbarBack.setOnClickListener { finish() }
        binding.BarCrawlMapPathToolBar.toolbar3dot.visibility = View.GONE
        binding.BarCrawlMapPathToolBar.toolbarBell.visibility = View.GONE
        binding.BarCrawlMapPathToolBar.toolbarCreateGrop.visibility = View.GONE
    }

    private fun initView() {
        binding.barcrwalMapPathBtn.setOnClickListener(this@BarCrwalPathMap)
        val supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.barCrawlPathMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this@BarCrwalPathMap)
        mMarkerPoints = ArrayList()
        commonViewModel = CommonViewModel(this@BarCrwalPathMap)
        isFromShareListActivity = intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.ISFROM_ShareListActivity,false)
        isFromSaveListActivity = intent.getBooleanExtra(AppConstant.INTENT_EXTRAS.ISFROM_SAVEDLIST_Activity,false)
        if(isFromSaveListActivity){
            venusSelectedOld= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.SAVEDLIST_Model) as BarcrwalSavedRes.Data

            binding.barcrwalMapPathBtn.text= resources.getString(R.string.Update)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.barcrwalMapPathBtn) {
            showDialog()
        }
    }

    lateinit var   userImgBarcrwal:ImageView
    lateinit var   dgSpin:Spinner
    private var imageUrl: Uri? = null
    private var filePath: File? = null
    private lateinit var reqFile: RequestBody
    var body: MultipartBody.Part? = null
    lateinit var dgDateBtn: TextView
    lateinit var dgEtBarCrwal: EditText
    private fun showDialog() {
        val adDialog = Dialog(this@BarCrwalPathMap, R.style.MyDialogThemeBlack)
        adDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        adDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        adDialog.setContentView(R.layout.dialog_createbarcrwal)
        adDialog.setCancelable(false)

        dgDateBtn = adDialog.findViewById(R.id.dgDateBtn)
        dgEtBarCrwal = adDialog.findViewById(R.id.dgEtBarCrwal)
        val dgCloseBtn: ImageView = adDialog.findViewById(R.id.dgCloseBtn)
        val dgDoneBtn: Button = adDialog.findViewById(R.id.dgDoneBtn)
        val userImgBarcrwalRel: RelativeLayout = adDialog.findViewById(R.id.userImgBarcrwalRel)
         dgSpin = adDialog.findViewById(R.id.dgSpin)
          userImgBarcrwal = adDialog.findViewById(R.id.userImgBarcrwal)
                setSpin()
        if(isFromSaveListActivity){
            dgEtBarCrwal.setText(venusSelectedOld.name)
            var dateMills=venusSelectedOld.date
            dgDateBtn.setText(Commons.millsToDateStr2(Commons.strToLong(dateMills)))
            //(1=>Public, 2=>Private)
            if(venusSelectedOld.public_private.equals("2")){
                dgSpin.setSelection(1)
            }
        }
        setTouchNClick(userImgBarcrwalRel)
        setTouchNClick(dgCloseBtn)
        setTouchNClick(dgDateBtn)
        setTouchNClick(dgDoneBtn)
        userImgBarcrwalRel.setOnClickListener {
            onSelectImage()
        }
        dgDateBtn.setOnClickListener {
            showDatePicker()
        }
        dgDoneBtn.setOnClickListener {
            if(isValidate()) {
                adDialog.dismiss()
                create_update_bar_crawlAPICall()
                //    startActivity(Intent(this@BarCrwalPathMap, SharedMemeberActvity::class.java))
            }
        }

        dgCloseBtn.setOnClickListener {
            adDialog.dismiss()
        }
        adDialog.show()
    }

    private fun isValidate(): Boolean {
        if(dgEtBarCrwal.text.isNullOrEmpty()){
            MyApp.popErrorMsg("","Please enter Bar Crawl name",THIS!!)
            return false
        }
        else if(dgDateBtn.text.equals(resources.getString(R.string.Select_Date))){
            MyApp.popErrorMsg("","Please choose date",THIS!!)
            return false
        }
        return true

    }

    private fun create_update_bar_crawlAPICall() {
        var stringBuilder = StringBuilder()
        for (i in 0 until listBarcrwal.size){
            stringBuilder.append(listBarcrwal[i].id)
            stringBuilder.append(",")

        }
        var listOfId =stringBuilder.toString().substring(0,stringBuilder.toString().length-1)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("name", dgEtBarCrwal.text.toString())
        builder.addFormDataPart("venue_list",""+listOfId )
        builder.addFormDataPart("public_private", publicPrivetValue)
        builder.addFormDataPart("city_lattitude", "3323332344")//dummy
        builder.addFormDataPart("city_longitude", "3232323232")
        builder.addFormDataPart("city", intent.getStringExtra(AppConstant.INTENT_EXTRAS.CITYNAME).toString())

        if(isFromShareListActivity){
            builder.addFormDataPart("saved_shared", "2")
        }else{
            builder.addFormDataPart("saved_shared", "1")//
        }
         builder.addFormDataPart("date", ""+Commons.strToTimemills3(dgDateBtn.text.toString()))
        if(barcrwalId.isNotBlank()) {
            builder.addFormDataPart("id", barcrwalId)
        }

        //builder.addFormDataPart("id", editProfileViewModel.addrs2) send durring editing

        // if (editProfileViewModel.profilePic != null) {
        if (body != null) {
            //  builder.addPart(editProfileViewModel.profilePic!!)
            builder.addPart(body!!)
        } else {
            builder.addFormDataPart("image", "")
        }


         createBarCrewal(builder.build())
    }


    private fun createBarCrewal(requestBody: MultipartBody) {
        progressDialog.show(this@BarCrwalPathMap)
        commonViewModel.createBarCrwalWidImg(requestBody).observe(this@BarCrwalPathMap, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {
                        if(isFromShareListActivity || isFromSaveListActivity){
                            Utills.showSuccessToast(THIS!!,resources.getString(R.string.barcrwal_update_suceesfully))
                            PreferenceKeeper.instance.isUpdatedBarcrwalSuccesfully=true
                            setResult(Activity.RESULT_OK)
                            finish()
                        }else {

                            Log.d("ok", "success: ")
                            var vv = it.data.id
                            DialogCustmYesNo.getInstance().createDialog(
                                this@BarCrwalPathMap,
                                "Bar crawl created Successfully.",
                                "Do you want share the created bar crawl with Friends?",
                                object : DialogCustmYesNo.Dialogclick {
                                    override fun onYES() {
                                        startActivityForResult(
                                            Intent(this@BarCrwalPathMap, ContactListNewActvity::class.java)
                                                .putExtra(AppConstant.PrefsName.ISFROM_BarCrwalPathMapActvity, true)
                                                .putExtra(AppConstant.INTENT_EXTRAS.BarcrwalID, it.data.id),REQCODE_CreateBarCrwlSuccess)

                                    }

                                    override fun onNO() {
                                        //  var intentt = Intent()
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }

                                })


                        }

                    }

                }
                Status.LOADING -> {
                    Log.d("ok", "LOADING: ")
                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    Log.d("ok", "ERROR: ")
                    Utills.showErrorToast( this@BarCrwalPathMap,  it.message!!, )
                }
            }
        })
    }


    private fun setSpin() {

                var listSpin = ArrayList<String>()
        listSpin.add("Public")
        listSpin.add("Private")

            val aa = ArrayAdapter(this, R.layout.simple_spinner_custm_item, listSpin)
            aa.setDropDownViewResource(R.layout.simple_spin_dropdownitem_contactus)
        dgSpin.adapter = aa

        dgSpin.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(position==0){
                        publicPrivetValue="1"
                    }else{
                        publicPrivetValue="2"
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }

    private val REQUEST_CAMERA_PERMISSION = 1
    var imageUri: Uri? = null
    var RequestCodeCamera = 100
    var RequestCodeGallery = 200

    fun onSelectImage() {
        try {
            val dialog = Dialog(this@BarCrwalPathMap)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.pop_profile)
            dialog.show()
            val txtGallery = dialog.findViewById<View>(R.id.layoutGallery) as LinearLayout
            val txtCamera = dialog.findViewById<View>(R.id.layoutCamera) as LinearLayout
            txtCamera.setOnClickListener { v: View? ->
                val currentAPIVersion = Build.VERSION.SDK_INT
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(
                            this@BarCrwalPathMap,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@BarCrwalPathMap,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            REQUEST_CAMERA_PERMISSION
                        )
                    } else {
                        selectCameraImage()
                        dialog.dismiss()
                    }
                } else {
                    selectCameraImage()
                    dialog.dismiss()
                }
            }
            txtGallery.setOnClickListener { v: View? ->
                val currentAPIVersion = Build.VERSION.SDK_INT
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    arrayOf(
                        if (ActivityCompat.checkSelfPermission(
                                this@BarCrwalPathMap,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@BarCrwalPathMap,
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                2
                            )
                        } else {
                            dialog.dismiss()
                            val intent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            intent.type = "image/*"
//                            intent.type = "*/*";
                            intent.action = Intent.ACTION_PICK
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodeCamera)
                        }
                    )

                } else {
                    dialog.dismiss()
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
//                    intent.type = "*/*";
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodeCamera)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectCameraImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, RequestCodeGallery)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodeCamera && resultCode == AppCompatActivity.RESULT_OK && data != null) {//Gallery
            imageUri = data.data
            startCropActivity(imageUri!!)


        }else if (requestCode == RequestCodeGallery && resultCode == AppCompatActivity.RESULT_OK && data != null) { //camera
            val extras: Bundle = data.extras!!
            val imageBitmap = extras["data"] as Bitmap?
            imageUri = Utills.getImageUri(this@BarCrwalPathMap, imageBitmap!!)
            Log.d("TAG", "iamgedsfas:: $imageUri")
            startCropActivity(imageUri!!)

        }
        else  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                try {  val bitmap: Bitmap?
                userImgBarcrwal.setImageBitmap(null)
                imageUrl = result.originalUri
                val resultUri = result.uri

                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(this@BarCrwalPathMap.contentResolver, resultUri)
                    } else {
                        val source = ImageDecoder.createSource(this@BarCrwalPathMap.contentResolver, resultUri)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    userImgBarcrwal.setImageBitmap(bitmap)
                    setBody(bitmap!!, "image")

                } catch (e: Exception) {
                    e.printStackTrace()
                   // Utills.showSnackBarFromTop(userImgBarcrwal, "catch-> $e", this@BarCrwalPathMap)
                }
            }
        }
        else if(requestCode == REQCODE_CreateBarCrwlSuccess && resultCode== Activity.RESULT_OK){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun setBody(bitmap: Bitmap, flag: String): MultipartBody.Part {
        val filePath = Utills.saveImage(this@BarCrwalPathMap, bitmap)
        this.filePath = File(filePath)
        reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), this.filePath!!)

//        if (flag == "store_logo") {
//            activity.binding.iconName.text = this.filePath!!.name
//        }

        body = MultipartBody.Part.createFormData(
            flag,
            this.filePath!!.name,
            reqFile
        )

        return body!!
    }

    private fun startCropActivity(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setOutputCompressQuality(100)
            .setAspectRatio(1, 1)
            .start(this@BarCrwalPathMap)
    }
    private fun showDatePicker() {
        // Get Current Date
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth -> dgDateBtn.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
        datePickerDialog.show()
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        p0!!.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))//set night mode
        addMarkers()
        drawPath()

    }



    private fun drawPath() {
        val url = mapsApiDirectionsUrl()
        if (!url.isNullOrBlank()) {
            readTask(url)
        // googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lang), 13f))
        } else {
            MyApp.popErrorMsg("", "URL blank !!", THIS!!)
        }
    }

    private fun addMarkers() {
        if (googleMap != null) {
            builder = LatLngBounds.Builder()
            for (i in 0 until listBarcrwal.size) {
                var lat = Commons.strToDouble(listBarcrwal[i].store_lattitude)
                var lang = Commons.strToDouble(listBarcrwal[i].store_longitude)
                drawMarker(LatLng(lat, lang), "$i")
               // googleMap!!.addMarker(MarkerOptions().position(LatLng(lat, lang)).title("" + i + "st Point"))
            }
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.30).toInt() // offset from edges of the map 10% of screen
            bounds = builder!!.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height, padding)
            googleMap.animateCamera(cu)
        }

    }
    private fun drawMarker(point: LatLng, text: String) {
        var value: Int = text.toInt()+65
        val c = value.toChar()
        val markerOptions = MarkerOptions()
        markerOptions.position(point).title("Point $c").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ic))
        googleMap.addMarker(markerOptions)
        builder?.include(markerOptions.position)
    }

    private fun mapsApiDirectionsUrl(): String {
        val origin =
            "origin=" + listBarcrwal[indexOfList].store_lattitude + "," + listBarcrwal[indexOfList].store_longitude
        val dest =
            "destination=" + listBarcrwal[indexOfList + 1].store_lattitude + "," + listBarcrwal[indexOfList + 1].store_longitude
        val sensor = "sensor=false"
        val key = "key=" + getString(R.string.google_maps_key)
        val parameters = "$origin&$dest&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$parameters"
    }


    fun readTask(url: String) {
        GlobalScope.launch {
            val http = HttpConnection()
            var data = http.readUrl(url)
            GlobalScope.launch {
                val jObject: JSONObject
                var routes: List<List<HashMap<String, String>>>? = null
                try {
                    jObject = JSONObject(data)
                    val parser = PathJSONParser()
                    routes = parser.parse(jObject)

                    //onPostExecute
                    var points: ArrayList<LatLng?>? = null
                    var polyLineOptions: PolylineOptions? = null
                    // traversing through routes
                    for (i in routes!!.indices) {
                        points = ArrayList()
                        polyLineOptions = PolylineOptions()
                        val path = routes[i]
                        for (j in path.indices) {

                            val point = path[j]
                            val lat = point["lat"]!!.toDouble()
                            val lng = point["lng"]!!.toDouble()
                            val position = LatLng(lat, lng)
                            points.add(position)
                        }
                        polyLineOptions.addAll(points)
                        polyLineOptions.width(5f)
                        polyLineOptions.color(resources.getColor(R.color.text_yello))

                    }
                    runOnUiThread {
                        if (polyLineOptions != null) {
                            googleMap!!.addPolyline(polyLineOptions)
                            if (indexOfList < listBarcrwal.size - 2) {
                                indexOfList++
                                drawPath()
                            }
                        } else{
                            MyApp.popErrorMsg("","Lat-Long not found!!",THIS!!)
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}

