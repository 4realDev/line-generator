package com.example.furnitures.calculator.trick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.calculator.bottombar.selection.FurnitureType
import com.example.furnitures.calculator.bottombar.selection.FurtnitureCategory
import java.util.*

class FurnitureRepositoryImp : FurnitureRepository {

    // Transformations.map wird erst Aufgerufen, wenn die LiveData Observed wird.

    private val furnitures = MutableLiveData<List<Furniture>>()
    // Mapping Funktion findet nur statt, wenn selectedFurnitures bereits observed wird!!!
    //private val selectedFurnitures = Transformations.map(furnitures, ::filterSelect)

    init {
        furnitures.value = createInitialFurnitures()
    }

    override fun getFurnitures(): LiveData<List<Furniture>> = furnitures

    //override fun getSelectedFurnitures(): LiveData<List<Furniture>> = selectedFurnitures

    override fun getSelectedFurnituresList(): List<Furniture>{
        // Collections sollten nie null sein
        return furnitures.value.orEmpty().filter{it.isSelected}
    }

    override fun getFurnitureById(id: String): Furniture? {
        lateinit var outputFurniture: Furniture
        furnitures.value?.forEach { furniture ->
            if (id == furniture.id) {
                outputFurniture = furniture
            }
        }
        return outputFurniture
    }

    override fun deleteFurnitureById(id: String) {

    }

    override fun updateFurnitureOnClick(clickedFurniture: FurnitureViewState) {
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

        furnitures.value.orEmpty().toMutableList().let { furnituresList ->

            val clickedFurniture: Furniture = furnituresList.find { it.id == clickedFurniture.id }
                ?: throw IllegalStateException("Unknown Furniture clicked")
            val indexOfClicked: Int = furnituresList.indexOf(clickedFurniture)
            val furnitureNowSelected: Furniture = clickedFurniture.copy(isSelected = !clickedFurniture.isSelected)
            furnituresList.removeAt(indexOfClicked)
            furnituresList.add(indexOfClicked, furnitureNowSelected)
            furnitures.value = furnituresList
        }
    }

    override fun createInitialFurnitures() = listOf(
        Furniture(randomUUID(), FurnitureType.BED, FurtnitureCategory.GARDEN, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.ARMCHAIR, FurtnitureCategory.HOUSE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.SOFA, FurtnitureCategory.ROOF, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.TEST1, FurtnitureCategory.ROOF, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.TEST2, FurtnitureCategory.ROOF, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.TEST3, FurtnitureCategory.ROOF, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.TEST3, FurtnitureCategory.ROOF, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.TEST3, FurtnitureCategory.ROOF, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.TEST3, FurtnitureCategory.ROOF, 0, 22.0, false, true, false)
    )

    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    private fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}