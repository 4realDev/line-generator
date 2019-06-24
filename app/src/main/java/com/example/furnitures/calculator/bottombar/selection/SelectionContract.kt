package com.example.furnitures.calculator.bottombar.selection

import androidx.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureViewState
import com.example.furnitures.calculator.trick.RowViewState

interface FurnitureContract {

    interface ViewModel {
        //region Actions
        fun onFurnitureClicked(furniture: FurnitureViewState)

//        fun onFilterFurnitures(filter: FurnitureCategory)
//        //endregion
//
//        //region Data
//        fun getFurnitureList(): LiveData<List<FurnitureViewState>>
        fun getFurnitureViewStateWithHeader(): LiveData<List<RowViewState>>
    }
}