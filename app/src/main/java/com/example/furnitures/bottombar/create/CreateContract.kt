package com.example.furnitures.bottombar.create

import androidx.lifecycle.LiveData
import com.example.furnitures.trick.DirectionIn
import com.example.furnitures.trick.DirectionOut
import com.example.furnitures.trick.FurnitureCategory
import com.example.furnitures.trick.FurnitureDifficulty

class CreateContract {
    interface ViewModel {
        fun checkInput(input: String?)
        fun getCheckInputEvent(): LiveData<Boolean>
        fun createTrick(
            name: String,
            directionIn: DirectionIn,
            directionOut: DirectionOut,
            category: FurnitureCategory,
            difficulty: FurnitureDifficulty
        )
    }
}