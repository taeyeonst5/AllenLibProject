package com.allen.allenlib.view.viewpager

import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.allen.allenlib.R
import com.allen.allenlib.util.logd

/**
 * for Pager2 item Visible at L,R edge
 */
class VisibleItemTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        //edge visible item
        val pageMarginPx = page.context.resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = page.context.resources.getDimensionPixelOffset(R.dimen.offset)

        val viewPager = page.parent.parent as ViewPager2
        val offset = position * -(2 * offsetPx + pageMarginPx)
        logd("offset$offset")
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }
    }
}