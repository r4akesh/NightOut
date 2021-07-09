package com.nightout.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

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
    }
}