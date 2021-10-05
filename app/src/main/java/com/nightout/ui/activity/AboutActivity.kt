package com.nightout.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.AboutActviityBinding
import com.nightout.utils.AppConstant
import com.nightout.utils.CustomProgressDialog
import com.nightout.utils.Utills
import com.nightout.vendor.services.Status
import com.nightout.viewmodel.AboutViewModel
import com.nightout.viewmodel.CommonViewModel

class AboutActivity : BaseActivity() {
    lateinit var  binding : AboutActviityBinding
    private var customProgressDialog = CustomProgressDialog()
   // lateinit var aboutViewModel : AboutViewModel
   private lateinit var commonViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@AboutActivity,R.layout.about_actviity)
        commonViewModel = CommonViewModel(this@AboutActivity)
        setToolBar()
        cmsAPICAll()
    }

    private fun cmsAPICAll() {
        customProgressDialog.show(this@AboutActivity, "")
        commonViewModel.aboutCms().observe(this@AboutActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.venulistingToolBar.toolbarTitle.text = resources.getString(R.string.About)
                            binding.aboutActvityText.setText(Html.fromHtml(myData.data.about_us[0].content, Html.FROM_HTML_MODE_COMPACT))
                        } else {
                            binding.venulistingToolBar.toolbarTitle.text = resources.getString(R.string.About)
                            binding.aboutActvityText.setText(Html.fromHtml(myData.data.about_us[0].content))
                        }
                      //  binding.aboutActvityText.setText(myData.data[0].content)
                        Log.d("TAG", "user_lost_itemsAPICAll: "+myData.data)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Utills.showSnackBarOnError(
                        binding.rootLayoutAbout,
                        it.message!!,
                        this@AboutActivity
                    )
                }
            }
        })
    }

    private fun setToolBar() {
        binding.venulistingToolBar.toolbarTitle.text = "About Us"
        binding.venulistingToolBar.toolbarBack.setOnClickListener {
            finish()
        }
        binding.venulistingToolBar.toolbar3dot.visibility=GONE
        binding.venulistingToolBar.toolbarBell.visibility=GONE
    }
}