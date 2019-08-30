package com.example.line_generator.extensions

import android.content.Context

fun dpFromPx(context: Context, px: Float): Float {
    return (px.div(context.resources.displayMetrics.density))
}

fun pxFromDp(context: Context, dp: Float): Float {
    return (dp.times(context.resources.displayMetrics.density))
}