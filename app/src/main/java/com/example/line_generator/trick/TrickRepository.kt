package com.example.line_generator.trick

import androidx.lifecycle.LiveData

interface TrickRepository {
    /* region data */
    fun getSortedTricks(): LiveData<List<Trick>>

    fun getSelectedTricks(): LiveData<List<Trick>>

    suspend fun insert(trick: TrickViewState)

    suspend fun deleteTrickById(id: String)

    suspend fun updateTrickOnClick(clickedTrick: TrickViewState)

    suspend fun updateAfterEdit(id: String, changedName: String, changedDirectionIn: DirectionIn, changedDirectionOut: DirectionOut, changedDifficulty: Difficulty)
    /* endregion */
}