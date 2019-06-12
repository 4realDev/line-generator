package com.example.furnitures.calculator.trick

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class Furniture(
    val id: String,
    val userCreateName: String?,
    val position: Int,
    val furnitureType: FurnitureType,
    val furnitureCategory: FurnitureCategory,
    val count: Int,
    val volume: Double,
    val isSelected: Boolean,
    val isDefault: Boolean,
    private val isTombstone: Boolean = false
)

enum class FurnitureType {
    UNDEFINED, BED, SOFA, ARMCHAIR, USER_CREATED_GRINDE, USER_CREATED_SLIDE, USER_CREATED_OTHER
}

enum class FurnitureCategory {
    UNDEFINED, SLIDE, OTHER, GRIND
}