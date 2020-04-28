package com.allen.allenlib.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    private const val IMAGE_CHILD_PATH = "jpw"

    //尚未使用
    fun convertBitmaptoFile(destinationFile: File, bitmap: Bitmap) {
        //create a file to write bitmap data
        destinationFile.createNewFile()
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        val bitmapData = bos.toByteArray()
        //write the bytes in file
        val fos = FileOutputStream(destinationFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }

    //尚未使用
    fun getBitmap(context: Context, imageUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    imageUri
                )
            )
        } else {
            context
                .contentResolver
                .openInputStream(imageUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
        }
    }

    fun delete(file: File): Boolean {
        return try {
            logd("delete file path: ${file.absolutePath}")
            file.delete()
        } catch (e: IOException) {
            loge("file delete fail path: ${file.absolutePath}")
            false
        }

    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                createFile(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!,
                    IMAGE_CHILD_PATH
                )
            } else {
                createFile(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_CHILD_PATH
                )
            }
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    fun saveBitmaptoGallery(context: Context, url: String, file: File) {
        val requestOptions = RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        context.let {
            val bitmap = Glide.with(it)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .submit()
                .get()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                //error
                logd("Failed to save bitmap")
            }
        }
    }

    private fun createFile(file: File, child: String): File {
        return File(
            file,
            child
        ).also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
    }

    fun getPicturesContentValues(filePath: String): ContentValues {
        return ContentValues().also { contentValues ->
            contentValues.put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                File(filePath).name
            )
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/$IMAGE_CHILD_PATH"
            )
        }
    }

    /**
     * 傳入File,Context 回傳FileProvider Uri
     * */
    fun getFileProviderFileUri(file: File, context: Context): Uri {
        return FileProvider.getUriForFile(
            context,
            "com.example.jpw.fileprovider",
            file
        )
    }

    /**
     * Android 10以上 必需將tmp file透過MediaStore存到public/Pictures/jpw底下讓圖片庫看到
     * */
    @Throws(IOException::class)
    fun AddPicToGallery(context: Context, filePath: String, bitmap: Bitmap) {
        MediaScannerConnection.scanFile(
            context, arrayOf(filePath),
            arrayOf("image/jpeg"),
            object : MediaScannerConnection.OnScanCompletedListener {
                override fun onScanCompleted(path: String?, uri: Uri?) {
                    logd("which path: $path,uri: $uri")

                    val contentResolver = context.contentResolver

                    var stream: OutputStream? = null
                    var mediaUri: Uri? = null

                    try {
                        mediaUri = contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            getPicturesContentValues(filePath)
                        )
                        mediaUri?.let {
                            stream = contentResolver.openOutputStream(it)
                        } ?: throw IOException("Failed media insert")

                        stream?.let {
                            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)) {

                            } else {
                                throw IOException("Failed to save bitmap")
                            }
                        } ?: throw IOException("Failed get output stream")
                    } catch (e: IOException) {
                        mediaUri?.let {
                            // Don't leave an orphan entry in the MediaStore
                            contentResolver.delete(it, null, null)
                        }
                        throw e
                    } finally {
                        stream?.close()
                    }
                }
            })
    }

}