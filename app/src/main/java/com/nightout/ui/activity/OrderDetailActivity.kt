package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.OrdrDetailAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.OrderdetailAvctivityBinding
import com.nightout.model.LocalStreModel
import com.nightout.model.OrderDetailListModel
import com.nightout.model.VenuDetailModel
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.allbar_actvity.*
import kotlinx.android.synthetic.main.orderdetail_avctivity.view.*
import kotlinx.android.synthetic.main.toolbar_common.view.*
import org.json.JSONArray
import org.json.JSONObject

class OrderDetailActivity : BaseActivity() {

    lateinit var binding: OrderdetailAvctivityBinding
    lateinit var recordsList: MutableList<VenuDetailModel.Record>
    var mainList = ArrayList<LocalStreModel>()
    var arList = ArrayList<VenuDetailModel.Product>()
    lateinit var mdlPkg: VenuDetailModel.Product
    lateinit var ordrDetailAdapter: OrdrDetailAdapter
    var totPrice : Double = 0.0
    var storeType =""
    private val progressDialog = CustomProgressDialog()
    lateinit var detailStore: VenuDetailModel.Data
    var jrrItemId=JSONArray()
    var jrrItemQty=JSONArray()
    lateinit var paymentViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OrderDetailActivity, R.layout.orderdetail_avctivity)
        paymentViewModel = CommonViewModel(this@OrderDetailActivity)
        setToolBar()


        setData()

    }

    private fun setData() {
        try {
            detailStore = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.StoreDetailPoJO) as VenuDetailModel.Data
            storeType = intent.getStringExtra(AppConstant.INTENT_EXTRAS.StoreType)!!
            binding.orderDeatilTitle.text= detailStore.store_name
            binding.orderDeatilAddrs.text= detailStore.store_address
            recordsList = ArrayList()
            mainList = ArrayList()
            jrrItemId=JSONArray()
            jrrItemQty=JSONArray()
            if (detailStore.all_products.size > 0) {
                for (i in 0 until 3) {//coz packege no include
                    for (j in 0 until detailStore.all_products[i].records.size) {
                        arList = ArrayList()
                        for (k in 0 until detailStore.all_products[i].records[j].products.size) {
                            if (detailStore.all_products[i].records[j].products[k].quantityLocal > 0) {
                                totPrice+=detailStore.all_products[i].records[j].products[k].totPriceLocal
                                recordsList.addAll(detailStore.all_products[i].records)
                                arList.add(detailStore.all_products[i].records[j].products[k])
                                //collect item ID's
                                jrrItemId.put(detailStore.all_products[i].records[j].products[k].id)
                                jrrItemQty.put(detailStore.all_products[i].records[j].products[k].quantityLocal)
                            }
                        }
                        if (arList.size > 0) {
                            var mdl = LocalStreModel(detailStore.all_products[i].records[j].category, arList)
                            mainList.add(mdl)
                        }
                    }
                }
            }
            if(storeType != "4"){
                //for package
                arList = ArrayList()
                for (j in 0 until detailStore.all_products[3].records.size) {
                    if (detailStore.all_products[3].records[j].quantityLocal > 0) {
                        totPrice+=detailStore.all_products[3].records[j].totPriceLocal
                        mdlPkg = VenuDetailModel.Product(
                            detailStore.all_products[3].records[j].quantityLocal,
                            detailStore.all_products[3].records[j].totPriceLocal,
                            detailStore.all_products[3].records[j].category_id,
                            detailStore.all_products[3].records[j].discount,
                            detailStore.all_products[3].records[j].title,
                            detailStore.all_products[3].records[j].price,
                            "","","","","","","","","","","","","","")
                        arList.add(mdlPkg)
                        //collect item ID's
                        jrrItemId.put(detailStore.all_products[3].records[j].id)
                        jrrItemQty.put(detailStore.all_products[3].records[j].quantityLocal)
                    }
                }
                if (arList.size > 0) {
                    var mdl = LocalStreModel("Package", arList)
                    mainList.add(mdl)
                }
            }

            binding.orderDetailAmt.text= resources.getString(R.string.currency_sumbol) +totPrice.toString()
            var serviceChrg=PreferenceKeeper.instance.SERVICE_CHARGE?.toInt()
            var grndTot=totPrice+serviceChrg!!
            binding.orderDetailTotAmt.text= resources.getString(R.string.currency_sumbol) +grndTot
            binding.orderDetailPay.text= "Pay "+resources.getString(R.string.currency_sumbol) +binding.orderDetailTotAmt.text.toString()
            binding.orderDetailServChrgeAmt.text=  resources.getString(R.string.currency_sumbol) +PreferenceKeeper.instance.SERVICE_CHARGE

            setListOrder()
        } catch (e: Exception) {
            MyApp.popErrorMsg("","Error occurred",THIS!!)
        }
    }


    private fun setListOrder() {
        ordrDetailAdapter = OrdrDetailAdapter(THIS!!, mainList, object : OrdrDetailAdapter.ClickListener {
                override fun onClick(pos: Int) {

                }

            })
        binding.orderDetailRecyle.also {
            it.layoutManager = LinearLayoutManager(THIS!!, LinearLayoutManager.VERTICAL, false)
            it.adapter = ordrDetailAdapter
        }
    }

    private fun setToolBar() {
        binding.constrentToolbar.toolbar_title.setText("Order Detail")
        binding.constrentToolbar.toolbar_3dot.visibility = GONE
        binding.constrentToolbar.toolbar_bell.visibility = GONE

        setTouchNClick(binding.constrentToolbar.oredrDetailToolBar)
        setTouchNClick(binding.orderDetailPay)
        binding.constrentToolbar.oredrDetailToolBar.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.orderDetailPay) {
           // startActivity(Intent(this@OrderDetailActivity, MyCardsActivity::class.java))
            makePaymentApiCall()
        }
    }

    private fun makePaymentApiCall() {
        progressDialog.show(this@OrderDetailActivity, "")
        var map = JSONObject()
        map.put("vendor_id", detailStore.vendor_detail.id)
        map.put("user_id", detailStore.vendor_detail.id)
        map.put("venue_id",detailStore.id)
        map.put("product_id",jrrItemId)
        map.put("qty", jrrItemQty)
        map.put("payment_mode", "0")//payment_mode (0=>Online, 1=>Debit Card 2=>Credit Card)
        paymentViewModel.doPayment(map).observe(this@OrderDetailActivity, {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressDialog.dialog.dismiss()
                        it.data?.let { detailData ->
                            Utills.showDefaultToast(THIS!!,detailData.message)
                            finish()

                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        progressDialog.dialog.dismiss()
                    }
                }
            })
    }
}