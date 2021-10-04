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
import com.nightout.adapter.SharedAdapter
import com.nightout.adapter.SharedSideMenuAdapter
import com.nightout.base.BaseActivity
import com.nightout.databinding.BarshredActivityBinding
import com.nightout.databinding.FragmentBarcrawlnewBinding
import com.nightout.model.SharedModel

class SideMenuShredListActivity : BaseActivity() {

    lateinit var binding: BarshredActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SideMenuShredListActivity,R.layout.barshred_activity)
        setToolBar()
        setListDummy()
    }

    private fun setToolBar() {
        binding.sharedBarCrawlToolBar.toolbarTitle.setText(resources.getString(R.string.Invited_BarCrawl))
        setTouchNClick( binding.sharedBarCrawlToolBar.toolbarBack)
        binding.sharedBarCrawlToolBar.toolbarBack.setOnClickListener { finish() }
        binding.sharedBarCrawlToolBar.toolbarCreateGrop.visibility=GONE
        binding.sharedBarCrawlToolBar.toolbarBell.visibility=GONE
        binding.sharedBarCrawlToolBar.toolbar3dot.visibility=GONE

    }

    lateinit var  sharedAdapter: SharedSideMenuAdapter
    private fun setListDummy() {
        var list = ArrayList<SharedModel>()
        list.add(SharedModel("Vanity Night Club",R.drawable.shared_img1,""))
        list.add(SharedModel("Vanity Night Club", R.drawable.shared_img2,""))
        list.add(SharedModel("Vanity Night Club",R.drawable.shared_img3,""))
        list.add(SharedModel("Vanity Night Club",R.drawable.shared_img1,""))
        list.add(SharedModel("Vanity Night Club",R.drawable.shared_img2,""))
        list.add(SharedModel("Vanity Night Club",R.drawable.shared_img3,""))



        sharedAdapter = SharedSideMenuAdapter(this@SideMenuShredListActivity,list,object : SharedSideMenuAdapter.ClickListener{
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