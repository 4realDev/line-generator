package com.example.furnitures.bottombar.create

class SettingsContract {
    interface ViewModel {
        fun setDifficulty(difficulty: Int)
        fun setMaxNumbOfTricks(maxNumbOfTricks: Int)
    }
}