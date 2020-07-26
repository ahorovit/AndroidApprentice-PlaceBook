package com.raywenderlich.placebook.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.raywenderlich.placebook.R
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bytes = stream.toByteArray()
        saveBytesToFile(context, bytes, filename)
    }

    private fun saveBytesToFile(context: Context, bytes: ByteArray, filename: String) {
        val outputStream: FileOutputStream

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(bytes)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadBitmapFromFile(context: Context, filename: String) : Bitmap? {
        val filePath = File(context.filesDir, filename).absolutePath
        return BitmapFactory.decodeFile(filePath)
    }

    @Throws(IOException::class)
    fun createUniqueImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val filename = "Placebook_${timeStamp}_"
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", filesDir)
    }

    fun decodeFileToSize(filePath: String, width: Int, height: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // Flag causes decodeFile() just to get image size, not load image
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(
            options.outWidth,
            options.outHeight,
            width,
            height
        )

        options.inJustDecodeBounds = false // Flag causes decodeFile() to load image this time
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun calculateInSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1

        if(height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeUriStreamToSize(uri: Uri, width: Int, height: Int, context: Context): Bitmap? {
        var inputStream: InputStream? = null
        val options: BitmapFactory.Options = BitmapFactory.Options()

        try {
            // opens inputStream from the file uri
            inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // image size is determined
            options.inJustDecodeBounds = true // only read image size
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            // reopen inputStream
            inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // read image file, downsampled
            options.inSampleSize = calculateInSampleSize(
                options.outWidth,
                options.outHeight,
                width,
                height
            )
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()
            return bitmap
        } catch (e: Exception) {
            return null
        } finally {
            inputStream?.close()
        }
    }
}