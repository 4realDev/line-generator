package com.example.furnitures.calculator.helper

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Copyright (c) 2019 fluidmobile GmbH. All rights reserved.
 */
class ItemDecorationSpace(
    private val mMarginBetween: Int,
    private val mMarginStart: Int = mMarginBetween,
    private val mMarginEnd: Int = mMarginBetween
) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (parent.layoutManager?.canScrollVertically() == true) {
            if (itemPosition == 0) {
                outRect.top = mMarginStart
            }
            if (itemPosition == itemCount - 1) {
                outRect.bottom = mMarginEnd
            }
            if (itemPosition > 0) {
                outRect.top = mMarginBetween
            }
        } else {
            if (itemPosition == 0) {
                outRect.left = mMarginStart
            }
            if (itemPosition == itemCount - 1) {
                outRect.right = mMarginEnd
            }
            if (itemPosition > 0) {
                outRect.left = mMarginBetween
            }
        }
    }
}