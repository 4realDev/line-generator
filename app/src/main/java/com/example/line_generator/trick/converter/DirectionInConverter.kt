package com.example.line_generator.trick.converter

import androidx.room.TypeConverter
import com.example.line_generator.trick.DirectionIn

class DirectionInConverter {
    companion object {
        @JvmStatic
        @TypeConverter
        fun directionInToString(enumType: DirectionIn) = enumType.name

        @JvmStatic
        @TypeConverter
        fun stringToDirectionIn(enumName: String): DirectionIn = DirectionIn.valueOf(enumName)
    }
}