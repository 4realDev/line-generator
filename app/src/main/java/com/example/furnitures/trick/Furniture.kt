package com.example.furnitures.trick

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
data class Furniture(
    val id: String,
    val position: Int,
    val furnitureType: FurnitureType,
    val furnitureCategory: FurnitureCategory,
    val furnitureDifficulty: FurnitureDifficulty,
    val userCreateName: String?,
    val isSelected: Boolean,
    val isDefault: Boolean
): Comparable<Furniture>{
    // jedesmal aufgerufen wenn sortBy aufgerufen wird
    override fun compareTo(other: Furniture): Int {
        // sortierung nach der reihenfolge
        //return this.furnitureCategory.compareTo(other.furnitureCategory)

        // compare(x: Int, y: Int)
        // Compares two int values numerically
        // Returns:
        // if x == y -> return 0
        // if x < y -> return -1
        // if x > y -> return 1
        // Aufsteigende Sortierung
        return Integer.compare(this.furnitureCategory.sortWeight, other.furnitureCategory.sortWeight)
    }
}

enum class FurnitureType {
    UNDEFINED, BED, SOFA, ARMCHAIR, USER_CREATED_GRINDE, USER_CREATED_SLIDE, USER_CREATED_OTHER
}

enum class  FurnitureCategory(val sortWeight : Int) {
    GRIND(1), SLIDE(2), OTHER(3)
}

enum class FurnitureDifficulty(val weight: Int) {
    JOKE(1), EASY(2), MIDDLE(3), HARD(4), CRAZY(5)
}