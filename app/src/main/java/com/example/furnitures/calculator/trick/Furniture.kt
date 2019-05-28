package com.example.furnitures.calculator.trick

import com.example.furnitures.calculator.container.selection.FurnitureType
import com.example.furnitures.calculator.container.selection.FurtnitureCategory

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class Furniture(
    val id: String,
    val furnitureType: FurnitureType,
    val furnitureCategory: FurtnitureCategory,
    val count: Int,
    val volume: Double,
    val isSelected: Boolean,
    val isDefault: Boolean,
    private val isTombstone: Boolean = false
)