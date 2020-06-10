package com.allen.allenlib.view.viewpager

import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2

/**
 * for Pager2 item Visible at L,R edge
 */
class VisibleItemTransformer(private var pageMarginPx: Int, private var offsetPx: Int) :
    ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        //edge visible item
//        pageMarginPx = page.context.resources.getDimensionPixelOffset(R.dimen.pageMargin)
//        offsetPx = page.context.resources.getDimensionPixelOffset(R.dimen.offset)

        val viewPager = page.parent.parent as ViewPager2
        val offset = position * -(2 * offsetPx + pageMarginPx)
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