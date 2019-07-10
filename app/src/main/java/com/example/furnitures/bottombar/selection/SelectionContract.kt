package com.example.furnitures.bottombar.selection

import androidx.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureViewState
import com.example.furnitures.calculator.trick.RowViewState

interface FurnitureContract {

    interface ViewModel {

        fun onFurnitureClicked(furniture: FurnitureViewState)

        fun getFurnitureViewStateWithHeader(): LiveData<List<RowViewState>>
    }
}