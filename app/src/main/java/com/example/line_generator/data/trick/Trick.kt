package com.example.line_generator.data.trick

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.line_generator.data.user.User


/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
// id als auch selected, default und OutComing sind im View nie sichtbar
// müssen somit nicht dem ViewState übergeben werden
// dont use is_ KeyWord in Combination with Room
@Entity (
    tableName = "trick_data",
    foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = ["id"],
    childColumns = ["user_id"],
    onDelete = CASCADE)])
data class Trick(
    @PrimaryKey
    @ColumnInfo(name = "id")                    var id: String,
    @ColumnInfo(name = "user_id")               var userId: String,
    @ColumnInfo(name = "position")              var position: Int,
    @ColumnInfo(name = "type")                  var trickType: TrickType,
    @ColumnInfo(name = "direction_in")          var directionIn: DirectionIn,
    @ColumnInfo(name = "direction_out")         var directionOut: DirectionOut,
    @ColumnInfo(name = "category")              var category: Category,
    @ColumnInfo(name = "difficulty")            var difficulty: Difficulty,
    @ColumnInfo(name = "user_created_name")     var userCreateName: String?,
    @ColumnInfo(name = "selected")              var selected: Boolean,
    @ColumnInfo(name = "default")               var default: Boolean
) : Comparable<Trick> {

    // jedesmal aufgerufen wenn sortBy aufgerufen wird
    override fun compareTo(other: Trick): Int {
        // sortierung nach der reihenfolge
        //return this.category.compareTo(other.category)

        // compare(x: Int, y: Int)
        // Compares two int values numerically
        // Returns:
        // if x == y -> return 0
        // if x < y -> return -1
        // if x > y -> return 1
        // Aufsteigende Sortierung
        return Integer.compare(this.category.sortWeight, other.category.sortWeight)
    }

    constructor(): this(
        id = "",
        userId = "",
        position = 0,
        trickType = TrickType.UNDEFINED,
        directionIn = DirectionIn.REGULAR,
        directionOut = DirectionOut.TO_REGULAR,
        category = Category.OTHER,
        difficulty = Difficulty.SAVE,
        userCreateName = null,
        selected = true,
        default = false
    )
}

enum class TrickType {
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

enum class Category(val sortWeight: Int) {
    GRIND(1), SLIDE(2), OTHER(3)
}

enum class Difficulty(val weight: Int) {
    SAVE(1), EASY(2), MIDDLE(3), HARD(4), CRAZY(5)
}