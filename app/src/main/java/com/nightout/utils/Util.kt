package com.nightout.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nightout.R

class Util {

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
    }
}