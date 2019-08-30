package com.example.line_generator.helper

import android.graphics.Rect
import android.view.View

class ItemDecorationEqualSpacing(private val spacing: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (parent.layoutManager?.canScrollVertically() == true) {
            if (itemPosition == 0) {
                outRect.top = spacing
            }
            if (itemPosition == itemCount - 1) {
                outRect.bottom = spacing
            }
            if (itemPosition > 0) {
                outRect.top = spacing
            }

            outRect.left = spacing / 2
            outRect.right = spacing / 2
        }
        // For Horizontal Scroll
        else {
            if (itemPosition == 0) {
                outRect.left = spacing
            }
            if (itemPosition == itemCount - 1) {
                outRect.right = spacing
            }
            if (itemPosition > 0) {
                outRect.left = spacing
            }

            outRect.top = spacing / 2
            outRect.bottom = spacing / 2
        }
    }
}