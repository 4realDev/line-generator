package com.example.furnitures.calculator.container.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.furnitures.calculator.trick.Furniture
import com.example.furnitures.calculator.trick.FurnitureTypeHelper
import com.example.furnitures.calculator.trick.FurnitureViewState
import com.example.furnitures.calculator.trick.RepositoryFactory
import java.util.*

class ListTrickViewModel(application: Application): AndroidViewModel(application), ListTrickContract.ViewModel {

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

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Log.d("DEBUGG", "toPosition: $toPosition fromPosition: $fromPosition")

        var newSwapUnitList = selectedFurnitureListViewState.value.orEmpty().toMutableList()
//        newSwapUnitList.addAll(selectedFurnitureListViewState.value.orEmpty())
//        Collections.swap(newSwapUnitList, fromPosition, toPosition)

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(newSwapUnitList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(newSwapUnitList, i, i - 1)
            }
        }
        selectedFurnitureListViewState.value = newSwapUnitList

    }

    override fun onItemDelete(furnitureViewState: FurnitureViewState) {
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