package com.nightout.handlers


import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.nightout.chat.chatinterface.ResponseType
import com.nightout.chat.chatinterface.WebSocketObserver
import com.nightout.chat.chatinterface.WebSocketSingleton
import com.nightout.chat.model.ResponseModel
import com.nightout.model.FSUsersModel
import com.nightout.model.LoginModel
import com.nightout.ui.activity.HomeActivityNew
import com.nightout.ui.activity.OTPActivity
import com.nightout.utils.CustomProgressDialog

import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills
import com.nightout.vendor.services.APIClient
import com.nightout.vendor.services.Status
import com.nightout.vendor.viewmodel.OtpViewModel
import org.json.JSONException
import org.json.JSONObject
import com.google.gson.reflect.TypeToken
import kotlin.math.log

open class OtpHandler(val activity: OTPActivity, var mobNo: String,var email: String):
    WebSocketObserver {
    private lateinit var regViewModel: OtpViewModel
    private val progressDialog = CustomProgressDialog()

    fun onClickSubmit(regViewModel: OtpViewModel) {
        this.regViewModel = regViewModel
        MyApp.hideSoftKeyboard(activity)
        if (regViewModel.isValidation(activity)) {
            Log.d("TAG", "isValide done: ")
            val map = HashMap<String, String>()
            var mobNo = mobNo
            mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
            map["otp"] = regViewModel.otp!!
            map["phonenumber"] = mobNo
            map["device_id"] = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
            map["device_type"] = "1"
            map["email"] = email
            otpCall(map, activity)
            WebSocketSingleton.getInstant()!!.register(this)
        }
    }

   lateinit var userData : LoginModel.Data
    private fun otpCall(map: HashMap<String, String>, activity: OTPActivity) {
        progressDialog.show(activity)
        regViewModel.otp(map).observe(activity) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    //progressBar.visibility = View.GONE
                    it.data?.let {
                        var logModel: LoginModel.Data = it.data
                        PreferenceKeeper.instance.bearerTokenSave = logModel.token
                        PreferenceKeeper.instance.loginResponse = logModel
                        PreferenceKeeper.instance.myUserDetail = FSUsersModel()
                        userData = it.data
                        fetchLoginAPI()
//                            Utills.showSuccessToast(activity,it.message)
//                          activity.startActivity(Intent(activity, HomeActivityNew::class.java))
//                          activity.finish()
                    }

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    MyApp.popErrorMsg("", "" + it.message!!, activity)
                    //   Toast.makeText(activity, "" + it.message!!, Toast.LENGTH_LONG).show()
                    //  Utills.showErrorToast(activity, it.message!!)
                    activity.binding.otpPinView.setText("")

                }
            }
        }
    }



    private fun fetchLoginAPI() {
        val jsonObject = JSONObject()
            try {
                jsonObject.put("userId", userData.id)
                jsonObject.put("userName", userData.email)
                jsonObject.put("firstName", userData.name)
                jsonObject.put("password", "1111")
                jsonObject.put("fcm_token", PreferenceKeeper.instance.fcmTokenSave)
                jsonObject.put("type", "loginOrCreate")
                jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_LOGIN)
                //            mWaitingDialog.show();
                WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
                Log.d("ok", "fetchLoginAPI55: ")
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d("ok", "fetchLoginAPI222: ")
            }


         }



    fun sendAgain(regViewModel: OtpViewModel){
          progressDialog.show(activity)
          activity.binding.otpActivitySendAgain.visibility = View.GONE
          activity.binding.otpDidnot.visibility = View.GONE
          activity.showTimer()
          val map = HashMap<String, String>()
          var mobNo = mobNo
          mobNo = mobNo.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()
          map["phonenumber"] = mobNo
          map["device_id"] = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
          map["device_type"] = "1"
          map["email"] = email
          regViewModel.otpResend(map).observe(activity) {
              when (it.status) {
                  Status.SUCCESS -> {
                      progressDialog.dialog.dismiss()
                      it.data?.let {

                      }
                      Utills.showSuccessToast(
                          activity,
                          it.data?.message!!,

                          )
                  }
                  Status.LOADING -> {
                      //progressBar.visibility = View.VISIBLE

                  }
                  Status.ERROR -> {
                      progressDialog.dialog.dismiss()
                      Utills.showErrorToast(activity, it.message!!)
                  }
              }
          }
    }

    fun bakPressd(){
        activity.finish()
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        try {
            activity.runOnUiThread {
                val gson = Gson()
                println("received message otp: $response")
                if (ResponseType.RESPONSE_TYPE_LOGIN.equalsTo(type) || ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE.equalsTo(type)) {
                    val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                    val fsUsersModelResponseModel: ResponseModel<FSUsersModel> = gson.fromJson<ResponseModel<FSUsersModel>>(response, type1)
                    if (fsUsersModelResponseModel.getStatus_code() == 200) {
                        //UserDetails.instance.myDetail = fsUsersModelResponseModel.getData()
                        PreferenceKeeper.instance.myUserDetail = fsUsersModelResponseModel.getData()
                        PreferenceKeeper.instance.loginUser(activity, fsUsersModelResponseModel.getData())
                        //  Toast.makeText(this@OTPActivity, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show()
                        PreferenceKeeper.instance.isUserLogin = true
                        Utills.showSuccessToast(activity,""+fsUsersModelResponseModel.getMessage())
                        activity.startActivity(Intent(activity, HomeActivityNew::class.java))
                        activity.finish()
                        //    startActivity(Intent(this@LoginActivity, RoomListActivity::class.java))
                        // finish()
                    }
                    else if(fsUsersModelResponseModel.getStatus_code() == 404){
                        Log.d("ok chat", "fetchLoginAPI call333: $type")
                        fetchLoginAPI()
                    }
                    else {
                        Log.d("ok chat", "elseeee: $type")
                        Toast.makeText(activity, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("ok chat", "onWebSocketResponse: $type")
                }
            }
        } catch (e: Exception) {
            Log.d("ok chat", "Exception: $type"+e)
            e.printStackTrace()
        }
    }

    override val activityName:  String = OTPActivity::class.java.name


    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
            ResponseType.RESPONSE_TYPE_LOGIN,
            ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE
        )
    }

}