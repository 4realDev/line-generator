package com.example.furnitures.trick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.extensions.randomUUID

class FurnitureRepositoryImp : FurnitureRepository {

    // Transformations.map wird erst Aufgerufen, wenn die LiveData Observed wird.

    private val furnitures = MutableLiveData<List<Furniture>>()
    // Mapping Funktion findet nur statt, wenn selectedFurnitures bereits observed wird!!!
    //private val selectedFurnitures = Transformations.map(furnitures, ::filterSelect)

    init {
        furnitures.value = createInitialFurnitures()
    }

    // sortierung vorher behebt fehler beim header einfügen
    override fun getSortedFurnitures(): LiveData<List<Furniture>>{
        sortFurnitures()
        return furnitures
    }

    //override fun getSelectedFurnitures(): LiveData<List<Furniture>> = selectedFurnitures

    override fun getSelectedFurnituresList(): List<Furniture> {
        // Collections sollten nie null sein
        return furnitures.value.orEmpty().filter { it.isSelected }
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

    override fun createFurniture(furnitureViewState: FurnitureViewState) {
        val furniture = map(furnitureViewState)
        furnitures.value = furnitures.value?.plus(furniture)
        sortFurnitures()
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

    // randomUUID
    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    override fun createInitialFurnitures() = listOf(
        Furniture(randomUUID(), null, 0, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 1, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 2, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 3, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 4, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 5, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 6, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 7, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 8, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 9, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 10, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 11, FurnitureType.SOFA, FurnitureCategory.GRIND, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 12, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 13, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 14, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 15, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 16, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 17, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 18, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 19, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 20, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 21, FurnitureType.BED, FurnitureCategory.SLIDE, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 22, FurnitureType.ARMCHAIR, FurnitureCategory.OTHER, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 23, FurnitureType.ARMCHAIR, FurnitureCategory.OTHER, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 24, FurnitureType.ARMCHAIR, FurnitureCategory.OTHER, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 25, FurnitureType.ARMCHAIR, FurnitureCategory.OTHER, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 26, FurnitureType.ARMCHAIR, FurnitureCategory.OTHER, 0, 22.0, false, true, false),
        Furniture(randomUUID(), null, 27, FurnitureType.ARMCHAIR, FurnitureCategory.OTHER, 0, 22.0, false, true, false)
    )

    private fun map(furnitureViewState: FurnitureViewState): Furniture {
        return Furniture(
            id = furnitureViewState.id,
            userCreateName = furnitureViewState.userCreatedName,
            position = furnitureViewState.position,
            furnitureType = furnitureViewState.furnitureType,
            furnitureCategory = furnitureViewState.furnitureCategory,
            count = 0,
            volume = 0.0,
            isSelected = furnitureViewState.isSelected,
            isDefault = false,
            isTombstone = false
        )
    }

    private fun sortFurnitures(){
        furnitures.value = furnitures.value!!.sortedBy { it.furnitureCategory.sortWeight}
    }
}