package com.nightout.utils



import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.util.DisplayMetrics

import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nightout.handlers.EditProfileHandler
import com.nightout.handlers.LoginHandler
import com.nightout.handlers.OtpHandler
import com.nightout.handlers.RegisterHandler
import com.nightout.model.VenuListModel
import com.nightout.ui.activity.EditProfileActivity
import com.nightout.ui.activity.LoginActivity
import com.nightout.ui.activity.OTPActivity
import com.nightout.ui.activity.RegisterActivity
import com.nightout.ui.fragment.HomeFragment


import dmax.dialog.SpotsDialog
import java.io.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


class MyApp : Application() {
    //private static HttpProxyCacheServer proxy;

    override fun onCreate() {
        super.onCreate()

        // init preference keeper
        PreferenceKeeper.setContext(applicationContext)
        application = this
        ctx = applicationContext
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }


    companion object {
        private var spotsDialog: SpotsDialog? = null
        private lateinit var application: MyApp
        private lateinit var dialog: Dialog
        private var ctx: Context? = null

        var SHARED_PREF_NAME = "Brng_Pref"

        fun getEditProfile(loginActivity: EditProfileActivity): EditProfileHandler {
            return EditProfileHandler(loginActivity)
        }



        fun getLoginHandler(loginActivity: LoginActivity): LoginHandler {
            return LoginHandler(loginActivity)
        }

        fun getRegHandler(registerActivity: RegisterActivity): RegisterHandler {
            return RegisterHandler(registerActivity)
        }

        fun getOtpHandler(otpActivity: OTPActivity, mobNo:String,email:String): OtpHandler {
            return OtpHandler(otpActivity,mobNo,email)
        }

       /* fun getHomeHandler(homeFragment: HomeFragment): HomeHandler {
            return HomeHandler(homeFragment)
        }*/

        open fun showSoftKeyboard(activity: Activity) {
            val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideSoftKeyboard(activity: Activity) {
            try {
                val inputMethodManager: InputMethodManager = activity
                    .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun getDate(time: Long): String {
            val format = SimpleDateFormat("yyyy/MM/dd")
            var current = format.format(time)
            current = current.replace("/", "-")
            return current

        }

        fun getDateVariation(variation: Int?, currentDate: Date?): Date? {
            val c: Calendar = Calendar.getInstance()
            c.setTime(currentDate)
            c.add(Calendar.DAY_OF_YEAR, +variation!!)
            return c.getTime()
        }

        fun preventDoubleClick(view: View) {
            view.isClickable = false
            view.postDelayed({ view.isClickable = true }, 1000)
        }


        fun dateZonetoDateFormat(date: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
            val date: Date =
                dateFormat.parse(date) //You will get date object relative to server/client timezone wherever it is parsed
            // dateFormat.parse("2017-04-26T20:55:00.000Z") //You will get date object relative to server/client timezone wherever it is parsed
            //2021-04-20T08:27:38.000000Z
            val formatter =
                SimpleDateFormat("MMM dd, yyyy HH:mm") //If you need time just put specific format for time like 'HH:mm:ss'

            val dateStr: String = formatter.format(date)
            return dateStr
        }

        fun dateZonetoDateFormat2(date: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date =
                dateFormat.parse(date) //You will get date object relative to server/client timezone wherever it is parsed
            // dateFormat.parse("2017-04-26T20:55:00.000Z") //You will get date object relative to server/client timezone wherever it is parsed
            //2021-04-20T08:27:38.000000Z
            val formatter =
                SimpleDateFormat("MMM dd, yyyy HH:mm aa") //If you need time just put specific format for time like 'HH:mm:ss'

            val dateStr: String = formatter.format(date)
            return dateStr
        }

        //  2021-04-22 10:11:33

        fun isValidLatLng(lat: Double?, lng: Double?): Boolean {
            lat ?: return false
            lng ?: return false
            if (lat < -90 || lat > 90) {
                return false
            } else if (lng < -180 || lng > 180) {
                return false
            }
            return true
        }


        fun popErrorMsg(
            titleMsg: String, errorMsg: String,
            context: Context
        ) {
            // pop error message
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(titleMsg).setMessage(errorMsg)
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }

            val alert = builder.create()
            alert.show()
        }

        fun ShowMassage(ctx: Context, msg: String) {
            val builder = AlertDialog.Builder(ctx)
            builder.setTitle(null).setMessage(msg)
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }

            val alert = builder.create()
            alert.show()
        }

        fun ShowTost(ctx1: Context, msg1: String) {
            Toast.makeText(ctx1, "" + msg1, Toast.LENGTH_SHORT).show()
        }


        fun getSharedPrefLong(preffConstant: String): Long {
            var longValue: Long = 0
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            longValue = sp.getLong(preffConstant, 0)
            return longValue
        }

        fun setSharedPrefLong(preffConstant: String, longValue: Long) {
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            val editor = sp.edit()
            editor.putLong(preffConstant, longValue)
            editor.commit()
        }

        fun getSharedPrefString(preffConstant: String): String {
            var stringValue: String? = ""
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            stringValue = sp.getString(preffConstant, "")
            return stringValue ?: ""
        }

        fun setSharedPrefString(
            preffConstant: String,
            stringValue: String
        ) {
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            val editor = sp.edit()
            editor.putString(preffConstant, stringValue)
            editor.commit()
        }

        fun getSharedPrefInteger(preffConstant: String): Int {
            var intValue = 0
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            intValue = sp.getInt(preffConstant, 0)
            return intValue
        }

        fun setSharedPrefInteger(preffConstant: String, value: Int) {
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            val editor = sp.edit()
            editor.putInt(preffConstant, value)
            editor.commit()
        }

        fun getSharedPrefFloat(preffConstant: String): Float {
            var floatValue = 0f
            val sp = application.getSharedPreferences(
                preffConstant, 0
            )
            floatValue = sp.getFloat(preffConstant, 0f)
            return floatValue
        }

        fun setSharedPrefFloat(preffConstant: String, floatValue: Float) {
            val sp = application.getSharedPreferences(
                preffConstant, 0
            )
            val editor = sp.edit()
            editor.putFloat(preffConstant, floatValue)
            editor.commit()
        }


        fun getStatus(name: String): Boolean {
            val status: Boolean
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            status = sp.getBoolean(name, false)
            return status
        }

        fun setStatus(name: String, istrue: Boolean) {
            val sp = application.getSharedPreferences(
                SHARED_PREF_NAME, 0
            )
            // String e = sp.getString(Constants.STATUS, null);
            val editor = sp.edit()
            editor.putBoolean(name, istrue)
            editor.commit()
        }

        fun isValidEmail(target: CharSequence): Boolean {
            return if (target == null) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches()
            }
        }

        @JvmStatic
        fun isConnectingToInternet(context: Context): Boolean {
            var connected = false
            val connectivity = context
                .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.activeNetworkInfo
            connected = info != null && info.isConnected && info.isAvailable
            return connected
        }

        /*    @JvmStatic
        fun showAlrtDialg(context: Context?, msg: String?) {
            spotsDialog = SpotsDialog(context, msg, R.style.Custom)
            spotsDialog!!.setCancelable(false)
            spotsDialog!!.show()
        }

        @JvmStatic
        fun showAlrtDialgWhite(context: Context?, msg: String?) {
            spotsDialog = SpotsDialog(context, msg, R.style.Custom_white)
            spotsDialog!!.setCancelable(false)
            spotsDialog!!.show()
        }

        @JvmStatic
        fun dismisAlrtDialog() {
            try {
                if(spotsDialog==null){
                    Log.d("TAG", "dismisAlrtDialog: null")
                }else{
                    Log.d("TAG", "dismisAlrtDialog: 1")
                }
                spotsDialog!!.dismiss()
            } catch (e: Exception) {
                Log.d("TAG", "dismisAlrtDialog: 2"+e.toString())
            }
        }*/

        @JvmStatic
        fun createDrawableFromView(activity: Activity, view: View): Bitmap? {
            try {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                view.setLayoutParams(
                    RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
                view.layout(0, 0, displayMetrics.widthPixels * 2, displayMetrics.heightPixels * 2)
                view.buildDrawingCache()
                val bitmap = Bitmap.createBitmap(
                    view.getMeasuredWidth(),
                    view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                return bitmap

/* if (view.getMeasuredHeight() <= 0) {
view.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
Canvas c = new Canvas(b);
view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
view.draw(c);
return b;
}*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


     /*        @SuppressLint("SdCardPath")
        fun writeVenuesList(hMap: ArrayList<VenuListModel.Data>) {
            val path: String
            try {
                path = "/data/data/" + ctx!!.packageName + "/VenuList.ser"
                val f = File(path)
                if (f.exists()) {
                    f.delete()
                    println("old file deleted>>>>>>>>> ")
                }
                val fileOut = FileOutputStream(path)
                val out = ObjectOutputStream(fileOut)
                out.writeObject(hMap)
                out.close()
                fileOut.close()
                println("my file replaced>>>>>>>>> ")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        @SuppressLint("SdCardPath")
        fun readVenuList():  ArrayList<VenuListModel.Data> {
            val path: String
            path =
                "/data/data/" + ctx!!.getPackageName() + "/VenuList.ser"
            val f = File(path)
            var hMap:  ArrayList<VenuListModel.Data> =  ArrayList<VenuListModel.Data>()
            if (f.exists()) {
                try {
                    System.gc()
                    val fileIn = FileInputStream(path)
                    val innn = ObjectInputStream(fileIn)
                    hMap = innn.readObject() as  ArrayList<VenuListModel.Data>
                    innn.close()
                    fileIn.close()
                } catch (e: StreamCorruptedException) {
                    e.printStackTrace()
                } catch (e: OptionalDataException) {
                    e.printStackTrace()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return hMap
        }*/

    }


}
