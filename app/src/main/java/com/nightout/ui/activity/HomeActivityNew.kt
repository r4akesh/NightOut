package com.nightout.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
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
import com.nightout.ui.activity.CMS.AboutActivity
import com.nightout.ui.activity.CMS.ContactUsActivity
import com.nightout.ui.activity.CMS.FAQActivity
import com.nightout.ui.activity.LostItem.LostitemActivity
import com.nightout.ui.activity.Prebooking.PrebookedListActivity
import com.nightout.ui.fragment.*
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.drawer_layout_new.*
import kotlinx.android.synthetic.main.drawer_layout_new.view.*
import kotlinx.android.synthetic.main.home_actvitynew.view.*

class HomeActivityNew : BaseActivity(), OnMenuOpenListener,OnSideMenuSelectListener {
    lateinit var binding: HomeActvitynewBinding
    private val endScale = 0.95f
    private var menuList:MutableList<SideMenuModel> = ArrayList()
    private var fragmentManager: FragmentManager? = null
    lateinit var homeFragment : HomeFragment
    lateinit var menuAdapter:SideMenuAdapter
    var currentFragment: Fragment? = null
    private var customProgressDialog = CustomProgressDialog()
    lateinit var logoutViewModel: CommonViewModel

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

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.bottomMyProfile){
            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomMyProfile.setDrawableColor(ContextCompat.getColor(this, R.color.text_yello))
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.text_yello))

            binding.bottomTransport.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomBarCrawl.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.white))

            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is ProfileFragment) {
                showFragment(ProfileFragment(this))
            }
        }
        else if(v==binding.bottomTransport){
            binding.bottomTransport.setDrawableColor(ContextCompat.getColor(this, R.color.text_yello))
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.text_yello))

            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomBarCrawl.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomMyProfile.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is TransportFragment) {
                showFragment(TransportFragment(this))
            }
        }
        else if(v==binding.bottomHomeRel){
            setHomeTab()
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is HomeFragment) {
                showFragment(HomeFragment(this))
            }
        }
        else if(v==binding.bottomChat){
            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_yello))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.text_yello))

            binding.bottomTransport.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomBarCrawl.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomMyProfile.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is ChatFragment) {
                showFragment(ChatFragment(this))
            }
        }
        else if(v==binding.bottomBarCrawl){
            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomBarCrawl.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_yello
                )
            )
            binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.text_yello))

            binding.bottomTransport.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.bottomMyProfile.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is BarCrwalFragment) {
                showFragment(BarCrwalFragment(this))
            }
        }
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
            resources.getString(R.string.BookedVenue)->{
                startActivity(Intent(THIS, PrebookedListActivity::class.java))//
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
            resources.getString(R.string.About)->{
                startActivity(Intent(THIS, AboutActivity::class.java))
            }
            resources.getString(R.string.FAQ)->{
                startActivity(Intent(THIS, FAQActivity::class.java))
            }
            resources.getString(R.string.Contact_Us)->{
                startActivity(Intent(THIS, ContactUsActivity::class.java))
            }
            resources.getString(R.string.Logout)->{
                showAlertLogout()
            }
        }
    }

    private fun showAlertLogout() {
        DialogCustmYesNo.getInstance().createDialog(THIS!!,resources.getString(R.string.app_name),"Are you sure you want to logout?",object:
            DialogCustmYesNo.Dialogclick{
            override fun onYES() {
                logoutAPICall()

            }

            override fun onNO() {
                //do nothing
            }

        })
    }

    private fun logoutAPICall() {
        var map = HashMap<String,String>()
        map["device_id"] = Settings.Secure.getString(THIS!!.contentResolver, Settings.Secure.ANDROID_ID)
        customProgressDialog.show(THIS!!, "")
        logoutViewModel.logoutUser(map).observe(THIS!!,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        PreferenceKeeper.instance.isUserLogin=false
                        startActivity(Intent(THIS,LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))

                        finish()
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()


                }
            }
        })
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

        logoutViewModel = CommonViewModel(THIS!!)
        //setUserName
        binding.homeActvityLayout.sidemenu_email.text = PreferenceKeeper.instance.loginResponse?.email
        binding.homeActvityLayout.sidemenu_username.text = PreferenceKeeper.instance.loginResponse?.first_name+" "+PreferenceKeeper.instance.loginResponse?.last_name
        Utills.setImage(THIS, binding.homeActvityLayout.sidemenu_profile,PreferenceKeeper.instance.loginResponse?.profile)

        setTouchNClick(binding.bottomMyProfile)
        setTouchNClick(binding.bottomTransport)
        setTouchNClick(binding.bottomHomeRel)
        setTouchNClick(binding.bottomChat)
        setTouchNClick(binding.bottomBarCrawl)
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
        menuList.add(SideMenuModel(R.drawable.sidemenu_event,resources.getString(R.string.BookedVenue),false))
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
      //  menuList.clear()
      //  setUpMenus()
    }

    @SuppressLint("WrongConstant")
    override fun onOpenMenu() {
            if(binding.homeActvityLayout.isDrawerOpen(Gravity.START)){
                closeDrawer()
            }else{
                openDrawer()
            }
    }


    private var back_pressed_time: Long = 0
    private val PERIOD: Long = 2000
    @SuppressLint("WrongConstant")
    override fun onBackPressed() {
        if (back_pressed_time + PERIOD > System.currentTimeMillis())
                finishAffinity()
            else {
                if(binding.homeActvityLayout.isDrawerOpen(Gravity.START)){
                    closeDrawer()
                } else {
                    Utills.showWarningToast(THIS!!,getResources().getString(R.string.press_again))
                    back_pressed_time = System.currentTimeMillis()
                }
            }
    }
}