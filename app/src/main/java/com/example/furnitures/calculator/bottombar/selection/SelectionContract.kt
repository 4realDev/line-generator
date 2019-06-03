package com.example.furnitures.calculator.bottombar.selection

import androidx.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureViewState

interface FurnitureContract {

    interface ViewModel {
        //region Actions
        fun onFurnitureClicked(furniture: FurnitureViewState)

        fun onFilterFurnitures(filter: FurtnitureCategory)
        //endregion

        //region Data
        fun getFurnitureList(): LiveData<List<FurnitureViewState>>
    }
}

enum class FurnitureType {
    UNDEFINED, BED, SOFA, TEST1, TEST2, TEST3, ARMCHAIR
}

enum class FurtnitureCategory {
    UNDEFINED, GARDEN, HOUSE, ROOF
}