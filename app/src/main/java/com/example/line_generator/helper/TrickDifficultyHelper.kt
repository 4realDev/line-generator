package com.example.line_generator.helper

import com.example.line_generator.data.trick.Difficulty

object TrickDifficultyHelper {

    fun getFurnitureDifficulty(difficultyString: String): Difficulty {
        return when (difficultyString) {
            Difficulty.SAVE.name.toLowerCase().capitalize() -> Difficulty.SAVE
            Difficulty.EASY.name.toLowerCase().capitalize() -> Difficulty.EASY
            Difficulty.MIDDLE.name.toLowerCase().capitalize() -> Difficulty.MIDDLE
            Difficulty.HARD.name.toLowerCase().capitalize() -> Difficulty.HARD
            Difficulty.CRAZY.name.toLowerCase().capitalize() -> Difficulty.CRAZY
            else -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + trickType")
        }
    }
}
