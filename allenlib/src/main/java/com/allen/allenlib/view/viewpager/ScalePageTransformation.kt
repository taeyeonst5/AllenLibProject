package com.allen.allenlib.view.viewpager

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.min

/**
 * for Pager2 animation scale
 */
open class ScalePageTransformation : ViewPager2.PageTransformer {

    companion object {
        private const val DEFAULT_SCALE = 1.0f
    }

    override fun transformPage(page: View, position: Float) {

        when {
            position < -1 -> {
                scaleToNormal(page)
            }

            position <= 1 -> {
                page.apply {
                    //scale
                    val scale = createScaleParam(position)
//                        val scale = max(0.8f, 1 - abs(position))
                    animate().scaleX(scale)
                    animate().scaleY(scale)
                }
            }
            else -> {
                scaleToNormal(page)
            }
        }
    }

    private fun scaleToNormal(page: View) {
        page.animate().scaleX(DEFAULT_SCALE)
        page.animate().scaleY(DEFAULT_SCALE)
    }

    open fun createScaleParam(position: Float) = min(1.15f, 2 - abs(position))
}