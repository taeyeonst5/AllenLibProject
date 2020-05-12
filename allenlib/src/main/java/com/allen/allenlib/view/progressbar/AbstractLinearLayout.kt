package com.allen.allenlib.view.progressbar

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.allen.allenlib.R

/**
 * Created by suneet on 10/10/17.
 */

abstract class AbstractLinearLayout : LinearLayout, LoaderContract {

    open var animDuration: Int = 500

    open var interpolator: Interpolator = LinearInterpolator()

    var dotsRadius: Int = 30

    var dotsDist: Int = 15

    var dotsColor: Int = ContextCompat.getColor(context, R.color.allen_lib_progress_default)

    abstract fun initView()

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

}
