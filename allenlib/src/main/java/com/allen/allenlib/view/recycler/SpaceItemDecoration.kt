package com.allen.allenlib.view.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val spacing: Int,
    private val initSpacing: Int,
    private val isHorizontal: Boolean
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val scale = parent.resources.displayMetrics.density

        if (parent.getChildAdapterPosition(view) == 0) {
            if (isHorizontal) {
                outRect.left = (initSpacing * scale).toInt()
            } else {
                outRect.top = (initSpacing * scale).toInt()
            }
        }

        if (isHorizontal) {
            outRect.right = (spacing * scale).toInt()
            outRect.top = (spacing * scale).toInt()
            outRect.bottom = (spacing * scale).toInt()
        } else {
            outRect.left = (spacing * scale).toInt()
            outRect.right = (spacing * scale).toInt()
            outRect.bottom = (spacing * scale).toInt()
        }
    }
}