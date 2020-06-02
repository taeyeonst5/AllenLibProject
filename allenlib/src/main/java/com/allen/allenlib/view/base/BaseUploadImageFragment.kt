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