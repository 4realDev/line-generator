package com.example.furnitures.trick

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
// id als auch isSelected, isDefault und OutComing sind im View nie sichtbar
// müssen somit nicht dem ViewState übergeben werden
data class Trick(
    val id: String,
    val position: Int,
    val furnitureType: FurnitureType,
    val directionIn: DirectionIn,
    val directionOut: DirectionOut,
    val furnitureCategory: FurnitureCategory,
    val furnitureDifficulty: FurnitureDifficulty,
    val userCreateName: String?,
    val isSelected: Boolean,
    val isDefault: Boolean
) : Comparable<Trick> {
    // jedesmal aufgerufen wenn sortBy aufgerufen wird
    override fun compareTo(other: Trick): Int {
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
    TAIL_STALL,
    NOSE_STALL,
    ROCK_TO_FAKIE,
    ROCK_N_ROLL,
    HALFCAB_ROCK,
    FULLCAB_ROCK,

    AXLE_STALL,
    PIVOT,
    FIVE_O_GRIND,
    FEEBLE_GRIND,
    SMITH_GRIND,
    CROOKED_GRIND,
    NOSE_GRIND,

    BONELESS,
    NO_COMPLY,
    OLLIE,
    DISTASTER,

    USER_CREATED_GRINDE,
    USER_CREATED_SLIDE,
    USER_CREATED_OTHER,
    UNDEFINED
}

// sealed class vorteil funktionen
//sealed class DirectionOut {
//    object TO_FAKIE : DirectionOut() {
//        override fun invert() = TO_REGULAR
//    }
//
//    object TO_REGULAR : DirectionOut() {
//        override fun invert() = TO_FAKIE
//    }
//
//    abstract fun invert(): DirectionOut
//}

enum class DirectionOut{
    TO_FAKIE, TO_REGULAR
}

enum class DirectionIn {
    FAKIE, REGULAR
}

enum class FurnitureCategory(val sortWeight: Int) {
    GRIND(1), SLIDE(2), OTHER(3)
}

enum class FurnitureDifficulty(val weight: Int) {
    SAVE(1), EASY(2), MIDDLE(3), HARD(4), CRAZY(5)
}