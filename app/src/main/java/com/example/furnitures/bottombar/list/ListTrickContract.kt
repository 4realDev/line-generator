package com.example.furnitures.bottombar.list

import androidx.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureViewState

class ListTrickContract {
    interface ViewModel {
        fun getSelectedItemsViewState(): LiveData<List<FurnitureViewState>>
        fun removeFurnitureItem(furnitureViewState: FurnitureViewState)
        fun changeFurnitureItemPosition(fromPosition: Int, toPosition: Int)
        fun getFurnitureAt(position: Int): FurnitureViewState
    }
}