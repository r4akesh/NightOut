package com.nightout.ui.activity

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.TicketconfirmActvityBinding
import com.nightout.model.VenuDetailModel
import com.nightout.utils.AppConstant
import com.nightout.utils.DialogCustmYesNo
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper

class TicketConfirmActvity : BaseActivity() {
    lateinit var binding: TicketconfirmActvityBinding
    lateinit var pojoEvntDetl: VenuDetailModel.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@TicketConfirmActvity, R.layout.ticketconfirm_actvity)
        pojoEvntDetl = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.EVENTDETAIL_POJO) as VenuDetailModel.Data
        initView()
        setData()
    }

    private fun setData() {
        try {
            binding.eventName.text = pojoEvntDetl.store_name
            binding.eventTime.text = "Start at :  ${pojoEvntDetl.event_start_time} To ${pojoEvntDetl.event_end_time}"
            binding.eventDate.text = pojoEvntDetl.event_date
            binding.eventAddrs.text = pojoEvntDetl.store_address
            binding.eventPrice.text = resources.getString(R.string.currency_sumbol) + intent.getStringExtra(AppConstant.INTENT_EXTRAS.TOTAL_AMT)
            binding.eventUser.text = PreferenceKeeper.instance.loginResponse?.name
            binding.tickeOrderID.text = "Ticket ID : " + intent.getStringExtra(AppConstant.INTENT_EXTRAS.TICKET_NO)
            //  binding.transID.text =  "Transaction ID : "+intent.getStringExtra(AppConstant.INTENT_EXTRAS.TRANSACTION_ID)
        } catch (e: Exception) {
        }
    }

    private fun initView() {
        setTouchNClick(binding.ticketDownload)
        setTouchNClick(binding.tickeCnfrmClose)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.ticketDownload) {
            // startActivity(Intent(this@TicketConfirmActvity,CongrtulationActvity::class.java))
            if (MyApp.isConnectingToInternet(this@TicketConfirmActvity)) {
                try {
                    var urlTicket = PreferenceKeeper.instance.imgPathSave + intent.getStringExtra(AppConstant.INTENT_EXTRAS.TICKET_URL)
                    var manager: DownloadManager? = null
                    Log.d("ok", "urlTicket: " + urlTicket)
                    if (intent.getStringExtra(AppConstant.INTENT_EXTRAS.TICKET_URL)!!.isNotBlank()) {
                        manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        val uri: Uri = Uri.parse(urlTicket)
                        val fileName: String = urlTicket.substring(urlTicket.lastIndexOf('/') + 1)
                        val request = DownloadManager.Request(uri)

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                        val reference = manager!!.enqueue(request)
                        DialogCustmYesNo.getInstance().createDialogOK(this@TicketConfirmActvity,"Info","Ticket has been saved storage/emulated/0/Download/"+fileName,object : DialogCustmYesNo.Dialogclick{
                            override fun onYES() {
                                jumpToHome()
                            }

                            override fun onNO() {

                            }


                        })

                    }else{
                        MyApp.popErrorMsg("","URL not valid",THIS!!)
                    }
                } catch (e: Exception) {
                    MyApp.popErrorMsg("","URL not valid $e.toString()",THIS!!)
                }

            }

        }
        else if(v==binding.tickeCnfrmClose){
            jumpToHome()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        jumpToHome()

    }

    private fun jumpToHome() {
        val i = Intent(this, HomeActivityNew::class.java)
        // set the new task and clear flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}