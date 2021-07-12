package com.nightout.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.core.content.ContextCompat

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
    }
}