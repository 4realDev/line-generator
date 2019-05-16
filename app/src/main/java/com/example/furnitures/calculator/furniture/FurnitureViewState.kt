package com.example.furnitures.calculator.furniture

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class FurnitureViewState(
    val id: String,
    val furnitureType: FurnitureType,
    val name: Int,
    val drawableResId: Int,
    val isSelected: Boolean
)