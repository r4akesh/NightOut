package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.OrderdetailAvctivityBinding
import com.nightout.model.OrderDetailListModel
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import kotlinx.android.synthetic.main.orderdetail_avctivity.view.*
import kotlinx.android.synthetic.main.toolbar_common.view.*

class OrderDetailActivity : BaseActivity() {

    lateinit var binding: OrderdetailAvctivityBinding
    lateinit var venuePkgList: ArrayList<VenuDetailModel.PkgModel>
    lateinit var drinksList: ArrayList<VenuDetailModel.CategoryDrinksMdl>
    lateinit var foodsList: ArrayList<VenuDetailModel.CategoryFoodMdl>
    lateinit var snacksList: ArrayList<VenuDetailModel.SnacksModl>

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
        if(detailStore.drinkProducts.categories.size>0){
           for (i in 0 until  detailStore.drinkProducts.categories.size){
               if(detailStore.drinkProducts.categories[i].isSelected){
                   isDrinkSelected=true
                   break
               }
           }
        }
        if(detailStore.foodProducts.categories.size>0){
            for (i in 0 until  detailStore.foodProducts.categories.size){
                if(detailStore.foodProducts.categories[i].isSelected){
                    isFoodSelected=true
                    break
                }
            }
        }
        if(detailStore.snackProducts.categories.size>0){
            for (i in 0 until  detailStore.snackProducts.categories.size){
                if(detailStore.snackProducts.categories[i].isSelected){
                    isSnaclSelected=true
                    break
                }
            }
        }
        if(detailStore.packageProducts.products.size>0){
            for (i in 0 until  detailStore.packageProducts.products.size){
                if(detailStore.packageProducts.products[i].isChekd){
                    isPkgSelected=true
                    break
                }
            }
        }
        if(isDrinkSelected) {
            //listOrder.add(OrderDetailListModel("", detailStore.drinkProducts.categories))
            var mdl : OrderDetailListModel.Product? = null
            for (i in 0 until detailStore.drinkProducts.categories.size){
                var vvMdl =detailStore.drinkProducts.categories[i].products[0].
                mdl.isChekd = vvMdl.
            }
        }
            listOrder.add(OrderDetailListModel("",detailStore.foodProducts.categories))
        setList(detailStore.drinkProducts)

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