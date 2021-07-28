package com.nightout.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.HomeActivityBinding
import com.nightout.ui.fragment.ChatFragment
import com.nightout.ui.fragment.HomeFragment
import com.nightout.utils.Util
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.home_activity.view.*

class HomeActivity : BaseActivity() {

    lateinit var binding: HomeActivityBinding
    private var fragmentManager: FragmentManager? = null
    var currentFragment: Fragment? = null
    private var slidingRootNav: SlidingRootNav? = null
    var sideMenuAbout: TextView? = null
    var sideMenuTermCond: TextView? = null
    var sideMenuTrackTrace: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   setContentView(R.layout.home_activity)
        binding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.home_activity)
        appBarAndStatusBar()


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


    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == sideMenuTrackTrace) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, TrackTrace::class.java))
        }
       else if (v == binding.header.headerSetting) {
            startActivity(Intent(this@HomeActivity, FillterActvity::class.java))
        } else if (v == binding.header.headerCreateGroup) {
            startActivity(Intent(this@HomeActivity, CreateGroupActvity::class.java))
        } else if (v == binding.bottomChat) {
            binding.header.headerMapIcon.visibility = INVISIBLE
            binding.header.headerAddrs.visibility = INVISIBLE
            binding.bottmHomeYello.visibility = GONE
            binding.bottomHome.setImageResource(R.drawable.bottom_home_unselect)

            binding.bottomChat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                R.drawable.btm_chat_ic_selected,
                0,
                0
            )
            binding.bottomChat.setTextColor(resources.getColor(R.color.text_yello))


            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            binding.header.headerTitle.setText("Chat Group")
            binding.header.headerCreateGroup.visibility = VISIBLE
            binding.header.headerSearch.visibility = GONE
            binding.header.headerNotification.visibility = GONE
            binding.header.headerSetting.visibility = GONE
            if (currentFragment !is ChatFragment) {
                //appBarAndStatusBarProfile()
                //  showFragmentIcon(R.drawable.btm_myprofle_ic, R.drawable.btm_tranport_ic, R.drawable.btm_home_ic, R.drawable.btm_chat_ic, R.drawable.btm_barcrawl_ic)
                showFragment(ChatFragment())
            }
        } else if (v == binding.bottomHomeRel) {
            binding.header.headerMapIcon.visibility = VISIBLE
            binding.header.headerAddrs.visibility = VISIBLE
            binding.bottmHomeYello.visibility = VISIBLE
            binding.bottomHome.setImageResource(R.drawable.btm_home_ic)

            //headerTop
            binding.header.headerTitle.setText("Hi, User")
            binding.header.headerCreateGroup.visibility = GONE
            binding.header.headerSearch.visibility = VISIBLE
            binding.header.headerNotification.visibility = VISIBLE
            binding.header.headerSetting.visibility = VISIBLE

            binding.bottomChat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                R.drawable.btm_chat_ic,
                0,
                0
            )
            binding.bottomChat.setTextColor(resources.getColor(R.color.white))
            //do countine


            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is HomeFragment) {
                //appBarAndStatusBarProfile()
                // showFragmentIcon(R.drawable.btm_myprofle_ic, R.drawable.btm_tranport_ic, R.drawable.btm_home_ic, R.drawable.btm_chat_ic, R.drawable.btm_barcrawl_ic)
                showFragment(HomeFragment())
            }
        } else if (v == binding.header.headerSideMenu) {
            if (slidingRootNav!!.isMenuOpened) {
                slidingRootNav!!.closeMenu()
            } else {
                slidingRootNav!!.openMenu()
            }
        } else if (v == binding.header.headerSearch) {
            startActivity(Intent(this@HomeActivity, SearchLocationActivity::class.java))
            overridePendingTransition(0, 0)
        } else if (v == sideMenuAbout) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, AboutActivity::class.java))
        } else if (v == sideMenuTermCond) {
            slidingRootNav!!.closeMenu()
            startActivity(Intent(this@HomeActivity, TermsNCondActivity::class.java))
        }
    }

    private fun showFragmentIcon(
        btmMyprofleIc: Int,
        btmTranportIc: Int,
        btmHomeIc: Int,
        btmChatIc: Int,
        btmBarcrawlIc: Int
    ) {
        binding.bottomBar.bottomChat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            R.drawable.btm_chat_ic_selected,
            0,
            0
        )
        binding.bottomBar.bottomChat.setTextColor(resources.getColor(R.color.text_yello))
        // Util.tintColor(this, binding.bottomBar.bottomChat, R.color.text_yello)
        binding.bottomBar.bottomBarCrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            R.drawable.btm_barcrawl_ic,
            0,
            0
        )
        binding.bottomBar.bottomBarCrawl.setTextColor(resources.getColor(R.color.white))

        binding.bottomBar.bottomTransport.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            R.drawable.btm_tranport_ic,
            0,
            0
        )
        binding.bottomBar.bottomTransport.setTextColor(resources.getColor(R.color.white))

        binding.bottomBar.bottomMyProfile.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            R.drawable.btm_myprofle_ic,
            0,
            0
        )
        binding.bottomBar.bottomMyProfile.setTextColor(resources.getColor(R.color.white))

        binding.bottomBar.bottmHomeYello.visibility = GONE
    }


    private fun inItView() {
        setTouchNClick(binding.header.headerCreateGroup)
        setTouchNClick(binding.bottomChat)
        //setTouchNClick(binding.bottomHome)
        setTouchNClick(binding.bottomHomeRel)
        setTouchNClick(binding.header.headerSideMenu)
        setTouchNClick(binding.header.headerSearch)
        setTouchNClick(binding.header.headerSetting)
        setTouchNClick(R.id.sideMenuAbout)
        sideMenuAbout = findViewById(R.id.sideMenuAbout)
        setTouchNClick(R.id.sideMenuTermCond)
        setTouchNClick(R.id.sideMenuTrackTrace)
        sideMenuTermCond = findViewById(R.id.sideMenuTermCond)
        sideMenuTrackTrace = findViewById(R.id.sideMenuTrackTrace)


    }

    private fun showFragment(fragment: Fragment) {
        if (slidingRootNav!!.isMenuOpened)
            slidingRootNav!!.closeMenu()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment).commit()
    }

    private fun appBarAndStatusBar() {
        binding.header.headerTitle.setText("Hi, User")
        //   binding.bottomMyProfile.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.primary_clr))
        //   binding.bottomTransport.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.primary_clr))
        //  binding.bottomHome.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.white))
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
}