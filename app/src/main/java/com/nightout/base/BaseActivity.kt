package com.nightout.base

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.nightout.R
import com.nightout.utils.ExceptionHandler
import com.nightout.utils.MyApp
import com.nightout.utils.TouchEffect
import dmax.dialog.SpotsDialog


open class BaseActivity:AppCompatActivity(),View.OnClickListener {
    val TOUCH = TouchEffect()
    var THIS: BaseActivity? = null
    private var exceptionHandler: ExceptionHandler? = null
    private var spotsDialog: SpotsDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        THIS = this
        exceptionHandler = ExceptionHandler(this)
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)


        //status bar full screen
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun showAlrtDialg(context: Context?, msg: String?) {
        spotsDialog = SpotsDialog(context, msg, R.style.Custom)
        spotsDialog!!.setCancelable(false)
        spotsDialog!!.show()
    }

    fun dismisAlrtDialog() {
        try {
            if(spotsDialog ==null){
                Log.d("TAG", "dismisAlrtDialog: null")
            }else{
                Log.d("TAG", "dismisAlrtDialog: 1")
            }
            spotsDialog!!.dismiss()
        } catch (e: Exception) {
            Log.d("TAG", "dismisAlrtDialog: 2"+e.toString())
        }
    }
    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
      /*fun parseStringData(data: String): RequestBody? {
        return RequestBody.create(MediaType.parse("text/plain"), data)
    }*/
    fun noNetConnection() {
        MyApp.popErrorMsg("", resources.getString(R.string.No_Internet), THIS!!)
    }
    fun noNetConnectionTost() {
        MyApp.ShowTost(THIS!!, resources.getString(R.string.No_Internet))
    }
    override fun onClick(v: View?) {

    }

    fun setTouchNClick(id: Int): View? {
        val v:View?=setClick(id)
        v?.setOnTouchListener(TOUCH)
        return v

    }


     fun setClick(id: Int): View {
         val v:View = findViewById(id)
         v.setOnClickListener(this)
         return v
    }
    fun setClick(v: View?): View? {


        v?.setOnClickListener(this)
        return v
    }
    fun setTouchNClick(v: View?): View? {
        setClick(v)
        v?.setOnTouchListener(TOUCH)
        return v
    }

    fun isPermissionsAllowed(permissions: Array<String>, shouldRequestIfNotAllowed: Boolean = false, requestCode: Int = -1): Boolean {
        var isGranted = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
                if (!isGranted)
                    break
            }
        }
        if (!isGranted && shouldRequestIfNotAllowed) {
            if (requestCode.equals(-1))
                throw RuntimeException("Send request code in third parameter")
            requestRequiredPermissions(permissions, requestCode)
        }

        return isGranted
    }

    fun requestRequiredPermissions(permissions: Array<String>, requestCode: Int) {
        val pendingPermissions: ArrayList<String> = ArrayList()
        permissions.forEachIndexed { index, permission ->
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
                pendingPermissions.add(permission)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val array = arrayOfNulls<String>(pendingPermissions.size)
            pendingPermissions.toArray(array)
            requestPermissions(array, requestCode)
        }
    }

    fun isAllPermissionsGranted(grantResults: IntArray): Boolean {
        var isGranted = true
        for (grantResult in grantResults) {
            isGranted = grantResult.equals(PackageManager.PERMISSION_GRANTED)
            if (!isGranted)
                break
        }
        return isGranted
    }
}