package com.example.line_generator.bottombar.create

import com.example.line_generator.data.trick.*
import kotlinx.coroutines.Job

class CreateContract {
    interface ViewModel {
        fun createTrick(
            name: String,
            directionIn: DirectionIn,
            directionOut: DirectionOut,
            category: Category,
            difficulty: Difficulty
        )

        fun insert(trickViewState: TrickViewState): Job
    }
}