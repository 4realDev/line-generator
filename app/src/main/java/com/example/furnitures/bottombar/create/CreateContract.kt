package com.example.furnitures.bottombar.create

import androidx.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureCategory

class CreateContract {
    interface ViewModel {
        fun checkInput(input: String?)
        fun getCheckInputEvent(): LiveData<Boolean>
        fun createTrick(name: String, category: FurnitureCategory)
    }
}