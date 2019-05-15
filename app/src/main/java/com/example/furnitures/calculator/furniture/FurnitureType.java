package com.example.furnitures.calculator.furniture;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.furnitures.calculator.furniture.FurnitureType.ARMCHAIR;
import static com.example.furnitures.calculator.furniture.FurnitureType.BED;
import static com.example.furnitures.calculator.furniture.FurnitureType.SOFA;
import static com.example.furnitures.calculator.furniture.FurnitureType.UNDEFINED;


/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
@Retention(RetentionPolicy.SOURCE)
// Elements of integer type, represents a logical type
// Its value should be one of the explicitly named constants
@IntDef({ UNDEFINED, BED, SOFA, ARMCHAIR })
public @interface FurnitureType {

    int UNDEFINED = 0;
    int BED = 1;
    int SOFA = 2;
    int ARMCHAIR = 3;
}
