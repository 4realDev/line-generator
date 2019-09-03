package com.example.line_generator.data.trick.converter

import androidx.room.TypeConverter
import com.example.line_generator.data.trick.DirectionOut

class DirectionOutConverter {
    companion object {
        @JvmStatic
        @TypeConverter
        fun directionOutToString(enumType: DirectionOut) = enumType.name

        @JvmStatic
        @TypeConverter
        fun stringToDirectionOut(enumName: String): DirectionOut = DirectionOut.valueOf(enumName)
    }
}