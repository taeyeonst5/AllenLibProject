package com.allen.allenlib.view.basicviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.allen.allenlib.R
import com.allen.allenlib.util.logd

class BottomArcLayout : ConstraintLayout {
    private var path: Path? = null
    private var paint: Paint? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        path = Path()

        paint?.style = Paint.Style.FILL
        paint?.color = ContextCompat.getColor(context, R.color.allen_lib_loader_selected)

        val horizontalOffset = w * 0.5f //中心點
        val top = -h * 0.8f
        val bottom = h.toFloat()
        logd("寬:$w,高:$h,horizontalOffset:$horizontalOffset,top:$top,bottom:$bottom")

        val ovalRect = RectF(-horizontalOffset, top, w + horizontalOffset, bottom)
        path?.lineTo(ovalRect.left, top)
        path?.arcTo(ovalRect, 0f, 180f, false) //由右至左 正值 順時針弧線
        path?.fillType = Path.FillType.INVERSE_EVEN_ODD
    }

    override fun onDraw(canvas: Canvas) {
        path?.let {
            paint?.let { paint ->
                canvas.drawPath(it, paint)
            }
        }
        super.onDraw(canvas)
    }
}