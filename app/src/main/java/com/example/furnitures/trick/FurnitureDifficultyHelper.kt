package com.example.furnitures.trick

object FurnitureDifficultyHelper {

    fun getFurnitureDifficulty(difficultyString: String): FurnitureDifficulty {
        return when (difficultyString) {
            FurnitureDifficulty.SAVE.name.toLowerCase().capitalize() -> FurnitureDifficulty.SAVE
            FurnitureDifficulty.EASY.name.toLowerCase().capitalize() -> FurnitureDifficulty.EASY
            FurnitureDifficulty.MIDDLE.name.toLowerCase().capitalize() -> FurnitureDifficulty.MIDDLE
            FurnitureDifficulty.HARD.name.toLowerCase().capitalize() -> FurnitureDifficulty.HARD
            FurnitureDifficulty.CRAZY.name.toLowerCase().capitalize() -> FurnitureDifficulty.CRAZY
            else -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType")
        }
    }
}
