package com.example.furnitures.calculator.trick

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class FurnitureViewState(
    val id: String,
    val position: Int,
    val furnitureType: FurnitureType,
    val furnitureCategory: FurnitureCategory,
    // nullable, da null, falls userCreatedName existiert
    val name: Int?,
    val userCreatedName: String?,
    val drawableResId: Int,
    val isSelected: Boolean
)