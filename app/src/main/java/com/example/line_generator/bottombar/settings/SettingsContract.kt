package com.example.line_generator.bottombar.create

import androidx.lifecycle.LiveData
import com.example.line_generator.data.trick.Difficulty

class SettingsContract {
    interface ViewModel {
//        fun calculateDifficultyPercentage(difficulty: difficulty): FloatArray
        fun getDifficulty(): LiveData<Difficulty>
        fun getDifficultyPercentage(): LiveData<FloatArray>
        fun onMaxTricksSelected(maxTricks: Int)
        fun onDifficultySelected(difficulty: Difficulty)
//        fun setTricksPercentage(trickPercentageArray: IntArray)
    }
}