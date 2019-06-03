package com.example.furnitures.calculator.trick

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.selection.FurnitureType

object FurnitureTypeHelper {

    // BUG: multiple pictures selects, when you click different items with the same drawable
    @DrawableRes
    fun getDrawable(furnitureType: FurnitureType): Int {
        when (furnitureType) {
            FurnitureType.ARMCHAIR -> return R.drawable.ic_furniture_armchair
            FurnitureType.BED -> return R.drawable.ic_furniture_bed
            FurnitureType.SOFA -> return R.drawable.ic_furniture_sofa
            FurnitureType.TEST1-> return R.drawable.ic_dice_24dp
            FurnitureType.TEST2 -> return R.drawable.ic_arrow_back_24dp
            FurnitureType.TEST3 -> return R.drawable.ic_search_black_24dp
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
            FurnitureType.TEST1 -> return R.string.furniture_sofa
            FurnitureType.TEST2 -> return R.string.furniture_sofa
            FurnitureType.TEST3 -> return R.string.furniture_sofa
            FurnitureType.UNDEFINED -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
            else -> throw IllegalStateException("Could not find String for furniteType $furnitureType")
        }
    }
}
