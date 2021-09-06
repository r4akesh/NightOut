package com.nightout.ui.activity

import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SharedMemeberActvity,R.layout.sharedmemeber_actvity)
        setToolBar()
        setListDummy()
    }
    lateinit var groupListAdapter: GroupListAdapter
    private fun setListDummy() {
        var list = ArrayList<GroupListModel>()
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, true))

        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, true))
        list.add(GroupListModel("Cameron Williamson", "Software Engineer", R.drawable.grp1, false))
        list.add(GroupListModel("Bessie Cooper", "Product Photographer", R.drawable.grp2, false))
        list.add(GroupListModel("Jane Cooper", "Interior Designer", R.drawable.grp3, false))
        groupListAdapter = GroupListAdapter(this@SharedMemeberActvity, list,true, object : GroupListAdapter.ClickListener {
            override fun onClickChk(pos: Int) {

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