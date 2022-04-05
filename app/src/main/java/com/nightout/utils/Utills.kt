package com.nightout.utils

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.makeramen.roundedimageview.RoundedImageView
import com.nightout.R
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
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

        /* fun showSnackBarOnError(view: View, message: String, context: Context) {
           *//* val snackBar = Snackbar.make(
                view, message, Snackbar.LENGTH_LONG
            )
            snackBar.changeFont()
            snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.view_line_gray2))
            snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackBar.show()*//*

             val snackBarView = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
             snackBarView.changeFont()
             val snackView = snackBarView.view
             val params = snackView.layoutParams as FrameLayout.LayoutParams
             params.gravity = Gravity.TOP
             params.topMargin = 150
             snackView.layoutParams = params
             snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_20213A))
             snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
             snackBarView.show()
        }*/

        fun showSnakBarCstm(view: View, message: String, context: Context){



            val snackbar: Snackbar
            snackbar = Snackbar.make(view, "Message", Snackbar.LENGTH_SHORT)
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
            val textView: TextView = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(context,R.color.red_clr2))
            snackbar.show()



        }

        private fun Snackbar.changeFont() {
            val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            val font = Typeface.createFromAsset(context.assets, "proximanova_reg.otf")
            tv.typeface = font
        }

        fun checkingPermissionIsEnabledOrNot(mContext: Context): Boolean {
          /*  val cameraPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA
            )
            val readStoragePermission =
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writeStoragePermission =
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val locationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            return cameraPermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED && writeStoragePermission == PackageManager.PERMISSION_GRANTED && locationPermission == PackageManager.PERMISSION_GRANTED*/
            val cameraPermission = mContext.packageManager.checkPermission(CAMERA,mContext.packageName)
            val locationPermission = mContext.packageManager.checkPermission(ACCESS_FINE_LOCATION,mContext.packageName)
            return cameraPermission == PackageManager.PERMISSION_GRANTED  && locationPermission == PackageManager.PERMISSION_GRANTED
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

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.PNG, 80, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
            return Uri.parse(path)
        }

        fun dateZonetoDateFormat2(date: String) : String{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val date: Date = dateFormat.parse(date) //You will get date object relative to server/client timezone wherever it is parsed
            // dateFormat.parse("2017-04-26T20:55:00.000Z") //You will get date object relative to server/client timezone wherever it is parsed
            //2021-04-20T08:27:38.000000Z
            val formatter  = SimpleDateFormat("dd MMM, yyyy") //If you need time just put specific format for time like 'HH:mm:ss'

            val dateStr: String = formatter.format(date)
            return dateStr
        }

        fun getMobNoSimpleFormat(phnoStr:String) : String{
            return phnoStr.replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim()

        }
        fun setImage(context: Context?, imageView: CircleImageView?, url: String?) {
            imageView?.let {
                Glide.with(context!!).load(PreferenceKeeper.instance.imgPathSave+url).centerCrop()
                    .placeholder(R.drawable.user_default_ic).into(it)
            }
        }
        fun setImageNormal(context: Context?, imageView: ImageView?, url: String?) {
            imageView?.let {
                Glide.with(context!!).load(PreferenceKeeper.instance.imgPathSave+url).centerCrop()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.app_icon).into(it)
            }
        }

        fun setImageFullPath(context: Context?, imageView: ImageView?, url: String?) {
            try {
                imageView?.let {
                    Glide.with(context!!).load(url)
                        .centerCrop()
                        .placeholder(R.drawable.app_icon)
                        .error((R.drawable.nodata_founf_img))
                        .into(it)
                }
            } catch (e: Exception) {
                Log.d("Glide", "setImageFullPath: "+e)
            }
        }
        fun phoneNoUKFormat(phno:String):String{
          return  PhoneNumberUtils.formatNumber(phno,"US")
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
        ///next fun
          fun showDialogImage(context: Context,imgPath: String,title: String) {
            val imagedialog = Dialog(context)
            val window: Window = imagedialog.getWindow()!!
            window.setGravity(Gravity.TOP)
            val layoutParams: WindowManager.LayoutParams = imagedialog.getWindow()!!.getAttributes()
            layoutParams.y = 170//margin top
            imagedialog.getWindow()!!.setAttributes(layoutParams);

            imagedialog.setContentView( R.layout.imagedialog)
            imagedialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            var  photo    = imagedialog.findViewById(R.id.photoenlarge) as ImageView
            var  viewTransTop    = imagedialog.findViewById(R.id.viewTransTop) as View
            var  titleDialog    = imagedialog.findViewById(R.id.titleDialog) as TextView

            titleDialog.setText(title)

            Glide.with(context)
                .load(imgPath)
                .error(R.drawable.no_image)
                .into(photo)
            val fade_in = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            fade_in.duration = 500
            fade_in.fillAfter = true
            photo.startAnimation(fade_in)
            Handler(Looper.getMainLooper()).postDelayed({
                viewTransTop.visibility= View.VISIBLE
                titleDialog.visibility= View.VISIBLE

            },490)

            imagedialog.show()
        }

        fun slideUp(view: View) {
            view.visibility = View.VISIBLE
            val animate = TranslateAnimation(0f,   0f,   view.height.toFloat(),  0f)
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
        }
        fun slideDown(view: View) {
            val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat()) // toYDelta
            animate.duration = 1000
            animate.fillAfter = true
            view.startAnimation(animate)
        }


        fun showErrorToast(ctx: Context, msg: String) {
            DynamicToast.makeError(ctx, msg, Toast.LENGTH_SHORT).show()
        }

        fun showSuccessToast(ctx: Context, msg: String) {
            DynamicToast.makeSuccess(ctx, msg, Toast.LENGTH_LONG).show()
        }

        fun showWarningToast(ctx: Context, msg: String) {
            DynamicToast.makeWarning(ctx, msg, Toast.LENGTH_LONG).show()
        }
        fun showDefaultToast(ctx: Context, msg: String) {
            DynamicToast.make(ctx, msg).show();
        }
        fun showIconToast(ctx: Context, msg: String) {
            DynamicToast.make(ctx, msg, R.drawable.app_icon).show();
        }




    }
}