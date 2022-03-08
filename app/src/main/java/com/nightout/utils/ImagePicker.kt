package com.nightout.utils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import java.io.File


class ImagePicker {
    companion object {
        val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
        val photoFileName = "photo.jpg"
        var photoFile: File? = null
        val APP_TAG = "MyCustomApp"

        fun onCaptureImage(context: Activity) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFileUri(photoFileName, context)
            if (photoFile != null) {
                val fileProvider: Uri = FileProvider.getUriForFile(context, "com.nightout.fileProvider", photoFile!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                if (intent.resolveActivity(context.packageManager) != null) {
                    startActivityForResult(
                        context,
                        intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE,
                        null
                    )
                }
            }
        }

        private fun getPhotoFileUri(fileName: String, context: Context): File {
            val mediaStorageDir =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {

            }

            return File(mediaStorageDir.path + File.separator + fileName)
        }
    }



}