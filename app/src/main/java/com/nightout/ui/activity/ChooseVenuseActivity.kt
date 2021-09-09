package com.nightout.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.*
import com.nightout.base.BaseActivity
import com.nightout.databinding.ChossesvenuesActvityBinding
import com.nightout.model.DashboardModel
import com.nightout.model.VenuListModel
import com.nightout.model.VenuModel
import com.nightout.model.VenuesModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.VenuListViewModel
import com.nightout.viewmodel.HomeViewModel
import com.nightout.viewmodel.LostItemSubmitViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.StringBuilder

class ChooseVenuseActivity : BaseActivity () {
    lateinit var binding : ChossesvenuesActvityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var homeViewModel: HomeViewModel
    lateinit var dashList: DashboardModel.Data
    var strID:StringBuilder= StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ChooseVenuseActivity,R.layout.chossesvenues_actvity)
        initView()

        dashboardAPICALL()
        setToolBar()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.chooseVenuesSend){
            if(isValidateData())
            collectData()
        }
    }

    private fun isValidateData(): Boolean {
        var flag:Boolean = false
          strID = StringBuilder()
        for (i in 0 until dashList.all_records.size ){
            var subList = dashList.all_records[i]
            for (j in 0 until subList.sub_records.size){
                if(subList.sub_records[j].isChked){
                    flag=true
                    strID.append(subList.sub_records[j].id+",")
                }
            }
        }
        if(!flag){
            MyApp.popErrorMsg("","Please Select Venues !!",THIS!!)
            return false
        }

        return true
    }

    private fun collectData() {
        var mdl = DataManager.instance.lostItemDetailCstmModel
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("venue_id", strID.substring(0,strID.length-1))
        builder.addFormDataPart("product_name", mdl.itemName)
        builder.addFormDataPart("customer_name",mdl.fullName  )
        builder.addFormDataPart("email",  mdl.emailID)
        builder.addFormDataPart("phonenumber", mdl.phno)
        builder.addFormDataPart("product_detail",mdl.pDetail )
        var resultUri= Uri.parse(mdl.imgPathUri)
        val  bitmap: Bitmap?
        if (Build.VERSION.SDK_INT < 28) {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
        } else {
            val source = ImageDecoder.createSource(contentResolver, resultUri)
            bitmap = ImageDecoder.decodeBitmap(source)
        }       
        builder.addPart(setBody(bitmap!!, "image"))
        addLostItemApiCall(builder.build())
        
    }

    lateinit var lostItemSubmitViewModel:LostItemSubmitViewModel
    private fun addLostItemApiCall(build: MultipartBody) {
        customProgressDialog.show(this@ChooseVenuseActivity)
        lostItemSubmitViewModel.submitLostItem(build).observe(this@ChooseVenuseActivity,{
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    if(it.data?.status_code==200) {
                        MyApp.ShowTost(THIS!!, "" + it.data?.status_code)
                    }
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                Status.LOADING -> {
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    customProgressDialog.dialog.dismiss()
                    try {
                        Utills.showSnackBarOnError(
                            binding.chooseVenuesToolbarConstrent,
                            it.message!!,
                            this@ChooseVenuseActivity
                        )
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    private var filePath: File? = null
    private lateinit var reqFile: RequestBody
    var body: MultipartBody.Part? = null
    private fun setBody(bitmap: Bitmap, flag: String): MultipartBody.Part {
        val filePath = Utills.saveImage(this@ChooseVenuseActivity, bitmap)
        this.filePath = File(filePath)
        reqFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            this.filePath!!
        )


        body = MultipartBody.Part.createFormData(
            flag,
            this.filePath!!.name,
            reqFile
        )

        return body!!
    }

    private fun initView() {
        lostItemSubmitViewModel = LostItemSubmitViewModel(this@ChooseVenuseActivity)
        homeViewModel = HomeViewModel(this@ChooseVenuseActivity)
        binding.chooseVenuesSend.setOnClickListener(this)

    }


    private fun setToolBar() {
        setTouchNClick(binding.chooseVenuesToolbar.toolbarBack)
        binding.chooseVenuesToolbar.toolbarBack.setOnClickListener { finish() }
        binding.chooseVenuesToolbar.toolbarTitle.setText("Choose Venues")
        binding.chooseVenuesToolbar.toolbar3dot.visibility= View.GONE
        binding.chooseVenuesToolbar.toolbarBell.visibility= View.GONE
    }


    private fun dashboardAPICALL() {
        customProgressDialog.show(this@ChooseVenuseActivity, "")
        homeViewModel.dashBoard().observe(this@ChooseVenuseActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()

                    it.data?.let { users ->
                        try {
                            dashList = users.data
                            if (!(dashList.all_records == null ||dashList.all_records.size <= 0)) {
                                    setListAllRecord(dashList.all_records)
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                Status.LOADING -> {
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    customProgressDialog.dialog.dismiss()
                    try {
                        Utills.showSnackBarOnError(
                            binding.chooseVenuesToolbarConstrent,
                            it.message!!,
                            this@ChooseVenuseActivity
                        )
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }
    lateinit var allRecordAdapter: AllRecordVenuseAdapter
    private fun setListAllRecord(allRecordsList: ArrayList<DashboardModel.AllRecord>) {
        allRecordAdapter = AllRecordVenuseAdapter(this@ChooseVenuseActivity,allRecordsList,object:
            AllRecordVenuseAdapter.ClickListener{
            override fun onClickNext(pos: Int) {
//                startActivity(
//                    Intent(this@ChooseVenuseActivity, VenuListActvity::class.java)
//                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,allRecordsList[pos].type ))
            }

            override fun onClickSub(subpos: Int, pos: Int) {
                if( allRecordsList[pos].sub_records[subpos].isChked )
                allRecordsList[pos].sub_records[subpos].isChked = false
                 else
                    allRecordsList[pos].sub_records[subpos].isChked = true
                allRecordAdapter.notifyItemChanged(pos)
            }
        })

        binding.chooseVenuesRecyle.also {
            it.layoutManager=LinearLayoutManager(this@ChooseVenuseActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = allRecordAdapter
        }



    }
}