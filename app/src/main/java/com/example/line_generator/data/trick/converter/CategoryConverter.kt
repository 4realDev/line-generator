package com.example.line_generator.data.trick.converter

import androidx.room.TypeConverter
import com.example.line_generator.data.trick.Category

class CategoryConverter {
    companion object {
        @JvmStatic
        @TypeConverter
        fun categoryToString(enumType: Category) = enumType.name

        @JvmStatic
        @TypeConverter
        fun stringToCategory(enumName: String): Category = Category.valueOf(enumName)
    }
}