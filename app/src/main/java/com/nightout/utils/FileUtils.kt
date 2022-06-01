package com.nightout.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import okio.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

class FileUtils {

    companion object {
        private val TAG = FileUtils::class.java.simpleName

        private var contentUri: Uri? = null
        /**
         * Get a file path from a [uri]. This will get the the path for Storage Access
         * Framework Documents, as well as the _data field for the MediaStore and
         * other file-based ContentProviders.<br>
         * <br>
         * Callers should check whether the path is local before assuming it
         * represents a local file.
         *
         * @param context The Activity context.
         */
        @SuppressLint("NewApi")
        fun getPath(context: Context, uri: Uri): String? {
            // check here to KITKAT or new version
            val isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            val selection: String
            val selectionArgs: Array<String>

            // DocumentProvider
            if (isKitkat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId: String = DocumentsContract.getDocumentId(uri)
                    val split: List<String> = docId.split(":")
                    val type: String = split[0]

                    val fullPath = getPathFromExtSD(context, split)

                    return if (fullPath.isNotEmpty()) fullPath else null
                }

                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val id: String = DocumentsContract.getDocumentId(uri)

                        context.contentResolver.query(
                            uri,
                            arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                            null,
                            null,
                            null
                        )
                            /* "use" is similar to java's try with resource */
                            .use { cursor ->
                                if (cursor != null && cursor.moveToFirst()) {
                                    val fileName = cursor.getString(0)
                                    val path =
                                        "${/*Environment.getExternalStorageDirectory()*/context.getExternalFilesDir(null)?.absolutePath}/Download/$fileName"
                                    if (path.isNotEmpty()) {
                                        return path
                                    }
                                }
                            }
                        if (id.isNotEmpty()) {
                            if (id.startsWith("raw:")) {
                                return id.replaceFirst("raw:", "")
                            }
                            val contentUriPrefixesToTry = arrayOf(
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                            )
                            for (contentUriPrefix: String in contentUriPrefixesToTry) {
                                return try {
                                    val contentUri: Uri =
                                        ContentUris.withAppendedId(
                                            Uri.parse(contentUriPrefix),
                                            id.toLong()
                                        )

                                    getDataColumn(context, contentUri, null, null)
                                } catch (e: NumberFormatException) {
                                    //In Android 8 and Android P the id is not a number
                                    uri.path?.replaceFirst("^/document/raw:", "")
                                        ?.replaceFirst("^raw:", "")
                                }
                            }

                        }
                    } else {
                        val id: String = DocumentsContract.getDocumentId(uri)

                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "")
                        }
                        try {
                            contentUri =
                                ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"),
                                    id.toLong()
                                )
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }

                        if (contentUri != null) {
                            return getDataColumn(context, contentUri!!, null, null)
                        }
                    }
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    val docId: String = DocumentsContract.getDocumentId(uri)
                    val split: List<String> = docId.split(":")

                    val contentUri: Uri? = when (split[0] /*type*/) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri!!, selection, selectionArgs)
                } else if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(uri, context)
                }
            }


            // MediaStore (and general)
            else if ("content".equals(uri.scheme, ignoreCase = true)) {

                if (isGooglePhotosUri(uri)) {
                    return uri.lastPathSegment
                }

                if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(uri, context)
                }
                return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                    // return getFilePathFromURI(context,uri);
                    getMediaFilePathForN(uri, context)
                    // return getRealPathFromURI(context,uri);
                } else {

                    getDataColumn(context, uri, null, null)
                }


            }
            // File
            else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }

            return null
        }

        /**
         * Check if a [filePath] exists on device
         */
        private fun fileExists(filePath: String): Boolean {
            val file = File(filePath)

            return file.exists()
        }

        /**
         * Get full file path from external storage
         *
         * @param pathData The storage type and the relative path
         */
        private fun getPathFromExtSD(context: Context, pathData: List<String>): String {
            val type: String = pathData[0]
            val relativePath = "/${pathData[1]}"
            var fullPath: String

            // on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
            // something like "71F8-2C0A", some kind of unique id per storage
            // don't know any API that can get the root path of that storage based on its id.
            //
            // so no "primary" type, but let the check here for other devices
            if ("primary".equals(type, ignoreCase = true)) {
                fullPath = /*Environment.getExternalStorageDirectory().toString()*/context.getExternalFilesDir(null)?.absolutePath + relativePath
                if (fileExists(fullPath)) {
                    return fullPath
                }
            }

            // Environment.isExternalStorageRemovable() is `true` for external and internal storage
            // so we cannot relay on it.
            //
            // instead, for each possible path, check if file exists
            // we'll start with secondary storage as this could be our (physically) removable sd card
            fullPath = System.getenv("SECONDARY_STORAGE")!! + relativePath
            if (fileExists(fullPath)) {
                return fullPath
            }

            fullPath = System.getenv("EXTERNAL_STORAGE")!! + relativePath
            if (fileExists(fullPath)) {
                return fullPath
            }

            return fullPath
        }

        private fun getDriveFilePath(uri: Uri, context: Context): String {
            var returnCursor: Cursor? = null
            val name: String
            try {
                returnCursor = context.contentResolver.query(uri, null, null, null, null)
                /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
                val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
                returnCursor?.moveToFirst()
                name = nameIndex?.let { returnCursor?.getString(it) }.toString()
                val size: String = sizeIndex?.let { returnCursor?.getLong(it) }.toString()

            } finally {
                returnCursor?.close()
            }

            val file = File(context.cacheDir, name)
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read: Int
                val maxBufferSize = 1024 * 1024
                val bytesAvailable: Int = inputStream?.available()!!

                //int bufferSize = 1024;
                val bufferSize = min(bytesAvailable, maxBufferSize)

                val buffers = ByteArray(bufferSize)
//            while ({read = inputStream?.read(buffers)!!; read}() != -1) {
                while (inputStream.read(buffers).let { read = it; it != -1 }) {
                    outputStream.write(buffers, 0, read)
                }
                Log.e("File Size", "Size ${file.length()}")
                inputStream.close()
                outputStream.close()
                Log.e("File Path", "Path ${file.path}")
                Log.e("File Size", "Size ${file.length()}")
            } catch (e: Exception) {
                Log.e("Exception", e.message!!)
            }
            return file.path
        }

        private fun getMediaFilePathForN(uri: Uri, context: Context): String {
            var returnCursor: Cursor? = null
            val name: String
            try {
                returnCursor = context.contentResolver.query(uri, null, null, null, null)
                /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
                val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
                returnCursor?.moveToFirst()
                name = nameIndex?.let { returnCursor?.getString(it) }.toString()
                val size = sizeIndex?.let { returnCursor?.getLong(it) }
            } finally {
                returnCursor?.close()
            }

            val file = File(context.filesDir, name)
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read: Int
                val maxBufferSize = 1024 * 1024
                val bytesAvailable = inputStream?.available()!!

                //int bufferSize = 1024;
                val bufferSize = min(bytesAvailable, maxBufferSize)

                val buffers = ByteArray(bufferSize)
                while (inputStream.read(buffers).let { read = it; it != -1 }) {
                    outputStream.write(buffers, 0, read)
                }
                Log.e("File Size", "Size ${file.length()}")
                inputStream.close()
                outputStream.close()
                Log.e("File Path", "Path ${file.path}")
                Log.e("File Size", "Size ${file.length()}")
            } catch (e: Exception) {
                Log.e("Exception", e.message!!)
            }

            return file.path
        }

        private fun getDataColumn(
            context: Context,
            uri: Uri,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            val column = "_data"
            val projection: Array<String> = arrayOf(column)
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                .use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val index = cursor.getColumnIndexOrThrow(column)
                        return cursor.getString(index)
                    }
                }

            return null
        }

        /**
         * @param uri - The Uri to check.
         * @return - Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean =
            ("com.android.externalstorage.documents" == uri.authority)

        /**
         * @param uri - The Uri to check.
         * @return - Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri): Boolean =
            ("com.android.providers.downloads.documents" == uri.authority)

        /**
         * @param uri - The Uri to check.
         * @return - Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri): Boolean =
            ("com.android.providers.media.documents" == uri.authority)

        /**
         * @param uri - The Uri to check.
         * @return - Whether the Uri authority is Google Photos.
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean =
            ("com.google.android.apps.photos.content" == uri.authority)

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Drive.
         */
        private fun isGoogleDriveUri(uri: Uri): Boolean =
            ("com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority)


        /**
         * get file path where the media will be saved after successfully recorded
         * @param context instance of class
         * @param interviewDetails Recording details to create unique file name
         * @param isVideo whether media is video or not
         * @return created file path where media will be saved
         */





    }
}
