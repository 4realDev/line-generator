package com.example.furnitures.calculator.extensions.Int

import android.content.Context
import android.util.DisplayMetrics

fun Int.convertDpToPixel(dp: Float, context: Context): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}