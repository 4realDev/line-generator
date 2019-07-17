package com.example.furnitures.trick

data class FurnitureViewState(
    override val id: String,
    val position: Int,
    val furnitureType: FurnitureType,
    override val furnitureCategory: FurnitureCategory,
    val furnitureDifficulty: FurnitureDifficulty,
    // nullable, da null, falls userCreatedName existiert
    val name: Int?,
    val userCreatedName: String?,
    val drawableResId: Int,
    val isSelected: Boolean
): RowViewState

data class HeaderViewState(
    override val id: String,
    val position: Int,
    override val furnitureCategory: FurnitureCategory,
    val selected: Boolean
): RowViewState

interface RowViewState{
    val id: String
    val furnitureCategory: FurnitureCategory
}