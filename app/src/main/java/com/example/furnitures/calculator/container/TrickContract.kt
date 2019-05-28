package com.example.furnitures.calculator.container.selection

import android.arch.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureViewState

interface NewTrickContract {

    interface ViewModel {
//        fun getSelectedFurnitureList(): LiveData<List<FurnitureViewState>>
        //endregion

        fun getViewState(): LiveData<ViewState>

        fun changeViewState()
    }
}

enum class ViewState {
    INITIAL_STATE, CHANGED_STATE
}