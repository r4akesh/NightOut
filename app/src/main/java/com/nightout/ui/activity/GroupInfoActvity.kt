package com.nightout.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.GroupChatImageAdapter
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.GroupinfoActvityBinding
import com.nightout.model.GroupChatImgModel
import com.nightout.model.GroupListModel

class GroupInfoActvity : BaseActivity() {
    lateinit var groupChatImageAdapter : GroupChatImageAdapter
    lateinit var binding : GroupinfoActvityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.groupinfo_actvity)
        binding = DataBindingUtil.setContentView(this@GroupInfoActvity,R.layout.groupinfo_actvity)
        setToolBar()
        setHorizonatlDummyList()
        setFrendListDummy()
    }

    private fun setToolBar() {
         binding.grupInfoToolBar.toolbarTitle.setText("Group Information")
         binding.grupInfoToolBar.toolbar3dot.visibility=GONE
         binding.grupInfoToolBar.toolbarBell.visibility=GONE
         binding.grupInfoToolBar.toolbarCreateGrop.visibility= VISIBLE
         binding.grupInfoToolBar.toolbarCreateGrop.setText("Exit Group")
        setTouchNClick(binding.grupInfoToolBar.toolbarBack)
         binding.grupInfoToolBar.toolbarBack.setOnClickListener { finish() }
    }

    private fun setFrendListDummy() {
       /* var list = ArrayList<GroupListModel>()
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, true))

        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, true))
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, false))
        groupListAdapter = GroupListAdapter(this@GroupInfoActvity, list,false, object : GroupListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {

                }

            })

        binding.groupInfoActvitityRecyleFrend.also {
            it.layoutManager =
                LinearLayoutManager(this@GroupInfoActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }*/
    }


    private fun setHorizonatlDummyList() {
        var list = ArrayList<GroupChatImgModel>()
        list.add(GroupChatImgModel(R.drawable.grupimg1))
        list.add(GroupChatImgModel(R.drawable.grupimg2))
        list.add(GroupChatImgModel(R.drawable.grupimg3))
        list.add(GroupChatImgModel(R.drawable.grupimg4))
       groupChatImageAdapter =  GroupChatImageAdapter(this@GroupInfoActvity,list,object :GroupChatImageAdapter.ClickListener{
           override fun onClick(pos: Int) {

           }

       } )

        binding.groupInfoActvitityRcyleHorzntl.also {
            it.layoutManager = LinearLayoutManager(this@GroupInfoActvity,LinearLayoutManager.HORIZONTAL,false)
            it.adapter = groupChatImageAdapter
        }
    }
}