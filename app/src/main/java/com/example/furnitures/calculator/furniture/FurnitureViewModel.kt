package com.example.furnitures.calculator.furniture

import android.app.Application
import android.arch.lifecycle.*

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class FurnitureViewModel(application: Application) : AndroidViewModel(application), FurnitureContract.ViewModel {

    private val repository = FurnitureRepositoryImp()

    // Wenn sich die Daten im Repository ändern -> zugreifen auf die Filter und diese anwenden
    // Wenn sich Filter ändern -> zugreifen auf die Daten und diese verändern
    // Daten Liste (Oberste Schicht)
    private val furnitures = repository.getFurnitures()
    private val furnituresFilter = MutableLiveData<FurtnitureCategory>()
    private val furnitureMediator = MediatorLiveData<List<Furniture>>()

    // Gemappte Daten für den View -> Observed alle Änderungen der Daten in Transformations.map
    private val furnituresViewState = Transformations.map(furnitureMediator, ::mapList)

    private val selectedFurnitures = repository.getSelectedFurnitures()
    private val selectedFurnituresViewState = Transformations.map(selectedFurnitures, ::mapList)

    init {
        furnitureMediator.addSource(furnitures) { furniture ->
            val filter = furnituresFilter.value
            if (filter == null) furnitureMediator.value = furniture
            else furnitureMediator.value = filter(furniture!!, filter)

        }
        furnitureMediator.addSource(furnituresFilter) { filter ->
            val furniture = furnitures.value
            if (furniture != null) {
                if (filter == null) furnitureMediator.value = furniture
                else furnitureMediator.value = filter(furniture, filter)
            }
        }
    }

    override fun onFurnitureClicked(furniture: FurnitureViewState) {
        repository.updateFurnitureOnClick(furniture)
    }

    override fun getFurnitureList(): LiveData<List<FurnitureViewState>> {
        return furnituresViewState
    }

    override fun getSelectedFurnitureList(): LiveData<List<FurnitureViewState>> {
        return selectedFurnituresViewState
    }

    override fun onFilterFurnitures(filter: FurtnitureCategory) {
        furnituresFilter.value = filter
    }

    private fun filter(furniture: List<Furniture>, filter: FurtnitureCategory): List<Furniture> =
        furniture.filter { it.furnitureCategory == filter }

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

    private fun mapList(furnitureList: List<Furniture>): List<FurnitureViewState> {
        return furnitureList
            //.filterNot { it.furnitureType == FurnitureType.OTHER }
            .map { map(it) }
    }
}