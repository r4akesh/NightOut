package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.SavedAdapter
import com.nightout.adapter.SharedAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarshredActivityBinding
import com.nightout.databinding.BarssavedActivityBinding
import com.nightout.databinding.FragmentBarcrawlnewBinding
import com.nightout.model.SharedModel

class BarCrawlSavedListActivity : BaseActivity() {

    lateinit var binding: BarssavedActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BarCrawlSavedListActivity,R.layout.barssaved_activity)
        setToolBar()
        setListDummy()
    }

    private fun setToolBar() {
        binding.savedBarCrawlToolBar.toolbarTitle.setText("Saved")
        setTouchNClick( binding.savedBarCrawlToolBar.toolbarBack)
        binding.savedBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.savedBarCrawlToolBar.toolbarCreateGrop.visibility=GONE
        binding.savedBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.savedBarCrawlToolBar.toolbar3dot.visibility=GONE

    }

    lateinit var  sharedAdapter: SavedAdapter
    private fun setListDummy() {
        var list = ArrayList<SharedModel>()
        list.add(SharedModel("Jane Birthday Party",R.drawable.shared_img1,"Date: 21 Aug 2021"))
        list.add(SharedModel("Ralph Edwards Anniversary", R.drawable.shared_img2,"Date: 18 Aug 2021"))
        list.add(SharedModel("Darrell Ceremony",R.drawable.shared_img3,"Date: 10 Aug 2021"))
        list.add(SharedModel("Albert Flores Birthday Party",R.drawable.shared_img1,"Date: 8 Aug 2021"))
        list.add(SharedModel("Jane Marriage Anniversary",R.drawable.shared_img2,"Date: 7 Aug 2021"))
        list.add(SharedModel("Darrell Ceremony",R.drawable.shared_img3,"Date: 5 Aug 2021"))



        sharedAdapter = SavedAdapter(this@BarCrawlSavedListActivity,list,object : SavedAdapter.ClickListener{
            override fun onClick(pos: Int) {
                startActivity(Intent(this@BarCrawlSavedListActivity,BarCrawlSavedMapActivity::class.java))
            }

        })

        binding.savedRecycleView.also {
            it.layoutManager = LinearLayoutManager(this@BarCrawlSavedListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = sharedAdapter
        }
    }


}