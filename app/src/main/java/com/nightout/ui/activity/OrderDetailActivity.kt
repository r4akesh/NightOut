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
import com.nightout.model.OrderDetailListModel
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import kotlinx.android.synthetic.main.allbar_actvity.*
import kotlinx.android.synthetic.main.orderdetail_avctivity.view.*
import kotlinx.android.synthetic.main.toolbar_common.view.*

class OrderDetailActivity : BaseActivity() {

    lateinit var binding: OrderdetailAvctivityBinding
   lateinit var  recordsList: ArrayList<VenuDetailModel.AllProduct>   erfsdjkfhdsuhf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OrderDetailActivity,R.layout.orderdetail_avctivity)
       setToolBar()
         var detailStore= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.StoreDetailPoJO) as VenuDetailModel.Data
        Log.d("TAG", "onCreate: ")
        var isDrinkSelected=false
        var isFoodSelected=false
        var isSnaclSelected=false
        var isPkgSelected=false
        var listOrder = ArrayList<OrderDetailListModel>()
        recordsList = ArrayList()
        if(detailStore.all_products.size>0){
        //   for (i in 0 until  detailStore.all_products.size){
           for (i in 0 until 3){//coz packege no include
            //   if(detailStore.all_products[i].isSelected){
                   for (j in 0 until detailStore.all_products[i].records.size){
                       for (k in 0 until detailStore.all_products[i].records[j].products.size){
                           if(detailStore.all_products[i].records[j].products[k].quantityLocal>0)
                               recordsList.addAll(detailStore.all_products)
                           break
                       }

                   }
              // }
           }
        }
        Log.d("TAG", "onCreate: "+recordsList)

        setListOrder()


    }

    lateinit var ordrDetailAdapter: OrdrDetailAdapter
    private fun setListOrder() {
        ordrDetailAdapter = OrdrDetailAdapter(THIS!!,recordsList,object:OrdrDetailAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })
        binding.orderDetailRecyle.also {
            it.layoutManager = LinearLayoutManager(THIS!!,LinearLayoutManager.VERTICAL,false)
            it.adapter = ordrDetailAdapter
        }
    }

    private fun setToolBar() {
        binding.constrentToolbar.toolbar_title.setText("Order Detail")
        binding.constrentToolbar.toolbar_3dot.visibility=GONE
        binding.constrentToolbar.toolbar_bell.visibility=GONE

        setTouchNClick(  binding.constrentToolbar.oredrDetailToolBar)
        setTouchNClick( binding.orderDetailPay)
         binding.constrentToolbar.oredrDetailToolBar.setOnClickListener {
             finish()
             overridePendingTransition(0,0)
         }
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.orderDetailPay){
            startActivity(Intent(this@OrderDetailActivity,MyCardsActivity::class.java))

        }
    }
}