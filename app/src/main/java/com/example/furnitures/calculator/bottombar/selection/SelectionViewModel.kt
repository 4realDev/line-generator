package com.example.furnitures.calculator.bottombar.selection

import android.app.Application
import androidx.lifecycle.*
import com.example.furnitures.calculator.trick.*

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class SelectionViewModel(application: Application) : AndroidViewModel(application), FurnitureContract.ViewModel {

    // N
    private val repository = RepositoryFactory.getFurnitureRepository()

    // Wenn sich die Daten im Repository ändern -> zugreifen auf die Filter und diese anwenden
    // Wenn sich Filter ändern -> zugreifen auf die Daten und diese verändern
    // Daten Liste (Oberste Schicht)
    private val furnitures = repository.getFurnitures()
    private val furnituresFilter = MutableLiveData<FurnitureCategory>()
    private val furnitureMediator = MediatorLiveData<List<Furniture>>()

    // Gemappte Daten für den View -> Observed alle Änderungen der Daten in Transformations.map
    private val furnituresViewState = Transformations.map(furnitureMediator, ::mapList)

    init {
        // Informiere Mediator (somit auch ViewState) wenn sich Data ändert
        // Speicher aktuellen Filter in tmp val
        // furnituresList == null -> mach nichts
        // furnituresList != null ->
        // currentFilter == null -> setze geänderte Data in Mediator
        // currentFilter != null -> filtriere geänderte Data mit aktuellen Filter

        furnitureMediator.addSource(furnitures) { furnituresList ->
            val currentFilter = furnituresFilter.value
            if (furnituresList != null) {
                if (currentFilter == null) furnitureMediator.value = furnituresList
                else furnitureMediator.value = filter(furnituresList!!, currentFilter)
            }
        }

        // Informiere Mediator (somit auch ViewState) wenn sich Filter ändert
        // Speichere aktuelle furnituresList in tmp val
        // furnituresList == null -> mach nichts
        // furnituresList != null ->
        // filter == null -> setze geänderte Data in Mediator (ohne filter)
        // filter != null -> filtriere geänderte Data mit aktuellen Filter

        furnitureMediator.addSource(furnituresFilter) { filter ->
            val furnituresList = furnitures.value
            if (furnituresList != null) {
                if (filter == null) furnitureMediator.value = furnituresList
                else furnitureMediator.value = filter(furnituresList, filter)
            }
        }
    }

    override fun onFurnitureClicked(furniture: FurnitureViewState) {
        repository.updateFurnitureOnClick(furniture)
    }

    override fun getFurnitureList(): LiveData<List<FurnitureViewState>> {
        return furnituresViewState
    }

    override fun onFilterFurnitures(filter: FurnitureCategory) {
        furnituresFilter.value = filter
    }

    private fun filter(furnituresList: List<Furniture>, filter: FurnitureCategory): List<Furniture> {
        return if (filter != FurnitureCategory.UNDEFINED)
            furnituresList.filter { it.furnitureCategory == filter }
        else furnituresList
    }


    private fun map(furniture: Furniture): FurnitureViewState {
        return FurnitureViewState(
            id = furniture.id,
            position = furniture.position,
            furnitureType = furniture.furnitureType,
            furnitureCategory = furniture.furnitureCategory,
            name = FurnitureTypeHelper.getString(furniture.furnitureType),
            userCreatedName = furniture.userCreateName,
            drawableResId = FurnitureTypeHelper.getDrawable(furniture.furnitureType),
            isSelected = furniture.isSelected
        )
    }

    private fun mapList(furnitureList: List<Furniture>): List<FurnitureViewState> {
        return furnitureList
            //.filterNot { it.trickType == FurnitureType.OTHER }
            .map { map(it) }
    }
}