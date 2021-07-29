package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.GroupListAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.ListparticipateActvityBinding
import com.nightout.model.GroupListModel

class ListParticipteActvity : BaseActivity() {
    lateinit var binding : ListparticipateActvityBinding
    lateinit var groupListAdapter: GroupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listparticipate_actvity)
        binding = DataBindingUtil.setContentView(this@ListParticipteActvity,R.layout.listparticipate_actvity)
        setToolBar()
        initView()
        setListDummy()

        //participateRecycle
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.participateShare){
            startActivity(Intent(this@ListParticipteActvity,AllBarCrawalActivity::class.java))
        }
    }
    private fun initView() {
         setTouchNClick(binding.participateShare)
    }

    private fun setToolBar() {
        binding.participateToolBar.toolbarTitle.text = "List of Participants"
        binding.participateToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.participateToolBar.toolbar3dot.visibility= View.GONE
        binding.participateToolBar.toolbarBell.visibility= View.GONE
    }

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
        groupListAdapter = GroupListAdapter(this@ListParticipteActvity, list,true, object : GroupListAdapter.ClickListener {
            override fun onClickChk(pos: Int) {

            }

        })

        binding.participateRecycle.also {
            it.layoutManager =
                LinearLayoutManager(this@ListParticipteActvity, LinearLayoutManager.VERTICAL, false)
            it.adapter = groupListAdapter
        }
    }
}