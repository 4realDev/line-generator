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

    // Daten Liste (Oberste Schicht)
    private val furnitureListEvent = MutableLiveData<List<Furniture>>()

    // Gemappte Daten für den View -> Observed alle Anderungen der Daten in Transformations.map
    private val furnitureListViewState = Transformations.map(furnitureListEvent, ::mapList)

    init {
        furnitureListEvent.value = setData()
    }

//    private val furnitureRepository = RepositoryProvider.getRepository(FurnitureRepository::class.java)

//    private val nonDefaultFurniture = furnitureRepository.furniture
//    private val furnitureList = Transformations.map(nonDefaultFurniture) { furniture ->
//        mapList(furniture).sortedBy { it.name }
//    }

//    private val upNavigationEvent = SingleLiveEvent<Void>()

    //region Actions
//    override fun onUpClicked() {
//        upNavigationEvent.call()
//    }

    override fun onFurnitureClicked(furniture: FurnitureViewState) {
        //if (furnitureListViewState.value == null) throw IllegalStateException("WTF Liste sollte nicht null sein wenn schon eins heklickt wurde")
        //val clickedFurnite = furnitureListViewState.value!!.find { it.id == furniture.id }
        //if (clickedFurnite == null) throw IllegalStateException("Wie kann etwas geklickt worden sein dass ich als ViewModel nicht kenne?")

        // Beim Click: nimm den Wert der Daten
        // falls Liste Leer ist, gib eine Leere Liste zurück (nicht null, sonst Crash)
        // Convertiere die Liste (ggf. leere Liste) zu einer MutableList (um remove und add anwenden zu können)
        // .let um die Werte zwischen zu  speichern
        // val clickedFurniture -> geclicktes altes Element
        // finde das geclickte Element anhand seiner id
        // Auch wenn es nicht das selbe Element ist, sondern eine neue kopierte Referenz
        // ?: falls das Element nicht exisiert
        // finde den Index des geclickten Element und zwischenspeichere ihn
        // Kopiere das alte Element mit umgekehrten !isSelected Wert in die neue Element Instanz
        // Entferne das alte
        // Füge das neue Element (furnitureNowSelected) an die Stelle das alten (indexOfClicked)
        // setze die neue Liste ins LiveData

        furnitureListEvent.value.orEmpty().toMutableList().let { furnitures ->

            val clickedFurniture: Furniture = furnitures.find { it.id == furniture.id }
                ?: throw IllegalStateException("Unknown Furniture clicked")
            val indexOfClicked: Int  = furnitures.indexOf(clickedFurniture)
            val furnitureNowSelected: Furniture = clickedFurniture.copy(isSelected = !clickedFurniture.isSelected)
            furnitures.removeAt(indexOfClicked)
            furnitures.add(indexOfClicked, furnitureNowSelected)
            furnitureListEvent.value = furnitures
        }







        //val currentFurniture = furnitureRepository.getFurnitureSync(furniture.id)
//        val currentFurniture = furniture.id
//        if (currentFurniture != null) {
//            val newSelectedState = !currentFurniture.isSelected
//            val newCount = if (!newSelectedState) 0 else 1
//            val changedFurniture = currentFurniture.copy(isSelected = newSelectedState, count = newCount)
//            furnitureRepository.update(changedFurniture)
    }

    //endregion

    //region Data
    override fun getFurnitureList(): LiveData<List<FurnitureViewState>> {
        return furnitureListViewState
    }
    //endregion

    //region NavigationEvents
//    override fun getUpNavigationEvent(): LiveData<Void> {
//        return upNavigationEvent
//    }
    //endregion

    private fun map(furniture: Furniture): FurnitureViewState {
        return FurnitureViewState(
            id = furniture.id,
            furnitureType = furniture.furnitureType,
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

    private fun setData(): List<Furniture> = listOf(
            Furniture(randomUUID(), FurnitureType.BED, 0, 22.0, false, true, false),
            Furniture(randomUUID(), FurnitureType.ARMCHAIR, 0, 22.0, false, true,false),
            Furniture(randomUUID(), FurnitureType.SOFA, 0, 22.0, false, true, false)
    )

    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    private fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}