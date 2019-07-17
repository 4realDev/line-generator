package com.example.furnitures.trick

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.furnitures.R

object FurnitureTypeHelper {

    @DrawableRes
    fun getDrawable(furnitureType: FurnitureType): Int {
        return when (furnitureType) {
            FurnitureType.ARMCHAIR -> R.drawable.ic_furniture_armchair
            FurnitureType.BED -> R.drawable.ic_furniture_bed
            FurnitureType.SOFA -> R.drawable.ic_furniture_sofa
            FurnitureType.USER_CREATED_GRINDE -> R.drawable.ic_letter_g
            FurnitureType.USER_CREATED_SLIDE -> R.drawable.ic_letter_s
            FurnitureType.USER_CREATED_OTHER -> R.drawable.ic_letter_o
            FurnitureType.UNDEFINED -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType")
            else -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + $furnitureType")
        }
    }

    @StringRes
    fun getString(furnitureType: FurnitureType): Int? {
        return when (furnitureType) {
            FurnitureType.ARMCHAIR -> R.string.furniture_armchair
            FurnitureType.BED -> R.string.furniture_bed
            FurnitureType.SOFA -> R.string.furniture_sofa
            FurnitureType.USER_CREATED_GRINDE -> null
            FurnitureType.USER_CREATED_SLIDE -> null
            FurnitureType.USER_CREATED_OTHER -> null
            FurnitureType.UNDEFINED -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
            else -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
        }
    }
}
