package com.example.furnitures.bottombar.create

import com.example.furnitures.trick.FurnitureDifficulty

class SettingsContract {
    interface ViewModel {
        fun setMaxTricks(maxTricks: Int)
        fun setDifficulty(difficulty: FurnitureDifficulty)
    }
}