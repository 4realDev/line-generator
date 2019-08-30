package com.example.line_generator.trick.converter

import androidx.room.TypeConverter
import com.example.line_generator.trick.Difficulty

class DifficultyConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun difficultyToString(enumType: Difficulty) =  enumType.name

        @TypeConverter
        @JvmStatic
        fun stringToDifficulty(enumName: String) = Difficulty.valueOf(enumName)
    }
}