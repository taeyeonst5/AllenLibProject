package com.allen.allenlib.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class CustomAutoCompleteTextView : AppCompatAutoCompleteTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * override for: always show all dropdown item (like spinner)
     * */
    override fun performFiltering(text: CharSequence?, keyCode: Int) {
        super.performFiltering("", 0)
    }
}