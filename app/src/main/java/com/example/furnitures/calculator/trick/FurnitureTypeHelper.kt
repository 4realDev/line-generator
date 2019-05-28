package com.example.furnitures.calculator.trick

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.example.furnitures.R
import com.example.furnitures.calculator.container.selection.FurnitureType

object FurnitureTypeHelper {

    @DrawableRes
    fun getDrawable(furnitureType: FurnitureType): Int {
        when (furnitureType) {
            FurnitureType.ARMCHAIR -> return R.drawable.ic_furniture_armchair
            FurnitureType.BED -> return R.drawable.ic_furniture_bed
            FurnitureType.SOFA -> return R.drawable.ic_furniture_sofa
            FurnitureType.UNDEFINED -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType")
            else -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType")
        }
    }

    @StringRes
    fun getString(furnitureType: FurnitureType): Int {
        when (furnitureType) {
            FurnitureType.ARMCHAIR -> return R.string.furniture_armchair
            FurnitureType.BED -> return R.string.furniture_bed
            FurnitureType.SOFA -> return R.string.furniture_sofa
            FurnitureType.UNDEFINED -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
            else -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
        }
    }
}
