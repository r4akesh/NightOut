package com.nightout.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
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
import kotlinx.android.synthetic.main.home_activity.view.*

class HomeActivity : BaseActivity() {

    lateinit var binding: HomeActivityBinding
    private var fragmentManager: FragmentManager? = null
    var currentFragment: Fragment? = null
    private var slidingRootNav: SlidingRootNav? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   setContentView(R.layout.home_activity)
        binding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.home_activity)
        appBarAndStatusBar()

        //sideMenu
        val widthRatio = Util.ratioOfScreen(this, 0.7f)
        Log.d("widthRatio_of_view", widthRatio.toString() + "")
        fragmentManager = supportFragmentManager

        inItView()
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
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.bottomChat) {
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            binding.header.headerTitle.setText("Chat Group")
            binding.header.headerCreateGroup.visibility = VISIBLE
            binding.header.headerSearch.visibility = GONE
            binding.header.headerNotification.visibility = GONE
            binding.header.headerSetting.visibility = GONE
            if (currentFragment !is ChatFragment) {
                //appBarAndStatusBarProfile()
                showFragmentIcon(R.drawable.btm_myprofle_ic, R.drawable.btm_tranport_ic, R.drawable.btm_home_ic, R.drawable.btm_chat_ic, R.drawable.btm_barcrawl_ic)
                showFragment(ChatFragment())
            }
        }
    }

    private fun showFragmentIcon(btmMyprofleIc: Int, btmTranportIc: Int, btmHomeIc: Int, btmChatIc: Int, btmBarcrawlIc: Int) {
        binding.bottomBar.bottomChat.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.btm_chat_ic_selected, 0, 0)
        binding.bottomBar.bottomChat.setTextColor(resources.getColor(R.color.text_yello))
        // Util.tintColor(this, binding.bottomBar.bottomChat, R.color.text_yello)
        binding.bottomBar.bottomBarCrawl.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.btm_barcrawl_ic, 0, 0)
        binding.bottomBar.bottomBarCrawl.setTextColor(resources.getColor(R.color.white))

        binding.bottomBar.bottomTransport.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.btm_tranport_ic, 0, 0)
        binding.bottomBar.bottomTransport.setTextColor(resources.getColor(R.color.white))

        binding.bottomBar.bottomMyProfile.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.btm_myprofle_ic, 0, 0)
        binding.bottomBar.bottomMyProfile.setTextColor(resources.getColor(R.color.white))

        binding.bottomBar.bottmHomeYello.visibility=GONE
    }


    private fun inItView() {
        setTouchNClick(binding.bottomChat)
        setTouchNClick(binding.bottomHome)
    }

    private fun showFragment(fragment: Fragment) {
        if (slidingRootNav!!.isMenuOpened)
            slidingRootNav!!.closeMenu()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment).commit()
    }

    private fun appBarAndStatusBar() {
        binding.header.headerTitle.setText("Hi User")
        //   binding.bottomMyProfile.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.primary_clr))
        //   binding.bottomTransport.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.primary_clr))
        //  binding.bottomHome.setColorFilter(ContextCompat.getColor(this@HomeActivity,R.color.white))
    }
}