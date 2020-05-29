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

class BottomArcLayout : ConstraintLayout {
    private val path: Path = Path()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.allen_lib_loader_selected)

        val horizontalOffset = w * 0.5f
        val top = -h * 0.8f
        val bottom = h.toFloat()

        val ovalRect = RectF(-horizontalOffset, top, w + horizontalOffset, bottom)
        path.lineTo(ovalRect.left, top)
        path.arcTo(ovalRect, 0f, 180f, false)
        path.fillType = Path.FillType.INVERSE_EVEN_ODD
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)

    }
}