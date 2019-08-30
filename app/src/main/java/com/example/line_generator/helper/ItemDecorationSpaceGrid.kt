package com.example.line_generator.helper

import android.graphics.Rect
import android.view.View

/**
 * Copyright (c) 2018 fluidmobile GmbH. All rights reserved.
 */
class ItemDecorationSpaceGrid(
        private val spanCount: Int,
        private val spacingHorizontal: Int,
        private val spacingVertical: Int,
        private val includeEdge: Boolean,
        private val headerNum: Int = 0
) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) - headerNum // item position

        if (position >= 0) {
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacingHorizontal - column * spacingHorizontal / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacingHorizontal / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position >= spanCount) { // top edge
                    outRect.top = spacingVertical
                }
            } else {
                outRect.left = column * spacingHorizontal / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacingHorizontal - (column + 1) * spacingHorizontal / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacingVertical // item top
                }
            }
        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }
}