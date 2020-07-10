package com.allen.allenlib.util

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.allen.allenlib.R
import com.allen.allenlib.view.CustomAutoCompleteTextView
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.IOException

fun Fragment.saveUrltoGallery(
    context: Application,
    url: String,
    baseImageFileUtils: BaseImageFileUtils
) {
    val photoFilePath: File = baseImageFileUtils.createImageFile(context)
    try {
        baseImageFileUtils.saveBitmaptoGallery(context, url, photoFilePath)
    } catch (e: IOException) {
        //error
        loge("e=${e.message}")
        null
    }
}

/**
 * Note: need change getFileProviderFileUri's  authority for each app ,also need change manifests
 */
fun Fragment.takePictureIntent(baseImageFileUtils: BaseImageFileUtils): String {
    var filePath = String()
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
            val photoFile: File? = try {
                baseImageFileUtils.createImageFile(requireContext()).also {
                    filePath = it.absolutePath
                }
            } catch (e: IOException) {
                //error
                loge("e=${e.message}")
                null
            }

            photoFile?.also {
                val photoURI: Uri =
                    baseImageFileUtils.getFileProviderFileUri(it, requireContext())

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent,
                    REQUEST_IMAGE_CAPTURE
                )
                logd("takePictureIntent startActivityForResult")
            }
        }
    }
    return filePath
}

fun Fragment.openGalleryIntent() {
    Intent(
        Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    ).also { pickPhotoIntent ->
        pickPhotoIntent.resolveActivity(requireActivity().packageManager)?.also {
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
        }
    }
}

//todo   can refactor for permessions list
fun Fragment.checkGetWriteStoragePermission(): Boolean {
    logd("checkWriteStoragePermission")
    return if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        logd("寫入檔案權限已取得")
        true
    } else {
        false

    }
}

fun Fragment.requestWriteStoragePermission() {
    logd("requestWriteStoragePermission")
    requestPermissions(
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        REQUEST_PERMISSION_WRITE_STORAGE
    )
}

fun Fragment.setRadioButtonTint(vararg radioButtons: RadioButton, @ColorRes colorRes: Int) {
    radioButtons.forEach {
        it.buttonTintList =
            ContextCompat.getColorStateList(requireContext(), colorRes)
    }
}

fun Fragment.setCheckBoxButtonTint(vararg checkBoxs: CheckBox, @ColorRes colorRes: Int) {
    checkBoxs.forEach {
        it.buttonTintList =
            ContextCompat.getColorStateList(requireContext(), colorRes)
    }
}

/**
 * for library cropImage: com.theartofdev.edmodo:android-image-cropper:2.8.0
 * */
fun Fragment.cropImage(
    uri: Uri?,
    setRatio: Boolean = false,
    ratioX: Int? = null,
    ratioY: Int? = null
) {
    if (uri == null) {
        loge("裁切圖片失敗: tmp file uri is null")
        return
    }

    logd("uri=$uri")
    val activity = CropImage.activity(uri)
    if (setRatio) {
        ratioX?.let { x ->
            ratioY?.let { y ->
                activity.setAspectRatio(x, y)
            }
        }
    }
    activity.start(requireContext(), this)
}

fun Fragment.cropImageSquareRatio(uri: Uri?) {
    cropImage(uri, true, 1, 1)
}

fun Fragment.createAutoTextViewSpinner(
    menuItem: List<String>,
    autoTextView: CustomAutoCompleteTextView,
    hasDefaultItem: Boolean = false,
    listener: AdapterView.OnItemClickListener
) {
    val adapter =
        ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            R.id.tvPopupItem,
            menuItem
        )
    autoTextView.setAdapter(adapter)
    if (hasDefaultItem) {
        autoTextView.setText(adapter.getItem(0).toString(), false)
    }
    autoTextView.onItemClickListener = listener
}

fun Fragment.updateImageToGallery(
    baseImageFileUtils: BaseImageFileUtils,
    uri: Uri,
    filePath: String
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        baseImageFileUtils.AddPicToGallery(
            requireContext(),
            filePath,
            BitmapFactory.decodeFile(uri.path)
        )
    } else {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            mediaScanIntent.data = uri
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }
}

fun Fragment.showErrorDialog(errorMsg: String, @IdRes actionToAlertDialogFragment: Int) {
    showAlertDialog(
        getAlertDialogArgsBundle(requireContext(), 0, "錯誤", errorMsg, "OK"),
        findNavController(), actionToAlertDialogFragment
    )
}

fun Fragment.popbackToDestination(navController: NavController, destinationId: Int) {
    navController.popBackStack(destinationId, false)
}

fun Fragment.initToolbar(toolbar: Toolbar) {
    val appBarConfiguration = AppBarConfiguration(findNavController().graph)
    toolbar.setupWithNavController(findNavController(), appBarConfiguration)
}

fun Fragment.setupBottomNavItemBackPress() {
    requireActivity().onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
}

fun Fragment.getStatusBarHeight(): Int {
    var result = 0
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        result = resources.getDimensionPixelSize(resId)
    }
    return result
}