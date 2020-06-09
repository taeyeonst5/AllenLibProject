package com.allen.allenlib.view.base

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.Fragment
import com.allen.allenlib.R
import com.allen.allenlib.util.*
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

abstract class BaseUploadImageFragment : Fragment() {

    /**
     * @return photoFilePath
     * */
    abstract fun getPhotoPath(): String //File(postArticleViewModel.currentPhotoPath)

    /**
     * @param uri after cropImage Result
     * */
    abstract fun handleImage(uri: Uri?)

    /**
     * after permission granted open dialog
     */
    abstract fun navigateToBottomSheet()

    abstract fun getImageFileUtil(): BaseImageFileUtils

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    logd("CAPTURE,RESULT_OK")
                    cropImageSquareRatio(
                        getImageFileUtil().getFileProviderFileUri(
                            File(getPhotoPath()),
                            requireContext()
                        )
                    )
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    toast(requireContext(), getString(R.string.toast_failure_takePhoto))
                    getImageFileUtil().delete(File(getPhotoPath()))
                }
            }
            REQUEST_IMAGE_PICK -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (checkIsLargeFileSize(data)) return

                    logd("PICK,RESULT_OK")
                    cropImageSquareRatio(data?.data)
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    toast(requireContext(), getString(R.string.toast_failure_choosePhoto))
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleImage(CropImage.getActivityResult(data).uri)
                } else {
                    loge("CropImage error resultCode= $resultCode")
                }
            }
        }
    }

    /**
     * @return 讀取 利用Uri取得Image檔案大小()  >= 10MB is true ,others false
     * note: 存取共享media檔案需用contentResolver  see https://developer.android.com/training/data-storage/shared/media
     */
    private fun checkIsLargeFileSize(data: Intent?): Boolean {
        var imageSize = 0L
        data?.data?.let {
            it.path?.let { path ->
                val resolver = requireContext().applicationContext.contentResolver
                resolver.openFileDescriptor(it, "r").use { pfd ->
                    pfd?.let {
                        imageSize = (it.statSize / 1000)
                        logd("pick file size: $imageSize ")
                    } ?: loge("openFileDescriptor return null")
                }
            }
        }

        if (imageSize >= 10000) { //檔案大於10MB就return
            toast(requireContext(), getString(R.string.toast_choose_below_10mb_image))
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        logd("RequestPermission result")
        if (requestCode == REQUEST_PERMISSION_WRITE_STORAGE) {
            logd("onRequestPermissionsResult called!")
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToBottomSheet()
            } else {
                toast(requireContext(), getString(R.string.toast_failure_needPermission))
            }
        }
    }
}