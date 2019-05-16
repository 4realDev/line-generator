package com.example.furnitures.calculator.furniture

import android.arch.lifecycle.LiveData

interface FurnitureContract {

    interface ViewModel {
        //region Actions
        fun onFurnitureClicked(furniture: FurnitureViewState)
        //endregion

        //region Data
        fun getFurnitureList(): LiveData<List<FurnitureViewState>>
        //endregion
    }
}

enum class FurnitureType {
    UNDEFINED, BED, SOFA, ARMCHAIR
}