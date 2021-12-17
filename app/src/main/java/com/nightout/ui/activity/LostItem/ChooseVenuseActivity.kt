package com.nightout.ui.activity.LostItem

import android.app.Activity
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
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.tabs.TabLayout
import com.nightout.R
import com.nightout.adapter.*
import com.nightout.base.BaseActivity
import com.nightout.databinding.ChossesvenuesActvityBinding
import com.nightout.model.LostItemChooseVenuResponse
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class ChooseVenuseActivity : BaseActivity() {
    lateinit var binding: ChossesvenuesActvityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var lostChooseVenues: CommonViewModel
    lateinit var venueList: ArrayList<LostItemChooseVenuResponse.AllVenue>
    var strID: StringBuilder = StringBuilder()
    lateinit var allRecordAdapter: VenuesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ChooseVenuseActivity,
            R.layout.chossesvenues_actvity
        )
        initView()
        lost_item_venuesAPICALL()

        setToolBar()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.chooseVenuesSend) {
            if (isValidateData())
                collectData()
        }
    }

    private fun isValidateData(): Boolean {
      /* var flag = false
        strID = StringBuilder()


            for (j in 0 until venueList.size) {
                if (venueList[j].isChk) {
                    flag = true
                    strID.append(venueList[j].id + ",")
                }
            }

        if (!flag) {
            MyApp.popErrorMsg("", resources.getString(R.string.Please_Select_Venues), THIS!!)
            return false
        }
*/
        return true
    }

    private fun collectData() {
        var mdl = DataManager.instance.lostItemDetailCstmModel
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (!mdl.itemID.equals("0")) {
            //send ID durring EditItem
            builder.addFormDataPart("id", mdl.itemID)
        }
        builder.addFormDataPart("venue_id", strID.substring(0, strID.length - 1))
        builder.addFormDataPart("product_name", mdl.itemName)
        builder.addFormDataPart("customer_name", mdl.fullName)
        builder.addFormDataPart("email", mdl.emailID)
        var mobNo = Utills.getMobNoSimpleFormat(mdl.phno)
        builder.addFormDataPart("phonenumber", mobNo)
        builder.addFormDataPart("product_detail", mdl.pDetail)
        if (mdl.imgPathUri.equals("")) {
            builder.addFormDataPart("image", "")
        } else {
            var resultUri = Uri.parse(mdl.imgPathUri)
            val bitmap: Bitmap?
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, resultUri)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
            builder.addPart(setBody(bitmap!!, "image"))
        }

        addLostItemApiCall(builder.build())

    }

    lateinit var lostItemSubmitViewModel: CommonViewModel
    private fun addLostItemApiCall(build: MultipartBody) {
        customProgressDialog.show(this@ChooseVenuseActivity)
        lostItemSubmitViewModel.submitLostItem(build).observe(this@ChooseVenuseActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    if (it.data?.status_code == 200) {
                        //  MyApp.ShowTost(THIS!!, "" + it.data?.status_code)
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
                        Utills.showErrorToast(
                            this@ChooseVenuseActivity,
                            it.message!!,

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
        lostItemSubmitViewModel = CommonViewModel(this@ChooseVenuseActivity)
        lostChooseVenues = CommonViewModel(this@ChooseVenuseActivity)
        binding.chooseVenuesSend.setOnClickListener(this)

    }


    private fun setToolBar() {
        setTouchNClick(binding.chooseVenuesToolbar.toolbarBack)
        binding.chooseVenuesToolbar.toolbarBack.setOnClickListener { finish() }
        binding.chooseVenuesToolbar.toolbarTitle.setText("Choose Venues")
        binding.chooseVenuesToolbar.toolbar3dot.visibility = View.GONE
        binding.chooseVenuesToolbar.toolbarBell.visibility = View.GONE
    }

    var responseItemList: MutableList<String>? = ArrayList()
    private fun lost_item_venuesAPICALL() {
        customProgressDialog.show(this@ChooseVenuseActivity, "")
        lostChooseVenues.lostChooseVenues().observe(this@ChooseVenuseActivity, {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    it.data?.let { users ->
                        try {

                            if (it.data.data.all_venues.size>0) {
                                venueList = ArrayList()
                                venueList.addAll(it.data.data.all_venues)
                               // setTabs()


                                    responseItemList?.add("invitation")
                                    responseItemList?.add("bars")
                                    responseItemList?.add("pubs")
                                    responseItemList?.add("clubs")
                                    responseItemList?.add("foods")

                                for (aq in responseItemList!!.indices) {
                                    binding.tabs!!.addTab(
                                        binding.tabs!!.newTab().setText(responseItemList!![aq])
                                    )
                                    binding.tabs!!.tabGravity = TabLayout.GRAVITY_FILL

                                }
                                 setProductListWidSection()
                            }
                        } catch (e: Exception) {
                            MyApp.popErrorMsg("",resources.getString(R.string.No_data_found),THIS!!)
                        }
                    }
                }
                Status.LOADING -> {
                    Log.d("ok", "loginCall:LOADING ")
                }
                Status.ERROR -> {
                    customProgressDialog.dialog.dismiss()
                    try {
                        Utills.showErrorToast(
                            this@ChooseVenuseActivity,
                            it.message!!,

                        )
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    private fun setProductListWidSection() {
        allRecordAdapter =
            AllRecordVenuseAdapter(this@ChooseVenuseActivity, venueList, object : AllRecordVenuseAdapter.ClickListener {
                override fun onClickNext(pos: Int) {
//                startActivity(
//                    Intent(this@ChooseVenuseActivity, VenuListActvity::class.java)
//                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,allRecordsList[pos].type ))
                }

                override fun onClickSub(subpos: Int, pos: Int) {
//                    if (venueList[pos].sub_records[subpos].isChked)
//                        dashList.all_records[pos].sub_records[subpos].isChked = false
//                    else
//                        dashList.all_records[pos].sub_records[subpos].isChked = true
//                    allRecordAdapter.notifyItemChanged(pos)
                }
            })

        linearLayoutManager =
            LinearLayoutManager(this@ChooseVenuseActivity, LinearLayoutManager.VERTICAL, false)
        binding.chooseVenuesRecyle.layoutManager = linearLayoutManager
        binding.chooseVenuesRecyle.adapter = allRecordAdapter

        setScrollListener()
        bindWidgetsWithAnEvent()
    }
    private var linearLayoutManager: LinearLayoutManager? = null
    private var checkCallBack = false // not using now
    private var checkScroll = true
    private var posSelected = 0
    private var totalCount = 0// not using now
    private var chkTabClicked = false

/*
    private fun setProductListWidSection() {
        allRecordAdapter =
            VenuesAdapter(this@ChooseVenuseActivity, venueList, object :
                VenuesAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    venueList[pos].isChk=!venueList[pos].isChk
                    allRecordAdapter.notifyItemChanged(pos)
                }


            })
        (binding.chooseVenuesRecyle?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false//stop blinking
        binding.chooseVenuesRecyle.also {
            it.layoutManager= LinearLayoutManager(this@ChooseVenuseActivity, LinearLayoutManager.VERTICAL, false)
            it.adapter = allRecordAdapter

        }
//        var linearLayoutManager =
//            LinearLayoutManager(this@ChooseVenuseActivity, LinearLayoutManager.VERTICAL, false)
//        binding.chooseVenuesRecyle.layoutManager = linearLayoutManager
//        binding.chooseVenuesRecyle.adapter = allRecordAdapter


    }*/


}