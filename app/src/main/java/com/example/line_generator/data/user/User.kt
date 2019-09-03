package com.example.line_generator.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
// id als auch selected, default und OutComing sind im View nie sichtbar
// müssen somit nicht dem ViewState übergeben werden
// dont use is_ KeyWord in Combination with Room
@Entity (tableName = "user_data")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")                 var id: String,
    @ColumnInfo(name = "name")               var name: String
){
    constructor(): this(
        id = "",
        name = ""
    )
}