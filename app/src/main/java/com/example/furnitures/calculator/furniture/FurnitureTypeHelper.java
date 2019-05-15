package com.example.furnitures.calculator.furniture;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import com.example.furnitures.R;

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
public final class FurnitureTypeHelper {

    private FurnitureTypeHelper() {
    }

    @DrawableRes
    public static int getDrawable(@FurnitureType int furnitureType) {
        switch (furnitureType) {
            case FurnitureType.ARMCHAIR:
                return R.drawable.ic_furniture_armchair;
            case FurnitureType.BED:
                return R.drawable.ic_furniture_bed;
            case FurnitureType.SOFA:
                return R.drawable.ic_furniture_sofa;
            case FurnitureType.UNDEFINED:
            default:
                throw new IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType");
        }
    }

    @StringRes
    public static int getString(@FurnitureType int furnitureType) {
        switch (furnitureType) {
            case FurnitureType.ARMCHAIR:
                return R.string.furniture_armchair;
            case FurnitureType.BED:
                return R.string.furniture_bed;
            case FurnitureType.SOFA:
                return R.string.furniture_sofa;
            case FurnitureType.UNDEFINED:
            default:
                throw new IllegalStateException("Could not find String for furniteType " + furnitureType);
        }
    }
}
