package com.nightout.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.CreategrupActivityBinding
import com.nightout.model.GroupListModel

class CreateGroupActvity : BaseActivity() {
    lateinit var binding: CreategrupActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.creategrup_activity)
        binding =
            DataBindingUtil.setContentView(this@CreateGroupActvity, R.layout.creategrup_activity)
        setToolbar()
        setDummyList()
    }

    private fun setToolbar() {
        binding.toolbarTitle.setText("Create New Group")
        setTouchNClick(binding.toolbarBack)
        binding.toolbarBack.setOnClickListener { finish() }
    }

    lateinit var groupListAdapter: GroupListAdapter
    private fun setDummyList() {
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
        groupListAdapter = GroupListAdapter(this@CreateGroupActvity, list,true, object : GroupListAdapter.ClickListener {
                override fun onClickChk(pos: Int) {

                }

            })

        binding.crateGroupRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@CreateGroupActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }
    }
}