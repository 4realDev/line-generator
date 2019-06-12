package com.example.furnitures.calculator.trick

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.furnitures.R

object FurnitureTypeHelper {

    // BUG: multiple pictures selects, when you click different items with the same drawable
    @DrawableRes
    fun getDrawable(furnitureType: FurnitureType): Int {
        when (furnitureType) {
            FurnitureType.ARMCHAIR -> return R.drawable.ic_furniture_armchair
            FurnitureType.BED -> return R.drawable.ic_furniture_bed
            FurnitureType.SOFA -> return R.drawable.ic_furniture_sofa
            FurnitureType.USER_CREATED_GRINDE -> return R.drawable.ic_letter_g
            FurnitureType.USER_CREATED_SLIDE -> return R.drawable.ic_letter_s
            FurnitureType.USER_CREATED_OTHER -> return R.drawable.ic_letter_o
            FurnitureType.UNDEFINED -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType")
            else -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + $furnitureType")
        }
    }

    @StringRes
    fun getString(furnitureType: FurnitureType): Int? {
        when (furnitureType) {
            FurnitureType.ARMCHAIR -> return R.string.furniture_armchair
            FurnitureType.BED -> return R.string.furniture_bed
            FurnitureType.SOFA -> return R.string.furniture_sofa
            FurnitureType.USER_CREATED_GRINDE -> return null
            FurnitureType.USER_CREATED_SLIDE -> return null
            FurnitureType.USER_CREATED_OTHER -> return null
            FurnitureType.UNDEFINED -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
            else -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
        }
    }
}
