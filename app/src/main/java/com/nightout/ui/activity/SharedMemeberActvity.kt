package com.nightout.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarcrawlSavedmapactivityBinding
import com.nightout.databinding.SharedmemeberActvityBinding
import com.nightout.model.GroupListModel

class SharedMemeberActvity : BaseActivity() {
    lateinit var binding: SharedmemeberActvityBinding
    var isSelectAll=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SharedMemeberActvity,R.layout.sharedmemeber_actvity)
        binding.sharedMemberSelectAll.setOnClickListener(this)
        setToolBar()
        setListDummy()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.sharedMemberSelectAll){
            if(isSelectAll) {
                isSelectAll=false
                binding.sharedMemberSelectAll.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.unchk_box,0)
            }
            else
            {
                isSelectAll=true
                binding.sharedMemberSelectAll.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.chk_box,0)
            }
            for (i in  0 until list.size){
                list[i].isChk = isSelectAll
            }
            groupListAdapter.notifyDataSetChanged()
        }
    }
    lateinit var groupListAdapter: GroupListAdapter
    var list = ArrayList<GroupListModel>()
    private fun setListDummy() {
        list = ArrayList()
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, false))
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, false))
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, false))
        groupListAdapter = GroupListAdapter(this@SharedMemeberActvity, list,true, object : GroupListAdapter.ClickListener {
            override fun onClickChk(pos: Int) {
                list[pos].isChk = !list[pos].isChk
                groupListAdapter.notifyItemChanged(pos)
            }

        })
        binding.sharedMemberRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@SharedMemeberActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }
    }

    private fun setToolBar() {
        binding.savedBarCrawlMemberToolBar.toolbarTitle.setText("Member")
        setTouchNClick( binding.savedBarCrawlMemberToolBar.toolbarBack)
        binding.savedBarCrawlMemberToolBar.toolbarBack.setOnClickListener { finish() }
        binding.savedBarCrawlMemberToolBar.toolbarBell.visibility=GONE
        binding.savedBarCrawlMemberToolBar.toolbar3dot.visibility=GONE
        binding.savedBarCrawlMemberToolBar.toolbarCreateGrop.visibility=GONE

    }
}