package com.android.cedecsi.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtil {

    companion object {
        fun saveImageToStorage(
            context: Context,
            bitmap: Bitmap,
            name: String,
            providerId: String,
            appName: String,
            response: (Uri?) -> Unit = {}
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                GlobalScope.launch(Dispatchers.IO) {
                    val collection =
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                    val dirDest = File(Environment.DIRECTORY_PICTURES, appName)
                    val date = System.currentTimeMillis()
                    val newImage = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, name)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                        put(MediaStore.MediaColumns.DATE_ADDED, date)
                        put(MediaStore.MediaColumns.DATE_MODIFIED, date)
                        put(MediaStore.MediaColumns.SIZE, bitmap.byteCount)
                        put(MediaStore.MediaColumns.WIDTH, bitmap.width)
                        put(MediaStore.MediaColumns.HEIGHT, bitmap.height)
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "$dirDest${File.separator}")
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }

                    context.contentResolver.insert(collection, newImage)?.let { uri ->
                        context.contentResolver.openOutputStream(uri, "w").use {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, it)
                        }
                        newImage.clear()
                        newImage.put(MediaStore.Images.Media.IS_PENDING, 0)
                        context.contentResolver.update(uri, newImage, null, null)
                        withContext(Dispatchers.Main) {
                            response(uri)
                        }
                    }
                }

                //val resolver = context.contentResolver
                //val contentValues = ContentValues().apply {
                //    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                //    put(MediaStore.MediaColumns.MIME_TYPE, MimeTypeEnum.png.mime)
                //    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/$appName")
                //}
//
                //try {
                //    val URI = if (isHuawei)
                //        resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                //    else resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                //    URI?.let { uri ->
                //        resolver.openOutputStream(uri).use {
                //            Log.e("document path", uri.path ?: "")
                //            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
                //            it?.flush()
                //            it?.close()
                //            return uri
                //        }
                //    } ?: kotlin.run { return null }
                //} catch (e: Exception) {
                //    e.printStackTrace()
                //    Log.e("main", "error $e")
                //    return null
                //}
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/$appName")
                    if (!storageDir.exists()) storageDir.mkdir()
                    Log.e("document path", storageDir?.path ?: "")
                    val file = File(storageDir, name)

                    try {
                        val fos = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
                        fos.flush()
                        fos.close()
                        fos.close()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            response(
                                FileProvider.getUriForFile(context,
                                    providerId, //BuildConfig.APPLICATION_ID+".provider"
                                    file)
                            )
                        } else {
                            response(Uri.fromFile(file))
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.e("main", "error $e")
                    }
                }
            }

        }
    }

}