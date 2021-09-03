package com.nightout.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.makeramen.roundedimageview.RoundedImageView
import com.nightout.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

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
           /* val snackBar = Snackbar.make(
                view, message, Snackbar.LENGTH_LONG
            )
            snackBar.changeFont()
            snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.view_line_gray2))
            snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackBar.show()*/

             val snackBarView = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
             snackBarView.changeFont()
             val snackView = snackBarView.view
             val params = snackView.layoutParams as FrameLayout.LayoutParams
             params.gravity = Gravity.TOP
             params.topMargin = 100
             snackView.layoutParams = params
             snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_20213A))
             snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
             snackBarView.show()
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


        fun showSnackBarFromTop(view: View, message: String, context: Context) {
            val snackBarView = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackBarView.changeFont()
            val snackView = snackBarView.view
            val params = snackView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snackView.layoutParams = params
            snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_20213A))
            snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            snackBarView.show()
        }


        fun saveImage(mContext: Context, myBitmap: Bitmap): String {
            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
            val wallpaperDirectory = mContext.getDir("images", Context.MODE_PRIVATE)

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs()
            }
            try {
                val f = File(
                    wallpaperDirectory, ((Calendar.getInstance()
                        .timeInMillis).toString() + ".jpg")
                )
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                MediaScannerConnection.scanFile(
                    mContext,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null
                )
                fo.close()
                println("f_absolutePath $f.absolutePath")
                return f.absolutePath
            } catch (e1: IOException) {
                e1.printStackTrace()
                println("error: ${e1.message}")
            }
            return ""
        }

        fun setImage(context: Context?, imageView: CircleImageView?, url: String?) {
            imageView?.let {
                Glide.with(context!!).load(url).centerCrop()
                    .placeholder(R.drawable.user_default_ic).into(it)
            }
        }
        fun setImageNormal(context: Context?, imageView: ImageView?, url: String?) {
            imageView?.let {
                Glide.with(context!!).load(PreferenceKeeper.instance.imgPathSave+url).centerCrop()
                    .placeholder(R.drawable.no_image).into(it)
            }
        }

        fun loadImage(
            view: RoundedImageView,
            url: String,
            userImage: Boolean,
            postImage: Boolean,
            dogImage: Boolean
        ) {
            val requestOptions = RequestOptions()
            requestOptions.isMemoryCacheable
            Glide.with(view)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .error(if (userImage) (R.drawable.no_image) else if (dogImage) (R.mipmap.ic_launcher) else (R.drawable.no_image))
                .placeholder(R.drawable.no_image)
                .centerCrop()
                .thumbnail(Glide.with(view).load(url))
                .into(view)

//    Glide.with(view)
//        .asBitmap()
//        .load(url)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .into(object : CustomTarget<Bitmap?>() {
//            override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
//                view.setImageBitmap(resource)
//                view.buildDrawingCache()
//            }
//
//            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
//        })

        }
    }
}