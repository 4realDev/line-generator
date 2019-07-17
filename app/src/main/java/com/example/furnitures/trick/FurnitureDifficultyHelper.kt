package com.example.furnitures.trick

object FurnitureDifficultyHelper {

    fun getFurnitureDifficulty(difficultyString: String): FurnitureDifficulty {
        return when (difficultyString) {
            FurnitureDifficulty.JOKE.name.toLowerCase().capitalize() -> FurnitureDifficulty.JOKE
            FurnitureDifficulty.EASY.name.toLowerCase().capitalize() -> FurnitureDifficulty.EASY
            FurnitureDifficulty.MIDDLE.name.toLowerCase().capitalize() -> FurnitureDifficulty.MIDDLE
            FurnitureDifficulty.HARD.name.toLowerCase().capitalize() -> FurnitureDifficulty.HARD
            FurnitureDifficulty.CRAZY.name.toLowerCase().capitalize() -> FurnitureDifficulty.CRAZY
            else -> throw IllegalStateException("\"Could not find Drawable for furniteType \" + furnitureType")
        }
    }
}
