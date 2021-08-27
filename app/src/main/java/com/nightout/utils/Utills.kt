package com.nightout.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.nightout.R

class Utills {

    companion object {
        fun ratioOfScreen(context: Activity, ratio: Float): Int {
            val displayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            return (pxToDp(context, width) * ratio).toInt()
        }

        fun pxToDp(context: Context, px: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        fun tintColor(context: Context?, imageView: ImageView, color: Int) {
            if (color == 0) {
                imageView.clearColorFilter()
            } else {
                imageView.setColorFilter(ContextCompat.getColor(context!!, color))
            }
        }
        fun getScreenWidth(context: Context): Int {
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun showSnackBarOnError(view: View, message: String, context: Context) {
            val snackBar = Snackbar.make(
                view, message, Snackbar.LENGTH_LONG
            )
            snackBar.changeFont()
            snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.view_line_gray2))
            snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackBar.show()
        }


        private fun Snackbar.changeFont() {
            val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            val font = Typeface.createFromAsset(context.assets, "proximanova_reg.otf")
            tv.typeface = font
        }

        fun checkingPermissionIsEnabledOrNot(mContext: Context): Boolean {
            val cameraPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA
            )
            val readStoragePermission =
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writeStoragePermission =
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val locationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            return cameraPermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED && writeStoragePermission == PackageManager.PERMISSION_GRANTED && locationPermission == PackageManager.PERMISSION_GRANTED
        }

        fun requestMultiplePermission(activity: Activity,requestPermissionCode:Int) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                requestPermissionCode
            )
        }






    }
}