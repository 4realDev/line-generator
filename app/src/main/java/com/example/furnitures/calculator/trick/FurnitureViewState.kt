package com.example.furnitures.calculator.trick

import com.example.furnitures.calculator.bottombar.selection.FurnitureType
import com.example.furnitures.calculator.bottombar.selection.FurtnitureCategory

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class FurnitureViewState(
    val id: String,
    val furnitureType: FurnitureType,
    val furnitureCategory: FurtnitureCategory,
    val name: Int,
    val drawableResId: Int,
    val isSelected: Boolean
)