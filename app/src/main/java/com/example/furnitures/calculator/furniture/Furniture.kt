package com.example.furnitures.calculator.furniture

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class Furniture(
    val id: String,
    val furnitureType: FurnitureType,
    val count: Int,
    val volume: Double,
    val isSelected: Boolean,
    val isDefault: Boolean,
    private val isTombstone: Boolean = false

) /*: DataObject {

    override fun getId(): String = id

    override fun isTombstone(): Boolean = isTombstone
}*/