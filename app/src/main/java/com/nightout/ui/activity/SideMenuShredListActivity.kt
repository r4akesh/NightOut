package com.nightout.ui.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.SharedSideMenuAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarshredActivityBinding
import com.nightout.model.InvitedBarCrwlResponse
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel

class SideMenuShredListActivity : BaseActivity() {

    lateinit var binding: BarshredActivityBinding
    private var customProgressDialog = CustomProgressDialog()
    private lateinit var commonViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SideMenuShredListActivity,R.layout.barshred_activity)
        commonViewModel = CommonViewModel(THIS!!)
        setToolBar()
       // setListDummy()
        barCrawlInvitedListAPICAll()
    }

    private fun barCrawlInvitedListAPICAll() {
        customProgressDialog.show(this@SideMenuShredListActivity, "")
        commonViewModel.barCrwalInvitedList().observe(this@SideMenuShredListActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        setList(myData.data)



                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showErrorToast(
                        this@SideMenuShredListActivity,
                        it.message!!,

                        )
                }
            }
        })
    }

    private fun setToolBar() {
        binding.sharedBarCrawlToolBar.toolbarTitle.text = resources.getString(R.string.Invited_BarCrawl)
        setTouchNClick( binding.sharedBarCrawlToolBar.toolbarBack)
        binding.sharedBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.sharedBarCrawlToolBar.toolbarCreateGrop.visibility=GONE
        binding.sharedBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.sharedBarCrawlToolBar.toolbar3dot.visibility=GONE

    }

    lateinit var  sharedAdapter: SharedSideMenuAdapter
    private fun setList(dataList: ArrayList<InvitedBarCrwlResponse.Data>) {




        sharedAdapter = SharedSideMenuAdapter(this@SideMenuShredListActivity,dataList,object : SharedSideMenuAdapter.ClickListener{
            override fun onClick3Dot(pos: Int, imgview : ImageView) {

            }

        })

        binding.sharedRecycle.also {
            it.layoutManager = LinearLayoutManager(this@SideMenuShredListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = sharedAdapter
        }
    }

    private fun openPopMenu(imgview: ImageView) {
        val popup = PopupMenu(this@SideMenuShredListActivity, imgview)
        popup.menu.add("Edit")
        popup.menu.add("Delete")
        popup.setGravity(Gravity.END)
        popup.setOnMenuItemClickListener { item ->
            // item.title
            if(item.title.equals("Edit")){
                // startActivity(Intent(this@LostitemActivity,LostItemEditActvity::class.java))
                //startActivity(Intent(this@BarCrawlShredListActivity,LostItemDetailsActvity::class.java))
            }
            true
        }
        popup.show()
    }
}