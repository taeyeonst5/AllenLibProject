package com.allen.allenlib.util

import android.os.SystemClock
import android.view.View

/**
 * for prevent click multiple.
 * @param defaultInterval default 1 sec, or you want
 * @param onSafeClick
 */
class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}