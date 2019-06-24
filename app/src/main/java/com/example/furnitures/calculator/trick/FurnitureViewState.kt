package com.example.furnitures.calculator.trick

data class FurnitureViewState(
    override val id: String,
    val position: Int,
    val furnitureType: FurnitureType,
    override val furnitureCategory: FurnitureCategory,
    // nullable, da null, falls userCreatedName existiert
    val name: Int?,
    val userCreatedName: String?,
    val drawableResId: Int,
    val isSelected: Boolean
): RowViewState

data class HeaderViewState(
    override val id: String,
    val position: Int,
    override val furnitureCategory: FurnitureCategory
): RowViewState

interface RowViewState{
    val id: String
    val furnitureCategory: FurnitureCategory
}