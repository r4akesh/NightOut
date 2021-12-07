package com.nightout.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.HomeActivityBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.ui.activity.CMS.AboutActivity
import com.nightout.ui.activity.CMS.ContactUsActivity
import com.nightout.ui.activity.CMS.FAQActivity
import com.nightout.ui.activity.CMS.TermsNCondActivity
import com.nightout.ui.activity.LostItem.LostitemActivity
import com.nightout.ui.fragment.*
import com.nightout.utils.*
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.CommonViewModel
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.home_activity.view.*
import kotlinx.android.synthetic.main.row_terms_cms_layout.view.*

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
    var sideMenuHome: TextView? = null
    var sideMenuVenues: TextView? = null
    var sideMenuEmrgyHistry: TextView? = null
    var sideMenuEmrgyContact: TextView? = null
    var sideMenuLogout: TextView? = null
    var sideMenuTrackTrace: TextView? = null
    var sidemenuCms: TextView? = null
    var sideMenuCmsArrowImg: ImageView? = null
    var sideMenuEvent: TextView? = null
    var sideMenuFood: TextView? = null
    var sideMenuInvite: TextView? = null
    var sideMenuFav: TextView? = null
    var sideMenuLostItem: TextView? = null
    var sidemenu_username: TextView? = null
    var sidemenu_email: TextView? = null
    var sideMenuFAQ: TextView? = null
    var sideMenuCmsLinear: LinearLayout? = null
    var sidemenu_profile: CircleImageView? = null
    lateinit var homeFragment : HomeFragment
    private var customProgressDialog = CustomProgressDialog()
    lateinit var logoutViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.home_activity)
        fragmentManager = supportFragmentManager
        setSideMenu()
        setHomeTab()
        showFragment(HomeFragment(this))
        inItView()
        Log.d("TAG", "onCreate: "+Settings.Secure.getString(this@HomeActivity?.contentResolver, Settings.Secure.ANDROID_ID))

    }

    private fun setSideMenu() {
        //sideMenu
        val widthRatio = Utills.ratioOfScreen(this, 0.7f)
        slidingRootNav = SlidingRootNavBuilder(this)
            .withDragDistance(widthRatio) //Horizontal translation of a view. Default == 180dp
            .withRootViewScale(1f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
            .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
            .withRootViewYTranslation(0) //
            .withMenuLayout(R.layout.drawer_layout) // Content view's translationY will be interpolated between 0 and 4. Default ==
            .inject()
        //  val sideMenuLayout = findViewById<LinearLayout>(R.id.sideMenuLayoutLL)
        //  sideMenuLayout.layoutParams.width = (Util.getScreenWidth(this) * 0.8).toInt()


    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        super.onClick(v)

        if (v == binding.bottomTransport) {
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
        } else if (v == binding.bottomHomeRel) {
            setHomeTab()
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is HomeFragment) {
                showFragment(HomeFragment(this))
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
                showFragment(ChatFragment(this))
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
            binding.bottomMyProfile.setDrawableColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.bottomMyProfile.setTextColor(ContextCompat.getColor(this, R.color.white))
            currentFragment = fragmentManager!!.findFragmentById(R.id.mainContainer)
            if (currentFragment !is BarCrwalFragment) {
                showFragment(BarCrwalFragment(this))
            }
        } else if (v == binding.bottomMyProfile) {
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
        }

       else  if (v == sideMenuTrackTrace) {
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuTrackTrace?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, TrackTrace::class.java))

        }
        else  if (v == sidemenuCms) {
            setBtnBgBlank()
            sidemenuCms?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            transitionAnim(sideMenuCmsLinear,sideMenuCmsArrowImg)

        }
        else if(v==sideMenuFood){
            //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuFood?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, VenuListActvity::class.java)
                .putExtra(AppConstant.INTENT_EXTRAS.StoreType,"4"))

        }
        else if(v==sideMenuEvent){
           // slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuEvent?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
                    startActivity(Intent(this@HomeActivity, VenuListActvity::class.java)
                        .putExtra(AppConstant.INTENT_EXTRAS.StoreType,"5"))//

        }
        else if(v==sideMenuLostItem){
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuLostItem?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, LostitemActivity::class.java)
             )

        }

        else if(v==sideMenuInvite){
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuInvite?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity,  SideMenuShredListActivity::class.java))

        }
        else if(v==sideMenuFav){
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuFav?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, FavListActivity::class.java))

        }

        else if(v==sideMenuEmrgyContact){
            //slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuEmrgyContact?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, EmergencyContactActivity::class.java))

        }
        else if(v==sideMenuVenues){
            //slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuVenues?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, VenuListActvity::class.java)
                .putExtra(AppConstant.INTENT_EXTRAS.StoreType,"1"))//here 1 is default

        }
        else if(v==sideMenuEmrgyHistry){
           // slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuEmrgyHistry?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, PanicHistoryActivity::class.java))

        }
 
        else if(v==sideMenuHome){
           slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuHome?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            showFragment(HomeFragment(this))
            setHomeTab()
        }

        else if (v == sideMenuAbout) {
           // slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuAbout?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, AboutActivity::class.java)
             )

        } else if (v == sideMenuTermCond) {
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuTermCond?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, TermsNCondActivity::class.java))
        } else if (v == sideMenuFaq) {
           // slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuFaq?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, FAQActivity::class.java))
        } else if (v == sideMenuContactUs) {
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuContactUs?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, ContactUsActivity::class.java))
        } else if (v == sideMenuSetting) {
           // slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuSetting?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
            startActivity(Intent(this@HomeActivity, SettingActivity::class.java))

        }
        else if (v == sideMenuLogout) {
          //  slidingRootNav!!.closeMenu()
            setBtnBgBlank()
            sideMenuLogout?.setBackgroundResource(R.drawable.gredient_bg_nocorner)
           showAlertLogout()
        }
    }

    private fun transitionAnim(mainContainerView: LinearLayout?, button: ImageView?) {
        if (mainContainerView?.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(mainContainerView, AutoTransition())
            val rotate = ObjectAnimator.ofFloat(button, "rotation", 0f, 90f)
            rotate.duration = 500
            rotate.start()
            mainContainerView.visibility = View.VISIBLE
        } else {
            val rotate = ObjectAnimator.ofFloat(button, "rotation", 90f, 0f)
            rotate.duration = 500
            rotate.start()
            mainContainerView?.visibility = View.GONE
        }
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

    private fun showAlertLogout() {
        DialogCustmYesNo.getInstance().createDialog(this@HomeActivity,resources.getString(R.string.app_name),"Are you sure you want to logout?",object:
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
        map["device_id"] = Settings.Secure.getString(this@HomeActivity?.contentResolver, Settings.Secure.ANDROID_ID)
        customProgressDialog.show(this@HomeActivity, "")
        logoutViewModel.logoutUser(map).observe(this@HomeActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        PreferenceKeeper.instance.isUserLogin=false
                        startActivity(Intent(this@HomeActivity,LoginActivity::class.java)
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

    private fun setBtnBgBlank() {
        sideMenuEmrgyContact?.setBackgroundResource(0)
        sideMenuLostItem?.setBackgroundResource(0)
        sideMenuTrackTrace?.setBackgroundResource(0)
        sidemenuCms?.setBackgroundResource(0)
        sideMenuVenues?.setBackgroundResource(0)
        sideMenuEmrgyHistry?.setBackgroundResource(0)
        sideMenuHome?.setBackgroundResource(0)
        sideMenuAbout?.setBackgroundResource(0)
        sideMenuTermCond?.setBackgroundResource(0)
        sideMenuFaq?.setBackgroundResource(0)
        sideMenuContactUs?.setBackgroundResource(0)
        sideMenuFood?.setBackgroundResource(0)
        sideMenuInvite?.setBackgroundResource(0)
        sideMenuFav?.setBackgroundResource(0)
        sideMenuEvent?.setBackgroundResource(0)
        sideMenuSetting?.setBackgroundResource(0)
        sideMenuLogout?.setBackgroundResource(0)
    }


    private fun inItView() {
        logoutViewModel = CommonViewModel(this@HomeActivity)
        sideMenuCmsArrowImg = findViewById(R.id.sideMenuCmsArrowImg)
        sideMenuCmsLinear = findViewById(R.id.sideMenuCmsLinear)
        sideMenuAbout = findViewById(R.id.sideMenuAbout)
        sideMenuTermCond = findViewById(R.id.sideMenuTermCond)
        sideMenuFaq = findViewById(R.id.sideMenuFAQ)
        sideMenuContactUs = findViewById(R.id.sideMenuContactUs)
        sideMenuSetting = findViewById(R.id.sideMenuSetting)
        sideMenuTrackTrace = findViewById(R.id.sideMenuTrackTrace)
        sidemenuCms = findViewById(R.id.sidemenuCms)
        sideMenuHome = findViewById(R.id.sideMenuHome)
        sideMenuEvent = findViewById(R.id.sideMenuEvent)
        sideMenuFood = findViewById(R.id.sideMenuFood)
        sideMenuInvite = findViewById(R.id.sideMenuInvite)
        sideMenuFav = findViewById(R.id.sideMenuFav)
        sideMenuLostItem = findViewById(R.id.sideMenuLostItem)
        sideMenuVenues = findViewById(R.id.sideMenuVenues)
        sideMenuEmrgyHistry = findViewById(R.id.sideMenuEmrgyHistry)
        sideMenuEmrgyContact = findViewById(R.id.sideMenuEmrgyContact)
        sideMenuLogout = findViewById(R.id.sideMenuLogout)
        sidemenu_username = findViewById(R.id.sidemenu_username)
        sidemenu_email = findViewById(R.id.sidemenu_email)
        sideMenuFAQ = findViewById(R.id.sideMenuFAQ)
        sidemenu_profile = findViewById(R.id.sidemenu_profile)

        setTouchNClick(binding.bottomChat)
        setTouchNClick(binding.bottomBarCrawl)
        setTouchNClick(binding.bottomHomeRel)
        setTouchNClick(binding.bottomMyProfile)
        setTouchNClick(binding.bottomTransport)
        setTouchNClick(sideMenuTermCond)
        setTouchNClick(sideMenuAbout)
        setTouchNClick(sideMenuHome)
        setTouchNClick(sideMenuTrackTrace)
        setTouchNClick(sidemenuCms)
        setTouchNClick(sideMenuFAQ)
        setTouchNClick(sideMenuContactUs)
        setTouchNClick(sideMenuSetting)
        setTouchNClick(sideMenuVenues)
        setTouchNClick(sideMenuEmrgyHistry)
        setTouchNClick(sideMenuEmrgyContact)
        setTouchNClick(sideMenuFood)
        setTouchNClick(sideMenuInvite)
        setTouchNClick(sideMenuFav)
        setTouchNClick(sideMenuLostItem)
        setTouchNClick(sideMenuEvent)
        setTouchNClick(sideMenuLogout)

        sidemenu_email?.setText(PreferenceKeeper.instance.loginResponse?.email)
        sidemenu_username?.setText(PreferenceKeeper.instance.loginResponse?.first_name+" "+PreferenceKeeper.instance.loginResponse?.last_name)
        Utills.setImage(this@HomeActivity,sidemenu_profile,PreferenceKeeper.instance.loginResponse?.profile)
    }

    private fun showFragment(fragment: Fragment) {
        if (slidingRootNav!!.isMenuOpened)
            slidingRootNav!!.closeMenu()
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment).commit()
        homeFragment = HomeFragment()
    }


    private var back_pressed_time: Long = 0
    private val PERIOD: Long = 2000
    override fun onBackPressed() {
        // super.onBackPressed()
        if (back_pressed_time + PERIOD > System.currentTimeMillis())
            finishAffinity()
        else {
            if (slidingRootNav!!.isMenuOpened) {
                slidingRootNav!!.closeMenu()
            } else {
                Toast.makeText(this@HomeActivity, getResources().getString(R.string.press_again), Toast.LENGTH_SHORT).show()
                back_pressed_time = System.currentTimeMillis()
            }
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        homeFragment.onActivityResultMy(requestCode, resultCode, data)
    }
}