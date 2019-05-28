package com.example.furnitures.calculator.container.list

import android.arch.lifecycle.LiveData
import com.example.furnitures.calculator.trick.FurnitureViewState

class ListTrickContract {
    interface ViewModel {
        fun getSelectedItemsViewState(): LiveData<List<FurnitureViewState>>
        fun onItemDelete(furnitureViewState: FurnitureViewState)
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun getFurnitureAt(position: Int): FurnitureViewState
    }
}