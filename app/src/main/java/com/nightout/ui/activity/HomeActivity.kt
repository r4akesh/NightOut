package com.nightout.ui.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.HomeActivityBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.ui.fragment.*
import com.nightout.utils.Util
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.home_activity.view.*

class HomeActivity : BaseActivity(), OnMenuOpenListener {

    lateinit var binding: HomeActivityBinding
    private var fragmentManager: FragmentManager? = null
    var currentFragment: Fragment? = null
    private var slidingRootNav: SlidingRootNav? = null
    var sideMenuAbout: TextView? = null
    var sideMenuTermCond: TextView? = null
    var sideMenuFaq: TextView? = null
    var sideMenuContactUs: TextView? = null
    var sideMenuSetting: TextView? = null
    var sideMenuProfile: ImageView? = null
    var sideMenuTrackTrace: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.home_activity)

        //  binding.header.headerTitle.setText("Hi, User")

        //sideMenu
        val widthRatio = Util.ratioOfScreen(this, 0.7f)
        Log.d("widthRatio_of_view", widthRatio.toString() + "")
        fragmentManager = supportFragmentManager


        slidingRootNav = SlidingRootNavBuilder(this)
            .withDragDistance(widthRatio) //Horizontal translation of a view. Default == 180dp
            .withRootViewScale(1f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
            .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
            .withRootViewYTranslation(0) //
            .withMenuLayout(R.layout.drawer_layout) // Content view's translationY will be interpolated between 0 and 4. Default ==
            .inject()
        val sideMenuLayout = findViewById<LinearLayout>(R.id.sideMenuLayoutLL)
        sideMenuLayout.layoutParams.width = (Util.getScreenWidth(this) * 0.8).toInt()
        showFragment(HomeFragment())
        inItView()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == sideMenuTrackTrace) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, TrackTrace::class.java))
        } else if (v == binding.bottomTransport) {

            binding.bottomTransport.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_yello
                )
            )
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.text_yello))

            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomBarCrawl.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomMyProfile.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))


            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is TransportFragment) {
                //appBarAndStatusBarProfile()
                // showFragmentIcon(R.drawable.btm_myprofle_ic, R.drawable.btm_tranport_ic, R.drawable.btm_home_ic, R.drawable.btm_chat_ic, R.drawable.btm_barcrawl_ic)
                showFragment(TransportFragment(this))
            }
        } else if (v == binding.bottomHomeRel) {

            binding.bottmHomeYello.visibility = VISIBLE
            binding.bottomHome.setImageResource(R.drawable.btm_home_ic)

            binding.bottomTransport.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))

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
            if (currentFragment !is HomeFragment) {
                //appBarAndStatusBarProfile()
                // showFragmentIcon(R.drawable.btm_myprofle_ic, R.drawable.btm_tranport_ic, R.drawable.btm_home_ic, R.drawable.btm_chat_ic, R.drawable.btm_barcrawl_ic)
                showFragment(HomeFragment())
            }
        } else if (v == binding.bottomChat) {

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
                //appBarAndStatusBarProfile()
                //  showFragmentIcon(R.drawable.btm_myprofle_ic, R.drawable.btm_tranport_ic, R.drawable.btm_home_ic, R.drawable.btm_chat_ic, R.drawable.btm_barcrawl_ic)
                showFragment(ChatFragment())
            }
        } else if (v == binding.bottomBarCrawl) {
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

            binding.bottomMyProfile.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))

            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is BarCrwalFragment) {

                showFragment(BarCrwalFragment())
            }
        }else if (v == binding.bottomMyProfile) {
            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomMyProfile.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_yello
                )
            )
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.text_yello))

            binding.bottomTransport.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomTransport.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomChat.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomChat.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.bottomBarCrawl.setDrawableColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_gray
                )
            )
            binding.bottomBarCrawl.setTextColor(ContextCompat.getColor(this, R.color.white))

            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is ProfileFragment) {

                showFragment(ProfileFragment(this))
            }
        } else if (v == sideMenuAbout) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, AboutActivity::class.java))
        } else if (v == sideMenuTermCond) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, TermsNCondActivity::class.java))
        } else if (v == sideMenuFaq) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, FAQActivity::class.java))
        } else if (v == sideMenuContactUs) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, ContactUsActivity::class.java))
        } else if (v == sideMenuSetting) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
        }
    }

    fun sideMenuBtnClick() {
        if (slidingRootNav!!.isMenuOpened) {
            slidingRootNav!!.closeMenu()
        } else {
            slidingRootNav!!.openMenu()
        }
    }

    fun filtterBtnClick() {
        startActivity(Intent(this@HomeActivity, FillterActvity::class.java))
    }

    fun seacrhBtnClick() {
        startActivity(Intent(this@HomeActivity, SearchLocationActivity::class.java))
        overridePendingTransition(0, 0)
    }

    //chatScreen
    fun crateGroupBtnClick() {
        startActivity(Intent(this@HomeActivity, CreateGroupActvity::class.java))
        overridePendingTransition(0, 0)
    }


    private fun inItView() {
        /*   setTouchNClick(binding.header.headerCreateGroup)
           setTouchNClick(binding.header.headerSideMenu)
           setTouchNClick(binding.header.headerSearch)
           setTouchNClick(binding.header.headerSetting)*/
        setTouchNClick(binding.bottomChat)
        setTouchNClick(binding.bottomBarCrawl)
        setTouchNClick(binding.bottomHomeRel)
        setTouchNClick(R.id.sideMenuAbout)
        setTouchNClick(binding.bottomMyProfile)
        sideMenuAbout = findViewById(R.id.sideMenuAbout)
        setTouchNClick(R.id.sideMenuTermCond)
        setTouchNClick(R.id.sideMenuTrackTrace)
        sideMenuTermCond = findViewById(R.id.sideMenuTermCond)
        setTouchNClick(R.id.sideMenuFAQ)
        sideMenuFaq = findViewById(R.id.sideMenuFAQ)
        setTouchNClick(R.id.sideMenuContactUs)
        sideMenuContactUs = findViewById(R.id.sideMenuContactUs)
        setTouchNClick(R.id.sideMenuSetting)
        sideMenuSetting = findViewById(R.id.sideMenuSetting)
        sideMenuTrackTrace = findViewById(R.id.sideMenuTrackTrace)
        setTouchNClick(binding.bottomTransport)


    }

    private fun showFragment(fragment: Fragment) {
        if (slidingRootNav!!.isMenuOpened)
            slidingRootNav!!.closeMenu()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment).commit()
    }


    private var back_pressed_time: Long = 0
    private val PERIOD: Long = 2000
    override fun onBackPressed() {
        // super.onBackPressed()
        if (back_pressed_time + PERIOD > System.currentTimeMillis())
            finishAffinity()
        else
            Toast.makeText(
                this@HomeActivity,
                getResources().getString(R.string.press_again),
                Toast.LENGTH_SHORT
            ).show();
        back_pressed_time = System.currentTimeMillis();
    }

    fun TextView.setDrawableColor(color: Int) {
        compoundDrawables.filterNotNull().forEach {
            it.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    override fun onOpenMenu() {
        if (slidingRootNav!!.isMenuOpened) {
            slidingRootNav!!.closeMenu()
        } else {
            slidingRootNav!!.openMenu()
        }
    }
}