package pe.com.service.common.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ImageUtils {

    companion object {

        // The new size we want to scale to
        val REQUIRED_SIZE = 2048

        fun resizeImage(path: String, file: File) {
            val bytes: ByteArray = getBytesPhoto(path)
            try {
                val bos = BufferedOutputStream(FileOutputStream(file))
                bos.write(bytes)
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun createImageFile(context: Context): File? {
            val timesTamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            val name = "JPEG_${timesTamp}_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(name, ".jpg", storageDir)
        }

        private fun getBytesPhoto(path: String): ByteArray {
            val file = File(path)
            val fileNameArray = path.split("\\.")
            val extension = fileNameArray[fileNameArray.size - 1]
            val ei = ExifInterface(path)
            var bitman = decodeFile(file)

            when(ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90->bitman = rotateBitmap(bitman, 90f)
                ExifInterface.ORIENTATION_ROTATE_180->bitman = rotateBitmap(bitman, 180f)
                ExifInterface.ORIENTATION_ROTATE_270->bitman = rotateBitmap(bitman, 270f)
            }

            val bos = ByteArrayOutputStream()
            bitman?.compress(Bitmap.CompressFormat.JPEG, 60, bos)
            return bos.toByteArray()

        }

        fun getPathFromUri(context: Context, uri: Uri): String? {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val arrays = docId.split(":")
                    val type = arrays[0]
                    if ("primary".equals(type, true)) {
                        return "${Environment.getExternalStorageDirectory()}/${arrays[1]}"
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong())
                    return getDataColumn(context, contentUri, null, null)
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val arrays = docId.split(":")
                    val type = arrays[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    val selection = "_id=?"
                    val args = arrayOf(arrays[1])
                    return getDataColumn(context, contentUri, selection, args)
                }
            }
            // MediaStore (and general)
            else if ("content".equals(uri.scheme, true)) {
                // Return the remote address
                if (isGooglePhotosUri(uri)) {
                    return uri.lastPathSegment
                }
                return getDataColumn(context, uri, null, null)
            }
            // File
            else if ("file".equals(uri.scheme, true)) {
                return uri.path
            }
            return null
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        private fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver
                    .query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        private fun rotateBitmap(source: Bitmap?, angle: Float): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(angle)
            source?.let {
                return Bitmap.createBitmap(
                    it,
                    0,
                    0,
                    it.width,
                    it.height,
                    matrix,
                    true
                )
            } ?: return null

        }

        private fun decodeFile(f: File): Bitmap? {
            try { // Decode image size
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(FileInputStream(f), null, o)

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (
                    o.outWidth / scale >= REQUIRED_SIZE &&
                    o.outHeight / scale >= REQUIRED_SIZE
                ) {
                    scale *= 2
                }
                // Decode with inSampleSize
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                return BitmapFactory.decodeStream(FileInputStream(f), null, o2)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

    }

}