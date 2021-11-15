package com.nightout.ui.activity.LostItem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.LostItemAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.LostitemActvityBinding
import com.nightout.model.GetLostItemListModel
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.DialogCustmYesNo
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel


class LostitemActivity :BaseActivity() {
    lateinit var binding :LostitemActvityBinding
    lateinit var getLostListViewModel : CommonViewModel
    lateinit var delItemViewModel: CommonViewModel
    private var customProgressDialog = CustomProgressDialog()
    var  lostList: ArrayList<GetLostItemListModel.Data> = ArrayList()
    lateinit  var lostItemAdapter:LostItemAdapter
var isEdit= true //for mainten tost msg
    var savePosForStatus =0
 //   var REQUESTCOD_LostItemFoundActvity=1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LostitemActivity, R.layout.lostitem_actvity)
        initView()
        user_lost_itemsAPICAll()
        setToolBar()
    }

    private fun initView() {
        getLostListViewModel = CommonViewModel(this@LostitemActivity)
        delItemViewModel = CommonViewModel(this@LostitemActivity)
    }
    private fun delete_lost_itemAPICall(pos:Int) {
        customProgressDialog.show(this@LostitemActivity, "")
        val map = HashMap<String, String>()
        map["id"]= lostList[pos].id
        delItemViewModel.delItem(map).observe(this@LostitemActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(binding.lostConstrentToolbar, it.data?.message!!, this@LostitemActivity)
                    var listSize=lostList.size
                    lostList.removeAt(pos)
                    lostItemAdapter.notifyItemRemoved(pos)
                    lostItemAdapter.notifyItemRangeChanged(pos, listSize)
                }
                Status.LOADING->{ }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                }
            }
        })
    }

    private fun user_lost_itemsAPICAll() {
        customProgressDialog.show(this@LostitemActivity, "")
        getLostListViewModel.getLostItemList().observe(this@LostitemActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        binding.lostItemNoDataConstrent.visibility= GONE
                        lostList = ArrayList()
                        lostList.addAll(myData.data)
                        lostList.reverse()
                        setItemList()
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    // Utills.showSnackBarOnError(binding.lostConstrentToolbar, it.message!!, this@LostitemActivity)
                    binding.lostItemNoDataConstrent.visibility= VISIBLE
                }
            }
        })
    }




    private fun setToolBar() {
        binding.lostItemToolBar.toolbarTitle.text = "Lost Items"
        binding.lostItemToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.lostItemToolBar.toolbar3dot.visibility= View.GONE
        binding.lostItemToolBar.toolbarBell.visibility= View.GONE
        binding.lostItemToolBar.toolbarCreateGrop.visibility = VISIBLE
        binding.lostItemToolBar.toolbarCreateGrop.setText("ADD")
        binding.lostItemToolBar.toolbarCreateGrop.setOnClickListener {
            isEdit = false


            //startActivityForResult(Intent(this@LostitemActivity,LostItemDetailsActvity::class.java),REQUESTCODE_LostItemDetailsActvity)

            startforResultLostItemDetails.launch(Intent(this@LostitemActivity,
                LostItemDetailsActvity::class.java))
        }
    }

    private fun setItemList() {
          lostItemAdapter = LostItemAdapter(this@LostitemActivity, lostList, object : LostItemAdapter.ClickListener {
                override fun onClickSetting(pos: Int, lostItem3Dot: ImageView) {
                    Log.d("TAG", "onClickSetting: ")
                    openPopMenu(lostItem3Dot,pos)
                }

                override fun onClick(pos: Int) {
                      savePosForStatus =pos

                    startForResultLostItemFound.launch(Intent(this@LostitemActivity,
                        LostItemFoundActvity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.LOSTITEM_POJO,lostList[pos]))
                }
            })
        binding.lostItemRecycler.also {
            it.layoutManager = LinearLayoutManager(
                this@LostitemActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter = lostItemAdapter
        }
    }
    val startForResultLostItemFound= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            lostList[savePosForStatus].status="1"
            lostItemAdapter.notifyItemChanged(savePosForStatus)
        }
    }
  //  var REQUESTCODE_LostItemDetailsActvity=100
    private fun openPopMenu(lostItem3Dot: ImageView, pos:Int) {
        val popup = PopupMenu(this@LostitemActivity, lostItem3Dot)
        if(!lostList[pos].status.equals("1")) {
            popup.menu.add("Edit")
        }
        popup.menu.add("Delete")
        popup.setGravity(Gravity.END)
        popup.setOnMenuItemClickListener { item ->
                if(item.title.equals("Edit")){
                    isEdit = true
                    startforResultLostItemDetails.launch(Intent(this@LostitemActivity,
                        LostItemDetailsActvity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.LOSTITEM_POJO,lostList[pos])
                        .putExtra(AppConstant.INTENT_EXTRAS.ISFROM_EDITITEM,true))


                }else{
                    showAlertpopUp(pos)
                }
            true
        }
        popup.show()
    }

    val startforResultLostItemDetails = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result:ActivityResult->
        if(result.resultCode==Activity.RESULT_OK){
            user_lost_itemsAPICAll()
            if(isEdit)
                Utills.showSnackBarOnError(binding.lostConstrentToolbar, "You have updated item successfully", this@LostitemActivity)
            else
                Utills.showSnackBarOnError(binding.lostConstrentToolbar, "You have added item successfully", this@LostitemActivity)
        }
    }

    private fun showAlertpopUp(pos: Int) {
            DialogCustmYesNo.getInstance().createDialog(this@LostitemActivity,"","Are you sure you want to delete item?",object:DialogCustmYesNo.Dialogclick{
                override fun onYES() {
                    delete_lost_itemAPICall(pos)
                }
                override fun onNO() {

                }

            })
    }



 /*   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUESTCODE_LostItemDetailsActvity && resultCode==Activity.RESULT_OK){
            user_lost_itemsAPICAll()
            if(isEdit)
            Utills.showSnackBarOnError(binding.lostConstrentToolbar, "You have updated item successfully", this@LostitemActivity)
            else
            Utills.showSnackBarOnError(binding.lostConstrentToolbar, "You have added item successfully", this@LostitemActivity)
        }
      else if(requestCode==REQUESTCOD_LostItemFoundActvity && resultCode==Activity.RESULT_OK){
            lostList[savePosForStatus].status="1"
            lostItemAdapter.notifyItemChanged(savePosForStatus)
        }
    }*/
}