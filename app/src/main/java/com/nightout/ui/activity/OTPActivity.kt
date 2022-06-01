package com.nightout.ui.activity


import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.OtpActvityBinding
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.vendor.viewmodel.OtpViewModel

class OTPActivity : BaseActivity() {

    lateinit var binding : OtpActvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@OTPActivity,R.layout.otp_actvity)
        initView()
        setPinView()
        showTimer()
//        var mob = intent.getStringExtra(AppConstant.INTENT_EXTRAS.MOBILENO)
//        Log.d("TAG", "onCreate: "+mob)


    }

    private fun setPinView() {
        binding.otpPinView.setPasswordHidden(false)
        binding.otpPinView.isCursorVisible = false
        binding.otpPinView.setCursorColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
        binding.otpPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("TAG", "afterTextChanged: "+s.toString())
            }

        })
    }

     fun showTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes: Long = millisUntilFinished / 1000 / 60
                val seconds: Long = millisUntilFinished / 1000 % 60
                if(seconds.toString().length==1)
                binding.otpTimer.setText("00 : 0" + seconds)
                else
                binding.otpTimer.setText("00 : " + seconds)
            }

            override fun onFinish() {
                binding.otpDidnot.visibility=VISIBLE
                binding.otpActivitySendAgain.visibility=VISIBLE

                Log.d("seconds remaining: ", "DONE")
            }
        }.start()
    }

    private fun initView() {
        setTouchNClick(binding.submitBtn)
        setTouchNClick(binding.otpActivitySendAgain)
        setTouchNClick(binding.otpActvityChange)
        setTouchNClick(binding.otpActivityBakBtn)

        var mobNo: String? = intent.getStringExtra(AppConstant.INTENT_EXTRAS.MOBILENO)
        if (mobNo!!.isNotEmpty()) {
            var mob1 = ""
            var mob2 = ""
            if (mobNo.length > 2) {
                mob2 = mobNo.substring(mobNo.length - 2)
                mob1 = mobNo.substring(1, 3)
            }
            binding.loginPhOtpText.setText(resources.getString(R.string.entrotp_recivedmobno) + " +44 " + mob1 + "******" + mob2)
        }


        var str1 = resources.getString(R.string.SendAgain)
        var settext = "<font color='#ffc800'><u>$str1 </u></font>"
        binding.otpActivitySendAgain.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

         str1 = resources.getString(R.string.Change)
          settext = "<font color='#ffc800'><u>$str1 </u></font>"
        binding.otpActvityChange.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

        binding.otpHandler = MyApp.getOtpHandler(this,intent.getStringExtra(AppConstant.INTENT_EXTRAS.MOBILENO)!!,intent.getStringExtra(AppConstant.INTENT_EXTRAS.EMAILID)!!)
        binding.otpviewmodel = OtpViewModel(this)

    }

    /*override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        try {
            runOnUiThread {
                val gson = Gson()
                if (ResponseType.RESPONSE_TYPE_LOGIN.equalsTo(type) || ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE.equalsTo(type)) {
                    val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                    val fsUsersModelResponseModel: ResponseModel<FSUsersModel> = gson.fromJson<ResponseModel<FSUsersModel>>(response, type1)
                    if (fsUsersModelResponseModel.getStatus_code() == 200) {
                        //UserDetails.instance.myDetail = fsUsersModelResponseModel.getData()
                        PreferenceKeeper.instance.myUserDetail = fsUsersModelResponseModel.getData()
                         PreferenceKeeper.instance.loginUser(THIS!!, fsUsersModelResponseModel.getData())
                      //  Toast.makeText(this@OTPActivity, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show()
                        PreferenceKeeper.instance.isUserLogin = true
                        Utills.showSuccessToast(this@OTPActivity,""+fsUsersModelResponseModel.getMessage())
                           startActivity(Intent(this@OTPActivity, HomeActivityNew::class.java))
                          finish()
                        //    startActivity(Intent(this@LoginActivity, RoomListActivity::class.java))
                        // finish()
                    }
                    else if(fsUsersModelResponseModel.getStatus_code() == 404){

                    }
                    else {
                        Toast.makeText(this@OTPActivity, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("ok chat", "onWebSocketResponse: $type")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override val activityName: String=OTPActivity::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_LOGIN,
            ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE
        )
    }*/



}