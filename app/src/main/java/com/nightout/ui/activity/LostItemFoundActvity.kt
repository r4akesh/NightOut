package com.nightout.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.LostitemeditActvityBinding
import com.nightout.model.GetLostItemListModel
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import java.lang.StringBuilder


class LostItemFoundActvity : BaseActivity() {

    lateinit var  binding : LostitemeditActvityBinding
    lateinit var itemFoundViewModel: CommonViewModel
    lateinit var lostModel : GetLostItemListModel.Data
    private var customProgressDialog = CustomProgressDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LostItemFoundActvity,R.layout.lostitemedit_actvity)
        setToolBar()
        initView()

    }

    private fun initView() {
        setTouchNClick(binding.lostEditFound)
        itemFoundViewModel = CommonViewModel(this@LostItemFoundActvity)
          lostModel = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.LOSTITEM_POJO) as GetLostItemListModel.Data
        binding.lostEditItemNameValue.setText(lostModel.product_name)
        binding.lostEditColorValue.setText(lostModel.product_detail)
        if(lostModel.status.equals("1")) binding.lostEditFound.visibility=GONE
        var stringBuilder = StringBuilder()
        for (i in 0 until lostModel.venues.size){
            stringBuilder.append(lostModel.venues[i].store_name+", ")
        }
        var venuList= stringBuilder.toString()
        binding.lostEditVenuesValues.text = venuList.substring(0,venuList.length-2)
        Utills.setImageNormal(this@LostItemFoundActvity,binding.lostEditImage,lostModel.image)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.lostEditFound){
            found_lost_itemAPICall()
        }
    }

    private fun found_lost_itemAPICall() {
        customProgressDialog.show(this@LostItemFoundActvity, "")

        var hashMap = HashMap<String,String>()
        hashMap["id"] = lostModel.id

        itemFoundViewModel.foundLostItem(hashMap).observe(this@LostItemFoundActvity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    setResult(Activity.RESULT_OK)
                    finish()
                }Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(
                        binding.lostEditConstrentToolbar,
                        it.message!!,
                        this@LostItemFoundActvity
                    )
                }
            }
        })
    }

    private fun setToolBar() {
        binding.lostItemEditToolBar.toolbarTitle.text = "Lost Items"
        binding.lostItemEditToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.lostItemEditToolBar.toolbar3dot.visibility= View.GONE
        binding.lostItemEditToolBar.toolbarBell.visibility= View.GONE
    }
}