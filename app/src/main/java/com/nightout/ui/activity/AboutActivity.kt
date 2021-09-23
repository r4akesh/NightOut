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

class AboutActivity : BaseActivity() {
    lateinit var  binding : AboutActviityBinding
    private var customProgressDialog = CustomProgressDialog()
    lateinit var aboutViewModel : AboutViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@AboutActivity,R.layout.about_actviity)
        aboutViewModel = AboutViewModel(this@AboutActivity)
        setToolBar()
        cmsAPICAll()
    }

    private fun cmsAPICAll() {
        customProgressDialog.show(this@AboutActivity, "")
        aboutViewModel.aboutCms().observe(this@AboutActivity,{
            when(it.status){
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    it.data?.let {myData->
                        var whichCMSPAGENO=0
                        if(intent.getIntExtra(AppConstant.INTENT_EXTRAS.WHICH_CMSPAGE,0)==1){
                            whichCMSPAGENO=0
                        }
                        else if(intent.getIntExtra(AppConstant.INTENT_EXTRAS.WHICH_CMSPAGE,0)==2){
                            whichCMSPAGENO=3
                        }  else if(intent.getIntExtra(AppConstant.INTENT_EXTRAS.WHICH_CMSPAGE,0)==3){
                            whichCMSPAGENO=2
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.venulistingToolBar.toolbarTitle.text = myData.data[whichCMSPAGENO].title
                            binding.aboutActvityText.setText(Html.fromHtml(myData.data[whichCMSPAGENO].content, Html.FROM_HTML_MODE_COMPACT))
                        } else {
                            binding.venulistingToolBar.toolbarTitle.text = myData.data[whichCMSPAGENO].title
                            binding.aboutActvityText.setText(Html.fromHtml(myData.data[whichCMSPAGENO].content))
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