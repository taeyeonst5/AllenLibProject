package com.allen.allenlib.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat


fun checkGetWriteStoragePermission(context: Context): Boolean {
    logd("checkWriteStoragePermission")
    return if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        logd("寫入檔案權限已取得")
        true
    } else {
        false

    }
}

fun requestWriteStoragePermission(activity: AppCompatActivity) {
    logd("requestWriteStoragePermission")
    requestPermissions(
        activity,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        REQUEST_PERMISSION_WRITE_STORAGE
    )
}

fun Activity.checkSpecificPermission(context: Context, permission: String): Boolean {
    logd("checkPermission")
    return if (ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        logd("權限已取得 -> $permission")
        true
    } else {
        false

    }
}

fun Activity.requestSpecificPermission(activity: AppCompatActivity, permission: String, requestCode: Int) {
    logd("requestPermission")
    requestPermissions(
        activity,
        arrayOf(permission),
        requestCode
    )
}

