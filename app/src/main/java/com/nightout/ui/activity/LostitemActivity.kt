package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.LostItemAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.LostitemActvityBinding
import com.nightout.model.GetLostItemListModel
import com.nightout.model.LostItemModel
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.GetLostListViewModel


class LostitemActivity :BaseActivity() {
    lateinit var binding :LostitemActvityBinding
    lateinit var getLostListViewModel : GetLostListViewModel
    private var customProgressDialog = CustomProgressDialog()
    var  lostList: ArrayList<GetLostItemListModel.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LostitemActivity, R.layout.lostitem_actvity)
        initView()
        user_lost_itemsAPICAll()
        setToolBar()
    }

    private fun initView() {
        getLostListViewModel = GetLostListViewModel(this@LostitemActivity)
    }

    private fun user_lost_itemsAPICAll() {
        customProgressDialog.show(this@LostitemActivity, "")
        getLostListViewModel.getLostItemList().observe(this@LostitemActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
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
                    Utills.showSnackBarOnError(
                        binding.lostConstrentToolbar,
                        it.message!!,
                        this@LostitemActivity
                    )
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
            startActivity(Intent(this@LostitemActivity,LostItemDetailsActvity::class.java))
        }
    }

    private fun setItemList() {
        var lostItemAdapter = LostItemAdapter(
            this@LostitemActivity,
            lostList,
            object : LostItemAdapter.ClickListener {
                override fun onClickSetting(pos: Int, lostItem3Dot: ImageView) {
                    Log.d("TAG", "onClickSetting: ")
                    openPopMenu(lostItem3Dot)
                }

                override fun onClick(pos: Int) {
                    startActivity(Intent(this@LostitemActivity,LostItemEditActvity::class.java))
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

    private fun openPopMenu(lostItem3Dot: ImageView) {
        val popup = PopupMenu(this@LostitemActivity, lostItem3Dot)
        popup.menu.add("Edit")
        popup.menu.add("Delete")
        popup.setGravity(Gravity.END)
        popup.setOnMenuItemClickListener { item ->
           // item.title
                if(item.title.equals("Edit")){

                   // startActivity(Intent(this@LostitemActivity,LostItemEditActvity::class.java))
                    startActivity(Intent(this@LostitemActivity,LostItemDetailsActvity::class.java))
                }
            true
        }
        popup.show()
    }
}