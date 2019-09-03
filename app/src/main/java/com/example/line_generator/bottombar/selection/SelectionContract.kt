package com.example.line_generator.bottombar.selection

import androidx.lifecycle.LiveData
import com.example.line_generator.data.trick.*
import kotlinx.coroutines.Job

interface TrickContract {

    interface ViewModel {

        fun onTrickClicked(trickViewState: TrickViewState)
//        fun isSectionEmpty(): Boolean

        fun getTrickViewStateWithHeader(): LiveData<List<RowViewState>?>
        fun getClickedItem(): TrickViewState?

        fun insert(trickViewState: TrickViewState): Job
        fun delete(trickViewState: TrickViewState)
        fun update(trickViewState: TrickViewState): Job
        fun updateAfterEdit(id: String, changedName: String, changedDirectionIn: DirectionIn, changedDirectionOut: DirectionOut, changedDifficulty: Difficulty): Job
    }
}