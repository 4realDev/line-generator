package com.example.line_generator.data.trick

import java.io.Serializable

data class TrickViewState(
    override val id: String,
    val userId: String,
    val position: Int,
    val trickType: TrickType,
    val directionIn: DirectionIn,
    val directionOut: DirectionOut,
    override val category: Category,
    val difficulty: Difficulty,
    // nullable, da null, falls userCreatedName existiert
    val name: Int?,
    val userCreatedName: String?,
    val drawableResId: Int,
    val selected: Boolean
): RowViewState, Serializable

data class HeaderViewState(
    override val id: String,
    val position: Int,
    override val category: Category
): RowViewState

interface RowViewState{
    val id: String
    val category: Category
}