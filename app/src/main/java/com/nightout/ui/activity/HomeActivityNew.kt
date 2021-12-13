package com.nightout.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.nightout.R
import com.nightout.adapter.SideMenuAdapter
import com.nightout.base.BaseActivity

import com.nightout.databinding.HomeActvitynewBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.interfaces.OnSideMenuSelectListener


import com.nightout.model.SideMenuModel
import com.nightout.ui.activity.LostItem.LostitemActivity
import com.nightout.ui.fragment.HomeFragment
import com.nightout.utils.AppConstant
import kotlinx.android.synthetic.main.drawer_layout_new.*
import kotlinx.android.synthetic.main.drawer_layout_new.view.*

class HomeActivityNew : BaseActivity(), OnMenuOpenListener,OnSideMenuSelectListener {
    lateinit var binding: HomeActvitynewBinding
    private val endScale = 0.95f
    private var menuList:MutableList<SideMenuModel> = ArrayList()
    private var fragmentManager: FragmentManager? = null
    lateinit var homeFragment : HomeFragment
    lateinit var menuAdapter:SideMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActvitynewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setUpMenus()
        setHomeTab()
        fragmentManager = supportFragmentManager
        showFragment(HomeFragment(this))
    }

    override fun onMenuSelect(menuTitle: String) {
        when (menuTitle) {
            resources.getString(R.string.Home) -> {
                closeDrawer()
            }
            resources.getString(R.string.Venues)->{
                startActivity(
                    Intent(THIS!!, VenuListActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,"1"))//here 1 is default
            }
            resources.getString(R.string.Foods)->{
                startActivity(Intent(THIS, VenuListActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,"4"))
            }
            resources.getString(R.string.Events)->{
                startActivity(Intent(THIS, VenuListActvity::class.java)
                    .putExtra(AppConstant.INTENT_EXTRAS.StoreType,"5"))//
            }

            resources.getString(R.string.TrackNTrace)->{
                startActivity(Intent(THIS, TrackTrace::class.java))
            }
            resources.getString(R.string.Favourite)->{
                startActivity(Intent(THIS, FavListActivity::class.java))
            }
            resources.getString(R.string.Invited_BarCrawl)->{
                startActivity(Intent(THIS,  SideMenuShredListActivity::class.java))

            }
            resources.getString(R.string.Reffer_friend)->{
            }
            resources.getString(R.string.Lost_Item)->{
                startActivity(Intent(THIS, LostitemActivity::class.java))
            } resources.getString(R.string.Settings)->{
            startActivity(Intent(THIS, SettingActivity::class.java))
            }
            resources.getString(R.string.Emergency_Contact)->{
                startActivity(Intent(THIS, EmergencyContactActivity::class.java))
            }
            resources.getString(R.string.Panic_Situation)->{
                startActivity(Intent(THIS, PanicHistoryActivity::class.java))
            }
            resources.getString(R.string.CMS_Pages)->{

            }
            resources.getString(R.string.Logout)->{
                showAlertLogout()
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment).commit()
        homeFragment = HomeFragment()
    }

    private fun initView() {
        binding.homeActvityLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val diffScaledOffset = slideOffset * (1 - endScale)
                val offsetScale = 1 - diffScaledOffset
                binding.contentView.scaleX = offsetScale
                binding.contentView.scaleY = offsetScale
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff = binding.contentView.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                binding.contentView.translationX = xTranslation
            }

            override fun onDrawerClosed(drawerView: View) {}
        })
    }

    private fun setHomeTab() {
        binding.bottmHomeYello.visibility = VISIBLE
        binding.bottomHome.setImageResource(R.drawable.btm_home_ic)
        binding.bottomTransport.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
        binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
        binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.bottomBarCrawl.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
        binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.bottomMyProfile.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
        binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun setUpMenus(){
        //menuList.add(MenuModel(R.drawable.ic_home_menu_icon,resources.getString(R.string.home)))
        menuList.add(SideMenuModel(R.drawable.sidemenu_home3x,resources.getString(R.string.Home),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_venues,resources.getString(R.string.Venues),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_foods,resources.getString(R.string.Foods),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_event,resources.getString(R.string.Events),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_track,resources.getString(R.string.TrackNTrace),false))
        menuList.add(SideMenuModel(R.drawable.ic_favorite_sidemenu,resources.getString(R.string.Favourite),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_invited,resources.getString(R.string.Invited_BarCrawl),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_refer,resources.getString(R.string.Reffer_friend),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_lostitem,resources.getString(R.string.Lost_Item),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_setting,resources.getString(R.string.Settings),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_emrgy,resources.getString(R.string.Emergency_Contact),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_panic,resources.getString(R.string.Emergency_History),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_cms,resources.getString(R.string.CMS_Pages),false))
        menuList.add(SideMenuModel(R.drawable.sidemenu_logout,resources.getString(R.string.Logout),false))
        binding.homeActvityLayout.sideMenuRecycle.layoutManager = LinearLayoutManager(this)
        menuAdapter = SideMenuAdapter(this,menuList,this)
        binding.homeActvityLayout.sideMenuRecycle.adapter = menuAdapter
    }

    fun TextView.setDrawableColor(color: Int) {
        compoundDrawables.filterNotNull().forEach {
            it.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun closeDrawer() {
        binding.homeActvityLayout.closeDrawer(navigationView)
    }

    @SuppressLint("WrongConstant")
    private fun openDrawer() {
        binding.homeActvityLayout.openDrawer(Gravity.START)
        menuList.clear()
        setUpMenus()
    }

    @SuppressLint("WrongConstant")
    override fun onOpenMenu() {
            if(binding.homeActvityLayout.isDrawerOpen(Gravity.START)){
                closeDrawer()
            }else{
                openDrawer()
            }
    }


}