package com.example.line_generator.data.trick.converter

import androidx.room.TypeConverter
import com.example.line_generator.data.trick.TrickType

class TrickTypeConverter {
    companion object {
        @JvmStatic
        @TypeConverter
        fun typeToString(enumType: TrickType) = enumType.name

        @JvmStatic
        @TypeConverter
        fun stringToType(enumName: String): TrickType = TrickType.valueOf(enumName)
    }
}