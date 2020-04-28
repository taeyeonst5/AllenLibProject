package com.allen.allenlib.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object DialogUtil {

    fun getAlertDialog(
        context: Context,
        title: String,
        message: String?,
        view: Int = 0,
        btnPositive: String,
        btnNegative: String? = null,
        listener: DialogInterface.OnClickListener? = null
    ): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message?.let { it })
            .setPositiveButton(btnPositive, listener?.let { it })
            .setNegativeButton(btnNegative, listener?.let { it })
            .also {
                if (view != 0) it.setView(view)
            }.create()
    }

    fun getSingleChoiceDialog(
        context: Context,
        title: String,
        items: Array<CharSequence>,
        checked: Int,
        itemListener: DialogInterface.OnClickListener
    ): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setSingleChoiceItems(
                items, checked
                , itemListener
            )
            .create()
    }
}