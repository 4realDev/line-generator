package com.example.furnitures.calculator.furniture

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import java.util.*

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class FurnitureViewModel(application: Application) : AndroidViewModel(application), FurnitureContract.ViewModel {

    private val repository = FurnitureRepositoryImp()

    // Daten Liste (Oberste Schicht)
    private val furnitures = repository.getFurnitures()
    // Gemappte Daten für den View -> Observed alle Änderungen der Daten in Transformations.map
    private val furnituresViewState = Transformations.map(furnitures, ::mapList)

    private val selectedFurnitures = repository.getSelectedFurnitures()
    private val selectedFurnituresViewState = Transformations.map(selectedFurnitures, ::mapList)

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