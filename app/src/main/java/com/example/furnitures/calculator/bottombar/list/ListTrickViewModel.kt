package com.example.furnitures.calculator.bottombar.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.calculator.trick.Furniture
import com.example.furnitures.calculator.trick.FurnitureTypeHelper
import com.example.furnitures.calculator.trick.FurnitureViewState
import com.example.furnitures.calculator.trick.RepositoryFactory

class ListTrickViewModel(application: Application) : AndroidViewModel(application), ListTrickContract.ViewModel {

    private val repository = RepositoryFactory.getFurnitureRepository()

    // dynamisch Änderungen müssen am ListTrickView nichtmehr übernommen werden
    // Verwendung von statische Liste
    // LiveData nur sinnvoll, wenn beide Screen parallel laufen könnten
    private var selectedFurnituresList = repository.getSelectedFurnituresList()
    private var selectedFurnitureListViewState = MutableLiveData<List<FurnitureViewState>>()


    init {
        // Anonyme Funktion -> ruft map(it) auf und springt da rein
        // selectedFurnitureListViewState.value = selectedFurnituresList.map{map(it)}
        // Methoden Referenz
        selectedFurnitureListViewState.value = selectedFurnituresList.map(::map)
    }

    override fun getSelectedItemsViewState(): LiveData<List<FurnitureViewState>> = selectedFurnitureListViewState

    override fun getFurnitureAt(position: Int): FurnitureViewState = selectedFurnitureListViewState.value.orEmpty()[position]

    override fun changeFurnitureItemPosition(fromPosition: Int, toPosition: Int) {
        var newSwapUnitList = selectedFurnitureListViewState.value.orEmpty().toMutableList()
        val removed = newSwapUnitList.removeAt(fromPosition)
        newSwapUnitList.add(toPosition, removed)
        selectedFurnitureListViewState.value = newSwapUnitList
        Log.d("DEBUGG", "fromViewModelPosition: $fromPosition toViewModelPosition: $toPosition")
    }

    override fun removeFurnitureItem(furnitureViewState: FurnitureViewState) {
        selectedFurnitureListViewState.value = selectedFurnitureListViewState.value?.minus(furnitureViewState)
    }

    private fun map(furniture: Furniture): FurnitureViewState {
        return FurnitureViewState(
            id = furniture.id,
            furnitureType = furniture.furnitureType,
            furnitureCategory = furniture.furnitureCategory,
            name = FurnitureTypeHelper.getString(furniture.furnitureType),
            drawableResId = FurnitureTypeHelper.getDrawable(furniture.furnitureType),
            isSelected = furniture.isSelected
        )
    }
}